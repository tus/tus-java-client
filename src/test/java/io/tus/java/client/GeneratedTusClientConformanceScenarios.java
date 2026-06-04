/*
 * Code generated from Transloadit API2 TUS protocol contracts; DO NOT EDIT.
 * If it looks wrong, please report the issue instead of editing this file by hand;
 * the source fix belongs in the protocol contract generator so all TUS clients stay in sync.
 */

package io.tus.java.client;

/**
 * Generated TUS client conformance scenario fixture used by tests.
 */
final class GeneratedTusClientConformanceScenarios {
    static final GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario[] CLIENT_CONFORMANCE_SCENARIOS =
            new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario[] {
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "single-upload-lifecycle",
                "success",
                null,
                "singleUploadLifecycle",
                "singleUploadLifecycle",
                new String[] {
                "createTusUpload",
                "patchTusUpload",
            },
                new String[] {
                "open-input-source",
                "fingerprint-input",
                "store-resume-url",
                "retry-with-backoff",
                "emit-progress",
                "abort-current-request",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact-except-allowed-extra-events",
                                null,
                                "milestone",
                                "may-emit-extra-samples"
                        ),
                        new String[] {
                        "fingerprint:contract-single-fingerprint",
                        "upload-url-available",
                        "url-storage-add:contract-single-fingerprint:https://tus.io/uploads/generated-contract",
                        "progress:0:11",
                        "progress:11:11",
                        "chunk-complete:11:11:11",
                        "success",
                        "source-close",
                    },
                        new String[][] {
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                    },
                        new String[] {
                        "progress:",
                    }
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "creation-with-upload",
                "success",
                null,
                "creationWithUpload",
                "creationWithUpload",
                new String[] {
                "createTusUpload",
            },
                new String[] {
                "upload-during-creation",
                "emit-progress",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact-except-allowed-extra-events",
                                null,
                                "milestone",
                                "may-emit-extra-samples"
                        ),
                        new String[] {
                        "progress:0:11",
                        "progress:11:11",
                        "upload-url-available",
                        "success",
                        "source-close",
                    },
                        new String[][] {
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                    },
                        new String[] {
                        "progress:",
                    }
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "creation-with-upload-partial-chunk",
                "success",
                null,
                "creationWithUpload",
                "creationWithUploadPartialChunk",
                new String[] {
                "createTusUpload",
                "patchTusUpload",
            },
                new String[] {
                "upload-during-creation",
                "emit-progress",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact-except-allowed-extra-events",
                                null,
                                "milestone",
                                "may-emit-extra-samples"
                        ),
                        new String[] {
                        "progress:0:11",
                        "progress:5:11",
                        "upload-url-available",
                        "chunk-complete:5:5:11",
                        "progress:5:11",
                        "progress:10:11",
                        "chunk-complete:5:10:11",
                        "progress:10:11",
                        "progress:11:11",
                        "chunk-complete:1:11:11",
                        "success",
                        "source-close",
                    },
                        new String[][] {
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                    },
                        new String[] {
                        "progress:",
                    }
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "creation-with-upload",
                "success",
                null,
                "protocolVersionSelection",
                "ietfDraft05CreationWithUpload",
                new String[] {
                "createTusUpload",
            },
                new String[] {
                "select-client-protocol",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact-except-allowed-extra-events",
                                null,
                                "milestone",
                                "may-emit-extra-samples"
                        ),
                        new String[] {
                        "progress:0:11",
                        "progress:11:11",
                        "upload-url-available",
                        "success",
                        "source-close",
                    },
                        new String[][] {
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                    },
                        new String[] {
                        "progress:",
                    }
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "upload-body-headers",
                "success",
                null,
                "protocolVersionSelection",
                "ietfDraft05ChunkedUploadComplete",
                new String[] {
                "getTusUploadOffset",
                "patchTusUpload",
            },
                new String[] {
                "select-client-protocol",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact-except-allowed-extra-events",
                                null,
                                "milestone",
                                "may-emit-extra-samples"
                        ),
                        new String[] {
                        "upload-url-available",
                        "progress:0:11",
                        "progress:5:11",
                        "chunk-complete:5:5:11",
                        "progress:5:11",
                        "progress:10:11",
                        "chunk-complete:5:10:11",
                        "progress:10:11",
                        "progress:11:11",
                        "chunk-complete:1:11:11",
                        "success",
                        "source-close",
                    },
                        new String[][] {
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                    },
                        new String[] {
                        "progress:",
                    }
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "upload-body-headers",
                "success",
                null,
                "protocolVersionSelection",
                "ietfDraft03ResumeWithoutKnownLength",
                new String[] {
                "getTusUploadOffset",
                "patchTusUpload",
            },
                new String[] {
                "select-client-protocol",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact-except-allowed-extra-events",
                                null,
                                "milestone",
                                "may-emit-extra-samples"
                        ),
                        new String[] {
                        "upload-url-available",
                        "progress:5:11",
                        "progress:11:11",
                        "chunk-complete:6:11:11",
                        "success",
                        "source-close",
                    },
                        new String[][] {
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                    },
                        new String[] {
                        "progress:",
                    }
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "start-option-validation",
                "error",
                "missingInput",
                "startOptionValidation",
                "startValidationMissingInput",
                new String[0],
                new String[] {
                "validate-start-options",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact",
                                null,
                                null,
                                null
                        ),
                        new String[0],
                        new String[0][0],
                        new String[0]
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "start-option-validation",
                "error",
                "missingEndpointOrUploadUrl",
                "startOptionValidation",
                "startValidationMissingEndpointOrUploadUrl",
                new String[0],
                new String[] {
                "validate-start-options",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact",
                                null,
                                null,
                                null
                        ),
                        new String[0],
                        new String[0][0],
                        new String[0]
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "start-option-validation",
                "error",
                "unsupportedProtocol",
                "startOptionValidation",
                "startValidationUnsupportedProtocol",
                new String[0],
                new String[] {
                "validate-start-options",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact",
                                null,
                                null,
                                null
                        ),
                        new String[0],
                        new String[0][0],
                        new String[0]
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "start-option-validation",
                "error",
                "retryDelaysNotArray",
                "startOptionValidation",
                "startValidationRetryDelaysNotArray",
                new String[0],
                new String[] {
                "validate-start-options",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact",
                                null,
                                null,
                                null
                        ),
                        new String[0],
                        new String[0][0],
                        new String[0]
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "start-option-validation",
                "error",
                "parallelUploadsWithUploadUrl",
                "startOptionValidation",
                "startValidationParallelUploadsWithUploadUrl",
                new String[0],
                new String[] {
                "validate-start-options",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact",
                                null,
                                null,
                                null
                        ),
                        new String[0],
                        new String[0][0],
                        new String[0]
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "start-option-validation",
                "error",
                "parallelUploadsWithUploadSize",
                "startOptionValidation",
                "startValidationParallelUploadsWithUploadSize",
                new String[0],
                new String[] {
                "validate-start-options",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact",
                                null,
                                null,
                                null
                        ),
                        new String[0],
                        new String[0][0],
                        new String[0]
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "start-option-validation",
                "error",
                "parallelUploadsWithDeferredLength",
                "startOptionValidation",
                "startValidationParallelUploadsWithDeferredLength",
                new String[0],
                new String[] {
                "validate-start-options",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact",
                                null,
                                null,
                                null
                        ),
                        new String[0],
                        new String[0][0],
                        new String[0]
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "start-option-validation",
                "error",
                "parallelUploadsWithUploadDataDuringCreation",
                "startOptionValidation",
                "startValidationParallelUploadsWithUploadDataDuringCreation",
                new String[0],
                new String[] {
                "validate-start-options",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact",
                                null,
                                null,
                                null
                        ),
                        new String[0],
                        new String[0][0],
                        new String[0]
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "start-option-validation",
                "error",
                "parallelBoundariesWithoutParallelUploads",
                "startOptionValidation",
                "startValidationParallelBoundariesWithoutParallelUploads",
                new String[0],
                new String[] {
                "validate-start-options",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact",
                                null,
                                null,
                                null
                        ),
                        new String[0],
                        new String[0][0],
                        new String[0]
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "start-option-validation",
                "error",
                "parallelBoundariesLengthMismatch",
                "startOptionValidation",
                "startValidationParallelBoundariesLengthMismatch",
                new String[0],
                new String[] {
                "validate-start-options",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact",
                                null,
                                null,
                                null
                        ),
                        new String[0],
                        new String[0][0],
                        new String[0]
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "detailed-error",
                "error",
                "unexpectedCreateResponse",
                "detailedErrors",
                "detailedCreateResponseError",
                new String[] {
                "createTusUpload",
            },
                new String[] {
                "report-detailed-errors",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact",
                                null,
                                null,
                                null
                        ),
                        new String[0],
                        new String[0][0],
                        new String[0]
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "detailed-error",
                "error",
                "createUploadRequestFailed",
                "detailedErrors",
                "detailedCreateRequestError",
                new String[] {
                "createTusUpload",
            },
                new String[] {
                "report-detailed-errors",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact",
                                null,
                                null,
                                null
                        ),
                        new String[0],
                        new String[0][0],
                        new String[0]
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "upload-body-headers",
                "success",
                null,
                "uploadBodyHeaders",
                "uploadBodyHeaders",
                new String[] {
                "createTusUpload",
                "patchTusUpload",
            },
                new String[] {
                "send-upload-body-headers",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact",
                                null,
                                null,
                                null
                        ),
                        new String[0],
                        new String[0][0],
                        new String[0]
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "custom-request-headers",
                "success",
                null,
                "customRequestHeaders",
                "customRequestHeaders",
                new String[] {
                "createTusUpload",
                "patchTusUpload",
            },
                new String[] {
                "apply-custom-request-headers",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact",
                                null,
                                null,
                                null
                        ),
                        new String[0],
                        new String[0][0],
                        new String[0]
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "request-id-headers",
                "success",
                null,
                "requestIdHeaders",
                "requestIdHeaders",
                new String[] {
                "createTusUpload",
                "patchTusUpload",
            },
                new String[] {
                "add-request-id-header",
                "apply-custom-request-headers",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact",
                                null,
                                null,
                                null
                        ),
                        new String[0],
                        new String[0][0],
                        new String[0]
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "resume-from-previous-upload",
                "success",
                null,
                "resumeUpload",
                "resumeFromPreviousUpload",
                new String[] {
                "getTusUploadOffset",
                "patchTusUpload",
            },
                new String[] {
                "fingerprint-input",
                "resume-from-previous-upload",
                "store-resume-url",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact-except-allowed-extra-events",
                                null,
                                "milestone",
                                "may-emit-extra-samples"
                        ),
                        new String[] {
                        "fingerprint:contract-resume-fingerprint",
                        "url-storage-find:contract-resume-fingerprint:1",
                        "fingerprint:contract-resume-fingerprint",
                        "upload-url-available",
                        "progress:5:11",
                        "progress:11:11",
                        "chunk-complete:6:11:11",
                        "url-storage-remove:tus::contract-resume-fingerprint::1337",
                        "success",
                        "source-close",
                    },
                        new String[][] {
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                    },
                        new String[] {
                        "progress:",
                    }
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "relative-location-resolution",
                "success",
                null,
                "relativeLocationResolution",
                "relativeLocationResolution",
                new String[] {
                "createTusUpload",
                "patchTusUpload",
            },
                new String[] {
                "resolve-relative-location",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact-except-allowed-extra-events",
                                null,
                                "milestone",
                                "may-emit-extra-samples"
                        ),
                        new String[] {
                        "upload-url-available",
                        "progress:0:11",
                        "progress:11:11",
                        "chunk-complete:11:11:11",
                        "success",
                        "source-close",
                    },
                        new String[][] {
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                    },
                        new String[] {
                        "progress:",
                    }
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "array-buffer-input",
                "success",
                null,
                "inputSources",
                "arrayBufferInput",
                new String[] {
                "createTusUpload",
                "patchTusUpload",
            },
                new String[] {
                "read-browser-file",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact",
                                null,
                                null,
                                null
                        ),
                        new String[] {
                        "source-open:array-buffer:11",
                        "success",
                        "source-close",
                    },
                        new String[][] {
                        new String[0],
                        new String[0],
                        new String[0],
                    },
                        new String[0]
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "array-buffer-view-input",
                "success",
                null,
                "inputSources",
                "arrayBufferViewInput",
                new String[] {
                "createTusUpload",
                "patchTusUpload",
            },
                new String[] {
                "read-browser-file",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact",
                                null,
                                null,
                                null
                        ),
                        new String[] {
                        "source-open:array-buffer-view:11",
                        "success",
                        "source-close",
                    },
                        new String[][] {
                        new String[0],
                        new String[0],
                        new String[0],
                    },
                        new String[0]
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "web-readable-stream-input",
                "success",
                null,
                "inputSources",
                "webReadableStreamInput",
                new String[] {
                "createTusUpload",
                "patchTusUpload",
            },
                new String[] {
                "read-web-stream",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact",
                                null,
                                null,
                                null
                        ),
                        new String[] {
                        "source-open:web-readable-stream:null",
                        "success",
                        "source-close",
                    },
                        new String[][] {
                        new String[0],
                        new String[0],
                        new String[0],
                    },
                        new String[0]
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "node-readable-stream-input",
                "success",
                null,
                "inputSources",
                "nodeReadableStreamInput",
                new String[] {
                "createTusUpload",
                "patchTusUpload",
            },
                new String[] {
                "read-node-stream",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact",
                                null,
                                null,
                                null
                        ),
                        new String[] {
                        "source-open:node-readable-stream:null",
                        "success",
                        "source-close",
                    },
                        new String[][] {
                        new String[0],
                        new String[0],
                        new String[0],
                    },
                        new String[0]
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "node-path-input",
                "success",
                null,
                "inputSources",
                "nodePathInput",
                new String[] {
                "createTusUpload",
                "patchTusUpload",
            },
                new String[] {
                "read-node-file",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact",
                                null,
                                null,
                                null
                        ),
                        new String[] {
                        "source-open:node-path-reference:11",
                        "success",
                        "source-close",
                    },
                        new String[][] {
                        new String[0],
                        new String[0],
                        new String[0],
                    },
                        new String[0]
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "deferred-length-upload",
                "success",
                null,
                "deferredLengthUpload",
                "deferredLengthUpload",
                new String[] {
                "createTusUpload",
                "patchTusUpload",
            },
                new String[] {
                "defer-upload-length",
                "emit-progress",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact-except-allowed-extra-events",
                                "allow-known-total-before-declaration",
                                "milestone",
                                "may-emit-extra-samples"
                        ),
                        new String[] {
                        "upload-url-available",
                        "progress:0:11",
                        "progress:11:11",
                        "chunk-complete:11:11:11",
                        "success",
                        "source-close",
                    },
                        new String[][] {
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                    },
                        new String[] {
                        "progress:",
                    }
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "deferred-length-upload",
                "success",
                null,
                "deferredLengthUpload",
                "deferredLengthChunkedUpload",
                new String[] {
                "createTusUpload",
                "patchTusUpload",
            },
                new String[] {
                "defer-upload-length",
                "emit-chunk-complete",
                "emit-progress",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact-except-allowed-extra-events",
                                "allow-known-total-before-declaration",
                                "milestone",
                                "may-emit-extra-samples"
                        ),
                        new String[] {
                        "upload-url-available",
                        "progress:0:null",
                        "progress:5:null",
                        "chunk-complete:5:5:null",
                        "progress:5:null",
                        "progress:10:null",
                        "chunk-complete:5:10:null",
                        "progress:10:11",
                        "progress:11:11",
                        "chunk-complete:1:11:11",
                        "success",
                        "source-close",
                    },
                        new String[][] {
                        new String[0],
                        new String[] {
                                "progress:0:11",
                            },
                        new String[] {
                                "progress:5:11",
                            },
                        new String[] {
                                "chunk-complete:5:5:11",
                            },
                        new String[] {
                                "progress:5:11",
                            },
                        new String[] {
                                "progress:10:11",
                            },
                        new String[] {
                                "chunk-complete:5:10:11",
                            },
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                    },
                        new String[] {
                        "progress:",
                    }
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "override-patch-method",
                "success",
                null,
                "overridePatchMethod",
                "overridePatchMethod",
                new String[] {
                "getTusUploadOffset",
                "patchTusUpload",
            },
                new String[] {
                "override-patch-method",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact",
                                null,
                                null,
                                null
                        ),
                        new String[0],
                        new String[0][0],
                        new String[0]
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "parallel-upload-concat",
                "success",
                null,
                "parallelUploadConcat",
                "parallelUploadConcat",
                new String[] {
                "createTusUpload",
                "createTusUpload",
                "patchTusUpload",
                "patchTusUpload",
                "createTusUpload",
            },
                new String[] {
                "concatenate-partial-uploads",
                "emit-progress",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact-except-allowed-extra-events",
                                null,
                                "milestone",
                                "may-emit-extra-samples"
                        ),
                        new String[] {
                        "progress:5:11",
                        "chunk-complete:5:5:11",
                        "progress:11:11",
                        "chunk-complete:6:11:11",
                    },
                        new String[][] {
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                    },
                        new String[] {
                        "progress:",
                    }
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "parallel-upload-abort-cleanup",
                "aborted",
                null,
                "parallelUploadConcat",
                "parallelUploadAbortCleanup",
                new String[] {
                "createTusUpload",
                "createTusUpload",
                "patchTusUpload",
                "patchTusUpload",
                "terminateTusUpload",
                "terminateTusUpload",
            },
                new String[] {
                "abort-current-request",
                "terminate-upload",
                "concatenate-partial-uploads",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact",
                                null,
                                null,
                                null
                        ),
                        new String[] {
                        "request-abort:3",
                    },
                        new String[][] {
                        new String[0],
                    },
                        new String[0]
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "retry-patch-after-offset-recovery",
                "success",
                null,
                "retryOffsetRecovery",
                "retryPatchAfterOffsetRecovery",
                new String[] {
                "createTusUpload",
                "patchTusUpload",
                "getTusUploadOffset",
                "patchTusUpload",
                "getTusUploadOffset",
                "patchTusUpload",
            },
                new String[] {
                "retry-with-backoff",
                "recover-offset-after-error",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact",
                                null,
                                null,
                                null
                        ),
                        new String[] {
                        "should-retry:0:true",
                        "retry-schedule:0",
                        "should-retry:0:true",
                        "retry-schedule:0",
                    },
                        new String[][] {
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                    },
                        new String[0]
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "request-lifecycle-hooks",
                "success",
                null,
                "requestLifecycleHooks",
                "requestLifecycleHooks",
                new String[] {
                "getTusUploadOffset",
            },
                new String[] {
                "run-request-hooks",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact",
                                null,
                                null,
                                null
                        ),
                        new String[] {
                        "before-request:0",
                        "after-response:0",
                        "success",
                        "source-close",
                    },
                        new String[][] {
                        new String[0],
                        new String[0],
                        new String[0],
                        new String[0],
                    },
                        new String[0]
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "abort-upload",
                "aborted",
                null,
                "abortUpload",
                "abortUpload",
                new String[] {
                "createTusUpload",
            },
                new String[] {
                "abort-current-request",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact",
                                null,
                                null,
                                null
                        ),
                        new String[] {
                        "request-abort:0",
                    },
                        new String[][] {
                        new String[0],
                    },
                        new String[0]
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "abort-upload-after-stored-url",
                "aborted",
                null,
                "abortUpload",
                "abortUploadAfterStoredUrl",
                new String[] {
                "createTusUpload",
                "patchTusUpload",
                "terminateTusUpload",
            },
                new String[] {
                "abort-current-request",
                "terminate-upload",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact",
                                null,
                                null,
                                null
                        ),
                        new String[] {
                        "request-abort:1",
                    },
                        new String[][] {
                        new String[0],
                    },
                        new String[0]
                )
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "terminate-with-retry",
                "terminated",
                null,
                "terminateUpload",
                "terminateWithRetry",
                new String[] {
                "createTusUpload",
                "patchTusUpload",
                "terminateTusUpload",
                "terminateTusUpload",
            },
                new String[] {
                "terminate-upload",
                "retry-with-backoff",
            },
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEvents(
                        new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                                "exact",
                                null,
                                null,
                                null
                        ),
                        new String[] {
                        "should-retry:0:true",
                        "retry-schedule:0",
                    },
                        new String[][] {
                        new String[0],
                        new String[0],
                    },
                        new String[0]
                )
        ),
    };

    private GeneratedTusClientConformanceScenarios() {
    }
}
