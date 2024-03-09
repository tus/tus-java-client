package io.tus.java.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

/**
 * This exception is thrown if the server sends a request with an unexpected status code or
 * missing/invalid headers.
 */
public class ProtocolException extends Exception {
    private HttpURLConnection connection;

    /**
     * Instantiates a new Object of type {@link ProtocolException}.
     * @param message Message to be thrown with the exception.
     */
    public ProtocolException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Object of type {@link ProtocolException}.
     * @param message Message to be thrown with the exception.
     * @param connection {@link HttpURLConnection}, where the error occurred.
     */
    public ProtocolException(String message, HttpURLConnection connection) {
        super(message);
        this.connection = connection;
    }

    /**
     * Create a new {@link ProtocolException} instance caused by an unexpected status code.
     * @param connection {@link HttpURLConnection}, where the error occurred.
     * @param action Description of the action when the error occurred.
     * @return {@link HttpURLConnection}
     */
    static ProtocolException unexpectedStatusCode(HttpURLConnection connection, String action) throws IOException {
        int code = connection.getResponseCode();
        String response = "n/a";
        //System.out.println(connection.getInputStream());
        InputStream responseStream = connection.getErrorStream();
        if (responseStream != null) {
            response = new String(responseStream.readAllBytes(), StandardCharsets.UTF_8).trim();
        }

        return new ProtocolException(
                "unexpected status code (" + code + ") while " + action + "; response is: " + response, connection);
    }

    /**
     * Returns the {@link HttpURLConnection} instance which caused the error.
     * @return {@link HttpURLConnection}
     */
    public HttpURLConnection getCausingConnection() {
        return connection;
    }

    /**
     * Determines whether a retry attempt should be made after a {@link ProtocolException} or not.
     * @return {@code true} if there should be a retry attempt.
     */
    public boolean shouldRetry() {
        if (connection == null) {
            return false;
        }

        try {
            int responseCode = connection.getResponseCode();

            // 5XX and 423 Resource Locked status codes should be retried.
            return (responseCode >= 500 && responseCode < 600) || responseCode == 423;
        } catch (IOException e) {
            return false;
        }
    }
}
