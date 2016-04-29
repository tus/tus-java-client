package io.tus.java.client;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * This exception is thrown if the server sends a request with an unexpected status code or
 * missing/invalid headers.
 */
public class ProtocolException extends Exception {
    private HttpURLConnection connection;

    public ProtocolException(String message) {
        super(message);
    }

    public ProtocolException(String message, HttpURLConnection connection) {
        super(message);
        this.connection = connection;
    }

    public HttpURLConnection getCausingConnection() {
        return connection;
    }

    public boolean shouldRetry() {
        if(connection == null) {
            return false;
        }

        try {
            int responseCode = connection.getResponseCode();

            return responseCode >= 500 && responseCode < 600 || responseCode == 423;
        } catch(IOException e) {
            return false;
        }
    }
}
