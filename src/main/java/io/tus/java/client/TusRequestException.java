package io.tus.java.client;

import java.io.IOException;
import java.net.URL;

/**
 * An {@link IOException} with TUS request context.
 */
public class TusRequestException extends IOException implements TusDetailedError {
    private final TusRequestSnapshot snapshot;

    TusRequestException(String message, TusRequestSnapshot snapshot, IOException cause) {
        super(message, cause);
        this.snapshot = snapshot;
    }

    @Override
    public Throwable getCausingError() {
        return getCause();
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
        return false;
    }

    @Override
    public String getOriginalResponseBody() {
        return TusProtocol.DETAILED_ERROR_MISSING_VALUE;
    }

    @Override
    public int getOriginalResponseStatus() {
        return -1;
    }
}
