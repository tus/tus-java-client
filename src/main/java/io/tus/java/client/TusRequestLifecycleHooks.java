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

        public String getMethod() {
            return method;
        }

        public HttpURLConnection getConnection() {
            return connection;
        }
    }

    /**
     * Callback invoked before transport sends the request.
     */
    public interface BeforeRequest {
        void beforeRequest(RequestContext context) throws IOException;
    }

    /**
     * Callback invoked after transport receives the response.
     */
    public interface AfterResponse {
        void afterResponse(RequestContext context) throws IOException;
    }

    private final BeforeRequest beforeRequest;
    private final AfterResponse afterResponse;

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
