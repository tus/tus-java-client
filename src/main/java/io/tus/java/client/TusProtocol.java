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
    static final String CREATE_UPLOAD_METHOD = "POST";
    static final String DEFAULT_PROTOCOL_VERSION = "1.0.0";
    static final Map<String, String> DEFAULT_REQUEST_HEADERS = defaultRequestHeaders();
    static final Map<String, String> DEFAULT_RESPONSE_HEADERS = defaultResponseHeaders();
    static final String LOCATION_HEADER_NAME = "Location";
    static final String METADATA_HEADER_NAME = "Upload-Metadata";
    static final String OFFSET_DISCOVERY_METHOD = "HEAD";
    static final String REQUEST_ID_HEADER_NAME = "X-Request-ID";
    static final int SUCCESS_RESPONSE_STATUS_CATEGORY = 200;
    static final String TERMINATE_UPLOAD_METHOD = "DELETE";
    static final String UPLOAD_BODY_CONTENT_TYPE = "application/offset+octet-stream";
    static final String UPLOAD_BODY_CONTENT_TYPE_HEADER_NAME = "Content-Type";
    static final String UPLOAD_CHUNK_METHOD = "PATCH";
    static final String UPLOAD_DEFER_LENGTH_HEADER_NAME = "Upload-Defer-Length";
    static final String UPLOAD_LENGTH_HEADER_NAME = "Upload-Length";
    static final String UPLOAD_OFFSET_HEADER_NAME = "Upload-Offset";

    private TusProtocol() {
    }

    static boolean isSuccessfulResponseStatus(int responseStatusCode) {
        return responseStatusCode >= SUCCESS_RESPONSE_STATUS_CATEGORY
                && responseStatusCode < SUCCESS_RESPONSE_STATUS_CATEGORY + 100;
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
