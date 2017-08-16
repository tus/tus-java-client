package io.tus.java.client;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * This class is used for creating or resuming uploads.
 */
public class TusClient {
    /**
     * Version of the tus protocol used by the client. The remote server needs to support this
     * version, too.
     */
    public final static String TUS_VERSION = "1.0.0";

    private URL uploadCreationURL;
    private boolean resumingEnabled;
    private TusURLStore urlStore;
    private boolean deferLengthEnabled;
    private TusUploadLengthStore sizeStore;
    private Map<String, String> headers;
    private int connectTimeout = 5000;

    /**
     * Create a new tus client.
     */
    public TusClient() {

    }

    /**
     * Set the URL used for creating new uploads. This is required if you want to initiate new
     * uploads using {@link #createUpload} or {@link #resumeOrCreateUpload} but is not used if you
     * only resume existing uploads.
     *
     * @param uploadCreationURL Absolute upload creation URL
     */
    public void setUploadCreationURL(URL uploadCreationURL) {
        this.uploadCreationURL = uploadCreationURL;
    }

    /**
     * Get the current upload creation URL
     *
     * @return Current upload creation URL
     */
    public URL getUploadCreationURL() {
        return uploadCreationURL;
    }

    /**
     * Enable resuming already started uploads. This step is required if you want to use
     * {@link #resumeUpload(TusUpload)}.
     *
     * @param urlStore Storage used to save and retrieve upload URLs by its fingerprint.
     */
    public void enableResuming(@NotNull TusURLStore urlStore) {
        resumingEnabled = true;
        this.urlStore = urlStore;
    }

    /**
     * Disable resuming started uploads.
     *
     * @see #enableResuming(TusURLStore)
     */
    public void disableResuming() {
        resumingEnabled = false;
        this.urlStore = null;
    }

    /**
     * Enable defer-length feature.
     *
     * @param sizeStore Storage used to save and retrieve upload data size by its fingerprint.
     */
    public void enableDeferLength(@NotNull TusUploadLengthStore sizeStore) {
        deferLengthEnabled = true;
        this.sizeStore = sizeStore;
    }

    /**
     * Disable defer-length feature.
     *
     * @see #enableResuming(TusURLStore)
     */
    public void disableDeferLength() {
        deferLengthEnabled = false;
        this.sizeStore = null;
    }

    /**
     * Get the current status if defer-length-feature is enabled.
     *
     * @see #enableDeferLength(TusUploadLengthStore)
     */
    public boolean isDeferLengthEnabled() {
        return deferLengthEnabled;
    }

    /**
     * Get the current status if resuming.
     *
     * @see #enableResuming(TusURLStore)
     * @see #disableResuming()
     *
     * @return True if resuming has been enabled using {@link #enableResuming(TusURLStore)}
     */
    public boolean resumingEnabled() {
        return resumingEnabled;
    }

    /**
     * Set headers which will be added to every HTTP requestes made by this TusClient instance.
     * These may to overwrite tus-specific headers, which can be identified by their Tus-*
     * prefix, and can cause unexpected behavior.
     *
     * @see #getHeaders()
     * @see #prepareConnection(HttpURLConnection)
     *
     * @param headers The map of HTTP headers
     */
    public void setHeaders(@Nullable Map<String, String> headers) {
        this.headers = headers;
    }

