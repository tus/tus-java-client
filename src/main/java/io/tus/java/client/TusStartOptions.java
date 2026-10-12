package io.tus.java.client;

import org.jetbrains.annotations.Nullable;

import java.net.URL;

/**
 * Options that can be validated before starting a TUS upload.
 */
public final class TusStartOptions {
    private URL endpointURL;
    private int parallelUploadBoundariesCount = -1;
    private int parallelUploads = TusProtocol.DEFAULT_PARALLEL_UPLOADS;
    private String protocol = TusProtocol.DEFAULT_PROTOCOL_VERSION;
    private TusUpload upload;
    private boolean uploadDataDuringCreation;
    private boolean uploadLengthDeferred;
    private Long uploadSize;
    private URL uploadURL;

    /**
     * Create default TUS start options.
     */
    public TusStartOptions() {
    }

    /**
     * Return the endpoint URL used to create a new upload.
     *
     * @return The endpoint URL, or null.
     */
    @Nullable
    public URL getEndpointURL() {
        return endpointURL;
    }

    /**
     * Set the endpoint URL used to create a new upload.
     *
     * @param endpointURL The endpoint URL, or null.
     */
    public void setEndpointURL(@Nullable URL endpointURL) {
        this.endpointURL = endpointURL;
    }

    /**
     * Return the number of explicit parallel upload boundaries.
     *
     * @return The boundary count, or -1 when unset.
     */
    public int getParallelUploadBoundariesCount() {
        return parallelUploadBoundariesCount;
    }

    /**
     * Set the number of explicit parallel upload boundaries.
     *
     * @param parallelUploadBoundariesCount The boundary count, or -1 when unset.
     */
    public void setParallelUploadBoundariesCount(int parallelUploadBoundariesCount) {
        this.parallelUploadBoundariesCount = parallelUploadBoundariesCount;
    }

    /**
     * Return the configured parallel upload count.
     *
     * @return The parallel upload count.
     */
    public int getParallelUploads() {
        return parallelUploads;
    }

    /**
     * Set the configured parallel upload count.
     *
     * @param parallelUploads The parallel upload count.
     */
    public void setParallelUploads(int parallelUploads) {
        this.parallelUploads = parallelUploads;
    }

    /**
     * Return the requested TUS protocol version.
     *
     * @return The protocol version.
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Set the requested TUS protocol version.
     *
     * @param protocol The protocol version.
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    /**
     * Return the upload source.
     *
     * @return The upload source, or null.
     */
    @Nullable
    public TusUpload getUpload() {
        return upload;
    }

    /**
     * Set the upload source.
     *
     * @param upload The upload source, or null.
     */
    public void setUpload(@Nullable TusUpload upload) {
        this.upload = upload;
    }

    /**
     * Return whether bytes should be sent during creation.
     *
     * @return True when creation with upload is requested.
     */
    public boolean isUploadDataDuringCreation() {
        return uploadDataDuringCreation;
    }

    /**
     * Set whether bytes should be sent during creation.
     *
     * @param uploadDataDuringCreation True to request creation with upload.
     */
    public void setUploadDataDuringCreation(boolean uploadDataDuringCreation) {
        this.uploadDataDuringCreation = uploadDataDuringCreation;
    }

    /**
     * Return whether upload length declaration should be deferred.
     *
     * @return True when upload length should be deferred.
     */
    public boolean isUploadLengthDeferred() {
        return uploadLengthDeferred;
    }

    /**
     * Set whether upload length declaration should be deferred.
     *
     * @param uploadLengthDeferred True to defer the upload length declaration.
     */
    public void setUploadLengthDeferred(boolean uploadLengthDeferred) {
        this.uploadLengthDeferred = uploadLengthDeferred;
    }

    /**
     * Return the configured upload size.
     *
     * @return The configured upload size, or null.
     */
    @Nullable
    public Long getUploadSize() {
        return uploadSize;
    }

    /**
     * Set the configured upload size.
     *
     * @param uploadSize The configured upload size, or null.
     */
    public void setUploadSize(@Nullable Long uploadSize) {
        this.uploadSize = uploadSize;
    }

    /**
     * Return the existing upload URL to resume.
     *
     * @return The upload URL, or null.
     */
    @Nullable
    public URL getUploadURL() {
        return uploadURL;
    }

    /**
     * Set the existing upload URL to resume.
     *
     * @param uploadURL The upload URL, or null.
     */
    public void setUploadURL(@Nullable URL uploadURL) {
        this.uploadURL = uploadURL;
    }
}
