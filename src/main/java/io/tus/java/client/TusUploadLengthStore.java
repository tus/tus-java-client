package io.tus.java.client;

import java.net.URL;

/**
 * Implementations of this interface are used to map an upload's url with the corresponding
 * total upload size. If there is an upload size for the url in the store, when the 'Upload-Length' header
 * is already transmitted to the TUS server (and no further update must happen in order to be compliant with TUS protocol)
 *
 * This functionality is used to allow TUS's Upload-Defer-Length feature.
 *
 * @author rfelgentraeger
 */
public interface TusUploadLengthStore {
    /**
     * Store a new upload url and its total size in bytes.
     *
     * @param uploadUrl An upload's url.
     * @param bytes The corresponding total size.
     * @throws IllegalArgumentException if the upload url is already set or the value is negative
     */
    void set(URL uploadUrl, long bytes);

    /**
     * Retrieve an upload's size for an upload url. If no matching entry is found this method will
     * return {@code null}.
     *
     * @param uploadUrl An upload's url.
     * @return The corresponding total upload size in bytes or {@code null}> if not found
     */
    Long get(URL uploadUrl);

    /**
     * Remove an entry from the store. Calling {@link #get(String)} with the same url will
     * return {@code null}. If no entry exists for this upload url no exception should be
     * thrown.
     *
     * @param uploadUrl An upload's url.
     */
    void remove(URL uploadUrl);
}
