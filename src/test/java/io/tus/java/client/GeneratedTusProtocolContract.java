/*
 * Code generated from Transloadit API2 TUS protocol contracts; DO NOT EDIT.
 * If it looks wrong, please report the issue instead of editing this file by hand;
 * the source fix belongs in the protocol contract generator so all TUS clients stay in sync.
 */

package io.tus.java.client;

/**
 * Generated TUS protocol contract fixture used by tests.
 */
final class GeneratedTusProtocolContract {
    static final GeneratedTusWireVersion[] WIRE_VERSIONS = new GeneratedTusWireVersion[] {
        new GeneratedTusWireVersion(
                true,
                "1.0.0"
        ),
    };

    static final GeneratedTusProtocolOperation[] OPERATIONS = new GeneratedTusProtocolOperation[] {
        new GeneratedTusProtocolOperation(
                "discoverTusCapabilities",
                "capability-discovery",
                "OPTIONS",
                "/resumable/files/",
                new GeneratedTusRequestContract(
                        "empty",
                        null,
                        new GeneratedTusHeaderVariant[0]
                ),
                new GeneratedTusResponseContract[] {
                new GeneratedTusResponseContract(
                        200,
                        "empty",
                        new GeneratedTusHeaderVariant[] {
                        new GeneratedTusHeaderVariant(
                                new GeneratedTusHeaderField[] {
                                new GeneratedTusHeaderField(
                                        "Tus-Extension",
                                        "tus-extension",
                                        true
                                ),
                                new GeneratedTusHeaderField(
                                        "Tus-Max-Size",
                                        "tus-max-size",
                                        true
                                ),
                                new GeneratedTusHeaderField(
                                        "Tus-Resumable",
                                        "tus-resumable",
                                        true
                                ),
                                new GeneratedTusHeaderField(
                                        "Tus-Version",
                                        "tus-version",
                                        true
                                ),
                            }
                        ),
                    }
                ),
            }
        ),
        new GeneratedTusProtocolOperation(
                "createTusUpload",
                "creation",
                "POST",
                "/resumable/files/",
                new GeneratedTusRequestContract(
                        "empty",
                        null,
                        new GeneratedTusHeaderVariant[] {
                        new GeneratedTusHeaderVariant(
                                new GeneratedTusHeaderField[] {
                                new GeneratedTusHeaderField(
                                        "Tus-Resumable",
                                        "tus-resumable",
                                        true
                                ),
                                new GeneratedTusHeaderField(
                                        "Upload-Length",
                                        "upload-length",
                                        true
                                ),
                                new GeneratedTusHeaderField(
                                        "Upload-Metadata",
                                        "upload-metadata",
                                        true
                                ),
                            }
                        ),
                        new GeneratedTusHeaderVariant(
                                new GeneratedTusHeaderField[] {
                                new GeneratedTusHeaderField(
                                        "Tus-Resumable",
                                        "tus-resumable",
                                        true
                                ),
                                new GeneratedTusHeaderField(
                                        "Upload-Defer-Length",
                                        "upload-defer-length",
                                        true
                                ),
                                new GeneratedTusHeaderField(
                                        "Upload-Metadata",
                                        "upload-metadata",
                                        true
                                ),
                            }
                        ),
                        new GeneratedTusHeaderVariant(
                                new GeneratedTusHeaderField[] {
                                new GeneratedTusHeaderField(
                                        "Tus-Resumable",
                                        "tus-resumable",
                                        true
                                ),
                                new GeneratedTusHeaderField(
                                        "Upload-Concat",
                                        "upload-concat",
                                        true
                                ),
                                new GeneratedTusHeaderField(
                                        "Upload-Length",
                                        "upload-length",
                                        true
                                ),
                                new GeneratedTusHeaderField(
                                        "Upload-Metadata",
                                        "upload-metadata",
                                        false
                                ),
                            }
                        ),
                        new GeneratedTusHeaderVariant(
                                new GeneratedTusHeaderField[] {
                                new GeneratedTusHeaderField(
                                        "Tus-Resumable",
                                        "tus-resumable",
                                        true
                                ),
                                new GeneratedTusHeaderField(
                                        "Upload-Concat",
                                        "upload-concat",
                                        true
                                ),
                                new GeneratedTusHeaderField(
                                        "Upload-Metadata",
                                        "upload-metadata",
                                        false
                                ),
                            }
                        ),
                    }
                ),
                new GeneratedTusResponseContract[] {
                new GeneratedTusResponseContract(
                        201,
                        "empty",
                        new GeneratedTusHeaderVariant[] {
                        new GeneratedTusHeaderVariant(
                                new GeneratedTusHeaderField[] {
                                new GeneratedTusHeaderField(
                                        "Location",
                                        "location",
                                        true
                                ),
                                new GeneratedTusHeaderField(
                                        "Tus-Resumable",
                                        "tus-resumable",
                                        true
                                ),
                            }
                        ),
                    }
                ),
            }
        ),
        new GeneratedTusProtocolOperation(
                "getTusUploadOffset",
                "offset-discovery",
                "HEAD",
                "/resumable/files/{upload_id}",
                new GeneratedTusRequestContract(
                        "empty",
                        null,
                        new GeneratedTusHeaderVariant[] {
                        new GeneratedTusHeaderVariant(
                                new GeneratedTusHeaderField[] {
                                new GeneratedTusHeaderField(
                                        "Tus-Resumable",
                                        "tus-resumable",
                                        true
                                ),
                            }
                        ),
                    }
                ),
                new GeneratedTusResponseContract[] {
                new GeneratedTusResponseContract(
                        200,
                        "empty",
                        new GeneratedTusHeaderVariant[] {
                        new GeneratedTusHeaderVariant(
                                new GeneratedTusHeaderField[] {
                                new GeneratedTusHeaderField(
                                        "Tus-Resumable",
                                        "tus-resumable",
                                        true
                                ),
                                new GeneratedTusHeaderField(
                                        "Upload-Length",
                                        "upload-length",
                                        true
                                ),
                                new GeneratedTusHeaderField(
                                        "Upload-Offset",
                                        "upload-offset",
                                        true
                                ),
                            }
                        ),
                        new GeneratedTusHeaderVariant(
                                new GeneratedTusHeaderField[] {
                                new GeneratedTusHeaderField(
                                        "Tus-Resumable",
                                        "tus-resumable",
                                        true
                                ),
                                new GeneratedTusHeaderField(
                                        "Upload-Defer-Length",
                                        "upload-defer-length",
                                        true
                                ),
                                new GeneratedTusHeaderField(
                                        "Upload-Offset",
                                        "upload-offset",
                                        true
                                ),
                            }
                        ),
                    }
                ),
            }
        ),
        new GeneratedTusProtocolOperation(
                "patchTusUpload",
                "upload-chunk",
                "PATCH",
                "/resumable/files/{upload_id}",
                new GeneratedTusRequestContract(
                        "binary",
                        "application/offset+octet-stream",
                        new GeneratedTusHeaderVariant[] {
                        new GeneratedTusHeaderVariant(
                                new GeneratedTusHeaderField[] {
                                new GeneratedTusHeaderField(
                                        "Content-Type",
                                        "content-type",
                                        true
                                ),
                                new GeneratedTusHeaderField(
                                        "Tus-Resumable",
                                        "tus-resumable",
                                        true
                                ),
                                new GeneratedTusHeaderField(
                                        "Upload-Offset",
                                        "upload-offset",
                                        true
                                ),
                            }
                        ),
                    }
                ),
                new GeneratedTusResponseContract[] {
                new GeneratedTusResponseContract(
                        204,
                        "empty",
                        new GeneratedTusHeaderVariant[] {
                        new GeneratedTusHeaderVariant(
                                new GeneratedTusHeaderField[] {
                                new GeneratedTusHeaderField(
                                        "Tus-Resumable",
                                        "tus-resumable",
                                        true
                                ),
                                new GeneratedTusHeaderField(
                                        "Upload-Offset",
                                        "upload-offset",
                                        true
                                ),
                            }
                        ),
                    }
                ),
            }
        ),
        new GeneratedTusProtocolOperation(
                "terminateTusUpload",
                "termination",
                "DELETE",
                "/resumable/files/{upload_id}",
                new GeneratedTusRequestContract(
                        "empty",
                        null,
                        new GeneratedTusHeaderVariant[] {
                        new GeneratedTusHeaderVariant(
                                new GeneratedTusHeaderField[] {
                                new GeneratedTusHeaderField(
                                        "Tus-Resumable",
                                        "tus-resumable",
                                        true
                                ),
                            }
                        ),
                    }
                ),
                new GeneratedTusResponseContract[] {
                new GeneratedTusResponseContract(
                        204,
                        "empty",
                        new GeneratedTusHeaderVariant[] {
                        new GeneratedTusHeaderVariant(
                                new GeneratedTusHeaderField[] {
                                new GeneratedTusHeaderField(
                                        "Tus-Resumable",
                                        "tus-resumable",
                                        true
                                ),
                            }
                        ),
                    }
                ),
            }
        ),
        new GeneratedTusProtocolOperation(
                "downloadTusUpload",
                "download",
                "GET",
                "/resumable/files/{upload_id}",
                new GeneratedTusRequestContract(
                        "empty",
                        null,
                        new GeneratedTusHeaderVariant[0]
                ),
                new GeneratedTusResponseContract[] {
                new GeneratedTusResponseContract(
                        200,
                        "binary",
                        new GeneratedTusHeaderVariant[0]
                ),
            }
        ),
    };

