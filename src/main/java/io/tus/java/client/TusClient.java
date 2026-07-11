package io.tus.java.client;

import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class is used for creating or resuming uploads.
 */
public class TusClient {
    /**
     * Version of the tus protocol used by the client. The remote server needs to support this
     * version, too.
     */
    public static final String TUS_VERSION = TusProtocol.DEFAULT_PROTOCOL_VERSION;

    private URL uploadCreationURL;
    private Proxy proxy;
    private boolean resumingEnabled;
    private boolean removeFingerprintOnSuccessEnabled;
    private boolean addRequestId;
    private TusURLStore urlStore;
    private Map<String, String> headers;
    private String protocol = TusProtocol.DEFAULT_CLIENT_PROTOCOL;
    private int connectTimeout = 5000;
    private TusRequestLifecycleHooks requestLifecycleHooks;
    private volatile HttpURLConnection currentConnection;

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
     * Get the current upload creation URL.
     *
     * @return Current upload creation URL
     */
    public URL getUploadCreationURL() {
        return uploadCreationURL;
    }

    /**
     * Set the proxy that will be used for all requests.
     *
     * @param proxy Proxy to use
     */
    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    /**
     * Get the current proxy used for all requests.
     *
     * @return Current proxy
     */
    public Proxy getProxy() {
        return proxy;
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
     * Enable removing fingerprints after a successful upload.
     *
     * @see #disableRemoveFingerprintOnSuccess()
     */
    public void enableRemoveFingerprintOnSuccess() {
        removeFingerprintOnSuccessEnabled = true;
    }

    /**
     * Disable removing fingerprints after a successful upload.
     *
     * @see #enableRemoveFingerprintOnSuccess()
     */
    public void disableRemoveFingerprintOnSuccess() {
        removeFingerprintOnSuccessEnabled = false;
    }

    /**
     * Get the current status if removing fingerprints after a successful upload.
     *
     * @see #enableRemoveFingerprintOnSuccess()
     * @see #disableRemoveFingerprintOnSuccess()
     *
     * @return True if resuming has been enabled using {@link #enableResuming(TusURLStore)}
     */
    public boolean removeFingerprintOnSuccessEnabled() {
        return removeFingerprintOnSuccessEnabled;
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

    /**
     * Select the TUS client protocol mode used for generated protocol headers.
     *
     * @param protocol The protocol mode, e.g. {@code tus-v1} or {@code ietf-draft-05}.
     */
    public void setProtocol(@Nullable String protocol) {
        final String normalizedProtocol = TusProtocol.normalizeProtocol(protocol);
        if (normalizedProtocol == null) {
            throw new IllegalArgumentException(
                    TusProtocol.START_OPTION_VALIDATION_UNSUPPORTED_PROTOCOL_PREFIX + protocol
            );
        }

        this.protocol = normalizedProtocol;
    }

    /**
     * Return the selected TUS client protocol mode.
     *
     * @return The selected protocol mode.
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Enable generated request IDs for every HTTP request made by this TusClient instance.
     */
    public void enableRequestIdHeader() {
        addRequestId = true;
    }

    /**
     * Disable generated request IDs for every HTTP request made by this TusClient instance.
     */
    public void disableRequestIdHeader() {
        addRequestId = false;
    }

    /**
     * Get the current generated request ID header setting.
     *
     * @return True if generated request IDs are enabled.
     */
    public boolean requestIdHeaderEnabled() {
        return addRequestId;
    }

    /**
     * Set request lifecycle callbacks for every HTTP request/response pair.
     *
     * @param requestLifecycleHooks Hooks to invoke, or null to disable hooks.
     */
    public void setRequestLifecycleHooks(@Nullable TusRequestLifecycleHooks requestLifecycleHooks) {
        this.requestLifecycleHooks = requestLifecycleHooks;
    }

    /**
     * Get the configured request lifecycle callbacks.
     *
     * @return The configured lifecycle hooks or null.
     */
    @Nullable
    public TusRequestLifecycleHooks getRequestLifecycleHooks() {
        return requestLifecycleHooks;
    }

    /**
     * Sets the timeout for a Connection.
     * @param timeout in milliseconds
     */
    public void setConnectTimeout(int timeout) {
        connectTimeout = timeout;
    }

    /**
     * Returns the Connection Timeout.
     * @return Timeout in milliseconds.
     */
    public int getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * Validate TUS start options before issuing any HTTP request.
     *
     * @param options The start options to validate.
     * @throws IllegalArgumentException Thrown when the options contain a known conflict.
     */
    public void validateStartOptions(@NotNull TusStartOptions options) {
        TusStartOptionValidator.validate(options);
    }

    /**
     * Abort the currently active request, if any.
     */
    public void abortUpload() {
        abortCurrentRequest();
    }

    /**
     * Abort an upload and optionally terminate the remote upload resource.
     *
     * @param uploader Uploader whose active request and upload URL should be aborted, or null to
     *                 only abort the current request tracked by this client.
     * @param terminateUpload True to issue a Termination request when the upload URL is known.
     * @throws ProtocolException Thrown if the termination request receives an unexpected response.
     * @throws IOException Thrown if the termination request fails.
     */
    public void abortUpload(@Nullable TusUploader uploader, boolean terminateUpload)
            throws ProtocolException, IOException {
        if (uploader == null) {
            abortCurrentRequest();
            return;
        }

        uploader.abort();
        if (!terminateUpload) {
            return;
        }

        terminateUpload(uploader.getUploadURL()).disconnect();
        removeStoredUpload(uploader.getUpload());
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
        return createUpload(upload, 0);
    }

    /**
     * Create a partial upload using the Concatenation extension.
     *
     * @param upload The partial upload source.
     * @return Use {@link TusUploader} to upload the partial file bytes.
     * @throws ProtocolException Thrown if the remote server sent an unexpected response, e.g.
     * wrong status codes or missing/invalid headers.
     * @throws IOException Thrown if an exception occurs while issuing the HTTP request.
     */
    public TusUploader createPartialUpload(@NotNull TusUpload upload)
            throws ProtocolException, IOException {
        return createUpload(upload, 0, true);
    }

    /**
     * Create a new upload and send the first bytes in the creation request using the
     * Creation With Upload extension. Before calling this function, an "upload creation URL"
     * must be defined using {@link #setUploadCreationURL(URL)} or else this function will fail.
     *
     * @param upload The file for which a new upload will be created
     * @param bytesToUpload Number of bytes to include in the creation request body
     * @return Use {@link TusUploader} to upload any remaining file chunks.
     * @throws ProtocolException Thrown if the remote server sent an unexpected response, e.g.
     * wrong status codes or missing/invalid headers.
     * @throws IOException Thrown if an exception occurs while issuing the HTTP request.
     */
    public TusUploader createUploadWithData(
            @NotNull TusUpload upload,
            int bytesToUpload
    ) throws ProtocolException, IOException {
        if (bytesToUpload < 0) {
            throw new IllegalArgumentException("creation upload byte count must not be negative");
        }
        if (bytesToUpload == 0) {
            return createUpload(upload);
        }
        if (upload.isUploadLengthDeferred()) {
            throw new IllegalArgumentException(
                    "creation with upload requires a known upload length"
            );
        }
        if (bytesToUpload > upload.getSize()) {
            throw new IllegalArgumentException(
                    "creation upload byte count "
                            + bytesToUpload
                            + " exceeds upload size "
                            + upload.getSize()
            );
        }

        return createUpload(upload, bytesToUpload);
    }

    private TusUploader createUpload(
            @NotNull TusUpload upload,
            int bytesToUpload
    ) throws ProtocolException, IOException {
        return createUpload(upload, bytesToUpload, false);
    }

    private TusUploader createUpload(
            @NotNull TusUpload upload,
            int bytesToUpload,
            boolean partialUpload
    ) throws ProtocolException, IOException {
        HttpURLConnection connection = openConnection(uploadCreationURL);
        connection.setRequestMethod(TusProtocol.CREATE_UPLOAD_METHOD);
        prepareConnection(connection);
        if (partialUpload) {
            connection.setRequestProperty(
                    TusProtocol.CONCATENATION_HEADER_NAME,
                    TusProtocol.CONCATENATION_PARTIAL_VALUE
            );
        }
        prepareUploadCreationHeaders(connection, upload);

        if (bytesToUpload > 0) {
            prepareUploadBodyHeaders(connection, bytesToUpload >= upload.getSize());
            connection.setDoOutput(true);
            connection.setFixedLengthStreamingMode(bytesToUpload);
        }

        registerCurrentRequest(connection);
        try {
            runBeforeRequest(TusProtocol.CREATE_UPLOAD_METHOD, connection);
            TusRequestSnapshot requestSnapshot = TusRequestSnapshot.fromConnection(connection);
            try {
                if (bytesToUpload > 0) {
                    writeUploadCreationData(connection, upload, bytesToUpload);
                } else {
                    connection.connect();
                }
            } catch (IOException error) {
                throw TusDetailedErrors.requestException(
                        TusProtocol.DETAILED_ERROR_CREATE_UPLOAD_REQUEST_FAILED,
                        requestSnapshot,
                        error
                );
            }

            int responseCode;
            try {
                responseCode = connection.getResponseCode();
            } catch (IOException error) {
                throw TusDetailedErrors.requestException(
                        TusProtocol.DETAILED_ERROR_CREATE_UPLOAD_REQUEST_FAILED,
                        requestSnapshot,
                        error
                );
            }
            runAfterResponse(TusProtocol.CREATE_UPLOAD_METHOD, connection);
            if (!TusProtocol.isSuccessfulResponseStatus(responseCode)) {
                throw TusDetailedErrors.responseException(
                        TusProtocol.DETAILED_ERROR_UNEXPECTED_CREATE_RESPONSE,
                        requestSnapshot,
                        connection
                );
            }

            String urlStr = connection.getHeaderField(TusProtocol.LOCATION_HEADER_NAME);
            if (urlStr == null || urlStr.length() == 0) {
                throw new ProtocolException("missing upload URL in response for creating upload", connection);
            }

            // The upload URL must be relative to the URL of the request by which is was returned,
            // not the upload creation URL. In most cases, there is no difference between those two
            // but there may be cases in which the POST request is redirected.
            URL uploadURL = new URL(connection.getURL(), urlStr);

            long offset = bytesToUpload > 0
                    ? readUploadCreationOffset(connection, bytesToUpload)
                    : 0L;

            if (resumingEnabled) {
                urlStore.set(upload.getFingerprint(), uploadURL);
            }

            return createUploader(upload, uploadURL, offset, bytesToUpload > 0);
        } finally {
            clearCurrentRequest(connection);
        }
    }

    /**
     * Create the final upload resource by concatenating partial upload URLs.
     *
     * @param uploadURLs Partial upload URLs in concatenation order.
     * @param metadata Metadata for the final upload, or null.
     * @return The final upload URL.
     * @throws ProtocolException Thrown if the remote server sent an unexpected response, e.g.
     * wrong status codes or missing/invalid headers.
     * @throws IOException Thrown if an exception occurs while issuing the HTTP request.
     */
    public URL concatenateUploads(
            @NotNull List<URL> uploadURLs,
            @Nullable Map<String, String> metadata
    ) throws ProtocolException, IOException {
        if (uploadURLs.isEmpty()) {
            throw new IllegalArgumentException("at least one partial upload URL is required");
        }

        HttpURLConnection connection = openConnection(uploadCreationURL);
        connection.setRequestMethod(TusProtocol.CREATE_UPLOAD_METHOD);
        prepareConnection(connection);
        connection.setRequestProperty(
                TusProtocol.CONCATENATION_HEADER_NAME,
                finalUploadConcatValue(uploadURLs)
        );
        prepareUploadMetadataHeaders(connection, metadata);

        registerCurrentRequest(connection);
        try {
            runBeforeRequest(TusProtocol.CREATE_UPLOAD_METHOD, connection);
            TusRequestSnapshot requestSnapshot = TusRequestSnapshot.fromConnection(connection);
            try {
                connection.connect();
            } catch (IOException error) {
                throw TusDetailedErrors.requestException(
                        TusProtocol.DETAILED_ERROR_CREATE_UPLOAD_REQUEST_FAILED,
                        requestSnapshot,
                        error
                );
            }

            int responseCode;
            try {
                responseCode = connection.getResponseCode();
            } catch (IOException error) {
                throw TusDetailedErrors.requestException(
                        TusProtocol.DETAILED_ERROR_CREATE_UPLOAD_REQUEST_FAILED,
                        requestSnapshot,
                        error
                );
            }
            runAfterResponse(TusProtocol.CREATE_UPLOAD_METHOD, connection);
            if (!TusProtocol.isSuccessfulResponseStatus(responseCode)) {
                throw TusDetailedErrors.responseException(
                        TusProtocol.DETAILED_ERROR_UNEXPECTED_CREATE_RESPONSE,
                        requestSnapshot,
                        connection
                );
            }

            String urlStr = connection.getHeaderField(TusProtocol.LOCATION_HEADER_NAME);
            if (urlStr == null || urlStr.length() == 0) {
                throw new ProtocolException(
                        "missing upload URL in response for concatenating uploads",
                        connection
                );
            }

            return new URL(connection.getURL(), urlStr);
        } finally {
            clearCurrentRequest(connection);
        }
    }

    private static void prepareUploadCreationHeaders(
            @NotNull HttpURLConnection connection,
            @NotNull TusUpload upload
    ) {
        String encodedMetadata = upload.getEncodedMetadata();
        if (encodedMetadata.length() > 0) {
            connection.setRequestProperty(TusProtocol.METADATA_HEADER_NAME, encodedMetadata);
        }

        if (upload.isUploadLengthDeferred()) {
            connection.addRequestProperty(TusProtocol.UPLOAD_DEFER_LENGTH_HEADER_NAME, "1");
        } else {
            connection.addRequestProperty(
                    TusProtocol.UPLOAD_LENGTH_HEADER_NAME,
                    Long.toString(upload.getSize())
            );
        }
    }

    private static void prepareUploadMetadataHeaders(
            @NotNull HttpURLConnection connection,
            @Nullable Map<String, String> metadata
    ) {
        String encodedMetadata = TusUpload.encodeMetadata(metadata);
        if (encodedMetadata.length() > 0) {
            connection.setRequestProperty(TusProtocol.METADATA_HEADER_NAME, encodedMetadata);
        }
    }

    private void prepareUploadBodyHeaders(
            @NotNull HttpURLConnection connection,
            boolean requestCompletesUpload
    ) {
        final String contentType = TusProtocol.protocolUploadBodyContentType(protocol);
        if (contentType != null) {
            connection.setRequestProperty(
                    TusProtocol.UPLOAD_BODY_CONTENT_TYPE_HEADER_NAME,
                    contentType
            );
        }

        final String uploadCompleteHeaderName =
                TusProtocol.protocolUploadCompleteHeaderName(protocol);
        if (uploadCompleteHeaderName == null) {
            return;
        }

        connection.setRequestProperty(
                uploadCompleteHeaderName,
                TusProtocol.protocolUploadCompleteHeaderValue(protocol, requestCompletesUpload)
        );
    }

    private static String finalUploadConcatValue(@NotNull List<URL> uploadURLs) {
        StringBuilder value = new StringBuilder(TusProtocol.CONCATENATION_FINAL_PREFIX);
        for (int index = 0; index < uploadURLs.size(); index++) {
            if (index > 0) {
                value.append(TusProtocol.CONCATENATION_UPLOAD_URL_SEPARATOR);
            }
            value.append(uploadURLs.get(index).toString());
        }

        return value.toString();
    }

    private static void writeUploadCreationData(
            @NotNull HttpURLConnection connection,
            @NotNull TusUpload upload,
            int bytesToUpload
    ) throws IOException {
        TusInputStream input = upload.getTusInputStream();

        byte[] buffer = new byte[Math.min(bytesToUpload, 8192)];
        int bytesRemaining = bytesToUpload;
        try (OutputStream output = connection.getOutputStream()) {
            while (bytesRemaining > 0) {
                int bytesRead = input.read(buffer, Math.min(buffer.length, bytesRemaining));
                if (bytesRead == -1) {
                    throw new EOFException(
                            "upload source ended before creation request wrote "
                                    + bytesToUpload
                                    + " bytes"
                    );
                }

                output.write(buffer, 0, bytesRead);
                bytesRemaining -= bytesRead;
            }
        }
    }

    private static long readUploadCreationOffset(
            @NotNull HttpURLConnection connection,
            int bytesToUpload
    ) throws ProtocolException {
        String offsetStr = connection.getHeaderField(TusProtocol.UPLOAD_OFFSET_HEADER_NAME);
        if (offsetStr == null || offsetStr.length() == 0) {
            throw new ProtocolException(
                    "missing upload offset in response for creating upload with data",
                    connection
            );
        }

        long offset;
        try {
            offset = Long.parseLong(offsetStr);
        } catch (NumberFormatException e) {
            throw new ProtocolException(
                    "invalid upload offset in response for creating upload with data",
                    connection
            );
        }

        if (offset != bytesToUpload) {
            throw new ProtocolException(
                    "response contains different Upload-Offset value ("
                            + offset
                            + ") than expected ("
                            + bytesToUpload
                            + ")",
                    connection
            );
        }

        return offset;
    }

    /**
     * Terminate an upload URL using the Termination extension.
     *
     * @param uploadURL The upload location URL to terminate.
     * @return The completed HTTP connection.
     * @throws ProtocolException Thrown if the remote server sent an unexpected response, e.g.
     * wrong status codes.
     * @throws IOException Thrown if an exception occurs while issuing the HTTP request.
     */
    public HttpURLConnection terminateUpload(@NotNull URL uploadURL)
            throws ProtocolException, IOException {
        HttpURLConnection connection = openConnection(uploadURL);
        connection.setRequestMethod(TusProtocol.TERMINATE_UPLOAD_METHOD);
        prepareConnection(connection);

        registerCurrentRequest(connection);
        try {
            runBeforeRequest(TusProtocol.TERMINATE_UPLOAD_METHOD, connection);
            connection.connect();

            int responseCode = connection.getResponseCode();
            runAfterResponse(TusProtocol.TERMINATE_UPLOAD_METHOD, connection);
            if (!TusProtocol.isSuccessfulResponseStatus(responseCode)) {
                throw new ProtocolException(
                        "unexpected status code (" + responseCode + ") while terminating upload",
                        connection);
            }

            return connection;
        } finally {
            clearCurrentRequest(connection);
        }
    }

    /**
     * Opens the HTTP connection used by this client.
     *
     * <p>Subclasses may override this method to provide a custom transport for tests or specialized
     * environments. Implementations should return a fresh {@link HttpURLConnection} for the given
     * URL and must not connect it; callers configure headers, method, and hooks after this method
     * returns.
     *
     * @param uploadURL The request URL.
     * @return A new, unconnected HTTP connection.
     * @throws IOException Thrown if a connection cannot be opened.
     */
    @NotNull
    protected HttpURLConnection openConnection(@NotNull URL uploadURL) throws IOException {
        if (proxy != null) {
            return (HttpURLConnection) uploadURL.openConnection(proxy);
        }
        return (HttpURLConnection) uploadURL.openConnection();
    }

    @NotNull
    private TusUploader createUploader(@NotNull TusUpload upload, @NotNull URL uploadURL, long offset)
        throws IOException {
        return createUploader(upload, uploadURL, offset, false);
    }

    @NotNull
    private TusUploader createUploader(
            @NotNull TusUpload upload,
            @NotNull URL uploadURL,
            long offset,
            boolean inputAlreadyAtOffset
    )
        throws IOException {
        TusUploader uploader = new TusUploader(
                this,
                upload,
                uploadURL,
                upload.getTusInputStream(),
                offset,
                inputAlreadyAtOffset
        );
        uploader.setProxy(proxy);
        return uploader;
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
    public TusUploader resumeUpload(@NotNull TusUpload upload) throws
            FingerprintNotFoundException, ResumingNotEnabledException, ProtocolException, IOException {
        if (!resumingEnabled) {
            throw new ResumingNotEnabledException();
        }

        URL uploadURL = urlStore.get(upload.getFingerprint());
        if (uploadURL == null) {
            throw new FingerprintNotFoundException(upload.getFingerprint());
        }

        return beginOrResumeUploadFromURL(upload, uploadURL);
    }

    /**
     * Begin an upload or alternatively resume it if the upload has already been started before. In contrast to
     * {@link #createUpload(TusUpload)} and {@link #resumeOrCreateUpload(TusUpload)} this method will not create a new
     * upload. The user must obtain the upload location URL on their own as this method will not send the POST request
     * which is normally used to create a new upload.
     * Therefore, this method is only useful if you are uploading to a service which takes care of creating the tus
     * upload for yourself. One example of such a service is the Vimeo API.
     * When called a HEAD request will be issued to find the current offset without uploading the file, yet.
     * The uploading can be started by using the returned {@link TusUploader} object.
     *
     * @param upload The file for which an upload will be resumed
     * @param uploadURL The upload location URL at which has already been created and this file should be uploaded to.
     * @return Use {@link TusUploader} to upload the remaining file's chunks.
     * @throws ProtocolException Thrown if the remote server sent an unexpected response, e.g.
     * wrong status codes or missing/invalid headers.
     * @throws IOException Thrown if an exception occurs while issuing the HTTP request.
     */
    public TusUploader beginOrResumeUploadFromURL(@NotNull TusUpload upload, @NotNull URL uploadURL) throws
            ProtocolException, IOException {
        HttpURLConnection connection = openConnection(uploadURL);
        connection.setRequestMethod(TusProtocol.OFFSET_DISCOVERY_METHOD);
        prepareConnection(connection);

        registerCurrentRequest(connection);
        try {
            runBeforeRequest(TusProtocol.OFFSET_DISCOVERY_METHOD, connection);
            connection.connect();

            int responseCode = connection.getResponseCode();
            runAfterResponse(TusProtocol.OFFSET_DISCOVERY_METHOD, connection);
            if (!TusProtocol.isSuccessfulResponseStatus(responseCode)) {
                throw new ProtocolException(
                        "unexpected status code (" + responseCode + ") while resuming upload", connection);
            }

            String offsetStr = connection.getHeaderField(TusProtocol.UPLOAD_OFFSET_HEADER_NAME);
            if (offsetStr == null || offsetStr.length() == 0) {
                throw new ProtocolException("missing upload offset in response for resuming upload", connection);
            }
            long offset = Long.parseLong(offsetStr);

            return createUploader(upload, uploadURL, offset);
        } finally {
            clearCurrentRequest(connection);
        }
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
     * @return {@link TusUploader} instance.
     */
    public TusUploader resumeOrCreateUpload(@NotNull TusUpload upload) throws ProtocolException, IOException {
        try {
            return resumeUpload(upload);
        } catch (FingerprintNotFoundException e) {
            return createUpload(upload);
        } catch (ResumingNotEnabledException e) {
            return createUpload(upload);
        } catch (ProtocolException e) {
            // If the attempt to resume returned a 404 Not Found, we immediately try to create a new
            // one since TusExectuor would not retry this operation.
            HttpURLConnection connection = e.getCausingConnection();
            if (connection != null && connection.getResponseCode() == 404) {
                return createUpload(upload);
            }

            throw e;
        }
    }

    /**
     * Set headers used for every HTTP request. Currently, this will add generated protocol default
     * headers and any custom header which can be configured using {@link #setHeaders(Map)},
     *
     * @param connection The connection whose headers will be modified.
     */
    public void prepareConnection(@NotNull HttpURLConnection connection) {
        // Only follow redirects, if the POST methods is preserved. If http.strictPostRedirect is
        // disabled, a POST request will be transformed into a GET request which is not wanted by us.

        // CHECKSTYLE:OFF
        // LineLength - Necessary because of length of the link
        // See:https://github.com/openjdk/jdk/blob/jdk7-b43/jdk/src/share/classes/sun/net/www/protocol/http/HttpURLConnection.java#L2020-L2035
        // CHECKSTYLE:ON
        connection.setInstanceFollowRedirects(Boolean.getBoolean("http.strictPostRedirect"));

        connection.setConnectTimeout(connectTimeout);
        TusProtocol.prepareRequestHeaders(connection, headers, addRequestId, protocol);
    }

    final void runBeforeRequest(
            @NotNull String method, @NotNull HttpURLConnection connection
    ) throws IOException {
        if (requestLifecycleHooks == null || requestLifecycleHooks.getBeforeRequest() == null) {
            return;
        }

        requestLifecycleHooks.getBeforeRequest().beforeRequest(
                new TusRequestLifecycleHooks.RequestContext(method, connection)
        );
    }

    final void runAfterResponse(
            @NotNull String method, @NotNull HttpURLConnection connection
    ) throws IOException {
        if (requestLifecycleHooks == null || requestLifecycleHooks.getAfterResponse() == null) {
            return;
        }

        requestLifecycleHooks.getAfterResponse().afterResponse(
                new TusRequestLifecycleHooks.RequestContext(method, connection)
        );
    }

    final void registerCurrentRequest(@NotNull HttpURLConnection connection) {
        currentConnection = connection;
    }

    final void clearCurrentRequest(@NotNull HttpURLConnection connection) {
        if (currentConnection == connection) {
            currentConnection = null;
        }
    }

    private void abortCurrentRequest() {
        HttpURLConnection connection = currentConnection;
        if (connection != null) {
            connection.disconnect();
        }
    }

    private void removeStoredUpload(@NotNull TusUpload upload) {
        if (!resumingEnabled) {
            return;
        }

        urlStore.remove(upload.getFingerprint());
    }

    /**
     * Actions to be performed after a successful upload completion.
     * Manages URL removal from the URL store if remove fingerprint on success is enabled
     *
     * @param upload that has been finished
     */
    protected void uploadFinished(@NotNull TusUpload upload) {
        if (resumingEnabled && removeFingerprintOnSuccessEnabled) {
            urlStore.remove(upload.getFingerprint());
        }
    }
}
