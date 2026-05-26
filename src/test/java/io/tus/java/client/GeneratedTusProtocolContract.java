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
                "singleUploadLifecycle",
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
                "resumeUpload",
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
                "deferredLengthUpload",
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
                "creationWithUpload",
                new String[] {
                "createTusUpload",
            },
                new String[] {
                "upload-during-creation",
                "emit-progress",
            }
        ),
        new GeneratedTusClientFeature(
                "overridePatchMethod",
                new String[] {
                "getTusUploadOffset",
                "patchTusUpload",
            },
                new String[] {
                "override-patch-method",
            }
        ),
        new GeneratedTusClientFeature(
                "parallelUploadConcat",
                new String[] {
                "createTusUpload",
                "patchTusUpload",
            },
                new String[] {
                "concatenate-partial-uploads",
                "emit-progress",
            }
        ),
        new GeneratedTusClientFeature(
                "retryOffsetRecovery",
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
                "terminateUpload",
                new String[] {
                "terminateTusUpload",
            },
                new String[] {
                "terminate-upload",
                "retry-with-backoff",
            }
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
        final String featureId;
        final String[] operationIds;
        final String[] primitives;

        GeneratedTusClientFeature(String featureId, String[] operationIds, String[] primitives) {
            this.featureId = featureId;
            this.operationIds = operationIds;
            this.primitives = primitives;
        }
    }
}
