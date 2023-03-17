package io.tus.java.client;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

/**
 * This class is used for doing the actual upload of the files. Instances are returned by
 * {@link TusClient#createUpload(TusUpload)}, {@link TusClient#createUpload(TusUpload)} and
 * {@link TusClient#resumeOrCreateUpload(TusUpload)}.
 * <br>
 * After obtaining an instance you can upload a file by following these steps:
 * <ol>
 *  <li>Upload a chunk using {@link #uploadChunk()}</li>
 *  <li>Optionally get the new offset ({@link #getOffset()} to calculate the progress</li>
 *  <li>Repeat step 1 until the {@link #uploadChunk()} returns -1</li>
 *  <li>Close HTTP connection and InputStream using {@link #finish()} to free resources</li>
 * </ol>
 */
public class TusUploader {
    private URL uploadURL;
    private Proxy proxy;
    private TusInputStream input;
    private long offset;
    private TusClient client;
    private TusUpload upload;
    private byte[] buffer;
    private int requestPayloadSize = 10 * 1024 * 1024;
    private int bytesRemainingForRequest;

    private HttpURLConnection connection;
    private OutputStream output;

    /**
     * Begin a new upload request by opening a PATCH request to specified upload URL. After this
     * method returns a connection will be ready and you can upload chunks of the file.
     *
     * @param client Used for preparing a request ({@link TusClient#prepareConnection(HttpURLConnection)}
     * @param upload {@link TusUpload} to be uploaded.
     * @param uploadURL URL to send the request to
     * @param input Stream to read (and seek) from and upload to the remote server
     * @param offset Offset to read from
     * @throws IOException Thrown if an exception occurs while issuing the HTTP request.
     */
    public TusUploader(TusClient client, TusUpload upload, URL uploadURL, TusInputStream input, long offset)
        throws IOException {
        this.uploadURL = uploadURL;
        this.input = input;
        this.offset = offset;
        this.client = client;
        this.upload = upload;

        input.seekTo(offset);

        setChunkSize(2 * 1024 * 1024);
    }

    private void openConnection() throws IOException, ProtocolException {
        // Only open a connection, if we have none open.
        if (connection != null) {
            return;
        }

        bytesRemainingForRequest = requestPayloadSize;
        input.mark(requestPayloadSize);

        if (proxy != null) {
            connection = (HttpURLConnection) uploadURL.openConnection(proxy);
        } else {
            connection = (HttpURLConnection) uploadURL.openConnection();
        }
        client.prepareConnection(connection);
        connection.setRequestProperty("Upload-Offset", Long.toString(offset));
        connection.setRequestProperty("Content-Type", "application/offset+octet-stream");
        connection.setRequestProperty("Expect", "100-continue");

        try {
            connection.setRequestMethod("PATCH");
            // Check whether we are running on a buggy JRE
        } catch (java.net.ProtocolException pe) {
            connection.setRequestMethod("POST");
            connection.setRequestProperty("X-HTTP-Method-Override", "PATCH");
        }

        connection.setDoOutput(true);
        connection.setChunkedStreamingMode(0);
        try {
            output = connection.getOutputStream();
        } catch (java.net.ProtocolException pe) {
            // If we already have a response code available, our expectation using the "Expect: 100-
            // continue" header failed and we should handle this response.
            if (connection.getResponseCode() != -1) {
                finish();
            }

            throw pe;
        }
    }

    /**
     * Sets the used chunk size. This number is used by {@link #uploadChunk()} to indicate how
     * much data is uploaded in a single take. When choosing a value for this parameter you need to
     * consider that uploadChunk() will only return once the specified number of bytes has been
     * sent. For slow internet connections this may take a long time. In addition, a buffer with
     * the chunk size is allocated and kept in memory.
     *
     * @param size The new chunk size
     */
    public void setChunkSize(int size) {
        buffer = new byte[size];
    }

    /**
     * Returns the current chunk size set using {@link #setChunkSize(int)}.
     *
     * @return Current chunk size
     */
    public int getChunkSize() {
        return buffer.length;
    }

