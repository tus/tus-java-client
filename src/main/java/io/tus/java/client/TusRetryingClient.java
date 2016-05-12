package io.tus.java.client;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class TusRetryingClient extends TusClient {
    private int[] delays = new int[]{500, 1000, 2000, 3000};

    public void setDelays(int[] delays) {
        this.delays = delays;
    }

    public int[] getDelays() {
        return delays;
    }

    @Override
    @Nullable
    public TusUploader createUpload(@NotNull TusUpload upload) throws ProtocolException, IOException {
        int attempt = 0;
        while(true) {
            attempt++;

            try {
                return super.createUpload(upload);
            } catch(ProtocolException e) {
                // Do not attempt a retry, if the Exception suggests so.
                if(!e.shouldRetry()) throw e;

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
                // the upload willingly and we return null as a signal.
                return null;
            }
        }
    }

    @Override
    @Nullable
    public TusUploader resumeUpload(@NotNull TusUpload upload) throws FingerprintNotFoundException, ResumingNotEnabledException, ProtocolException, IOException {
        int attempt = 0;
        while(true) {
            attempt++;

            try {
                return super.resumeUpload(upload);
            } catch(ProtocolException e) {
                // Do not attempt a retry, if the Exception suggests so.
                if(!e.shouldRetry()) throw e;

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
                // the upload willingly and we return null as a signal.
                return null;
            }
        }
    }

    // TODO resumeOrCreateUpload will return null if resumeUpload does so without trying to create one.
}
