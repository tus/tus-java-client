/*
 * Code generated from Transloadit API2 TUS protocol contracts; DO NOT EDIT.
 * If it looks wrong, please report the issue instead of editing this file by hand;
 * the source fix belongs in the protocol contract generator so all TUS clients stay in sync.
 */

package io.tus.java.client;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Tests generated TUS client conformance event fixtures.
 */
public class TestGeneratedTusConformanceEvents {
    private static final GeneratedTusEventCanaryCase[] CASES =
            new GeneratedTusEventCanaryCase[] {
        new GeneratedTusEventCanaryCase(
                "singleUploadLifecycle",
                "singleUploadLifecycle",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                        "exact-except-extra-progress",
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
            }
        ),
        new GeneratedTusEventCanaryCase(
                "creationWithUpload",
                "creationWithUpload",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                        "exact-except-extra-progress",
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
            }
        ),
        new GeneratedTusEventCanaryCase(
                "creationWithUpload",
                "creationWithUploadPartialChunk",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                        "exact-except-extra-progress",
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
            }
        ),
        new GeneratedTusEventCanaryCase(
                "protocolVersionSelection",
                "ietfDraft05CreationWithUpload",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                        "exact-except-extra-progress",
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
            }
        ),
        new GeneratedTusEventCanaryCase(
                "protocolVersionSelection",
                "ietfDraft05ChunkedUploadComplete",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                        "exact-except-extra-progress",
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
            }
        ),
        new GeneratedTusEventCanaryCase(
                "protocolVersionSelection",
                "ietfDraft03ResumeWithoutKnownLength",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                        "exact-except-extra-progress",
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
            }
        ),
        new GeneratedTusEventCanaryCase(
                "resumeUpload",
                "resumeFromPreviousUpload",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                        "exact-except-extra-progress",
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
            }
        ),
        new GeneratedTusEventCanaryCase(
                "relativeLocationResolution",
                "relativeLocationResolution",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                        "exact-except-extra-progress",
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
            }
        ),
        new GeneratedTusEventCanaryCase(
                "inputSources",
                "arrayBufferInput",
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
            }
        ),
        new GeneratedTusEventCanaryCase(
                "inputSources",
                "arrayBufferViewInput",
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
            }
        ),
        new GeneratedTusEventCanaryCase(
                "inputSources",
                "webReadableStreamInput",
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
            }
        ),
        new GeneratedTusEventCanaryCase(
                "inputSources",
                "nodeReadableStreamInput",
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
            }
        ),
        new GeneratedTusEventCanaryCase(
                "inputSources",
                "nodePathInput",
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
            }
        ),
        new GeneratedTusEventCanaryCase(
                "deferredLengthUpload",
                "deferredLengthUpload",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                        "exact-except-extra-progress",
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
            }
        ),
        new GeneratedTusEventCanaryCase(
                "deferredLengthUpload",
                "deferredLengthChunkedUpload",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                        "exact-except-extra-progress",
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
            }
        ),
        new GeneratedTusEventCanaryCase(
                "parallelUploadConcat",
                "parallelUploadConcat",
                new GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy(
                        "exact-except-extra-progress",
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
            }
        ),
        new GeneratedTusEventCanaryCase(
                "parallelUploadConcat",
                "parallelUploadAbortCleanup",
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
            }
        ),
        new GeneratedTusEventCanaryCase(
                "retryOffsetRecovery",
                "retryPatchAfterOffsetRecovery",
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
            }
        ),
        new GeneratedTusEventCanaryCase(
                "requestLifecycleHooks",
                "requestLifecycleHooks",
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
            }
        ),
        new GeneratedTusEventCanaryCase(
                "abortUpload",
                "abortUpload",
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
            }
        ),
        new GeneratedTusEventCanaryCase(
                "abortUpload",
                "abortUploadAfterStoredUrl",
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
            }
        ),
        new GeneratedTusEventCanaryCase(
                "terminateUpload",
                "terminateWithRetry",
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
            }
        ),
    };

    private static final GeneratedTusProofProfileCase[] PROOF_CASES =
            new GeneratedTusProofProfileCase[] {
        new GeneratedTusProofProfileCase(
                "urlStorageCreateFlow",
                "single-upload-lifecycle",
                "success",
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
            }
        ),
        new GeneratedTusProofProfileCase(
                "customRequestHeaders",
                "custom-request-headers",
                "success",
                "customRequestHeaders",
                "customRequestHeaders",
                new String[] {
                "createTusUpload",
                "patchTusUpload",
            },
                new String[] {
                "apply-custom-request-headers",
            }
        ),
        new GeneratedTusProofProfileCase(
                "overridePatchMethod",
                "override-patch-method",
                "success",
                "overridePatchMethod",
                "overridePatchMethod",
                new String[] {
                "getTusUploadOffset",
                "patchTusUpload",
            },
                new String[] {
                "override-patch-method",
            }
        ),
        new GeneratedTusProofProfileCase(
                "nodePathFileUpload",
                "node-path-input",
                "success",
                "inputSources",
                "nodePathInput",
                new String[] {
                "createTusUpload",
                "patchTusUpload",
            },
                new String[] {
                "read-node-file",
            }
        ),
        new GeneratedTusProofProfileCase(
                "resumeFromPreviousUpload",
                "resume-from-previous-upload",
                "success",
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
            }
        ),
    };

    /**
     * Verifies generated feature-level event keys survive in the Java fixture.
     */
    @Test
    public void testGeneratedScenarioEventKeys() {
        for (GeneratedTusEventCanaryCase testCase : CASES) {
            GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario scenario =
                    findScenario(testCase.scenarioId);
            GeneratedTusProtocolContract.GeneratedTusClientFeature feature =
                    findFeature(testCase.featureId);

            assertEquals(testCase.featureId, scenario.featureId);
            assertContains(feature.conformance.scenarioIds, scenario.scenarioId);
            assertEventPolicyEquals(testCase.eventPolicy, scenario.eventPolicy);
            assertArrayEquals(testCase.eventKeys, scenario.eventKeys);
            assertStringMatrixEquals(
                    testCase.eventKeyAlternativeGroups,
                    scenario.eventKeyAlternativeGroups);
        }
    }

    /**
     * Verifies generated named proof-profile scenarios survive in the Java fixture.
     */
    @Test
    public void testGeneratedProofProfileScenarios() {
        for (GeneratedTusProofProfileCase testCase : PROOF_CASES) {
            GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario scenario =
                    findScenario(testCase.scenarioId);
            GeneratedTusProtocolContract.GeneratedTusClientFeature feature =
                    findFeature(testCase.featureId);

            assertEquals(testCase.behavior, scenario.behavior);
            assertEquals(testCase.completionKind, scenario.completionKind);
            assertEquals(testCase.featureId, scenario.featureId);
            assertContains(feature.conformance.scenarioIds, scenario.scenarioId);
            assertArrayEquals(testCase.operationIds, scenario.operationIds);
            assertArrayEquals(testCase.primitives, scenario.primitives);
        }
    }

    /**
     * Verifies managed-upload proof scenarios stay wired to protocol features and primitives.
     */
    @Test
    public void testGeneratedManagedUploadProofScenarios() {
        for (GeneratedTusProtocolContract.GeneratedTusManagedUploadProofCase testCase
                : GeneratedTusProtocolContract.MANAGED_UPLOAD_PROOF_CASES) {
            assertEquals("managedUpload", testCase.featureId);
            assertEquals("feature-over-protocol", testCase.layer);
            assertContains(
                    GeneratedTusProtocolContract.MANAGED_UPLOAD_SCENARIO_IDS,
                    testCase.scenarioId);
            assertArrayEquals(
                    GeneratedTusProtocolContract.MANAGED_UPLOAD_RUNTIME_PROFILES,
                    testCase.runtimeProfiles);

            for (String primitive : testCase.requiredPrimitives) {
                assertContains(GeneratedTusProtocolContract.MANAGED_UPLOAD_PRIMITIVES, primitive);
            }
            for (String featureId : testCase.protocolFeatureIds) {
                findFeature(featureId);
            }
        }
    }

    private static GeneratedTusProtocolContract.GeneratedTusClientFeature findFeature(
            String featureId) {
        for (GeneratedTusProtocolContract.GeneratedTusClientFeature feature
                : GeneratedTusProtocolContract.CLIENT_FEATURES) {
            if (feature.featureId.equals(featureId)) {
                return feature;
            }
        }

        throw new AssertionError("Missing generated TUS client feature: " + featureId);
    }

    private static GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario findScenario(
            String scenarioId) {
        for (GeneratedTusProtocolContract.GeneratedTusClientConformanceScenario scenario
                : GeneratedTusProtocolContract.CLIENT_CONFORMANCE_SCENARIOS) {
            if (scenario.scenarioId.equals(scenarioId)) {
                return scenario;
            }
        }

        throw new AssertionError("Missing generated TUS client scenario: " + scenarioId);
    }

    private static void assertContains(String[] values, String expected) {
        for (String value : values) {
            if (value.equals(expected)) {
                return;
            }
        }

        throw new AssertionError("Missing generated value: " + expected);
    }

    private static void assertEventPolicyEquals(
            GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy expected,
            GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy actual) {
        assertEquals(expected.matching, actual.matching);
        assertEquals(expected.deferredLengthBytesTotal, actual.deferredLengthBytesTotal);
        assertEquals(expected.progress, actual.progress);
        assertEquals(expected.transportProgress, actual.transportProgress);
    }

    private static void assertStringMatrixEquals(String[][] expected, String[][] actual) {
        assertEquals(expected.length, actual.length);
        for (int index = 0; index < expected.length; index += 1) {
            assertArrayEquals(expected[index], actual[index]);
        }
    }

    private static final class GeneratedTusEventCanaryCase {
        final String featureId;
        final String scenarioId;
        final GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy eventPolicy;
        final String[] eventKeys;
        final String[][] eventKeyAlternativeGroups;

        GeneratedTusEventCanaryCase(
                String featureId,
                String scenarioId,
                GeneratedTusProtocolContract.GeneratedTusClientConformanceEventPolicy eventPolicy,
                String[] eventKeys,
                String[][] eventKeyAlternativeGroups) {
            this.featureId = featureId;
            this.scenarioId = scenarioId;
            this.eventPolicy = eventPolicy;
            this.eventKeys = eventKeys;
            this.eventKeyAlternativeGroups = eventKeyAlternativeGroups;
        }
    }

    private static final class GeneratedTusProofProfileCase {
        final String profile;
        final String behavior;
        final String completionKind;
        final String featureId;
        final String scenarioId;
        final String[] operationIds;
        final String[] primitives;

        GeneratedTusProofProfileCase(
                String profile,
                String behavior,
                String completionKind,
                String featureId,
                String scenarioId,
                String[] operationIds,
                String[] primitives) {
            this.profile = profile;
            this.behavior = behavior;
            this.completionKind = completionKind;
            this.featureId = featureId;
            this.scenarioId = scenarioId;
            this.operationIds = operationIds;
            this.primitives = primitives;
        }
    }
}
