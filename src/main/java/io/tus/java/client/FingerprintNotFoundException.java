package io.tus.java.client;

/**
 * This exception is thrown by {@link TusClient#resumeUpload(TusUpload)} if no upload URL
 * has been stored in the {@link TusURLStore}.
 */
public class FingerprintNotFoundException  extends Exception {
    public FingerprintNotFoundException(String fingerprint) {
        super("fingerprint not in storage found: " + fingerprint);
    }
}
