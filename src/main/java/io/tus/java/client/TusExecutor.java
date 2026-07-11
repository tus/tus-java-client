package io.tus.java.client;

import java.io.IOException;

/**
 * TusExecutor is a wrapper class which you can build around your uploading mechanism and any
 * exception thrown by it will be caught and may result in a retry. This way you can easily add
 * retrying functionality to your application with defined delays between them.
 *
 * This can be achieved by extending TusExecutor and implementing the abstract makeAttempt() method:
 * <pre>
 * {@code
 *  TusExecutor executor = new TusExecutor() {
 *      {@literal @}Override
 *      protected void makeAttempt() throws ProtocolException, IOException {
 *          TusUploader uploader = client.resumeOrCreateUpload(upload);
 *          while(uploader.uploadChunk() > -1) {}
 *          uploader.finish();
 *      }
 *  };
 *  executor.makeAttempts();
 * }
 * </pre>
 *
 * The retries are basically just calling the {@link #makeAttempt()} method which should then
 * retrieve an {@link TusUploader} using {@link TusClient#resumeOrCreateUpload(TusUpload)} and then
 * invoke {@link TusUploader#uploadChunk()} as long as possible without catching
 * {@link ProtocolException}s or {@link IOException}s as this is taken over by this class.
 *
 * The current attempt can be interrupted using {@link Thread#interrupt()} which will cause the
 * {@link #makeAttempts()} method to return <code>false</code> immediately.
 */
public abstract class TusExecutor {
    private int[] delays = new int[]{500, 1000, 2000, 3000};
    private int retryAttempt;

    /**
     * Set the delays at which TusExecutor will issue a retry if {@link #makeAttempt()} throws an
     * exception. If the methods call fails for the first time it will wait <code>delays[0]</code>ms
     * before calling it again. If this second calls also does not return normally
     * <code>delays[1]</code>ms will be waited on so on.
     * It total <code>delays.length</code> retries may be issued, resulting in up to
     * <code>delays.length + 1</code> calls to {@link #makeAttempt()}.
     * The default delays are set to 500ms, 1s, 2s and 3s.
     *
     * @see #getDelays()
     *
     * @param delays The desired delay values to be used
     */
    public void setDelays(int[] delays) {
        this.delays = delays;
    }

    /**
     * Get the delays which will be used for waiting before attempting retries.
     *
     * @see #setDelays(int[])
     *
     * @return The dalys previously set
     */
    public int[] getDelays() {
        return delays;
    }

    /**
     * This method is basically just calling the {@link #makeAttempt()} method which should then
     * retrieve an {@link TusUploader} using {@link TusClient#resumeOrCreateUpload(TusUpload)} and then
     * invoke {@link TusUploader#uploadChunk()} as long as possible without catching
     * {@link ProtocolException}s or {@link IOException}s as this is taken over by this class.
     *
     * The current attempt can be interrupted using {@link Thread#interrupt()} which will cause the
     * method to return <code>false</code> immediately.
     *
     * @return <code>true</code> if the {@link #makeAttempt()} method returned normally and
     * <code>false</code> if the thread was interrupted while sleeping until the next attempt.
     *
     * @throws ProtocolException
     * @throws IOException
     */
    public boolean makeAttempts() throws ProtocolException, IOException {
        retryAttempt = 0;
        while (true) {
            try {
                makeAttempt();
                // Returning true is the signal that the makeAttempt() function exited without
                // throwing an error.
                return true;
            } catch (ProtocolException e) {
                // Do not attempt a retry, if the Exception suggests so.
                if (!shouldRetry(e, retryAttempt)) {
                    throw e;
                }

                if (retryAttempt >= delays.length) {
                    // We exceeds the number of maximum retries. In this case the latest exception
                    // is thrown.
                    throw e;
                }
            }  catch (IOException e) {
                if (!shouldRetry(e, retryAttempt)) {
                    throw e;
                }

                if (retryAttempt >= delays.length) {
                    // We exceeds the number of maximum retries. In this case the latest exception
                    // is thrown.
                    throw e;
                }
            }

            int delay = delays[retryAttempt];
            onRetryScheduled(retryAttempt, delay);
            retryAttempt++;

            try {
                // Sleep for the specified delay before attempting the next retry.
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                // If we get interrupted while waiting for the next retry, the user has cancelled
                // the upload willingly and we return false as a signal.
                return false;
            }
        }
    }

    /**
     * Reset the retry attempt counter. Call this from {@link #makeAttempt()} after observing accepted
     * server-side upload progress, so later retry decisions start from the first retry delay again.
     */
    protected void resetRetryAttempts() {
        retryAttempt = 0;
    }

    /**
     * Decide whether a protocol failure should be retried.
     *
     * @param exception Protocol failure from the current attempt.
     * @param retryAttempt Zero-based retry attempt since the last successful progress reset.
     * @return {@code true} if another attempt should be scheduled.
     */
    protected boolean shouldRetry(ProtocolException exception, int retryAttempt) {
        return exception.shouldRetry();
    }

    /**
     * Decide whether an I/O failure should be retried.
     *
     * @param exception I/O failure from the current attempt.
     * @param retryAttempt Zero-based retry attempt since the last successful progress reset.
     * @return {@code true} if another attempt should be scheduled.
     */
    protected boolean shouldRetry(IOException exception, int retryAttempt) {
        return true;
    }

    /**
     * Observe a scheduled retry.
     *
     * @param retryAttempt Zero-based retry attempt since the last successful progress reset.
     * @param delayMillis Delay in milliseconds before the next attempt.
     */
    protected void onRetryScheduled(int retryAttempt, int delayMillis) { }

    /**
     * This method must be implemented by the specific caller. It will be invoked once or multiple
     * times by the {@link #makeAttempts()} method.
     * A proper implementation should retrieve an {@link TusUploader} using
     * {@link TusClient#resumeOrCreateUpload(TusUpload)} and then invoke
     * {@link TusUploader#uploadChunk()} as long as possible without catching
     * {@link ProtocolException}s or {@link IOException}s as this is taken over by this class.
     *
     * @throws ProtocolException
     * @throws IOException
     */
    protected abstract void makeAttempt() throws ProtocolException, IOException;
}
