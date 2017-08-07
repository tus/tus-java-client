package io.tus.java.client;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * The values will only be stored as long as the application is running. This store will not
 * keep the values after your application crashes or restarts.
 *
 * @author rfelgentraeger
 */
public class TusUploadLengthStoreImpl implements TusUploadLengthStore {

    private Map<URL, Long> store = new HashMap<URL, Long>();

    @Override
    public void set(URL uploadUrl, long bytes) {
        if (store.containsKey(uploadUrl)) {
            throw new IllegalArgumentException("upload size is immutable");
        }
        if (bytes < 0) {
            throw new IllegalArgumentException("upload size must be a non-negative value");
        }
        store.put(uploadUrl, bytes);
    }

    @Override
    public Long get(URL uploadUrl) {
        return store.get(uploadUrl);
    }

    @Override
    public void remove(URL uploadUrl) {
        store.remove(uploadUrl);
    }
}