    /**
     * Get the HTTP headers which should be contained in every request and were configured using
     * {@link #setHeaders(Map)}.
     *
     * @see #setHeaders(Map)
     * @see #prepareConnection(HttpURLConnection)
     *
     * @return The map of configured HTTP headers
     */
    @Nullable
    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setConnectTimeout(int timeout) {
        connectTimeout = timeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    @Nullable
    public TusUploadLengthStore getSizeStore() {
        return sizeStore;
    }

    /**
     * Create a new upload using the Creation extension. Before calling this function, an "upload
     * creation URL" must be defined using {@link #setUploadCreationURL(URL)} or else this
     * function will fail.
     * In order to create the upload a POST request will be issued. The file's chunks must be
     * uploaded manually using the returned {@link TusUploader} object.
     *
     * @param upload The file for which a new upload will be created
     * @return Use {@link TusUploader} to upload the file's chunks.
     * @throws ProtocolException Thrown if the remote server sent an unexpected response, e.g.
     * wrong status codes or missing/invalid headers.
     * @throws IOException Thrown if an exception occurs while issuing the HTTP request.
     */
    public TusUploader createUpload(@NotNull TusUpload upload) throws ProtocolException, IOException {
        HttpURLConnection connection = (HttpURLConnection) uploadCreationURL.openConnection();
        connection.setRequestMethod("POST");
        prepareConnection(connection);

        String encodedMetadata = upload.getEncodedMetadata();
        if(encodedMetadata.length() > 0) {
            connection.setRequestProperty("Upload-Metadata", encodedMetadata);
        }

        // If the defer-length feature is disabled and the total upload size is not known the "Upload-Length"
        // header is set to "0" (backward compatible behaviour!)
        if (!isDeferLengthEnabled()
                || upload.isSizeConfigured()) {
            connection.addRequestProperty("Upload-Length", Long.toString(upload.getSize()));
        } else {
            connection.addRequestProperty("Upload-Defer-Length", "1");
        }
        boolean uploadLengthHeaderSet = connection.getRequestProperties().containsKey("Upload-Length");
        connection.connect();

        int responseCode = connection.getResponseCode();
        if(!(responseCode >= 200 && responseCode < 300)) {
            throw new ProtocolException("unexpected status code (" + responseCode + ") while creating upload", connection);
        }

        String urlStr = connection.getHeaderField("Location");
        if(urlStr == null || urlStr.length() == 0) {
            throw new ProtocolException("missing upload URL in response for creating upload", connection);
        }

        // The upload URL must be relative to the URL of the request by which is was returned,
        // not the upload creation URL. In most cases, there is no difference between those two
        // but there may be cases in which the POST request is redirected.
        URL uploadURL = new URL(connection.getURL(), urlStr);

        if(resumingEnabled) {
            urlStore.set(upload.getFingerprint(), uploadURL);
        }

        if (isDeferLengthEnabled()) {
            if (uploadLengthHeaderSet) {
                getSizeStore().set(uploadURL, upload.getSize());
            }
        }

        return new TusUploader(this, uploadURL, upload.getTusInputStream(), 0);
    }

    /**
     * Try to resume an already started upload. Before call this function, resuming must be
     * enabled using {@link #enableResuming(TusURLStore)}. This method will look up the URL for this
     * upload in the {@link TusURLStore} using the upload's fingerprint (see
     * {@link TusUpload#getFingerprint()}). After a successful lookup a HEAD request will be issued
     * to find the current offset without uploading the file, yet.
     *
     * @param upload The file for which an upload will be resumed
     * @return Use {@link TusUploader} to upload the remaining file's chunks.
     * @throws FingerprintNotFoundException Thrown if no matching fingerprint has been found in
     * {@link TusURLStore}. Use {@link #createUpload(TusUpload)} to create a new upload.
     * @throws ResumingNotEnabledException Throw if resuming has not been enabled using {@link
     * #enableResuming(TusURLStore)}.
     * @throws ProtocolException Thrown if the remote server sent an unexpected response, e.g.
     * wrong status codes or missing/invalid headers.
     * @throws IOException Thrown if an exception occurs while issuing the HTTP request.
     */
    public TusUploader resumeUpload(@NotNull TusUpload upload) throws FingerprintNotFoundException, ResumingNotEnabledException, ProtocolException, IOException {
        if(!resumingEnabled) {
            throw new ResumingNotEnabledException();
        }

        URL uploadURL = urlStore.get(upload.getFingerprint());
        if(uploadURL == null) {
            throw new FingerprintNotFoundException(upload.getFingerprint());
        }

        HttpURLConnection connection = (HttpURLConnection) uploadURL.openConnection();
        connection.setRequestMethod("HEAD");
        prepareConnection(connection);
        connection.connect();

        int responseCode = connection.getResponseCode();
        if(!(responseCode >= 200 && responseCode < 300)) {
            throw new ProtocolException("unexpected status code (" + responseCode + ") while resuming upload", connection);
        }

        String offsetStr = connection.getHeaderField("Upload-Offset");
        if(offsetStr == null || offsetStr.length() == 0) {
            throw new ProtocolException("missing upload offset in response for resuming upload", connection);
        }

        long offset = Long.parseLong(offsetStr);

        // keep the client in sync with server regarding "Upload-Length" header
        String deferLength = connection.getHeaderField("Upload-Defer-Length");
        String uploadLength = connection.getHeaderField("Upload-Length");
        if (deferLength == null && uploadLength == null
                || deferLength != null && uploadLength != null) {
            throw new ProtocolException("'HEAD' request must return a header named either 'Upload-Defer-Length' or 'Upload-Length'");
        }

        Long uploadSize = null;
        if (isDeferLengthEnabled()) {
            // keep the client in sync (respect the server and not the client state!)
            sizeStore.remove(uploadURL);
            if (uploadLength != null) {
                sizeStore.set(uploadURL, Long.parseLong(uploadLength));
            } else {
                // upload size is unknown to server, but maybe it can be set now
                if (upload.isSizeConfigured()) {
                    uploadSize = upload.getSize();
                }
            }
        }

        return new TusUploader(this, uploadURL, upload.getTusInputStream(), offset, uploadSize);
    }

    /**
     * Try to resume an upload using {@link #resumeUpload(TusUpload)}. If the method call throws
     * an {@link ResumingNotEnabledException} or {@link FingerprintNotFoundException}, a new upload
     * will be created using {@link #createUpload(TusUpload)}.
     *
     * @param upload The file for which an upload will be resumed
     * @throws ProtocolException Thrown if the remote server sent an unexpected response, e.g.
     * wrong status codes or missing/invalid headers.
     * @throws IOException Thrown if an exception occurs while issuing the HTTP request.
     */
    public TusUploader resumeOrCreateUpload(@NotNull TusUpload upload) throws ProtocolException, IOException {
        try {
            return resumeUpload(upload);
        } catch(FingerprintNotFoundException e) {
            return createUpload(upload);
        } catch(ResumingNotEnabledException e) {
            return createUpload(upload);
        } catch(ProtocolException e) {
            // If the attempt to resume returned a 404 Not Found, we immediately try to create a new
            // one since TusExectuor would not retry this operation.
            HttpURLConnection connection = e.getCausingConnection();
            if(connection != null && connection.getResponseCode() == 404) {
                return createUpload(upload);
            }

            throw e;
        }
    }

    /**
     * Set headers used for every HTTP request. Currently, this will add the Tus-Resumable header
     * and any custom header which can be configured using {@link #setHeaders(Map)},
     *
     * @param connection The connection whose headers will be modified.
     */
    public void prepareConnection(@NotNull HttpURLConnection connection) {
        // Only follow redirects, if the POST methods is preserved. If http.strictPostRedirect is
        // disabled, a POST request will be transformed into a GET request which is not wanted by us.
        // See: http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/7u40-b43/sun/net/www/protocol/http/HttpURLConnection.java#2372
        connection.setInstanceFollowRedirects(Boolean.getBoolean("http.strictPostRedirect"));

        connection.setConnectTimeout(connectTimeout);
        connection.addRequestProperty("Tus-Resumable", TUS_VERSION);

        if(headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.addRequestProperty(entry.getKey(), entry.getValue());
            }
        }
    }
}
