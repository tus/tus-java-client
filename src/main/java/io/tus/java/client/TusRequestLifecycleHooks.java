package io.tus.java.client;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Callbacks invoked around each HTTP request/response pair.
 */
public final class TusRequestLifecycleHooks {
    /**
     * Request context passed to lifecycle hooks.
     */
    public static final class RequestContext {
        private final String method;
        private final HttpURLConnection connection;

        RequestContext(String method, HttpURLConnection connection) {
            this.method = method;
            this.connection = connection;
        }

        /**
         * Get the logical TUS request method for this request.
         *
         * @return The request method.
         */
        public String getMethod() {
            return method;
        }

        /**
         * Get the HTTP connection for this request.
         *
         * @return The mutable HTTP connection.
         */
        public HttpURLConnection getConnection() {
            return connection;
        }
    }

    /**
     * Callback invoked before transport sends the request.
     */
    public interface BeforeRequest {
        /**
         * Handle a request before it is sent.
         *
         * @param context The request context.
         * @throws IOException when the request should fail.
         */
        void beforeRequest(RequestContext context) throws IOException;
    }

    /**
     * Callback invoked after transport receives the response.
     */
    public interface AfterResponse {
        /**
         * Handle a response after it has been received.
         *
         * @param context The request context.
         * @throws IOException when the response should fail.
         */
        void afterResponse(RequestContext context) throws IOException;
    }

    private final BeforeRequest beforeRequest;
    private final AfterResponse afterResponse;

    /**
     * Create request lifecycle hooks.
     *
     * @param beforeRequest Callback invoked before a request is sent.
     * @param afterResponse Callback invoked after a response is received.
     */
    public TusRequestLifecycleHooks(BeforeRequest beforeRequest, AfterResponse afterResponse) {
        this.beforeRequest = beforeRequest;
        this.afterResponse = afterResponse;
    }

    BeforeRequest getBeforeRequest() {
        return beforeRequest;
    }

    AfterResponse getAfterResponse() {
        return afterResponse;
    }
}
