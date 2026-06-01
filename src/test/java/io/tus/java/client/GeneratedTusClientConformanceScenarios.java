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
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceCompletion(
                        "success",
                        null
                ),
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
                new String[] {
                "fingerprint:contract-single-fingerprint",
                "upload-url-available",
                "url-storage-add:contract-single-fingerprint:https://tus.io/uploads/generated-contract",
                "progress:0:11",
                "progress:11:11",
                "chunk-complete:11:11:11",
                "success",
                "source-close",
            }
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "creation-with-upload",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceCompletion(
                        "success",
                        null
                ),
                "creationWithUpload",
                "creationWithUpload",
                new String[] {
                "createTusUpload",
            },
                new String[] {
                "upload-during-creation",
                "emit-progress",
            },
                new String[] {
                "progress:0:11",
                "progress:11:11",
                "upload-url-available",
                "success",
                "source-close",
            }
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "creation-with-upload-partial-chunk",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceCompletion(
                        "success",
                        null
                ),
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
            }
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "creation-with-upload",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceCompletion(
                        "success",
                        null
                ),
                "protocolVersionSelection",
                "ietfDraft05CreationWithUpload",
                new String[] {
                "createTusUpload",
            },
                new String[] {
                "select-client-protocol",
            },
                new String[] {
                "progress:0:11",
                "progress:11:11",
                "upload-url-available",
                "success",
                "source-close",
            }
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "upload-body-headers",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceCompletion(
                        "success",
                        null
                ),
                "protocolVersionSelection",
                "ietfDraft03ResumeWithoutKnownLength",
                new String[] {
                "getTusUploadOffset",
                "patchTusUpload",
            },
                new String[] {
                "select-client-protocol",
            },
                new String[] {
                "upload-url-available",
                "progress:5:11",
                "progress:11:11",
                "chunk-complete:6:11:11",
                "success",
                "source-close",
            }
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "start-option-validation",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceCompletion(
                        "error",
                        "missingInput"
                ),
                "startOptionValidation",
                "startValidationMissingInput",
                new String[0],
                new String[] {
                "validate-start-options",
            },
                new String[0]
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "start-option-validation",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceCompletion(
                        "error",
                        "missingEndpointOrUploadUrl"
                ),
                "startOptionValidation",
                "startValidationMissingEndpointOrUploadUrl",
                new String[0],
                new String[] {
                "validate-start-options",
            },
                new String[0]
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "start-option-validation",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceCompletion(
                        "error",
                        "unsupportedProtocol"
                ),
                "startOptionValidation",
                "startValidationUnsupportedProtocol",
                new String[0],
                new String[] {
                "validate-start-options",
            },
                new String[0]
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "start-option-validation",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceCompletion(
                        "error",
                        "retryDelaysNotArray"
                ),
                "startOptionValidation",
                "startValidationRetryDelaysNotArray",
                new String[0],
                new String[] {
                "validate-start-options",
            },
                new String[0]
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "start-option-validation",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceCompletion(
                        "error",
                        "parallelUploadsWithUploadUrl"
                ),
                "startOptionValidation",
                "startValidationParallelUploadsWithUploadUrl",
                new String[0],
                new String[] {
                "validate-start-options",
            },
                new String[0]
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "start-option-validation",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceCompletion(
                        "error",
                        "parallelUploadsWithUploadSize"
                ),
                "startOptionValidation",
                "startValidationParallelUploadsWithUploadSize",
                new String[0],
                new String[] {
                "validate-start-options",
            },
                new String[0]
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "start-option-validation",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceCompletion(
                        "error",
                        "parallelUploadsWithDeferredLength"
                ),
                "startOptionValidation",
                "startValidationParallelUploadsWithDeferredLength",
                new String[0],
                new String[] {
                "validate-start-options",
            },
                new String[0]
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "start-option-validation",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceCompletion(
                        "error",
                        "parallelUploadsWithUploadDataDuringCreation"
                ),
                "startOptionValidation",
                "startValidationParallelUploadsWithUploadDataDuringCreation",
                new String[0],
                new String[] {
                "validate-start-options",
            },
                new String[0]
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "start-option-validation",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceCompletion(
                        "error",
                        "parallelBoundariesWithoutParallelUploads"
                ),
                "startOptionValidation",
                "startValidationParallelBoundariesWithoutParallelUploads",
                new String[0],
                new String[] {
                "validate-start-options",
            },
                new String[0]
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "start-option-validation",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceCompletion(
                        "error",
                        "parallelBoundariesLengthMismatch"
                ),
                "startOptionValidation",
                "startValidationParallelBoundariesLengthMismatch",
                new String[0],
                new String[] {
                "validate-start-options",
            },
                new String[0]
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "detailed-error",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceCompletion(
                        "error",
                        "unexpectedCreateResponse"
                ),
                "detailedErrors",
                "detailedCreateResponseError",
                new String[] {
                "createTusUpload",
            },
                new String[] {
                "report-detailed-errors",
            },
                new String[0]
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "detailed-error",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceCompletion(
                        "error",
                        "createUploadRequestFailed"
                ),
                "detailedErrors",
                "detailedCreateRequestError",
                new String[] {
                "createTusUpload",
            },
                new String[] {
                "report-detailed-errors",
            },
                new String[0]
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "upload-body-headers",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceCompletion(
                        "success",
                        null
                ),
                "uploadBodyHeaders",
                "uploadBodyHeaders",
                new String[] {
                "createTusUpload",
                "patchTusUpload",
            },
                new String[] {
                "send-upload-body-headers",
            },
                new String[0]
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "custom-request-headers",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceCompletion(
                        "success",
                        null
                ),
                "customRequestHeaders",
                "customRequestHeaders",
                new String[] {
                "createTusUpload",
                "patchTusUpload",
            },
                new String[] {
                "apply-custom-request-headers",
            },
                new String[0]
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "resume-from-previous-upload",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceCompletion(
                        "success",
                        null
                ),
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
            }
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "relative-location-resolution",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceCompletion(
                        "success",
                        null
                ),
                "relativeLocationResolution",
                "relativeLocationResolution",
                new String[] {
                "createTusUpload",
                "patchTusUpload",
            },
                new String[] {
                "resolve-relative-location",
            },
                new String[] {
                "upload-url-available",
                "progress:0:11",
                "progress:11:11",
                "chunk-complete:11:11:11",
                "success",
                "source-close",
            }
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "array-buffer-input",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceCompletion(
                        "success",
                        null
                ),
                "inputSources",
                "arrayBufferInput",
                new String[] {
                "createTusUpload",
                "patchTusUpload",
            },
                new String[] {
                "read-browser-file",
            },
                new String[] {
                "source-open:array-buffer:11",
                "success",
                "source-close",
            }
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "array-buffer-view-input",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceCompletion(
                        "success",
                        null
                ),
                "inputSources",
                "arrayBufferViewInput",
                new String[] {
                "createTusUpload",
                "patchTusUpload",
            },
                new String[] {
                "read-browser-file",
            },
                new String[] {
                "source-open:array-buffer-view:11",
                "success",
                "source-close",
            }
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "web-readable-stream-input",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceCompletion(
                        "success",
                        null
                ),
                "inputSources",
                "webReadableStreamInput",
                new String[] {
                "createTusUpload",
                "patchTusUpload",
            },
                new String[] {
                "read-web-stream",
            },
                new String[] {
                "source-open:web-readable-stream:null",
                "success",
                "source-close",
            }
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "node-readable-stream-input",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceCompletion(
                        "success",
                        null
                ),
                "inputSources",
                "nodeReadableStreamInput",
                new String[] {
                "createTusUpload",
                "patchTusUpload",
            },
                new String[] {
                "read-node-stream",
            },
                new String[] {
                "source-open:node-readable-stream:null",
                "success",
                "source-close",
            }
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "node-path-input",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceCompletion(
                        "success",
                        null
                ),
                "inputSources",
                "nodePathInput",
                new String[] {
                "createTusUpload",
                "patchTusUpload",
            },
                new String[] {
                "read-node-file",
            },
                new String[] {
                "source-open:node-path-reference:11",
                "success",
                "source-close",
            }
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "deferred-length-upload",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceCompletion(
                        "success",
                        null
                ),
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
                new String[] {
                "upload-url-available",
                "progress:0:11",
                "progress:11:11",
                "chunk-complete:11:11:11",
                "success",
                "source-close",
            }
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "override-patch-method",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceCompletion(
                        "success",
                        null
                ),
                "overridePatchMethod",
                "overridePatchMethod",
                new String[] {
                "getTusUploadOffset",
                "patchTusUpload",
            },
                new String[] {
                "override-patch-method",
            },
                new String[0]
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "parallel-upload-concat",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceCompletion(
                        "success",
                        null
                ),
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
                new String[] {
                "progress:5:11",
                "chunk-complete:5:5:11",
                "progress:11:11",
                "chunk-complete:6:11:11",
            }
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "parallel-upload-abort-cleanup",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceCompletion(
                        "aborted",
                        null
                ),
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
                new String[] {
                "request-abort:3",
            }
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "retry-patch-after-offset-recovery",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceCompletion(
                        "success",
                        null
                ),
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
                new String[] {
                "should-retry:0:true",
                "retry-schedule:0",
                "should-retry:0:true",
                "retry-schedule:0",
            }
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "request-lifecycle-hooks",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceCompletion(
                        "success",
                        null
                ),
                "requestLifecycleHooks",
                "requestLifecycleHooks",
                new String[] {
                "getTusUploadOffset",
            },
                new String[] {
                "run-request-hooks",
            },
                new String[] {
                "before-request:0",
                "after-response:0",
                "success",
                "source-close",
            }
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "abort-upload",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceCompletion(
                        "aborted",
                        null
                ),
                "abortUpload",
                "abortUpload",
                new String[] {
                "createTusUpload",
            },
                new String[] {
                "abort-current-request",
            },
                new String[] {
                "request-abort:0",
            }
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "abort-upload-after-stored-url",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceCompletion(
                        "aborted",
                        null
                ),
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
                new String[] {
                "request-abort:1",
            }
        ),
        new GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario(
                "terminate-with-retry",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceCompletion(
                        "terminated",
                        null
                ),
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
                new String[0]
        ),
    };

    private GeneratedTusClientConformanceScenarios() {
    }
}
