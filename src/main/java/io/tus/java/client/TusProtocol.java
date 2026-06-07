/*
 * Code generated from Transloadit API2 TUS protocol contracts; DO NOT EDIT.
 * If it looks wrong, please report the issue instead of editing this file by hand;
 * the source fix belongs in the protocol contract generator so all TUS clients stay in sync.
 */

package io.tus.java.client;

import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Generated TUS protocol constants used by the runtime client.
 */
final class TusProtocol {
    static final String DEFAULT_PROTOCOL_VERSION = "1.0.0";
    static final Map<String, String> DEFAULT_REQUEST_HEADERS = defaultRequestHeaders();
    static final Map<String, String> DEFAULT_RESPONSE_HEADERS = defaultResponseHeaders();
    static final String REQUEST_ID_HEADER_NAME = "X-Request-ID";

    private TusProtocol() {
    }

    static void prepareRequestHeaders(
            HttpURLConnection connection,
            Map<String, String> customHeaders,
            boolean addRequestId
    ) {
        addDefaultRequestHeaders(connection);
        addCustomRequestHeaders(connection, customHeaders);
        addRequestIdHeader(connection, addRequestId);
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

    private static void addDefaultRequestHeaders(HttpURLConnection connection) {
        for (Map.Entry<String, String> entry : DEFAULT_REQUEST_HEADERS.entrySet()) {
            connection.addRequestProperty(entry.getKey(), entry.getValue());
        }
    }

    private static void addCustomRequestHeaders(
            HttpURLConnection connection,
            Map<String, String> customHeaders
    ) {
        if (customHeaders == null) {
            return;
        }

        for (Map.Entry<String, String> entry : customHeaders.entrySet()) {
            connection.addRequestProperty(entry.getKey(), entry.getValue());
        }
    }

    private static void addRequestIdHeader(HttpURLConnection connection, boolean addRequestId) {
        if (!addRequestId) {
            return;
        }

        connection.setRequestProperty(REQUEST_ID_HEADER_NAME, UUID.randomUUID().toString());
    }
}