    /**
     * Set the maximum payload size for a single request counted in bytes. This is useful for splitting
     * bigger uploads into multiple requests. For example, if you have a resource of 2MB and
     * the payload size set to 1MB, the upload will be transferred by two requests of 1MB each.
     *
     * The default value for this setting is 10 * 1024 * 1024 bytes (10 MiB).
     *
     * Be aware that setting a low maximum payload size (in the low megabytes or even less range) will result in
     * decreased performance since more requests need to be used for an upload. Each request will come with its overhead
     * in terms of longer upload times.
     *
     * Be aware that setting a high maximum payload size may result in a high memory usage since
     * tus-java-client usually allocates a buffer with the maximum payload size (this buffer is used
     * to allow retransmission of lost data if necessary). If the client is running on a memory-
     * constrained device (e.g. mobile app) and the maximum payload size is too high, it might
     * result in an {@link OutOfMemoryError}.
     *
     * This method must not be called when the uploader has currently an open connection to the
     * remote server. In general, try to set the payload size before invoking {@link #uploadChunk()}
     * the first time.
     *
     * @see #getRequestPayloadSize()
     *
     * @param size Number of bytes for a single payload
     * @throws IllegalStateException Thrown if the uploader currently has a connection open
     */
    public void setRequestPayloadSize(int size) throws IllegalStateException {
        if (connection != null) {
            throw new IllegalStateException("payload size for a single request must not be "
                    + "modified as long as a request is in progress");
        }

        requestPayloadSize = size;
    }

    /**
     * Get the current maximum payload size for a single request.
     *
     * @see #setChunkSize(int)
     *
     * @return Number of bytes for a single payload
     */
    public int getRequestPayloadSize() {
        return requestPayloadSize;
    }

    /**
     * Upload a part of the file by reading a chunk from the InputStream and writing
     * it to the HTTP request's body. If the number of available bytes is lower than the chunk's
     * size, all available bytes will be uploaded and nothing more.
     * No new connection will be established when calling this method, instead the connection opened
     * in the previous calls will be used.
     * The size of the read chunk can be obtained using {@link #getChunkSize()} and changed
     * using {@link #setChunkSize(int)}.
     * In order to obtain the new offset, use {@link #getOffset()} after this method returns.
     *
     * @return Number of bytes read and written.
     * @throws IOException  Thrown if an exception occurs while reading from the source or writing
     *                      to the HTTP request.
     */
    public int uploadChunk() throws IOException, ProtocolException {
        openConnection();

        int bytesToRead = Math.min(getChunkSize(), bytesRemainingForRequest);

        int bytesRead = input.read(buffer, bytesToRead);
        if (bytesRead == -1) {
            // No bytes were read since the input stream is empty
            return -1;
        }

        // Do not write the entire buffer to the stream since the array will
        // be filled up with 0x00s if the number of read bytes is lower then
        // the chunk's size.
        output.write(buffer, 0, bytesRead);
        output.flush();

        offset += bytesRead;
        bytesRemainingForRequest -= bytesRead;

        if (bytesRemainingForRequest <= 0) {
            finishConnection();
        }

        return bytesRead;
    }

    /**
     * Upload a part of the file by read a chunks specified size from the InputStream and writing
     * it to the HTTP request's body. If the number of available bytes is lower than the chunk's
     * size, all available bytes will be uploaded and nothing more.
     * No new connection will be established when calling this method, instead the connection opened
     * in the previous calls will be used.
     * In order to obtain the new offset, use {@link #getOffset()} after this method returns.
     *
     * This method ignored the payload size per request, which may be set using
     * {@link #setRequestPayloadSize(int)}. Please, use {@link #uploadChunk()} instead.
     *
     * @deprecated This method is inefficient and has been replaced by {@link #setChunkSize(int)}
     *             and {@link #uploadChunk()} and should not be used anymore. The reason is, that
     *             this method allocates a new buffer with the supplied chunk size for each time
     *             it's called without reusing it. This results in a high number of memory
     *             allocations and should be avoided. The new methods do not have this issue.
     *
     * @param chunkSize Maximum number of bytes which will be uploaded. When choosing a value
     *                  for this parameter you need to consider that the method call will only
     *                  return once the specified number of bytes have been sent. For slow
     *                  internet connections this may take a long time.
     * @return Number of bytes read and written.
     * @throws IOException  Thrown if an exception occurs while reading from the source or writing
     *                      to the HTTP request.
     */
    @Deprecated public int uploadChunk(int chunkSize) throws IOException, ProtocolException {
        openConnection();

        byte[] buf = new byte[chunkSize];
        int bytesRead = input.read(buf, chunkSize);
        if (bytesRead == -1) {
            // No bytes were read since the input stream is empty
            return -1;
        }

        // Do not write the entire buffer to the stream since the array will
        // be filled up with 0x00s if the number of read bytes is lower then
        // the chunk's size.
        output.write(buf, 0, bytesRead);
        output.flush();

        offset += bytesRead;

        return bytesRead;
    }

