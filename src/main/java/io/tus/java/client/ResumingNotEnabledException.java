package io.tus.java.client;

/**
 * This exception is thrown when you try to resume an upload using
 * {@link TusClient#resumeUpload(TusUpload)} without enabling it first.
 */
public class ResumingNotEnabledException extends Exception {
    /**
     * Instantiates a new Object of Type {@link ResumingNotEnabledException}.
     */
    public ResumingNotEnabledException() {
        super("resuming not enabled for this client. use enableResuming() to do so");
    }
}
