package io.tus.java.client;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * Persistent URL store backed by a local properties file.
 */
public class TusURLFileStore implements TusURLStore {
    private static final String NAMESPACE = "tus";
    private static final String SEPARATOR = "::";

    private final File file;

    /**
     * Create a persistent URL store.
     *
     * @param file File used for storing upload URLs.
     */
    public TusURLFileStore(@NotNull File file) {
        this.file = file;
    }

    /**
     * Stores the upload's fingerprint and URL.
     *
     * @param fingerprint An upload's fingerprint.
     * @param url The corresponding upload URL.
     */
    @Override
    public synchronized void set(String fingerprint, URL url) {
        Properties properties = readProperties();
        properties.setProperty(newKey(fingerprint), url.toString());
        writeProperties(properties);
    }

    /**
     * Returns the first stored upload URL for a fingerprint.
     *
     * @param fingerprint An upload's fingerprint.
     * @return The corresponding upload URL.
     */
    @Override
    public synchronized URL get(String fingerprint) {
        Properties properties = readProperties();
        List<String> keys = keysForFingerprint(properties, fingerprint);
        if (keys.isEmpty()) {
            return null;
        }

        try {
            return new URL(properties.getProperty(keys.get(0)));
        } catch (MalformedURLException error) {
            return null;
        }
    }

    /**
     * Removes all stored upload URLs for a fingerprint.
     *
     * @param fingerprint An upload's fingerprint.
     */
    @Override
    public synchronized void remove(String fingerprint) {
        Properties properties = readProperties();
        for (String key : keysForFingerprint(properties, fingerprint)) {
            properties.remove(key);
        }
        writeProperties(properties);
    }

    /**
     * Returns the number of stored upload URLs.
     *
     * @return Stored upload URL count.
     */
    public synchronized int size() {
        return readProperties().size();
    }

    /**
     * Returns whether a stored key starts with the given prefix.
     *
     * @param prefix Key prefix.
     * @return True if a stored key starts with the prefix.
     */
    public synchronized boolean hasKeyWithPrefix(String prefix) {
        Properties properties = readProperties();
        for (Object key : properties.keySet()) {
            if (String.valueOf(key).startsWith(prefix)) {
                return true;
            }
        }

        return false;
    }

    private String newKey(String fingerprint) {
        return keyPrefix(fingerprint) + UUID.randomUUID().toString();
    }

    private static String keyPrefix(String fingerprint) {
        return NAMESPACE + SEPARATOR + fingerprint + SEPARATOR;
    }

    private static List<String> keysForFingerprint(Properties properties, String fingerprint) {
        final String prefix = keyPrefix(fingerprint);
        final List<String> result = new ArrayList<String>();
        for (Object key : properties.keySet()) {
            final String stringKey = String.valueOf(key);
            if (stringKey.startsWith(prefix)) {
                result.add(stringKey);
            }
        }
        Collections.sort(result);
        return result;
    }

    private Properties readProperties() {
        final Properties properties = new Properties();
        if (!file.exists()) {
            return properties;
        }

        try (FileInputStream input = new FileInputStream(file)) {
            properties.load(input);
        } catch (IOException error) {
            throw new IllegalStateException("could not read TUS URL storage file", error);
        }

        return properties;
    }

    private void writeProperties(Properties properties) {
        final File parent = file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IllegalStateException("could not create TUS URL storage directory");
        }

        try (FileOutputStream output = new FileOutputStream(file)) {
            properties.store(output, "tus-java-client URL storage");
        } catch (IOException error) {
            throw new IllegalStateException("could not write TUS URL storage file", error);
        }
    }
}
