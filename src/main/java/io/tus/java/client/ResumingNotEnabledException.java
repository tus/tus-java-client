package io.tus.java.client;

/**
 * This expcetion is thrown when you try to resume an upload using
 * {@link TusClient#resumeUpload(TusUpload)} without enabling it first.
 */
public class ResumingNotEnabledException extends Exception {
    public ResumingNotEnabledException() {
        super("resuming not enabled for this client. use enableResuming() to do so");
    }
}
