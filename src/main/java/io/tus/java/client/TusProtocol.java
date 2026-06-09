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
    static final String CONCATENATION_FINAL_PREFIX =
            "final;";
    static final String CONCATENATION_HEADER_NAME =
            "Upload-Concat";
    static final String CONCATENATION_PARTIAL_VALUE =
            "partial";
    static final String CONCATENATION_UPLOAD_URL_SEPARATOR =
            " ";
    static final String CREATE_UPLOAD_METHOD = "POST";
    static final String DETAILED_ERROR_CAUSE_STRING_TEMPLATE =
            "Error: {message}";
    static final String DETAILED_ERROR_CAUSED_BY_TEMPLATE =
            ", caused by {cause}";
    static final String DETAILED_ERROR_CREATE_UPLOAD_REQUEST_FAILED =
            "tus: failed to create upload";
    static final String DETAILED_ERROR_EMPTY_RESPONSE_BODY =
            "";
    static final String DETAILED_ERROR_MISSING_VALUE =
            "n/a";
    static final String DETAILED_ERROR_REQUEST_CONTEXT_TEMPLATE =
            ", originated from request (method: {method}, url: {url}, response code: {status}, response text: {body}, request id: {requestId})";
    static final String DETAILED_ERROR_UNEXPECTED_CREATE_RESPONSE =
            "tus: unexpected response while creating upload";
    static final String DEFAULT_PROTOCOL_VERSION = "1.0.0";
    static final int DEFAULT_PARALLEL_UPLOADS = 1;
    static final Map<String, String> DEFAULT_REQUEST_HEADERS = defaultRequestHeaders();
    static final Map<String, String> DEFAULT_RESPONSE_HEADERS = defaultResponseHeaders();
    static final String LOCATION_HEADER_NAME = "Location";
    static final String METADATA_HEADER_NAME = "Upload-Metadata";
    static final int MINIMUM_PARALLEL_UPLOADS = 2;
    static final String OFFSET_DISCOVERY_METHOD = "HEAD";
    static final String REQUEST_ID_HEADER_NAME = "X-Request-ID";
    static final String START_OPTION_VALIDATION_MISSING_ENDPOINT_OR_UPLOAD_URL =
            "tus: neither an endpoint or an upload URL is provided";
    static final String START_OPTION_VALIDATION_MISSING_INPUT =
            "tus: no file or stream to upload provided";
    static final String START_OPTION_VALIDATION_PARALLEL_BOUNDARIES_LENGTH_MISMATCH =
            "tus: the `parallelUploadBoundaries` must have the same length as the value of `parallelUploads`";
    static final String START_OPTION_VALIDATION_PARALLEL_BOUNDARIES_WITHOUT_PARALLEL_UPLOADS =
            "tus: cannot use the `parallelUploadBoundaries` option when `parallelUploads` is disabled";
    static final String START_OPTION_VALIDATION_PARALLEL_UPLOADS_WITH_DEFERRED_LENGTH =
            "tus: cannot use the `uploadLengthDeferred` option when parallelUploads is enabled";
    static final String START_OPTION_VALIDATION_PARALLEL_UPLOADS_WITH_UPLOAD_DATA_DURING_CREATION =
            "tus: cannot use the `uploadDataDuringCreation` option when parallelUploads is enabled";
    static final String START_OPTION_VALIDATION_PARALLEL_UPLOADS_WITH_UPLOAD_SIZE =
            "tus: cannot use the `uploadSize` option when parallelUploads is enabled";
    static final String START_OPTION_VALIDATION_PARALLEL_UPLOADS_WITH_UPLOAD_URL =
            "tus: cannot use the `uploadUrl` option when parallelUploads is enabled";
    static final String START_OPTION_VALIDATION_RETRY_DELAYS_NOT_ARRAY =
            "tus: the `retryDelays` option must either be an array or null";
    static final String START_OPTION_VALIDATION_UNSUPPORTED_PROTOCOL_PREFIX =
            "tus: unsupported protocol ";
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

    static String formatDetailedErrorMessage(String template, Map<String, String> values) {
        String result = template;
        for (Map.Entry<String, String> entry : values.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return result;
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
