package io.tus.java.client;

final class TusStartOptionValidator {
    static void validate(TusStartOptions options) {
        if (missingInput(options)) {
            throw new IllegalArgumentException(TusProtocol.START_OPTION_VALIDATION_MISSING_INPUT);
        }

        if (options.getEndpointURL() == null && options.getUploadURL() == null) {
            throw new IllegalArgumentException(
                    TusProtocol.START_OPTION_VALIDATION_MISSING_ENDPOINT_OR_UPLOAD_URL
            );
        }

        if (!TusProtocol.DEFAULT_PROTOCOL_VERSION.equals(options.getProtocol())) {
            throw new IllegalArgumentException(
                    TusProtocol.START_OPTION_VALIDATION_UNSUPPORTED_PROTOCOL_PREFIX
                            + options.getProtocol()
            );
        }

        final boolean parallelUploadsEnabled =
                options.getParallelUploads() >= TusProtocol.MINIMUM_PARALLEL_UPLOADS;
        final boolean parallelBoundariesSet = options.getParallelUploadBoundariesCount() >= 0;

        if (parallelBoundariesSet && !parallelUploadsEnabled) {
            throw new IllegalArgumentException(
                    TusProtocol
                            .START_OPTION_VALIDATION_PARALLEL_BOUNDARIES_WITHOUT_PARALLEL_UPLOADS
            );
        }

        if (!parallelUploadsEnabled) {
            return;
        }

        if (options.getUploadURL() != null) {
            throw new IllegalArgumentException(
                    TusProtocol.START_OPTION_VALIDATION_PARALLEL_UPLOADS_WITH_UPLOAD_URL
            );
        }

        if (options.getUploadSize() != null) {
            throw new IllegalArgumentException(
                    TusProtocol.START_OPTION_VALIDATION_PARALLEL_UPLOADS_WITH_UPLOAD_SIZE
            );
        }

        if (options.isUploadLengthDeferred() || options.getUpload().isUploadLengthDeferred()) {
            throw new IllegalArgumentException(
                    TusProtocol.START_OPTION_VALIDATION_PARALLEL_UPLOADS_WITH_DEFERRED_LENGTH
            );
        }

        if (options.isUploadDataDuringCreation()) {
            throw new IllegalArgumentException(
                    TusProtocol
                            .START_OPTION_VALIDATION_PARALLEL_UPLOADS_WITH_UPLOAD_DATA_DURING_CREATION
            );
        }

        if (parallelBoundariesSet
                && options.getParallelUploadBoundariesCount() != options.getParallelUploads()) {
            throw new IllegalArgumentException(
                    TusProtocol.START_OPTION_VALIDATION_PARALLEL_BOUNDARIES_LENGTH_MISMATCH
            );
        }
    }

    private static boolean missingInput(TusStartOptions options) {
        return options == null
                || options.getUpload() == null
                || options.getUpload().getInputStream() == null;
    }

    private TusStartOptionValidator() {
        throw new IllegalStateException("Utility class");
    }
}
