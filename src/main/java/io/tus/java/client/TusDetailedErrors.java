package io.tus.java.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

final class TusDetailedErrors {
    static TusRequestException requestException(
            String baseMessage,
            TusRequestSnapshot request,
            IOException cause
    ) {
        return new TusRequestException(
                detailedErrorMessage(baseMessage, cause, request, -1, null),
                request,
                cause
        );
    }

    static TusResponseException responseException(
            String baseMessage,
            TusRequestSnapshot request,
            HttpURLConnection connection
    ) {
        final int responseStatus = responseStatus(connection);
        final String responseBody = responseBody(connection);
        return new TusResponseException(
                detailedErrorMessage(baseMessage, null, request, responseStatus, responseBody),
                connection,
                request,
                responseStatus,
                responseBody
        );
    }

    private static String detailedErrorMessage(
            String baseMessage,
            IOException cause,
            TusRequestSnapshot request,
            int responseStatus,
            String responseBody
    ) {
        String message = baseMessage;
        if (cause != null) {
            final Map<String, String> causeValues = new LinkedHashMap<String, String>();
            causeValues.put("message", missingIfEmpty(cause.getMessage()));
            final String causeMessage = TusProtocol.formatDetailedErrorMessage(
                    TusProtocol.DETAILED_ERROR_CAUSE_STRING_TEMPLATE,
                    causeValues
            );

            final Map<String, String> causedByValues = new LinkedHashMap<String, String>();
            causedByValues.put("cause", causeMessage);
            message += TusProtocol.formatDetailedErrorMessage(
                    TusProtocol.DETAILED_ERROR_CAUSED_BY_TEMPLATE,
                    causedByValues
            );
        }

        final Map<String, String> contextValues = new LinkedHashMap<String, String>();
        contextValues.put("body", responseBody == null
                ? TusProtocol.DETAILED_ERROR_MISSING_VALUE
                : responseBody);
        contextValues.put("method", request.method);
        contextValues.put("requestId", request.requestId);
        contextValues.put("status", responseStatus < 0
                ? TusProtocol.DETAILED_ERROR_MISSING_VALUE
                : Integer.toString(responseStatus));
        contextValues.put("url", request.url.toString());

        return message + TusProtocol.formatDetailedErrorMessage(
                TusProtocol.DETAILED_ERROR_REQUEST_CONTEXT_TEMPLATE,
                contextValues
        );
    }

    private static String responseBody(HttpURLConnection connection) {
        InputStream stream = connection.getErrorStream();
        if (stream == null) {
            try {
                stream = connection.getInputStream();
            } catch (IOException error) {
                return TusProtocol.DETAILED_ERROR_MISSING_VALUE;
            }
        }

        try {
            final byte[] body = readAllBytes(stream);
            if (body.length == 0) {
                return TusProtocol.DETAILED_ERROR_EMPTY_RESPONSE_BODY;
            }
            return new String(body, StandardCharsets.UTF_8);
        } catch (IOException error) {
            return TusProtocol.DETAILED_ERROR_MISSING_VALUE;
        }
    }

    private static int responseStatus(HttpURLConnection connection) {
        try {
            return connection.getResponseCode();
        } catch (IOException error) {
            return -1;
        }
    }

    private static byte[] readAllBytes(InputStream stream) throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final byte[] buffer = new byte[8192];
        int read;
        while ((read = stream.read(buffer)) != -1) {
            output.write(buffer, 0, read);
        }

        return output.toByteArray();
    }

    private static String missingIfEmpty(String value) {
        if (value == null || value.length() == 0) {
            return TusProtocol.DETAILED_ERROR_MISSING_VALUE;
        }

        return value;
    }

    private TusDetailedErrors() {
    }
}
