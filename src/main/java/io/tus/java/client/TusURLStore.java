package io.tus.java.client;

import java.net.URL;

public interface TusURLStore {
    void set(String fingerprint, URL url);
    URL get(String fingerprint);
    void remove(String fingerprint);
}
