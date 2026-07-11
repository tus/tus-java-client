package io.tus.java.client;

import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link TusURLFileStore}.
 */
public class TestTusURLFileStore {
    /**
     * Tests if file-backed URL storage persists and removes upload URLs.
     *
     * @throws Exception if the temporary file or URL cannot be created.
     */
    @Test
    public void test() throws Exception {
        File file = Files.createTempFile("tus-url-store", ".properties").toFile();
        assertTrue(file.delete());

        URL url = new URL("https://tusd.tusdemo.net/files/hello");
        TusURLFileStore store = new TusURLFileStore(file);
        store.set("foo", url);

        assertEquals(url, store.get("foo"));
        assertEquals(1, store.size());
        assertTrue(store.hasKeyWithPrefix("tus::foo::"));

        TusURLFileStore restoredStore = new TusURLFileStore(file);
        assertEquals(url, restoredStore.get("foo"));

        restoredStore.remove("foo");
        assertNull(restoredStore.get("foo"));
        assertEquals(0, restoredStore.size());

        assertTrue(file.delete());
    }
}
