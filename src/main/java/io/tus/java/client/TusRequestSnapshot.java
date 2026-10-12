package io.tus.java.client;

import java.net.HttpURLConnection;
import java.net.URL;

final class TusRequestSnapshot {
    final String method;
    final String requestId;
    final URL url;

    private TusRequestSnapshot(String method, URL url, String requestId) {
        this.method = method;
        this.requestId = requestId;
        this.url = url;
    }

    static TusRequestSnapshot fromConnection(HttpURLConnection connection) {
        return new TusRequestSnapshot(
                connection.getRequestMethod(),
                connection.getURL(),
                requestId(connection)
        );
    }

    private static String requestId(HttpURLConnection connection) {
        final String requestId = connection.getRequestProperty(TusProtocol.REQUEST_ID_HEADER_NAME);
        if (requestId == null || requestId.length() == 0) {
            return TusProtocol.DETAILED_ERROR_MISSING_VALUE;
        }

        return requestId;
    }
}
