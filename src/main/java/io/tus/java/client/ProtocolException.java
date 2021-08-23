package io.tus.java.client;

import java.io.IOException;
import java.net.HttpURLConnection;

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
     Instantiates a new Object of type {@link ProtocolException}.
     * @param message Message to be thrown with the exception.
     * @param connection {@link HttpURLConnection}, where the error occurred.
     */
    public ProtocolException(String message, HttpURLConnection connection) {
        super(message);
        this.connection = connection;
    }

    /**
     * Returns the {@link HttpURLConnection} instances, which caused the error.
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
