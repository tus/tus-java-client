package io.tus.java.client;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Tus compatible client with a few extra features to
 * support GetSocial use cases:
 *
 * 1- Fast start support.
 *
 * Ability to start uploads from an existing URL, by immediately
 * making PATCH requests, skipping the POST and HEAD requests.
 *
 * 2- Readiness and "real URL" support.
 *
 * From Tus' perspective, once the last chunk is uploaded,
 * the file is "done". But in our case there is a lot of
 * post-processing involved before the file is publicly
 * available so we need a polling mechanism the make sure
 * the upload is really "done" and the file is available
 * on the "real URL".
 *
 * See {@link #getsocialUpload(TusUpload, String)} for details.
 */
public class GetsocialTusClient extends TusClient {
    public GetsocialTusClient() {
        super();
        super.enableResuming(new TusURLMemoryStore());
    }

    @Override
    public void enableResuming(@NotNull TusURLStore urlStore) {
        // Resuming is always enabled with a memory store.
    }

    @Override
    public void disableResuming() {
        // Resuming can't be disabled.
    }

    /**
     * Fast start an upload using the given URL by skipping the creation and offset checks.
     *
     * This method assumes a 0-length Tus resource already exists at the given URL.
     *
     * @param upload The file for which a new upload will be created
     * @param urlStr The URL of the existing 0-length Tus resource
     * @return Use {@link TusUploader} to upload the file's chunks.
     * @throws IOException Thrown if an exception occurs while issuing the HTTP request.
     */
    private TusUploader getsocialUpload(@NotNull TusUpload upload, @NotNull String urlStr) throws IOException {
        URL uploadURL=new URL(urlStr);
        urlStore.set(upload.getFingerprint(), uploadURL);
        return new TusUploader(this, uploadURL, upload.getTusInputStream(), 0);
    }

    /**
     * Try to resume an upload using {@link #resumeUpload(TusUpload)}. If the method call throws
     * a {@link FingerprintNotFoundException}, a new upload
     * will be created using {@link #getsocialUpload(TusUpload, String)}.
     *
     * @param upload The file for which an upload will be resumed
     * @throws ProtocolException Thrown if the remote server sent an unexpected response, e.g.
     *                           wrong status codes or missing/invalid headers.
     * @throws IOException       Thrown if an exception occurs while issuing the HTTP request.
     */
    public TusUploader resumeOrCreateUpload(@NotNull TusUpload upload, @NotNull String urlStr) throws ProtocolException, IOException {
        try {
            return resumeUpload(upload);
        } catch (FingerprintNotFoundException e) {
            return getsocialUpload(upload, urlStr);
        } catch (ResumingNotEnabledException e) {
            // Resuming can't be disabled.
            throw new ProtocolException("Resuming can't be disabled with this client");
        } catch (ProtocolException e) {
            // If the attempt to resume returned a 404 Not Found, we immediately try to create a new
            // one since TusExectuor would not retry this operation.
            HttpURLConnection connection=e.getCausingConnection();
            if (connection != null && connection.getResponseCode() == 404) {
                return createUpload(upload);
            }

            throw e;
        }
    }
}
