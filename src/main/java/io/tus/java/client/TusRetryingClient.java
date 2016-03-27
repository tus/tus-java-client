package io.tus.java.client;


import java.io.IOException;

public class TusRetryingClient extends TusClient {
    private int maxRetries = 5;

    public void setMaxRetries(int retries) {
        maxRetries = retries;
    }

    @Override
    public TusUploader createUpload(TusUpload upload) throws ProtocolException, IOException {
        int attempt = 0;
        while(true) {
            attempt++;

            try {
                return super.createUpload(upload);
            } catch(IOException e) {
                if(attempt < maxRetries) {
                    continue;
                }

                throw e;
            } catch(ProtocolException e) {
                if(e.shouldRetry()) {
                    continue;
                }

                if(attempt < maxRetries) {
                    continue;
                }

                throw e;
            }
        }
    }

    @Override
    public TusUploader resumeUpload(TusUpload upload) throws FingerprintNotFoundException, ResumingNotEnabledException, ProtocolException, IOException {
        int attempt = 0;
        while(true) {
            attempt++;

            try {
                return super.resumeUpload(upload);
            } catch(IOException e) {
                if(attempt < maxRetries) {
                    continue;
                }

                throw e;
            } catch(ProtocolException e) {
                if(e.shouldRetry()) {
                    continue;
                }

                if(attempt < maxRetries) {
                    continue;
                }

                throw e;
            }
        }
    }

    @Override
    public TusUploader resumeOrCreateUpload(TusUpload upload) throws IOException, ProtocolException {
        int attempt = 0;
        while(true) {
            attempt++;

            try {
                return super.resumeOrCreateUpload(upload);
            } catch(IOException e) {
                if(attempt < maxRetries) {
                    continue;
                }

                break;
            } catch(ProtocolException e) {
                if(e.shouldRetry()) {
                    continue;
                }

                if(attempt < maxRetries) {
                    continue;
                }

                break;
            }
        }

        // Fall back to creating an upload if resuming failed for too long.
        return createUpload(upload);
    }
}
