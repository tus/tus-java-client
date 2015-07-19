package io.tus.java.client;

import java.net.URL;

/**
 * Implementations of this interface are used to map an upload's fingerprint with the corresponding
 * upload URL. This functionality is used to allow resuming uploads. The fingerprint is usually
 * retrieved using {@link TusUpload#getFingerprint()}.
 */
public interface TusURLStore {
    /**
     * Store a new fingerprint and its upload URL.
     *
     * @param fingerprint An upload's fingerprint.
     * @param url The corresponding upload URL.
     */
    void set(String fingerprint, URL url);

    /**
     * Retrieve an upload's URL for a fingerprint. If no matching entry is found this method will
     * return <code>null</code>.
     *
     * @param fingerprint An upload's fingerprint.
     * @return The corresponding upload URL.
     */
    URL get(String fingerprint);

    /**
     * Remove an entry from the store. Calling {@link #get(String)} with the same fingerprint will
     * return <code>null</code>. If no entry exists for this fingerprint no exception should be
     * thrown.
     *
     * @param fingerprint An upload's fingerprint.
     */
    void remove(String fingerprint);
}
