package io.tus.java.client;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A {@link ProtocolException} with TUS request and response context.
 */
public class TusResponseException extends ProtocolException implements TusDetailedError {
    private final String responseBody;
    private final int responseStatus;
    private final TusRequestSnapshot snapshot;

    TusResponseException(
            String message,
            HttpURLConnection connection,
            TusRequestSnapshot snapshot,
            int responseStatus,
            String responseBody
    ) {
        super(message, connection);
        this.responseBody = responseBody;
        this.responseStatus = responseStatus;
        this.snapshot = snapshot;
    }

    @Override
    public Throwable getCausingError() {
        return null;
    }

    @Override
    public String getOriginalRequestMethod() {
        return snapshot.method;
    }

    @Override
    public String getOriginalRequestId() {
        return snapshot.requestId;
    }

    @Override
    public URL getOriginalRequestURL() {
        return snapshot.url;
    }

    @Override
    public boolean hasOriginalResponse() {
        return true;
    }

    @Override
    public String getOriginalResponseBody() {
        return responseBody;
    }

    @Override
    public int getOriginalResponseStatus() {
        return responseStatus;
    }
}