    static final GeneratedTusClientFeature[] CLIENT_FEATURES = new GeneratedTusClientFeature[] {
        new GeneratedTusClientFeature(
                new GeneratedTusClientFeatureConformance(
                        new String[] {
                        "singleUploadLifecycle",
                    },
                        "covered-by-generated-scenario"
                ),
                "Create an upload, store its URL, upload bytes, and finish successfully.",
                "singleUploadLifecycle",
                new GeneratedTusClientFeatureFlowStep[] {
                new GeneratedTusClientFeatureFlowStep(
                        "primitive",
                        "",
                        "open-input-source",
                        "",
                        "Open the caller input as a sliceable source."
                ),
                new GeneratedTusClientFeatureFlowStep(
                        "operation",
                        "createTusUpload",
                        "",
                        "",
                        "Create the remote upload resource."
                ),
                new GeneratedTusClientFeatureFlowStep(
                        "operation",
                        "patchTusUpload",
                        "",
                        "",
                        "Upload bytes until the accepted offset reaches the known length."
                ),
            },
                new String[] {
                "createTusUpload",
                "getTusUploadOffset",
                "patchTusUpload",
            },
                new String[] {
                "open-input-source",
                "fingerprint-input",
                "store-resume-url",
                "retry-with-backoff",
                "emit-progress",
                "abort-current-request",
            }
        ),
        new GeneratedTusClientFeature(
                new GeneratedTusClientFeatureConformance(
                        new String[] {
                        "resumeFromPreviousUpload",
                    },
                        "covered-by-generated-scenario"
                ),
                "Resume a stored upload URL by discovering the remote offset before patching.",
                "resumeUpload",
                new GeneratedTusClientFeatureFlowStep[] {
                new GeneratedTusClientFeatureFlowStep(
                        "primitive",
                        "",
                        "resume-from-previous-upload",
                        "",
                        "Load a stored upload URL selected by fingerprint."
                ),
                new GeneratedTusClientFeatureFlowStep(
                        "operation",
                        "getTusUploadOffset",
                        "",
                        "",
                        "Read the server offset for the stored upload URL."
                ),
                new GeneratedTusClientFeatureFlowStep(
                        "operation",
                        "patchTusUpload",
                        "",
                        "",
                        "Continue uploading from the discovered offset."
                ),
            },
                new String[] {
                "getTusUploadOffset",
                "patchTusUpload",
            },
                new String[] {
                "fingerprint-input",
                "resume-from-previous-upload",
                "store-resume-url",
            }
        ),
        new GeneratedTusClientFeature(
                new GeneratedTusClientFeatureConformance(
                        new String[] {
                        "deferredLengthUpload",
                    },
                        "covered-by-generated-scenario"
                ),
                "Create an upload without a known length and declare the length on final PATCH.",
                "deferredLengthUpload",
                new GeneratedTusClientFeatureFlowStep[] {
                new GeneratedTusClientFeatureFlowStep(
                        "operation",
                        "createTusUpload",
                        "",
                        "",
                        "Create the upload with deferred length."
                ),
                new GeneratedTusClientFeatureFlowStep(
                        "primitive",
                        "",
                        "defer-upload-length",
                        "",
                        "Track the source until the final chunk reveals the total size."
                ),
                new GeneratedTusClientFeatureFlowStep(
                        "operation",
                        "patchTusUpload",
                        "",
                        "",
                        "Declare Upload-Length on the final chunk request."
                ),
            },
                new String[] {
                "createTusUpload",
                "patchTusUpload",
            },
                new String[] {
                "defer-upload-length",
                "emit-progress",
            }
        ),
        new GeneratedTusClientFeature(
                new GeneratedTusClientFeatureConformance(
                        new String[] {
                        "creationWithUpload",
                        "creationWithUploadPartialChunk",
                    },
                        "covered-by-generated-scenario"
                ),
                "Send the first bytes on the creation request when the server/client support it.",
                "creationWithUpload",
                new GeneratedTusClientFeatureFlowStep[] {
                new GeneratedTusClientFeatureFlowStep(
                        "operation",
                        "createTusUpload",
                        "",
                        "",
                        "Create the upload while streaming the initial body."
                ),
                new GeneratedTusClientFeatureFlowStep(
                        "primitive",
                        "",
                        "upload-during-creation",
                        "",
                        "Interpret the creation response as an accepted offset."
                ),
            },
                new String[] {
                "createTusUpload",
                "patchTusUpload",
            },
                new String[] {
                "upload-during-creation",
                "emit-progress",
            }
        ),
        new GeneratedTusClientFeature(
                new GeneratedTusClientFeatureConformance(
                        new String[] {
                        "uploadBodyHeaders",
                    },
                        "covered-by-generated-scenario"
                ),
                "Send protocol-specific upload body headers whenever the client transmits file bytes.",
                "uploadBodyHeaders",
                new GeneratedTusClientFeatureFlowStep[] {
                new GeneratedTusClientFeatureFlowStep(
                        "primitive",
                        "",
                        "send-upload-body-headers",
                        "",
                        "Attach the protocol-specific upload body content type when a request has bytes."
                ),
                new GeneratedTusClientFeatureFlowStep(
                        "operation",
                        "patchTusUpload",
                        "",
                        "",
                        "Upload bytes with the protocol-specific body headers."
                ),
            },
                new String[] {
                "createTusUpload",
                "patchTusUpload",
            },
                new String[] {
                "send-upload-body-headers",
            }
        ),
        new GeneratedTusClientFeature(
                new GeneratedTusClientFeatureConformance(
                        new String[] {
                        "customRequestHeaders",
                    },
                        "covered-by-generated-scenario"
                ),
                "Apply user-provided request headers to every upload request.",
                "customRequestHeaders",
                new GeneratedTusClientFeatureFlowStep[] {
                new GeneratedTusClientFeatureFlowStep(
                        "primitive",
                        "",
                        "apply-custom-request-headers",
                        "",
                        "Merge user-provided headers after protocol headers are prepared."
                ),
                new GeneratedTusClientFeatureFlowStep(
                        "operation",
                        "createTusUpload",
                        "",
                        "",
                        "Create uploads with the configured custom headers."
                ),
                new GeneratedTusClientFeatureFlowStep(
                        "operation",
                        "patchTusUpload",
                        "",
                        "",
                        "Upload bytes with the configured custom headers."
                ),
            },
                new String[] {
                "createTusUpload",
                "patchTusUpload",
            },
                new String[] {
                "apply-custom-request-headers",
            }
        ),
        new GeneratedTusClientFeature(
                new GeneratedTusClientFeatureConformance(
                        new String[] {
                        "overridePatchMethod",
                    },
                        "covered-by-generated-scenario"
                ),
                "Tunnel PATCH through POST with the method-override header.",
                "overridePatchMethod",
                new GeneratedTusClientFeatureFlowStep[] {
                new GeneratedTusClientFeatureFlowStep(
                        "operation",
                        "getTusUploadOffset",
                        "",
                        "",
                        "Resume from the upload URL before sending bytes."
                ),
                new GeneratedTusClientFeatureFlowStep(
                        "primitive",
                        "",
                        "override-patch-method",
                        "",
                        "Replace PATCH with POST while preserving the protocol operation intent."
                ),
                new GeneratedTusClientFeatureFlowStep(
                        "operation",
                        "patchTusUpload",
                        "",
                        "",
                        "Upload bytes through the overridden request."
                ),
            },
                new String[] {
                "getTusUploadOffset",
                "patchTusUpload",
            },
                new String[] {
                "override-patch-method",
            }
        ),
        new GeneratedTusClientFeature(
                new GeneratedTusClientFeatureConformance(
                        new String[] {
                        "parallelUploadConcat",
                        "parallelUploadAbortCleanup",
                    },
                        "covered-by-generated-scenario"
                ),
                "Split one input into partial uploads, run the parts concurrently, clean up aborted parts, and concatenate their upload URLs.",
                "parallelUploadConcat",
                new GeneratedTusClientFeatureFlowStep[] {
                new GeneratedTusClientFeatureFlowStep(
                        "primitive",
                        "",
                        "split-parallel-upload-boundaries",
                        "",
                        "Split the input into stable byte ranges."
                ),
                new GeneratedTusClientFeatureFlowStep(
                        "operation",
                        "createTusUpload",
                        "",
                        "",
                        "Create partial uploads for each range."
                ),
                new GeneratedTusClientFeatureFlowStep(
                        "primitive",
                        "",
                        "concatenate-partial-uploads",
                        "",
                        "Create the final upload from completed partial upload URLs."
                ),
            },
                new String[] {
                "createTusUpload",
                "patchTusUpload",
            },
                new String[] {
                "concatenate-partial-uploads",
                "emit-progress",
                "split-parallel-upload-boundaries",
                "terminate-upload",
            }
        ),
        new GeneratedTusClientFeature(
                new GeneratedTusClientFeatureConformance(
                        new String[] {
                        "retryPatchAfterOffsetRecovery",
                    },
                        "covered-by-generated-scenario"
                ),
                "Recover from a failed chunk by reading the server offset before retrying.",
                "retryOffsetRecovery",
                new GeneratedTusClientFeatureFlowStep[] {
                new GeneratedTusClientFeatureFlowStep(
                        "operation",
                        "patchTusUpload",
                        "",
                        "",
                        "Attempt the chunk upload."
                ),
                new GeneratedTusClientFeatureFlowStep(
                        "primitive",
                        "",
                        "recover-offset-after-error",
                        "",
                        "Discover the accepted offset after a retryable failure."
                ),
                new GeneratedTusClientFeatureFlowStep(
                        "operation",
                        "getTusUploadOffset",
                        "",
                        "",
                        "Use HEAD to recover the offset before retrying PATCH."
                ),
            },
                new String[] {
                "createTusUpload",
                "getTusUploadOffset",
                "patchTusUpload",
            },
                new String[] {
                "retry-with-backoff",
                "recover-offset-after-error",
            }
        ),
        new GeneratedTusClientFeature(
                new GeneratedTusClientFeatureConformance(
                        new String[] {
                        "retryPatchAfterOffsetRecovery",
                    },
                        "covered-by-generated-scenario"
                ),
                "Schedule retry timers and reset retry attempts after accepted progress.",
                "retryStateTransitions",
                new GeneratedTusClientFeatureFlowStep[] {
                new GeneratedTusClientFeatureFlowStep(
                        "primitive",
                        "",
                        "schedule-retry-timer",
                        "",
                        "Consume the current retry delay and restart the upload after that timer fires."
                ),
                new GeneratedTusClientFeatureFlowStep(
                        "primitive",
                        "",
                        "reset-retry-attempt-after-progress",
                        "",
                        "Reset retry attempts once a later retry observes server-side offset progress."
                ),
            },
                new String[] {
                "getTusUploadOffset",
                "patchTusUpload",
            },
                new String[] {
                "retry-with-backoff",
                "schedule-retry-timer",
                "reset-retry-attempt-after-progress",
            }
        ),
        new GeneratedTusClientFeature(
                new GeneratedTusClientFeatureConformance(
                        new String[] {
                        "terminateWithRetry",
                    },
                        "covered-by-generated-scenario"
                ),
                "Terminate an upload resource and retry retryable termination failures.",
                "terminateUpload",
                new GeneratedTusClientFeatureFlowStep[] {
                new GeneratedTusClientFeatureFlowStep(
                        "primitive",
                        "",
                        "terminate-upload",
                        "",
                        "Choose server-side termination for an upload URL."
                ),
                new GeneratedTusClientFeatureFlowStep(
                        "operation",
                        "terminateTusUpload",
                        "",
                        "",
                        "Delete the upload resource."
                ),
            },
                new String[] {
                "terminateTusUpload",
            },
                new String[] {
                "terminate-upload",
                "retry-with-backoff",
            }
        ),
        new GeneratedTusClientFeature(
                new GeneratedTusClientFeatureConformance(
                        new String[] {
                        "abortUpload",
                        "abortUploadAfterStoredUrl",
                    },
                        "covered-by-generated-scenario"
                ),
                "Abort the active request, pending retry timer, and any partial uploads.",
                "abortUpload",
                new GeneratedTusClientFeatureFlowStep[] {
                new GeneratedTusClientFeatureFlowStep(
                        "primitive",
                        "",
                        "abort-current-request",
                        "",
                        "Cancel in-flight transport work without emitting user callbacks after abort."
                ),
            },
                new String[] {
                "terminateTusUpload",
            },
                new String[] {
                "abort-current-request",
                "terminate-upload",
            }
        ),
        new GeneratedTusClientFeature(
                new GeneratedTusClientFeatureConformance(
                        new String[] {
                        "singleUploadLifecycle",
                        "creationWithUpload",
                        "resumeFromPreviousUpload",
                    },
                        "covered-by-generated-scenario"
                ),
                "Expose progress and accepted-chunk callbacks from runtime upload activity.",
                "uploadCallbacks",
                new GeneratedTusClientFeatureFlowStep[] {
                new GeneratedTusClientFeatureFlowStep(
                        "primitive",
                        "",
                        "emit-progress",
                        "",
                        "Report bytes sent against known or deferred length."
                ),
                new GeneratedTusClientFeatureFlowStep(
                        "primitive",
                        "",
                        "emit-chunk-complete",
                        "",
                        "Report chunk size, accepted offset, and total size after server acceptance."
                ),
                new GeneratedTusClientFeatureFlowStep(
                        "primitive",
                        "",
                        "emit-upload-url",
                        "",
                        "Notify once a usable upload URL is known."
                ),
            },
                new String[0],
                new String[] {
                "emit-progress",
                "emit-chunk-complete",
                "emit-upload-url",
            }
        ),
        new GeneratedTusClientFeature(
                new GeneratedTusClientFeatureConformance(
                        new String[] {
                        "requestLifecycleHooks",
                        "retryPatchAfterOffsetRecovery",
                    },
                        "covered-by-generated-scenario"
                ),
                "Run before-request, after-response, and custom retry hooks around transport.",
                "requestLifecycleHooks",
                new GeneratedTusClientFeatureFlowStep[] {
                new GeneratedTusClientFeatureFlowStep(
                        "primitive",
                        "",
                        "run-request-hooks",
                        "",
                        "Call user hooks around each HTTP request/response pair."
                ),
                new GeneratedTusClientFeatureFlowStep(
                        "primitive",
                        "",
                        "customize-retry",
                        "",
                        "Let user retry policy override default retry decisions."
                ),
            },
                new String[0],
                new String[] {
                "customize-retry",
                "run-request-hooks",
            }
        ),
        new GeneratedTusClientFeature(
                new GeneratedTusClientFeatureConformance(
                        new String[] {
                        "singleUploadLifecycle",
                        "resumeFromPreviousUpload",
                    },
                        "covered-by-generated-scenario"
                ),
                "Persist, find, resume, and optionally remove upload URLs by fingerprint.",
                "resumeUrlStorage",
                new GeneratedTusClientFeatureFlowStep[] {
                new GeneratedTusClientFeatureFlowStep(
                        "primitive",
                        "",
                        "fingerprint-input",
                        "",
                        "Derive a stable key for the input when possible."
                ),
                new GeneratedTusClientFeatureFlowStep(
                        "primitive",
                        "",
                        "store-resume-url",
                        "",
                        "Persist upload URLs and partial-upload URLs for future resumption."
                ),
                new GeneratedTusClientFeatureFlowStep(
                        "primitive",
                        "",
                        "remove-stored-url-on-success",
                        "",
                        "Remove stored upload URLs when configured after success or invalidation."
                ),
            },
                new String[0],
                new String[] {
                "fingerprint-input",
                "store-resume-url",
                "remove-stored-url-on-success",
            }
        ),
        new GeneratedTusClientFeature(
                new GeneratedTusClientFeatureConformance(
                        new String[] {
                        "arrayBufferInput",
                        "arrayBufferViewInput",
                        "webReadableStreamInput",
                        "nodeReadableStreamInput",
                        "nodePathInput",
                    },
                        "covered-by-generated-scenario"
                ),
                "Support the reference client input/source families across runtimes.",
                "inputSources",
                new GeneratedTusClientFeatureFlowStep[] {
                new GeneratedTusClientFeatureFlowStep(
                        "primitive",
                        "",
                        "read-browser-file",
                        "",
                        "Read browser Blob/File and ArrayBuffer-family inputs."
                ),
                new GeneratedTusClientFeatureFlowStep(
                        "primitive",
                        "",
                        "read-node-stream",
                        "",
                        "Read Node streams when size and chunk constraints are satisfied."
                ),
                new GeneratedTusClientFeatureFlowStep(
                        "primitive",
                        "",
                        "read-web-stream",
                        "",
                        "Read Web Streams with deferred or configured size."
                ),
                new GeneratedTusClientFeatureFlowStep(
                        "primitive",
                        "",
                        "read-node-file",
                        "",
                        "Read filesystem paths and fs streams, including parallel ranges."
                ),
            },
                new String[0],
                new String[] {
                "read-browser-file",
                "read-node-file",
                "read-node-stream",
                "read-web-stream",
            }
        ),
        new GeneratedTusClientFeature(
                new GeneratedTusClientFeatureConformance(
                        new String[] {
                        "webStorageUrlStorageBackend",
                        "fileUrlStorageBackend",
                    },
                        "covered-by-generated-scenario"
                ),
                "Support browser and file-backed URL storage implementations.",
                "urlStorageBackends",
                new GeneratedTusClientFeatureFlowStep[] {
                new GeneratedTusClientFeatureFlowStep(
                        "primitive",
                        "",
                        "store-browser-url",
                        "",
                        "Persist upload records in browser localStorage."
                ),
                new GeneratedTusClientFeatureFlowStep(
                        "primitive",
                        "",
                        "store-file-url",
                        "",
                        "Persist upload records in the Node file store."
                ),
            },
                new String[0],
                new String[] {
                "store-browser-url",
                "store-file-url",
            }
        ),
        new GeneratedTusClientFeature(
                new GeneratedTusClientFeatureConformance(
                        new String[] {
                        "ietfDraft05CreationWithUpload",
                        "ietfDraft03ResumeWithoutKnownLength",
                    },
                        "covered-by-generated-scenario"
                ),
                "Select between tus v1 and supported IETF draft client protocol modes.",
                "protocolVersionSelection",
                new GeneratedTusClientFeatureFlowStep[] {
                new GeneratedTusClientFeatureFlowStep(
                        "primitive",
                        "",
                        "select-client-protocol",
                        "",
                        "Choose request headers and response expectations for the selected protocol."
                ),
            },
                new String[] {
                "createTusUpload",
                "getTusUploadOffset",
                "patchTusUpload",
            },
                new String[] {
                "select-client-protocol",
            }
        ),
        new GeneratedTusClientFeature(
                new GeneratedTusClientFeatureConformance(
                        new String[] {
                        "relativeLocationResolution",
                    },
                        "covered-by-generated-scenario"
                ),
                "Normalize relative Location headers against the request endpoint.",
                "relativeLocationResolution",
                new GeneratedTusClientFeatureFlowStep[] {
                new GeneratedTusClientFeatureFlowStep(
                        "primitive",
                        "",
                        "resolve-relative-location",
                        "",
                        "Resolve server Location headers with the creation endpoint as origin."
                ),
            },
                new String[] {
                "createTusUpload",
            },
                new String[] {
                "resolve-relative-location",
            }
        ),
        new GeneratedTusClientFeature(
                new GeneratedTusClientFeatureConformance(
                        new String[] {
                        "startValidationMissingInput",
                        "startValidationMissingEndpointOrUploadUrl",
                        "startValidationUnsupportedProtocol",
                        "startValidationRetryDelaysNotArray",
                        "startValidationParallelUploadsWithUploadUrl",
                        "startValidationParallelUploadsWithUploadSize",
                        "startValidationParallelUploadsWithDeferredLength",
                        "startValidationParallelUploadsWithUploadDataDuringCreation",
                        "startValidationParallelBoundariesWithoutParallelUploads",
                        "startValidationParallelBoundariesLengthMismatch",
                    },
                        "covered-by-generated-scenario"
                ),
                "Validate option combinations before starting runtime work.",
                "startOptionValidation",
                new GeneratedTusClientFeatureFlowStep[] {
                new GeneratedTusClientFeatureFlowStep(
                        "primitive",
                        "",
                        "validate-start-options",
                        "",
                        "Reject missing inputs and incompatible parallel/deferred/resume options."
                ),
            },
                new String[0],
                new String[] {
                "validate-start-options",
            }
        ),
        new GeneratedTusClientFeature(
                new GeneratedTusClientFeatureConformance(
                        new String[] {
                        "detailedCreateResponseError",
                        "detailedCreateRequestError",
                    },
                        "covered-by-generated-scenario"
                ),
                "Attach request, response, status, body, and request ID context to errors.",
                "detailedErrors",
                new GeneratedTusClientFeatureFlowStep[] {
                new GeneratedTusClientFeatureFlowStep(
                        "primitive",
                        "",
                        "report-detailed-errors",
                        "",
                        "Return user-facing errors with enough transport context for debugging."
                ),
            },
                new String[0],
                new String[] {
                "report-detailed-errors",
            }
        ),
    };

