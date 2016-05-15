package io.tus.java.client;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestTusURLMemoryStore {

    @Test
    public void test() throws MalformedURLException {
        TusURLStore store = new TusURLMemoryStore();
        URL url = new URL("https://master.tus.io/files/hello");
        String fingerprint = "foo";
        store.set(fingerprint, url);

        assertEquals(store.get(fingerprint), url);

        store.remove(fingerprint);

        assertEquals(store.get(fingerprint), null);
    }
}
