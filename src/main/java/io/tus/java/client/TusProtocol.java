/*
 * Code generated from Transloadit API2 TUS protocol contracts; DO NOT EDIT.
 * If it looks wrong, please report the issue instead of editing this file by hand;
 * the source fix belongs in the protocol contract generator so all TUS clients stay in sync.
 */

package io.tus.java.client;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Generated TUS protocol constants used by the runtime client.
 */
final class TusProtocol {
    static final String DEFAULT_PROTOCOL_VERSION = "1.0.0";
    static final Map<String, String> DEFAULT_REQUEST_HEADERS = defaultRequestHeaders();
    static final Map<String, String> DEFAULT_RESPONSE_HEADERS = defaultResponseHeaders();

    private TusProtocol() {
    }

    private static Map<String, String> defaultRequestHeaders() {
        Map<String, String> result = new LinkedHashMap<String, String>();
        result.put("Tus-Resumable", "1.0.0");
        return Collections.unmodifiableMap(result);
    }

    private static Map<String, String> defaultResponseHeaders() {
        Map<String, String> result = new LinkedHashMap<String, String>();
        result.put("Tus-Resumable", "1.0.0");
        return Collections.unmodifiableMap(result);
    }
}
