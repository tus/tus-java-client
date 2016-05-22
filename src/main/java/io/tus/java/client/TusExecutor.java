package io.tus.java.client;

import java.io.IOException;

public abstract class TusExecutor {
    private int[] delays = new int[]{500, 1000, 2000, 3000};

    public void setDelays(int[] delays) {
        this.delays = delays;
    }

    public int[] getDelays() {
        return delays;
    }

    public boolean makeAttempts() throws ProtocolException, IOException {
        int attempt = -1;
        while(true) {
            attempt++;

            try {
                makeAttempt();
                // Returning true is the signal that the makeAttempt() function exited without
                // throwing an error.
                return true;
            } catch(ProtocolException e) {
                // Do not attempt a retry, if the Exception suggests so.
                if(!e.shouldRetry()) {
                    throw e;
                }

                if(attempt >= delays.length) {
                    // We exceeds the number of maximum retries. In this case the latest exception
                    // is thrown.
                    throw e;
                }
            }  catch(IOException e) {
                if(attempt >= delays.length) {
                    // We exceeds the number of maximum retries. In this case the latest exception
                    // is thrown.
                    throw e;
                }
            }

            try {
                // Sleep for the specified delay before attempting the next retry.
                Thread.sleep(delays[attempt]);
            } catch(InterruptedException e) {
                // If we get interrupted while waiting for the next retry, the user has cancelled
                // the upload willingly and we return false as a signal.
                return false;
            }
        }
    }

    protected abstract void makeAttempt() throws ProtocolException, IOException;
}