    /**
     * Get the current offset for the upload. This is the number of all bytes uploaded in total and
     * in all requests (not only this one). You can use it in conjunction with
     * {@link TusUpload#getSize()} to calculate the progress.
     *
     * @return The upload's current offset.
     */
    public long getOffset() {
        return offset;
    }

    /**
     * This methods returns the destination {@link URL} of the upload.
     * @return The {@link URL} of the upload.
     */
    public URL getUploadURL() {
        return uploadURL;
    }

    /**
     * Set the proxy that will be used when uploading.
     *
     * @param proxy Proxy to use
     */
    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    /**
     * This methods returns the proxy used when uploading.
     *
     * @return The {@link Proxy} used for the upload or null when not set.
     */
    public Proxy getProxy() {
        return proxy;
    }

    /**
     * Finish the request by closing the HTTP connection and the InputStream.
     * You can call this method even before the entire file has been uploaded. Use this behavior to
     * enable pausing uploads.
     * This method is equivalent to calling {@code finish(false)}.
     *
     * @throws ProtocolException Thrown if the server sends an unexpected status
     * code
     * @throws IOException  Thrown if an exception occurs while cleaning up.
     */
    public void finish() throws ProtocolException, IOException {
        finish(true);
    }

    /**
     * Finish the request by closing the HTTP connection. You can choose whether to close the InputStream or not.
     * You can call this method even before the entire file has been uploaded. Use this behavior to
     * enable pausing uploads.
     * Be aware that it doesn't automatically release local resources if {@code closeStream == false} and you do
     * not close the InputStream on your own. To be safe use {@link TusUploader#finish()}.
     * @param closeInputStream Determines whether the InputStream is closed with the HTTP connection. Not closing the
     *                         Input Stream may be useful for future upload a future continuation of the upload.
     * @throws ProtocolException Thrown if the server sends an unexpected status code
     * @throws IOException  Thrown if an exception occurs while cleaning up.
     */
    public void finish(boolean closeInputStream) throws ProtocolException, IOException {
        finishConnection();
        if (upload.getSize() == offset) {
            client.uploadFinished(upload);
        }

        // Close the TusInputStream after checking the response and closing the connection to ensure
        // that we will not need to read from it again in the future.
        if (closeInputStream) {
            input.close();
        }
    }

    private void finishConnection() throws ProtocolException, IOException {
        if (output != null) {
            output.close();
        }

        if (connection != null) {
            int responseCode = connection.getResponseCode();
            connection.disconnect();

            if (!(responseCode >= 200 && responseCode < 300)) {
                throw new ProtocolException("unexpected status code (" + responseCode + ") while uploading chunk",
                        connection);
            }

            // TODO detect changes and seek accordingly
            long serverOffset = getHeaderFieldLong(connection, "Upload-Offset");
            if (serverOffset == -1) {
                throw new ProtocolException("response to PATCH request contains no or invalid Upload-Offset header",
                        connection);
            }
            if (offset != serverOffset) {
                throw new ProtocolException(
                        String.format("response contains different Upload-Offset value (%d) than expected (%d)",
                                serverOffset,
                                offset),
                        connection);
            }

            connection = null;
        }
    }

    private long getHeaderFieldLong(URLConnection connection, String field) {
        String value = connection.getHeaderField(field);
        if (value == null) {
            return -1;
        }

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
