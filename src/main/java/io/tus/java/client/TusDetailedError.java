package io.tus.java.client;

import java.net.URL;

/**
 * Exposes request and response context for TUS failures.
 */
public interface TusDetailedError {
    /**
     * Returns the lower-level cause for request failures, or null for response failures.
     *
     * @return Causing error, or null.
     */
    Throwable getCausingError();

    /**
     * Returns the method of the original TUS request.
     *
     * @return HTTP method.
     */
    String getOriginalRequestMethod();

    /**
     * Returns the request ID attached to the original TUS request.
     *
     * @return Request ID, or the generated missing-value marker.
     */
    String getOriginalRequestId();

    /**
     * Returns the URL of the original TUS request.
     *
     * @return Request URL.
     */
    URL getOriginalRequestURL();

    /**
     * Returns whether a response was available.
     *
     * @return True when a response was captured.
     */
    boolean hasOriginalResponse();

    /**
     * Returns the captured response body.
     *
     * @return Response body, or the generated missing-value marker.
     */
    String getOriginalResponseBody();

    /**
     * Returns the captured response status.
     *
     * @return Response status, or -1 when absent.
     */
    int getOriginalResponseStatus();
}
