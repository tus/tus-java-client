package io.tus.java.client;

/**
 * This exception is thrown if the server sends a request with an unexpected status code or
 * missing/invalid headers.
 */
public class ProtocolException extends Exception {
    public ProtocolException(String message) {
        super(message);
    }
}