    static final GeneratedTusClientConformanceScenario[] CLIENT_CONFORMANCE_SCENARIOS =
            new GeneratedTusClientConformanceScenario[] {
        new GeneratedTusClientConformanceScenario(
                "single-upload-lifecycle",
                new GeneratedTusClientConformanceCompletion(
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
        new GeneratedTusClientConformanceScenario(
                "creation-with-upload",
                new GeneratedTusClientConformanceCompletion(
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
        new GeneratedTusClientConformanceScenario(
                "creation-with-upload-partial-chunk",
                new GeneratedTusClientConformanceCompletion(
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
        new GeneratedTusClientConformanceScenario(
                "creation-with-upload",
                new GeneratedTusClientConformanceCompletion(
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
        new GeneratedTusClientConformanceScenario(
                "upload-body-headers",
                new GeneratedTusClientConformanceCompletion(
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
        new GeneratedTusClientConformanceScenario(
                "start-option-validation",
                new GeneratedTusClientConformanceCompletion(
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
        new GeneratedTusClientConformanceScenario(
                "start-option-validation",
                new GeneratedTusClientConformanceCompletion(
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
        new GeneratedTusClientConformanceScenario(
                "start-option-validation",
                new GeneratedTusClientConformanceCompletion(
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
        new GeneratedTusClientConformanceScenario(
                "start-option-validation",
                new GeneratedTusClientConformanceCompletion(
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
        new GeneratedTusClientConformanceScenario(
                "start-option-validation",
                new GeneratedTusClientConformanceCompletion(
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
        new GeneratedTusClientConformanceScenario(
                "start-option-validation",
                new GeneratedTusClientConformanceCompletion(
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
        new GeneratedTusClientConformanceScenario(
                "start-option-validation",
                new GeneratedTusClientConformanceCompletion(
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
        new GeneratedTusClientConformanceScenario(
                "start-option-validation",
                new GeneratedTusClientConformanceCompletion(
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
        new GeneratedTusClientConformanceScenario(
                "start-option-validation",
                new GeneratedTusClientConformanceCompletion(
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
        new GeneratedTusClientConformanceScenario(
                "start-option-validation",
                new GeneratedTusClientConformanceCompletion(
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
        new GeneratedTusClientConformanceScenario(
                "detailed-error",
                new GeneratedTusClientConformanceCompletion(
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
        new GeneratedTusClientConformanceScenario(
                "detailed-error",
                new GeneratedTusClientConformanceCompletion(
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
        new GeneratedTusClientConformanceScenario(
                "upload-body-headers",
                new GeneratedTusClientConformanceCompletion(
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
        new GeneratedTusClientConformanceScenario(
                "custom-request-headers",
                new GeneratedTusClientConformanceCompletion(
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
        new GeneratedTusClientConformanceScenario(
                "resume-from-previous-upload",
                new GeneratedTusClientConformanceCompletion(
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
        new GeneratedTusClientConformanceScenario(
                "relative-location-resolution",
                new GeneratedTusClientConformanceCompletion(
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
        new GeneratedTusClientConformanceScenario(
                "array-buffer-input",
                new GeneratedTusClientConformanceCompletion(
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
        new GeneratedTusClientConformanceScenario(
                "array-buffer-view-input",
                new GeneratedTusClientConformanceCompletion(
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
        new GeneratedTusClientConformanceScenario(
                "web-readable-stream-input",
                new GeneratedTusClientConformanceCompletion(
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
        new GeneratedTusClientConformanceScenario(
                "node-readable-stream-input",
                new GeneratedTusClientConformanceCompletion(
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
        new GeneratedTusClientConformanceScenario(
                "node-path-input",
                new GeneratedTusClientConformanceCompletion(
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
        new GeneratedTusClientConformanceScenario(
                "deferred-length-upload",
                new GeneratedTusClientConformanceCompletion(
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
        new GeneratedTusClientConformanceScenario(
                "override-patch-method",
                new GeneratedTusClientConformanceCompletion(
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
        new GeneratedTusClientConformanceScenario(
                "parallel-upload-concat",
                new GeneratedTusClientConformanceCompletion(
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
        new GeneratedTusClientConformanceScenario(
                "parallel-upload-abort-cleanup",
                new GeneratedTusClientConformanceCompletion(
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
        new GeneratedTusClientConformanceScenario(
                "retry-patch-after-offset-recovery",
                new GeneratedTusClientConformanceCompletion(
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
        new GeneratedTusClientConformanceScenario(
                "request-lifecycle-hooks",
                new GeneratedTusClientConformanceCompletion(
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
        new GeneratedTusClientConformanceScenario(
                "abort-upload",
                new GeneratedTusClientConformanceCompletion(
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
        new GeneratedTusClientConformanceScenario(
                "abort-upload-after-stored-url",
                new GeneratedTusClientConformanceCompletion(
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
        new GeneratedTusClientConformanceScenario(
                "terminate-with-retry",
                new GeneratedTusClientConformanceCompletion(
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

    private GeneratedTusProtocolContract() {
    }

    /**
     * Generated wire-version fixture.
     */
    static final class GeneratedTusWireVersion {
        final boolean defaultVersion;
        final String value;

        GeneratedTusWireVersion(boolean defaultVersion, String value) {
            this.defaultVersion = defaultVersion;
            this.value = value;
        }
    }

    /**
     * Generated HTTP header field fixture.
     */
    static final class GeneratedTusHeaderField {
        final String displayName;
        final String name;
        final boolean required;

        GeneratedTusHeaderField(String displayName, String name, boolean required) {
            this.displayName = displayName;
            this.name = name;
            this.required = required;
        }
    }

    /**
     * Generated alternative HTTP header set fixture.
     */
    static final class GeneratedTusHeaderVariant {
        final GeneratedTusHeaderField[] fields;

        GeneratedTusHeaderVariant(GeneratedTusHeaderField[] fields) {
            this.fields = fields;
        }
    }

    /**
     * Generated request contract fixture.
     */
    static final class GeneratedTusRequestContract {
        final String bodyKind;
        final String contentType;
        final GeneratedTusHeaderVariant[] headerVariants;

        GeneratedTusRequestContract(
                String bodyKind,
                String contentType,
                GeneratedTusHeaderVariant[] headerVariants) {
            this.bodyKind = bodyKind;
            this.contentType = contentType;
            this.headerVariants = headerVariants;
        }
    }

    /**
     * Generated response contract fixture.
     */
    static final class GeneratedTusResponseContract {
        final int statusCode;
        final String bodyKind;
        final GeneratedTusHeaderVariant[] headerVariants;

        GeneratedTusResponseContract(
                int statusCode,
                String bodyKind,
                GeneratedTusHeaderVariant[] headerVariants) {
            this.statusCode = statusCode;
            this.bodyKind = bodyKind;
            this.headerVariants = headerVariants;
        }
    }

    /**
     * Generated protocol operation fixture.
     */
    static final class GeneratedTusProtocolOperation {
        final String operationId;
        final String role;
        final String method;
        final String path;
        final GeneratedTusRequestContract request;
        final GeneratedTusResponseContract[] responses;

        GeneratedTusProtocolOperation(
                String operationId,
                String role,
                String method,
                String path,
                GeneratedTusRequestContract request,
                GeneratedTusResponseContract[] responses) {
            this.operationId = operationId;
            this.role = role;
            this.method = method;
            this.path = path;
            this.request = request;
            this.responses = responses;
        }
    }

    /**
     * Generated client feature fixture.
     */
    static final class GeneratedTusClientFeature {
        final GeneratedTusClientFeatureConformance conformance;
        final String description;
        final String featureId;
        final GeneratedTusClientFeatureFlowStep[] flow;
        final String[] operationIds;
        final String[] primitives;

        GeneratedTusClientFeature(
                GeneratedTusClientFeatureConformance conformance,
                String description,
                String featureId,
                GeneratedTusClientFeatureFlowStep[] flow,
                String[] operationIds,
                String[] primitives) {
            this.conformance = conformance;
            this.description = description;
            this.featureId = featureId;
            this.flow = flow;
            this.operationIds = operationIds;
            this.primitives = primitives;
        }
    }

    /**
     * Generated client feature conformance coverage.
     */
    static final class GeneratedTusClientFeatureConformance {
        final String[] scenarioIds;
        final String status;

        GeneratedTusClientFeatureConformance(String[] scenarioIds, String status) {
            this.scenarioIds = scenarioIds;
            this.status = status;
        }
    }

    /**
     * Generated client feature flow step.
     */
    static final class GeneratedTusClientFeatureFlowStep {
        final String kind;
        final String operationId;
        final String primitive;
        final String condition;
        final String summary;

        GeneratedTusClientFeatureFlowStep(
                String kind,
                String operationId,
                String primitive,
                String condition,
                String summary) {
            this.kind = kind;
            this.operationId = operationId;
            this.primitive = primitive;
            this.condition = condition;
            this.summary = summary;
        }
    }

    /**
     * Generated client conformance scenario fixture.
     */
    static final class GeneratedTusClientConformanceScenario {
        final String behavior;
        final String completionKind;
        final String completionReason;
        final String featureId;
        final String scenarioId;
        final String[] operationIds;
        final String[] primitives;
        final String[] eventKeys;

        GeneratedTusClientConformanceScenario(
                String behavior,
                GeneratedTusClientConformanceCompletion completion,
                String featureId,
                String scenarioId,
                String[] operationIds,
                String[] primitives,
                String[] eventKeys) {
            this.behavior = behavior;
            this.completionKind = completion.kind;
            this.completionReason = completion.reason;
            this.featureId = featureId;
            this.scenarioId = scenarioId;
            this.operationIds = operationIds;
            this.primitives = primitives;
            this.eventKeys = eventKeys;
        }
    }

    /**
     * Generated client conformance completion fixture.
     */
    static final class GeneratedTusClientConformanceCompletion {
        final String kind;
        final String reason;

        GeneratedTusClientConformanceCompletion(String kind, String reason) {
            this.kind = kind;
            this.reason = reason;
        }
    }
}
