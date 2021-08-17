package io.tus.java.client;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * TusInputStream is an internal abstraction above an InputStream which allows seeking to a
 * position relative to the beginning of the stream. In comparision {@link InputStream#skip(long)}
 * only supports skipping bytes relative to the current position.
 */
class TusInputStream {
    private InputStream stream;
    private long bytesRead;
    private long lastMark = -1;

    /**
     * Create a new TusInputStream which reads from and operates on the supplied stream.
     *
     * @param stream The stream to read from
     */
    TusInputStream(InputStream stream) {
        if (!stream.markSupported()) {
            stream = new BufferedInputStream(stream);
        }

        this.stream = stream;
    }

    /**
     * Read a specific amount of bytes from the stream and write them to the start of the supplied
     * buffer.
     * See {@link InputStream#read(byte[], int, int)} for more details.
     *
     * @param buffer The array to write the bytes to
     * @param length Number of bytes to read at most
     * @return Actual number of bytes read
     * @throws IOException
     */
    public int read(byte[] buffer, int length) throws IOException {
        int bytesReadNow = stream.read(buffer, 0, length);
        bytesRead += bytesReadNow;
        return bytesReadNow;
    }

    /**
     * Seek to the position relative to the start of the stream.
     *
     * @param position Absolute position to seek to
     * @throws IOException
     */
    public void seekTo(long position) throws IOException {
        if (lastMark != -1) {
            stream.reset();
            stream.skip(position - lastMark);
            lastMark = -1;
        } else {
            stream.skip(position);
        }

        bytesRead = position;
    }

    /**
     * Mark the current position to allow seeking to a position after this mark.
     * See {@link InputStream#mark(int)} for details.
     *
     * @param readLimit Number of bytes to read before this mark gets invalidated
     */
    public void mark(int readLimit) {
        lastMark = bytesRead;
        stream.mark(readLimit);
    }

    /**
     * Close the underlying instance of InputStream.
     *
     * @throws IOException
     */
    public void close() throws IOException {
        stream.close();
    }
}
