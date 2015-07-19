package io.tus.java.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

/**
 * This class contains information about a file which will be uploaded later. Uploading is not
 * done using this class but using {@link TusUploader} whose instances are returned by
 * {@link TusClient#createUpload(TusUpload)}, {@link TusClient#createUpload(TusUpload)} and
 * {@link TusClient#resumeOrCreateUpload(TusUpload)}.
 */
public class TusUpload {
    private long size;
    private InputStream input;
    private String fingerprint;
    // TODO: Implement metadata header
    private Map<String, String> metadata;

    /**
     * Create a new TusUpload object.
     */
    public TusUpload() {
    }

    /**
     * Create a new TusUpload object using the supplied File object. The corresponding {@link
     * InputStream}, size and fingerprint will be automatically set.
     *
     * @param file The file whose content should be later uploaded.
     * @throws FileNotFoundException Thrown if the file cannot be found.
     */
    public TusUpload(File file) throws FileNotFoundException {
        size = file.length();
        input = new FileInputStream(file);

        fingerprint = String.format("%s-%d", file.getAbsolutePath(), size);
    }

    public long getSize() {
        return size;
    }

    /**
     * Set the file's size in bytes.
     *
     * @param size File's size in bytes.
     */
    public void setSize(long size) {
        this.size = size;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public InputStream getInputStream() {
        return input;
    }

    /**
     * Set the source from which will be read if the file will be later uploaded.
     *
     * @param inputStream The stream which will be read.
     */
    public void setInputStream(InputStream inputStream) {
        input = inputStream;
    }
}
