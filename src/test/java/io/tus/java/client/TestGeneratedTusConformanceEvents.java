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
        new GeneratedTusEventCanaryCase(
                "creationWithUpload",
                "creationWithUpload",
                new String[] {
                "progress:0:11",
                "progress:11:11",
                "upload-url-available",
                "success",
                "source-close",
            }
        ),
        new GeneratedTusEventCanaryCase(
                "creationWithUpload",
                "creationWithUploadPartialChunk",
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
        new GeneratedTusEventCanaryCase(
                "protocolVersionSelection",
                "ietfDraft05CreationWithUpload",
                new String[] {
                "progress:0:11",
                "progress:11:11",
                "upload-url-available",
                "success",
                "source-close",
            }
        ),
        new GeneratedTusEventCanaryCase(
                "protocolVersionSelection",
                "ietfDraft03ResumeWithoutKnownLength",
                new String[] {
                "upload-url-available",
                "progress:5:11",
                "progress:11:11",
                "chunk-complete:6:11:11",
                "success",
                "source-close",
            }
        ),
        new GeneratedTusEventCanaryCase(
                "resumeUpload",
                "resumeFromPreviousUpload",
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
        new GeneratedTusEventCanaryCase(
                "relativeLocationResolution",
                "relativeLocationResolution",
                new String[] {
                "upload-url-available",
                "progress:0:11",
                "progress:11:11",
                "chunk-complete:11:11:11",
                "success",
                "source-close",
            }
        ),
        new GeneratedTusEventCanaryCase(
                "inputSources",
                "arrayBufferInput",
                new String[] {
                "source-open:array-buffer:11",
                "success",
                "source-close",
            }
        ),
        new GeneratedTusEventCanaryCase(
                "inputSources",
                "arrayBufferViewInput",
                new String[] {
                "source-open:array-buffer-view:11",
                "success",
                "source-close",
            }
        ),
        new GeneratedTusEventCanaryCase(
                "inputSources",
                "webReadableStreamInput",
                new String[] {
                "source-open:web-readable-stream:null",
                "success",
                "source-close",
            }
        ),
        new GeneratedTusEventCanaryCase(
                "inputSources",
                "nodeReadableStreamInput",
                new String[] {
                "source-open:node-readable-stream:null",
                "success",
                "source-close",
            }
        ),
        new GeneratedTusEventCanaryCase(
                "inputSources",
                "nodePathInput",
                new String[] {
                "source-open:node-path-reference:11",
                "success",
                "source-close",
            }
        ),
        new GeneratedTusEventCanaryCase(
                "deferredLengthUpload",
                "deferredLengthUpload",
                new String[] {
                "upload-url-available",
                "progress:0:11",
                "progress:11:11",
                "chunk-complete:11:11:11",
                "success",
                "source-close",
            }
        ),
        new GeneratedTusEventCanaryCase(
                "parallelUploadConcat",
                "parallelUploadConcat",
                new String[] {
                "progress:5:11",
                "chunk-complete:5:5:11",
                "progress:11:11",
                "chunk-complete:6:11:11",
            }
        ),
        new GeneratedTusEventCanaryCase(
                "parallelUploadConcat",
                "parallelUploadAbortCleanup",
                new String[] {
                "request-abort:3",
            }
        ),
        new GeneratedTusEventCanaryCase(
                "retryOffsetRecovery",
                "retryPatchAfterOffsetRecovery",
                new String[] {
                "should-retry:0:true",
                "retry-schedule:0",
                "should-retry:0:true",
                "retry-schedule:0",
            }
        ),
        new GeneratedTusEventCanaryCase(
                "requestLifecycleHooks",
                "requestLifecycleHooks",
                new String[] {
                "before-request:0",
                "after-response:0",
                "success",
                "source-close",
            }
        ),
        new GeneratedTusEventCanaryCase(
                "abortUpload",
                "abortUpload",
                new String[] {
                "request-abort:0",
            }
        ),
        new GeneratedTusEventCanaryCase(
                "abortUpload",
                "abortUploadAfterStoredUrl",
                new String[] {
                "request-abort:1",
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
            assertArrayEquals(testCase.eventKeys, scenario.eventKeys);
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

    private static final class GeneratedTusEventCanaryCase {
        final String featureId;
        final String scenarioId;
        final String[] eventKeys;

        GeneratedTusEventCanaryCase(String featureId, String scenarioId, String[] eventKeys) {
            this.featureId = featureId;
            this.scenarioId = scenarioId;
            this.eventKeys = eventKeys;
        }
    }
}
