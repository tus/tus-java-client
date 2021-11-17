package io.tus.java.client;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link TusURLMemoryStore}.
 */
public class TestTusURLMemoryStore {

    /**
     * Tests if setting and deleting of an url in the {@link TusURLMemoryStore} works.
     * @throws MalformedURLException
     */
    @Test
    public void test() throws MalformedURLException {
        TusURLStore store = new TusURLMemoryStore();
        URL url = new URL("https://tusd.tusdemo.net/files/hello");
        String fingerprint = "foo";
        store.set(fingerprint, url);

        assertEquals(store.get(fingerprint), url);

        store.remove(fingerprint);

        assertEquals(store.get(fingerprint), null);
    }
}
