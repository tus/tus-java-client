package io.tus.java.client;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

class TusInputStream {
    private InputStream stream;
    private long bytesRead;
    private long lastMark = -1;

    public TusInputStream(InputStream stream) {
        if(!stream.markSupported()) {
            stream = new BufferedInputStream(stream);
        }

        this.stream = stream;
    }

    public int read(byte[] buffer, int length) throws IOException {
        int bytesReadNow = stream.read(buffer, 0, length);
        bytesRead += bytesReadNow;
        return bytesReadNow;
    }

    public void seekTo(long position) throws IOException {
        if(lastMark != -1) {
            stream.reset();
            stream.skip(position - lastMark);
            lastMark = -1;
        } else {
            stream.skip(position);
        }

        bytesRead = position;
    }

    public void mark(int readLimit) {
        lastMark = bytesRead;
        stream.mark(readLimit);
    }

    public void close() throws IOException {
        stream.close();
    }
}
