package io.tus.java.client;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to map an upload's fingerprint with the corresponding upload URL by storing
 * the entries in a {@link HashMap}. This functionality is used to allow resuming uploads. The
 * fingerprint is usually retrieved using {@link TusUpload#getFingerprint()}.
 * <br>
 * The values will only be stored as long as the application is running. This store will not
 * keep the values after your application crashes or restarts.
 */
public class TusURLMemoryStore implements TusURLStore {
    private Map<String, URL> store = new HashMap<String, URL>();

    /**
     * Stores the upload's fingerprint and url.
     * @param fingerprint An upload's fingerprint.
     * @param url The corresponding upload URL.
     */
    @Override
    public void set(String fingerprint, URL url) {
        store.put(fingerprint, url);
    }

    /**
     * Returns the corresponding Upload URL to a given fingerprint.
     * @param fingerprint An upload's fingerprint.
     * @return The corresponding upload URL.
     */
    @Override
    public URL get(String fingerprint) {
        return store.get(fingerprint);
    }

    /**
     * Removes the corresponding entry to a fingerprint from the {@link TusURLMemoryStore}.
     * @param fingerprint An upload's fingerprint.
     */
    @Override
    public void remove(String fingerprint) {
        store.remove(fingerprint);
    }
}
