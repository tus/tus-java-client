/*
 * Code generated from Transloadit API2 TUS protocol contracts; DO NOT EDIT.
 * If it looks wrong, please report the issue instead of editing this file by hand;
 * the source fix belongs in the protocol contract generator so all TUS clients stay in sync.
 */

package io.tus.java.client;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Generated TUS protocol contract fixture used by tests.
 */
final class GeneratedTusProtocolContract {
    static final Map<String, String> DEFAULT_REQUEST_HEADERS = defaultRequestHeaders();
    static final Map<String, String> DEFAULT_RESPONSE_HEADERS = defaultResponseHeaders();

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
                        "deferredLengthChunkedUpload",
                    },
                        "covered-by-generated-scenario"
                ),
                "Create an upload without a known length and declare the length on the final upload request.",
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
                        "Track the source until the final upload request reveals the total size."
                ),
                new GeneratedTusClientFeatureFlowStep(
                        "operation",
                        "patchTusUpload",
                        "",
                        "",
                        "Declare Upload-Length on the final upload request."
                ),
            },
                new String[] {
                "createTusUpload",
                "patchTusUpload",
            },
                new String[] {
                "defer-upload-length",
                "emit-chunk-complete",
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
                        "requestIdHeaders",
                    },
                        "covered-by-generated-scenario"
                ),
                "Add generated request IDs after protocol and custom request headers.",
                "requestIdHeaders",
                new GeneratedTusClientFeatureFlowStep[] {
                new GeneratedTusClientFeatureFlowStep(
                        "primitive",
                        "",
                        "add-request-id-header",
                        "",
                        "Generate a request ID and apply it after custom request headers so it is authoritative."
                ),
                new GeneratedTusClientFeatureFlowStep(
                        "operation",
                        "createTusUpload",
                        "",
                        "",
                        "Create uploads with a generated request ID."
                ),
                new GeneratedTusClientFeatureFlowStep(
                        "operation",
                        "patchTusUpload",
                        "",
                        "",
                        "Upload bytes with a generated request ID."
                ),
            },
                new String[] {
                "createTusUpload",
                "patchTusUpload",
            },
                new String[] {
                "add-request-id-header",
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
                "abort-current-request",
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
                        "ietfDraft05ChunkedUploadComplete",
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

    static final String MANAGED_UPLOAD_JSON = "{\n  \"capabilities\": {\n    \"cleanup\": {\n      \"policies\": [\n        \"absent-after-source-unavailable\",\n        \"remove-owned-source-after-success\",\n        \"remove-owned-source-after-cancel\",\n        \"retain-owned-source-while-deferred\",\n        \"retain-owned-source-after-permanent-failure\",\n        \"retain-source-after-retryable-failure\",\n        \"remove-managed-state-after-terminal-retention\"\n      ]\n    },\n    \"failureClassification\": {\n      \"permanentFailures\": [\n        \"source-unavailable\",\n        \"unretryable-protocol-error\",\n        \"retry-policy-exhausted\"\n      ],\n      \"retryableFailures\": [\n        \"retryable-protocol-error\",\n        \"io-error\",\n        \"network-unavailable\"\n      ]\n    },\n    \"networkConstraints\": {\n      \"options\": [\n        \"any-network\",\n        \"unmetered-network\"\n      ]\n    },\n    \"retryPolicy\": {\n      \"controls\": [\n        \"max-attempts\",\n        \"deadline\",\n        \"progress-sensitive-budget\",\n        \"unbounded-until-permanent-failure\"\n      ],\n      \"permanentFailure\": \"stop-without-retry\",\n      \"progressReset\": \"reset-budget-after-accepted-offset-advances\"\n    },\n    \"scheduling\": {\n      \"strategies\": [\n        \"foreground-task\",\n        \"process-lifetime-worker-pool\",\n        \"durable-os-scheduler\"\n      ]\n    },\n    \"sourceDurability\": {\n      \"ownedCopyCleanup\": \"after-success-or-cancel\",\n      \"strategies\": [\n        \"copy-to-owned-storage\",\n        \"reference-original-source\",\n        \"memory-only\"\n      ]\n    },\n    \"stateReporting\": {\n      \"states\": [\n        \"pending\",\n        \"running\",\n        \"succeeded\",\n        \"failed\"\n      ],\n      \"terminalRetention\": \"session-and-next-launch\",\n      \"transientRetention\": \"until-terminal\"\n    }\n  },\n  \"conformance\": {\n    \"scenarioIds\": [\n      \"managedUploadDurableRetry\",\n      \"managedUploadPermanentFailure\",\n      \"managedUploadRetryPolicyExhausted\",\n      \"managedUploadSourceUnavailable\",\n      \"managedUploadNetworkConstraint\"\n    ],\n    \"status\": \"covered-by-generated-scenario\"\n  },\n  \"description\": \"Submit upload work that can make sources durable, schedule/resume execution, retry, report state, and clean up while reusing the raw TUS protocol features underneath.\",\n  \"featureId\": \"managedUpload\",\n  \"flow\": [\n    {\n      \"kind\": \"managed-primitive\",\n      \"primitive\": \"accept-upload-submission\",\n      \"summary\": \"Accept source, metadata, headers, endpoint, and retry/scheduling policy.\"\n    },\n    {\n      \"kind\": \"managed-primitive\",\n      \"primitive\": \"make-source-durable\",\n      \"summary\": \"Keep the source readable according to the selected runtime durability strategy.\"\n    },\n    {\n      \"kind\": \"managed-primitive\",\n      \"primitive\": \"schedule-upload-work\",\n      \"summary\": \"Run upload work according to the runtime scheduler capability.\"\n    },\n    {\n      \"featureId\": \"singleUploadLifecycle\",\n      \"kind\": \"protocol-feature\",\n      \"summary\": \"Use the raw protocol upload lifecycle for each execution attempt.\"\n    },\n    {\n      \"featureId\": \"retryOffsetRecovery\",\n      \"kind\": \"protocol-feature\",\n      \"summary\": \"Use protocol retry and offset recovery before classifying terminal failure.\"\n    },\n    {\n      \"kind\": \"managed-primitive\",\n      \"primitive\": \"publish-upload-state\",\n      \"summary\": \"Expose pending, running, succeeded, and failed state snapshots.\"\n    },\n    {\n      \"kind\": \"managed-primitive\",\n      \"primitive\": \"cleanup-managed-upload\",\n      \"summary\": \"Remove owned sources and terminal state according to cleanup policy.\"\n    }\n  ],\n  \"layer\": \"feature-over-protocol\",\n  \"primitives\": [\n    \"accept-upload-submission\",\n    \"make-source-durable\",\n    \"schedule-upload-work\",\n    \"run-protocol-upload\",\n    \"apply-managed-retry-policy\",\n    \"classify-failure\",\n    \"publish-upload-state\",\n    \"cleanup-managed-upload\"\n  ],\n  \"protocolPrimitives\": [\n    \"store-resume-url\",\n    \"resume-from-previous-upload\",\n    \"recover-offset-after-error\",\n    \"retry-with-backoff\",\n    \"emit-progress\",\n    \"emit-chunk-complete\",\n    \"terminate-upload\"\n  ],\n  \"runtimeProfiles\": [\n    {\n      \"networkConstraints\": [\n        \"any-network\",\n        \"unmetered-network\"\n      ],\n      \"runtime\": \"android\",\n      \"scheduler\": \"durable-os-scheduler\",\n      \"sourceDurability\": [\n        \"copy-to-owned-storage\",\n        \"reference-original-source\"\n      ],\n      \"stateBackend\": \"platform-key-value-store\"\n    },\n    {\n      \"networkConstraints\": [\n        \"any-network\",\n        \"unmetered-network\"\n      ],\n      \"runtime\": \"ios\",\n      \"scheduler\": \"durable-os-scheduler\",\n      \"sourceDurability\": [\n        \"copy-to-owned-storage\",\n        \"reference-original-source\"\n      ],\n      \"stateBackend\": \"platform-key-value-store\"\n    },\n    {\n      \"networkConstraints\": [\n        \"any-network\"\n      ],\n      \"runtime\": \"browser\",\n      \"scheduler\": \"foreground-task\",\n      \"sourceDurability\": [\n        \"reference-original-source\",\n        \"memory-only\"\n      ],\n      \"stateBackend\": \"web-storage\"\n    },\n    {\n      \"networkConstraints\": [\n        \"any-network\"\n      ],\n      \"runtime\": \"java\",\n      \"scheduler\": \"process-lifetime-worker-pool\",\n      \"sourceDurability\": [\n        \"copy-to-owned-storage\",\n        \"reference-original-source\"\n      ],\n      \"stateBackend\": \"filesystem\"\n    },\n    {\n      \"networkConstraints\": [\n        \"any-network\"\n      ],\n      \"runtime\": \"node\",\n      \"scheduler\": \"process-lifetime-worker-pool\",\n      \"sourceDurability\": [\n        \"copy-to-owned-storage\",\n        \"reference-original-source\",\n        \"memory-only\"\n      ],\n      \"stateBackend\": \"filesystem\"\n    },\n    {\n      \"networkConstraints\": [\n        \"any-network\"\n      ],\n      \"runtime\": \"react-native\",\n      \"scheduler\": \"foreground-task\",\n      \"sourceDurability\": [\n        \"reference-original-source\",\n        \"memory-only\"\n      ],\n      \"stateBackend\": \"platform-key-value-store\"\n    }\n  ],\n  \"scenarios\": [\n    {\n      \"proofs\": [\n        {\n          \"attempts\": [\n            {\n              \"attemptIndex\": 0,\n              \"failure\": {\n                \"afterAcceptedOffset\": 7,\n                \"kind\": \"io-error\",\n                \"phase\": \"after-accepted-offset\"\n              },\n              \"requests\": [\n                {\n                  \"bodySize\": 0,\n                  \"headers\": {\n                    \"Upload-Length\": \"14\"\n                  },\n                  \"operationId\": \"createTusUpload\",\n                  \"response\": {\n                    \"headers\": {\n                      \"Location\": \"https://tus.io/uploads/managed-durable-retry\"\n                    },\n                    \"statusCode\": 201\n                  },\n                  \"url\": \"endpoint\"\n                },\n                {\n                  \"bodySize\": 7,\n                  \"headers\": {\n                    \"Upload-Offset\": \"0\"\n                  },\n                  \"operationId\": \"patchTusUpload\",\n                  \"response\": {\n                    \"headers\": {\n                      \"Upload-Offset\": \"7\"\n                    },\n                    \"statusCode\": 204\n                  },\n                  \"url\": \"upload\"\n                }\n              ],\n              \"stateAfterAttempt\": \"failed\"\n            },\n            {\n              \"attemptIndex\": 1,\n              \"requests\": [\n                {\n                  \"headers\": {},\n                  \"operationId\": \"getTusUploadOffset\",\n                  \"response\": {\n                    \"headers\": {\n                      \"Upload-Length\": \"14\",\n                      \"Upload-Offset\": \"7\"\n                    },\n                    \"statusCode\": 200\n                  },\n                  \"url\": \"upload\"\n                },\n                {\n                  \"bodySize\": 7,\n                  \"headers\": {\n                    \"Upload-Offset\": \"7\"\n                  },\n                  \"operationId\": \"patchTusUpload\",\n                  \"response\": {\n                    \"headers\": {\n                      \"Upload-Offset\": \"14\"\n                    },\n                    \"statusCode\": 204\n                  },\n                  \"url\": \"upload\"\n                }\n              ],\n              \"stateAfterAttempt\": \"succeeded\"\n            }\n          ],\n          \"cleanup\": {\n            \"ownedSource\": \"remove-owned-source-after-success\",\n            \"resumeUrl\": \"remove-after-success\"\n          },\n          \"input\": {\n            \"chunkSize\": 7,\n            \"content\": \"hello managed!\",\n            \"fingerprint\": \"managed-durable-retry-fingerprint\",\n            \"metadata\": {\n              \"filename\": \"managed.txt\"\n            },\n            \"uploadPath\": \"managed-durable-retry\"\n          },\n          \"network\": {\n            \"current\": \"unmetered-network\",\n            \"decision\": \"start-upload-work\",\n            \"required\": \"any-network\"\n          },\n          \"outcome\": {\n            \"kind\": \"terminal\",\n            \"state\": \"succeeded\"\n          },\n          \"retryDelays\": [\n            0\n          ],\n          \"sourceAvailability\": \"available\",\n          \"sourceDurability\": \"copy-to-owned-storage\",\n          \"states\": [\n            \"pending\",\n            \"running\",\n            \"failed\",\n            \"running\",\n            \"succeeded\"\n          ],\n          \"runtime\": \"java\",\n          \"scheduler\": \"process-lifetime-worker-pool\",\n          \"stateBackend\": \"filesystem\"\n        },\n        {\n          \"attempts\": [\n            {\n              \"attemptIndex\": 0,\n              \"failure\": {\n                \"afterAcceptedOffset\": 7,\n                \"kind\": \"io-error\",\n                \"phase\": \"after-accepted-offset\"\n              },\n              \"requests\": [\n                {\n                  \"bodySize\": 0,\n                  \"headers\": {\n                    \"Upload-Length\": \"14\"\n                  },\n                  \"operationId\": \"createTusUpload\",\n                  \"response\": {\n                    \"headers\": {\n                      \"Location\": \"https://tus.io/uploads/managed-durable-retry\"\n                    },\n                    \"statusCode\": 201\n                  },\n                  \"url\": \"endpoint\"\n                },\n                {\n                  \"bodySize\": 7,\n                  \"headers\": {\n                    \"Upload-Offset\": \"0\"\n                  },\n                  \"operationId\": \"patchTusUpload\",\n                  \"response\": {\n                    \"headers\": {\n                      \"Upload-Offset\": \"7\"\n                    },\n                    \"statusCode\": 204\n                  },\n                  \"url\": \"upload\"\n                }\n              ],\n              \"stateAfterAttempt\": \"failed\"\n            },\n            {\n              \"attemptIndex\": 1,\n              \"requests\": [\n                {\n                  \"headers\": {},\n                  \"operationId\": \"getTusUploadOffset\",\n                  \"response\": {\n                    \"headers\": {\n                      \"Upload-Length\": \"14\",\n                      \"Upload-Offset\": \"7\"\n                    },\n                    \"statusCode\": 200\n                  },\n                  \"url\": \"upload\"\n                },\n                {\n                  \"bodySize\": 7,\n                  \"headers\": {\n                    \"Upload-Offset\": \"7\"\n                  },\n                  \"operationId\": \"patchTusUpload\",\n                  \"response\": {\n                    \"headers\": {\n                      \"Upload-Offset\": \"14\"\n                    },\n                    \"statusCode\": 204\n                  },\n                  \"url\": \"upload\"\n                }\n              ],\n              \"stateAfterAttempt\": \"succeeded\"\n            }\n          ],\n          \"cleanup\": {\n            \"ownedSource\": \"remove-owned-source-after-success\",\n            \"resumeUrl\": \"remove-after-success\"\n          },\n          \"input\": {\n            \"chunkSize\": 7,\n            \"content\": \"hello managed!\",\n            \"fingerprint\": \"managed-durable-retry-fingerprint\",\n            \"metadata\": {\n              \"filename\": \"managed.txt\"\n            },\n            \"uploadPath\": \"managed-durable-retry\"\n          },\n          \"network\": {\n            \"current\": \"unmetered-network\",\n            \"decision\": \"start-upload-work\",\n            \"required\": \"any-network\"\n          },\n          \"outcome\": {\n            \"kind\": \"terminal\",\n            \"state\": \"succeeded\"\n          },\n          \"retryDelays\": [\n            0\n          ],\n          \"sourceAvailability\": \"available\",\n          \"sourceDurability\": \"copy-to-owned-storage\",\n          \"states\": [\n            \"pending\",\n            \"running\",\n            \"failed\",\n            \"running\",\n            \"succeeded\"\n          ],\n          \"runtime\": \"android\",\n          \"scheduler\": \"durable-os-scheduler\",\n          \"stateBackend\": \"platform-key-value-store\"\n        }\n      ],\n      \"requiredPrimitives\": [\n        \"accept-upload-submission\",\n        \"make-source-durable\",\n        \"schedule-upload-work\",\n        \"run-protocol-upload\",\n        \"apply-managed-retry-policy\",\n        \"publish-upload-state\",\n        \"cleanup-managed-upload\"\n      ],\n      \"scenarioId\": \"managedUploadDurableRetry\",\n      \"summary\": \"Submit a durable source, survive scheduler/process interruption, resume by stored upload URL, and finish with cleanup.\"\n    },\n    {\n      \"proofs\": [\n        {\n          \"attempts\": [\n            {\n              \"attemptIndex\": 0,\n              \"failure\": {\n                \"kind\": \"unretryable-protocol-error\",\n                \"phase\": \"during-protocol-request\"\n              },\n              \"requests\": [\n                {\n                  \"bodySize\": 0,\n                  \"headers\": {\n                    \"Upload-Length\": \"14\"\n                  },\n                  \"operationId\": \"createTusUpload\",\n                  \"response\": {\n                    \"headers\": {},\n                    \"statusCode\": 400\n                  },\n                  \"url\": \"endpoint\"\n                }\n              ],\n              \"stateAfterAttempt\": \"failed\"\n            }\n          ],\n          \"cleanup\": {\n            \"ownedSource\": \"retain-owned-source-after-permanent-failure\",\n            \"resumeUrl\": \"absent-after-permanent-failure\"\n          },\n          \"input\": {\n            \"chunkSize\": 7,\n            \"content\": \"hello failure!\",\n            \"fingerprint\": \"managed-permanent-failure-fingerprint\",\n            \"metadata\": {\n              \"filename\": \"managed-permanent-failure.txt\"\n            },\n            \"uploadPath\": \"managed-permanent-failure\"\n          },\n          \"network\": {\n            \"current\": \"unmetered-network\",\n            \"decision\": \"start-upload-work\",\n            \"required\": \"any-network\"\n          },\n          \"outcome\": {\n            \"failure\": \"unretryable-protocol-error\",\n            \"kind\": \"terminal\",\n            \"state\": \"failed\"\n          },\n          \"retryDelays\": [],\n          \"sourceAvailability\": \"available\",\n          \"sourceDurability\": \"copy-to-owned-storage\",\n          \"states\": [\n            \"pending\",\n            \"running\",\n            \"failed\"\n          ],\n          \"runtime\": \"java\",\n          \"scheduler\": \"process-lifetime-worker-pool\",\n          \"stateBackend\": \"filesystem\"\n        },\n        {\n          \"attempts\": [\n            {\n              \"attemptIndex\": 0,\n              \"failure\": {\n                \"kind\": \"unretryable-protocol-error\",\n                \"phase\": \"during-protocol-request\"\n              },\n              \"requests\": [\n                {\n                  \"bodySize\": 0,\n                  \"headers\": {\n                    \"Upload-Length\": \"14\"\n                  },\n                  \"operationId\": \"createTusUpload\",\n                  \"response\": {\n                    \"headers\": {},\n                    \"statusCode\": 400\n                  },\n                  \"url\": \"endpoint\"\n                }\n              ],\n              \"stateAfterAttempt\": \"failed\"\n            }\n          ],\n          \"cleanup\": {\n            \"ownedSource\": \"retain-owned-source-after-permanent-failure\",\n            \"resumeUrl\": \"absent-after-permanent-failure\"\n          },\n          \"input\": {\n            \"chunkSize\": 7,\n            \"content\": \"hello failure!\",\n            \"fingerprint\": \"managed-permanent-failure-fingerprint\",\n            \"metadata\": {\n              \"filename\": \"managed-permanent-failure.txt\"\n            },\n            \"uploadPath\": \"managed-permanent-failure\"\n          },\n          \"network\": {\n            \"current\": \"unmetered-network\",\n            \"decision\": \"start-upload-work\",\n            \"required\": \"any-network\"\n          },\n          \"outcome\": {\n            \"failure\": \"unretryable-protocol-error\",\n            \"kind\": \"terminal\",\n            \"state\": \"failed\"\n          },\n          \"retryDelays\": [],\n          \"sourceAvailability\": \"available\",\n          \"sourceDurability\": \"copy-to-owned-storage\",\n          \"states\": [\n            \"pending\",\n            \"running\",\n            \"failed\"\n          ],\n          \"runtime\": \"android\",\n          \"scheduler\": \"durable-os-scheduler\",\n          \"stateBackend\": \"platform-key-value-store\"\n        }\n      ],\n      \"requiredPrimitives\": [\n        \"accept-upload-submission\",\n        \"make-source-durable\",\n        \"schedule-upload-work\",\n        \"run-protocol-upload\",\n        \"classify-failure\",\n        \"publish-upload-state\",\n        \"cleanup-managed-upload\"\n      ],\n      \"scenarioId\": \"managedUploadPermanentFailure\",\n      \"summary\": \"Classify unretryable protocol failures as terminal without further retry.\"\n    },\n    {\n      \"proofs\": [\n        {\n          \"attempts\": [\n            {\n              \"attemptIndex\": 0,\n              \"failure\": {\n                \"kind\": \"retryable-protocol-error\",\n                \"phase\": \"during-protocol-request\"\n              },\n              \"requests\": [\n                {\n                  \"bodySize\": 0,\n                  \"headers\": {\n                    \"Upload-Length\": \"14\"\n                  },\n                  \"operationId\": \"createTusUpload\",\n                  \"response\": {\n                    \"headers\": {},\n                    \"statusCode\": 500\n                  },\n                  \"url\": \"endpoint\"\n                }\n              ],\n              \"stateAfterAttempt\": \"failed\"\n            },\n            {\n              \"attemptIndex\": 1,\n              \"failure\": {\n                \"kind\": \"retryable-protocol-error\",\n                \"phase\": \"during-protocol-request\"\n              },\n              \"requests\": [\n                {\n                  \"bodySize\": 0,\n                  \"headers\": {\n                    \"Upload-Length\": \"14\"\n                  },\n                  \"operationId\": \"createTusUpload\",\n                  \"response\": {\n                    \"headers\": {},\n                    \"statusCode\": 500\n                  },\n                  \"url\": \"endpoint\"\n                }\n              ],\n              \"stateAfterAttempt\": \"failed\"\n            },\n            {\n              \"attemptIndex\": 2,\n              \"failure\": {\n                \"kind\": \"retryable-protocol-error\",\n                \"phase\": \"during-protocol-request\"\n              },\n              \"requests\": [\n                {\n                  \"bodySize\": 0,\n                  \"headers\": {\n                    \"Upload-Length\": \"14\"\n                  },\n                  \"operationId\": \"createTusUpload\",\n                  \"response\": {\n                    \"headers\": {},\n                    \"statusCode\": 500\n                  },\n                  \"url\": \"endpoint\"\n                }\n              ],\n              \"stateAfterAttempt\": \"failed\"\n            }\n          ],\n          \"cleanup\": {\n            \"ownedSource\": \"retain-owned-source-after-permanent-failure\",\n            \"resumeUrl\": \"absent-after-permanent-failure\"\n          },\n          \"input\": {\n            \"chunkSize\": 7,\n            \"content\": \"hello retries!\",\n            \"fingerprint\": \"managed-retry-exhausted-fingerprint\",\n            \"metadata\": {\n              \"filename\": \"managed-retry-exhausted.txt\"\n            },\n            \"uploadPath\": \"managed-retry-exhausted\"\n          },\n          \"network\": {\n            \"current\": \"unmetered-network\",\n            \"decision\": \"start-upload-work\",\n            \"required\": \"any-network\"\n          },\n          \"outcome\": {\n            \"failure\": \"retry-policy-exhausted\",\n            \"kind\": \"terminal\",\n            \"state\": \"failed\"\n          },\n          \"retryDelays\": [\n            0,\n            0\n          ],\n          \"sourceAvailability\": \"available\",\n          \"sourceDurability\": \"copy-to-owned-storage\",\n          \"states\": [\n            \"pending\",\n            \"running\",\n            \"failed\",\n            \"running\",\n            \"failed\",\n            \"running\",\n            \"failed\"\n          ],\n          \"runtime\": \"java\",\n          \"scheduler\": \"process-lifetime-worker-pool\",\n          \"stateBackend\": \"filesystem\"\n        },\n        {\n          \"attempts\": [\n            {\n              \"attemptIndex\": 0,\n              \"failure\": {\n                \"kind\": \"retryable-protocol-error\",\n                \"phase\": \"during-protocol-request\"\n              },\n              \"requests\": [\n                {\n                  \"bodySize\": 0,\n                  \"headers\": {\n                    \"Upload-Length\": \"14\"\n                  },\n                  \"operationId\": \"createTusUpload\",\n                  \"response\": {\n                    \"headers\": {},\n                    \"statusCode\": 500\n                  },\n                  \"url\": \"endpoint\"\n                }\n              ],\n              \"stateAfterAttempt\": \"failed\"\n            },\n            {\n              \"attemptIndex\": 1,\n              \"failure\": {\n                \"kind\": \"retryable-protocol-error\",\n                \"phase\": \"during-protocol-request\"\n              },\n              \"requests\": [\n                {\n                  \"bodySize\": 0,\n                  \"headers\": {\n                    \"Upload-Length\": \"14\"\n                  },\n                  \"operationId\": \"createTusUpload\",\n                  \"response\": {\n                    \"headers\": {},\n                    \"statusCode\": 500\n                  },\n                  \"url\": \"endpoint\"\n                }\n              ],\n              \"stateAfterAttempt\": \"failed\"\n            },\n            {\n              \"attemptIndex\": 2,\n              \"failure\": {\n                \"kind\": \"retryable-protocol-error\",\n                \"phase\": \"during-protocol-request\"\n              },\n              \"requests\": [\n                {\n                  \"bodySize\": 0,\n                  \"headers\": {\n                    \"Upload-Length\": \"14\"\n                  },\n                  \"operationId\": \"createTusUpload\",\n                  \"response\": {\n                    \"headers\": {},\n                    \"statusCode\": 500\n                  },\n                  \"url\": \"endpoint\"\n                }\n              ],\n              \"stateAfterAttempt\": \"failed\"\n            }\n          ],\n          \"cleanup\": {\n            \"ownedSource\": \"retain-owned-source-after-permanent-failure\",\n            \"resumeUrl\": \"absent-after-permanent-failure\"\n          },\n          \"input\": {\n            \"chunkSize\": 7,\n            \"content\": \"hello retries!\",\n            \"fingerprint\": \"managed-retry-exhausted-fingerprint\",\n            \"metadata\": {\n              \"filename\": \"managed-retry-exhausted.txt\"\n            },\n            \"uploadPath\": \"managed-retry-exhausted\"\n          },\n          \"network\": {\n            \"current\": \"unmetered-network\",\n            \"decision\": \"start-upload-work\",\n            \"required\": \"any-network\"\n          },\n          \"outcome\": {\n            \"failure\": \"retry-policy-exhausted\",\n            \"kind\": \"terminal\",\n            \"state\": \"failed\"\n          },\n          \"retryDelays\": [\n            0,\n            0\n          ],\n          \"sourceAvailability\": \"available\",\n          \"sourceDurability\": \"copy-to-owned-storage\",\n          \"states\": [\n            \"pending\",\n            \"running\",\n            \"failed\",\n            \"running\",\n            \"failed\",\n            \"running\",\n            \"failed\"\n          ],\n          \"runtime\": \"android\",\n          \"scheduler\": \"durable-os-scheduler\",\n          \"stateBackend\": \"platform-key-value-store\"\n        }\n      ],\n      \"requiredPrimitives\": [\n        \"accept-upload-submission\",\n        \"make-source-durable\",\n        \"schedule-upload-work\",\n        \"run-protocol-upload\",\n        \"apply-managed-retry-policy\",\n        \"classify-failure\",\n        \"publish-upload-state\",\n        \"cleanup-managed-upload\"\n      ],\n      \"scenarioId\": \"managedUploadRetryPolicyExhausted\",\n      \"summary\": \"Retry transient protocol failures up to the managed retry budget and then classify the upload as terminally failed.\"\n    },\n    {\n      \"proofs\": [\n        {\n          \"attempts\": [\n            {\n              \"attemptIndex\": 0,\n              \"failure\": {\n                \"kind\": \"source-unavailable\",\n                \"phase\": \"before-protocol-request\"\n              },\n              \"requests\": [],\n              \"stateAfterAttempt\": \"failed\"\n            }\n          ],\n          \"cleanup\": {\n            \"ownedSource\": \"absent-after-source-unavailable\",\n            \"resumeUrl\": \"absent-after-permanent-failure\"\n          },\n          \"input\": {\n            \"chunkSize\": 7,\n            \"content\": \"hello missing!\",\n            \"fingerprint\": \"managed-source-unavailable-fingerprint\",\n            \"metadata\": {\n              \"filename\": \"managed-source-unavailable.txt\"\n            },\n            \"uploadPath\": \"managed-source-unavailable\"\n          },\n          \"network\": {\n            \"current\": \"unmetered-network\",\n            \"decision\": \"start-upload-work\",\n            \"required\": \"any-network\"\n          },\n          \"outcome\": {\n            \"failure\": \"source-unavailable\",\n            \"kind\": \"terminal\",\n            \"state\": \"failed\"\n          },\n          \"retryDelays\": [],\n          \"sourceAvailability\": \"missing-before-durable-copy\",\n          \"sourceDurability\": \"copy-to-owned-storage\",\n          \"states\": [\n            \"pending\",\n            \"running\",\n            \"failed\"\n          ],\n          \"runtime\": \"java\",\n          \"scheduler\": \"process-lifetime-worker-pool\",\n          \"stateBackend\": \"filesystem\"\n        },\n        {\n          \"attempts\": [\n            {\n              \"attemptIndex\": 0,\n              \"failure\": {\n                \"kind\": \"source-unavailable\",\n                \"phase\": \"before-protocol-request\"\n              },\n              \"requests\": [],\n              \"stateAfterAttempt\": \"failed\"\n            }\n          ],\n          \"cleanup\": {\n            \"ownedSource\": \"absent-after-source-unavailable\",\n            \"resumeUrl\": \"absent-after-permanent-failure\"\n          },\n          \"input\": {\n            \"chunkSize\": 7,\n            \"content\": \"hello missing!\",\n            \"fingerprint\": \"managed-source-unavailable-fingerprint\",\n            \"metadata\": {\n              \"filename\": \"managed-source-unavailable.txt\"\n            },\n            \"uploadPath\": \"managed-source-unavailable\"\n          },\n          \"network\": {\n            \"current\": \"unmetered-network\",\n            \"decision\": \"start-upload-work\",\n            \"required\": \"any-network\"\n          },\n          \"outcome\": {\n            \"failure\": \"source-unavailable\",\n            \"kind\": \"terminal\",\n            \"state\": \"failed\"\n          },\n          \"retryDelays\": [],\n          \"sourceAvailability\": \"missing-before-durable-copy\",\n          \"sourceDurability\": \"copy-to-owned-storage\",\n          \"states\": [\n            \"pending\",\n            \"running\",\n            \"failed\"\n          ],\n          \"runtime\": \"android\",\n          \"scheduler\": \"durable-os-scheduler\",\n          \"stateBackend\": \"platform-key-value-store\"\n        }\n      ],\n      \"requiredPrimitives\": [\n        \"accept-upload-submission\",\n        \"make-source-durable\",\n        \"schedule-upload-work\",\n        \"classify-failure\",\n        \"publish-upload-state\",\n        \"cleanup-managed-upload\"\n      ],\n      \"scenarioId\": \"managedUploadSourceUnavailable\",\n      \"summary\": \"Classify source disappearance before protocol requests as terminal without issuing a TUS request.\"\n    },\n    {\n      \"proofs\": [\n        {\n          \"attempts\": [],\n          \"cleanup\": {\n            \"ownedSource\": \"retain-owned-source-while-deferred\",\n            \"resumeUrl\": \"absent-while-deferred\"\n          },\n          \"input\": {\n            \"chunkSize\": 7,\n            \"content\": \"hello later!\",\n            \"fingerprint\": \"managed-network-constraint-fingerprint\",\n            \"metadata\": {\n              \"filename\": \"managed-network-constraint.txt\"\n            },\n            \"uploadPath\": \"managed-network-constraint\"\n          },\n          \"network\": {\n            \"current\": \"metered-network\",\n            \"decision\": \"defer-until-network-constraint-satisfied\",\n            \"required\": \"unmetered-network\"\n          },\n          \"outcome\": {\n            \"kind\": \"deferred\",\n            \"reason\": \"network-constraint-unsatisfied\",\n            \"state\": \"pending\"\n          },\n          \"retryDelays\": [],\n          \"sourceAvailability\": \"available\",\n          \"sourceDurability\": \"copy-to-owned-storage\",\n          \"states\": [\n            \"pending\"\n          ],\n          \"runtime\": \"android\",\n          \"scheduler\": \"durable-os-scheduler\",\n          \"stateBackend\": \"platform-key-value-store\"\n        }\n      ],\n      \"requiredPrimitives\": [\n        \"accept-upload-submission\",\n        \"make-source-durable\",\n        \"schedule-upload-work\",\n        \"publish-upload-state\"\n      ],\n      \"scenarioId\": \"managedUploadNetworkConstraint\",\n      \"summary\": \"Honor network constraints before starting or resuming upload work.\"\n    }\n  ]\n}\n";

    static final String[] MANAGED_UPLOAD_PRIMITIVES =
            new String[] {
            "accept-upload-submission",
            "make-source-durable",
            "schedule-upload-work",
            "run-protocol-upload",
            "apply-managed-retry-policy",
            "classify-failure",
            "publish-upload-state",
            "cleanup-managed-upload",
        };

    static final String[] MANAGED_UPLOAD_RUNTIME_PROFILES =
            new String[] {
            "android",
            "ios",
            "browser",
            "java",
            "node",
            "react-native",
        };

    static final String[] MANAGED_UPLOAD_SCENARIO_IDS =
            new String[] {
            "managedUploadDurableRetry",
            "managedUploadPermanentFailure",
            "managedUploadRetryPolicyExhausted",
            "managedUploadSourceUnavailable",
            "managedUploadNetworkConstraint",
        };

    static final GeneratedTusManagedUploadProofCase[] MANAGED_UPLOAD_PROOF_CASES =
            new GeneratedTusManagedUploadProofCase[] {
        new GeneratedTusProtocolContract.GeneratedTusManagedUploadProofCase(
                "managedUpload",
                "feature-over-protocol",
                "managedUploadDurableRetry",
                new String[] {
                "java",
                "android",
            },
                new String[] {
                "accept-upload-submission",
                "make-source-durable",
                "schedule-upload-work",
                "run-protocol-upload",
                "apply-managed-retry-policy",
                "publish-upload-state",
                "cleanup-managed-upload",
            },
                new String[] {
                "singleUploadLifecycle",
                "retryOffsetRecovery",
            },
                new String[] {
                "android",
                "ios",
                "browser",
                "java",
                "node",
                "react-native",
            }
        ),
        new GeneratedTusProtocolContract.GeneratedTusManagedUploadProofCase(
                "managedUpload",
                "feature-over-protocol",
                "managedUploadPermanentFailure",
                new String[] {
                "java",
                "android",
            },
                new String[] {
                "accept-upload-submission",
                "make-source-durable",
                "schedule-upload-work",
                "run-protocol-upload",
                "classify-failure",
                "publish-upload-state",
                "cleanup-managed-upload",
            },
                new String[] {
                "singleUploadLifecycle",
                "retryOffsetRecovery",
            },
                new String[] {
                "android",
                "ios",
                "browser",
                "java",
                "node",
                "react-native",
            }
        ),
        new GeneratedTusProtocolContract.GeneratedTusManagedUploadProofCase(
                "managedUpload",
                "feature-over-protocol",
                "managedUploadRetryPolicyExhausted",
                new String[] {
                "java",
                "android",
            },
                new String[] {
                "accept-upload-submission",
                "make-source-durable",
                "schedule-upload-work",
                "run-protocol-upload",
                "apply-managed-retry-policy",
                "classify-failure",
                "publish-upload-state",
                "cleanup-managed-upload",
            },
                new String[] {
                "singleUploadLifecycle",
                "retryOffsetRecovery",
            },
                new String[] {
                "android",
                "ios",
                "browser",
                "java",
                "node",
                "react-native",
            }
        ),
        new GeneratedTusProtocolContract.GeneratedTusManagedUploadProofCase(
                "managedUpload",
                "feature-over-protocol",
                "managedUploadSourceUnavailable",
                new String[] {
                "java",
                "android",
            },
                new String[] {
                "accept-upload-submission",
                "make-source-durable",
                "schedule-upload-work",
                "classify-failure",
                "publish-upload-state",
                "cleanup-managed-upload",
            },
                new String[] {
                "singleUploadLifecycle",
                "retryOffsetRecovery",
            },
                new String[] {
                "android",
                "ios",
                "browser",
                "java",
                "node",
                "react-native",
            }
        ),
        new GeneratedTusProtocolContract.GeneratedTusManagedUploadProofCase(
                "managedUpload",
                "feature-over-protocol",
                "managedUploadNetworkConstraint",
                new String[] {
                "android",
            },
                new String[] {
                "accept-upload-submission",
                "make-source-durable",
                "schedule-upload-work",
                "publish-upload-state",
            },
                new String[] {
                "singleUploadLifecycle",
                "retryOffsetRecovery",
            },
                new String[] {
                "android",
                "ios",
                "browser",
                "java",
                "node",
                "react-native",
            }
        ),
    };

    static final GeneratedTusClientConformanceScenario[] CLIENT_CONFORMANCE_SCENARIOS =
            GeneratedTusClientConformanceScenarios.CLIENT_CONFORMANCE_SCENARIOS;

    private GeneratedTusProtocolContract() {
    }

    private static Map<String, String> defaultRequestHeaders() {
        Map<String, String> result = new LinkedHashMap<String, String>();
        result.put("Tus-Resumable", "1.0.0");
        return Collections.unmodifiableMap(result);
    }

    private static Map<String, String> defaultResponseHeaders() {
        Map<String, String> result = new LinkedHashMap<String, String>();
        result.put("Tus-Resumable", "1.0.0");
        return Collections.unmodifiableMap(result);
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
     * Generated managed-upload feature proof fixture.
     */
    static final class GeneratedTusManagedUploadProofCase {
        final String featureId;
        final String layer;
        final String scenarioId;
        final String[] proofRuntimes;
        final String[] requiredPrimitives;
        final String[] protocolFeatureIds;
        final String[] runtimeProfiles;

        GeneratedTusManagedUploadProofCase(
                String featureId,
                String layer,
                String scenarioId,
                String[] proofRuntimes,
                String[] requiredPrimitives,
                String[] protocolFeatureIds,
                String[] runtimeProfiles) {
            this.featureId = featureId;
            this.layer = layer;
            this.scenarioId = scenarioId;
            this.proofRuntimes = proofRuntimes;
            this.requiredPrimitives = requiredPrimitives;
            this.protocolFeatureIds = protocolFeatureIds;
            this.runtimeProfiles = runtimeProfiles;
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
        final GeneratedTusClientConformanceEventPolicy eventPolicy;
        final String[] eventKeys;
        final String[][] eventKeyAlternativeGroups;

        GeneratedTusClientConformanceScenario(
                String behavior,
                GeneratedTusClientConformanceCompletion completion,
                String featureId,
                String scenarioId,
                String[] operationIds,
                String[] primitives,
                GeneratedTusClientConformanceEvents events) {
            this.behavior = behavior;
            this.completionKind = completion.kind;
            this.completionReason = completion.reason;
            this.featureId = featureId;
            this.scenarioId = scenarioId;
            this.operationIds = operationIds;
            this.primitives = primitives;
            this.eventPolicy = events.policy;
            this.eventKeys = events.keys;
            this.eventKeyAlternativeGroups = events.alternativeGroups;
        }
    }

    /**
     * Generated client conformance event fixture bundle.
     */
    static final class GeneratedTusClientConformanceEvents {
        final GeneratedTusClientConformanceEventPolicy policy;
        final String[] keys;
        final String[][] alternativeGroups;

        GeneratedTusClientConformanceEvents(
                GeneratedTusClientConformanceEventPolicy policy,
                String[] keys,
                String[][] alternativeGroups) {
            this.policy = policy;
            this.keys = keys;
            this.alternativeGroups = alternativeGroups;
        }
    }

    /**
     * Generated client conformance event policy fixture.
     */
    static final class GeneratedTusClientConformanceEventPolicy {
        final String matching;
        final String deferredLengthBytesTotal;
        final String progress;
        final String transportProgress;

        GeneratedTusClientConformanceEventPolicy(
                String matching,
                String deferredLengthBytesTotal,
                String progress,
                String transportProgress) {
            this.matching = matching;
            this.deferredLengthBytesTotal = deferredLengthBytesTotal;
            this.progress = progress;
            this.transportProgress = transportProgress;
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
