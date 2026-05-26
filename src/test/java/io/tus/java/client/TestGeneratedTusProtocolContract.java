package io.tus.java.client;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests the generated API2 protocol contract canary.
 */
public class TestGeneratedTusProtocolContract {

    /**
     * Verifies the runtime constant is sourced from the generated protocol fixture.
     */
    @Test
    public void testDefaultProtocolVersionMatchesRuntimeConstant() {
        String generatedDefault = null;
        int defaultCount = 0;

        for (GeneratedTusProtocolContract.GeneratedTusWireVersion wireVersion
                : GeneratedTusProtocolContract.WIRE_VERSIONS) {
            if (wireVersion.defaultVersion) {
                defaultCount++;
                generatedDefault = wireVersion.value;
            }
        }

        assertEquals(1, defaultCount);
        assertEquals("1.0.0", generatedDefault);
        assertEquals(generatedDefault, TusProtocol.DEFAULT_PROTOCOL_VERSION);
        assertEquals(generatedDefault, TusClient.TUS_VERSION);
    }

    /**
     * Verifies generated request-header variants retain creation requirements.
     */
    @Test
    public void testCreateUploadOperationKeepsRequiredHeaders() {
        GeneratedTusProtocolContract.GeneratedTusProtocolOperation operation =
                findOperation("createTusUpload");

        assertEquals("POST", operation.method);
        assertEquals("/resumable/files/", operation.path);
        assertRequiredHeaderVariant(
                operation.request.headerVariants, "tus-resumable", "upload-length");
        assertRequiredHeaderVariant(
                operation.request.headerVariants, "tus-resumable", "upload-defer-length");
        assertRequiredHeaderVariant(
                operation.request.headerVariants,
                "tus-resumable",
                "upload-concat",
                "upload-length");
        assertRequiredHeaderVariant(
                operation.request.headerVariants, "tus-resumable", "upload-concat");
    }

    /**
     * Verifies the generated high-level lifecycle feature points at raw protocol operations.
     */
    @Test
    public void testSingleUploadLifecycleFeatureReferencesProtocolOperations() {
        GeneratedTusProtocolContract.GeneratedTusClientFeature feature =
                findFeature("singleUploadLifecycle");

        assertContains(feature.operationIds, "createTusUpload");
        assertContains(feature.operationIds, "getTusUploadOffset");
        assertContains(feature.operationIds, "patchTusUpload");
        assertContains(feature.primitives, "store-resume-url");
        assertContains(feature.primitives, "emit-progress");
    }

    private static GeneratedTusProtocolContract.GeneratedTusProtocolOperation findOperation(
            String operationId) {
        for (GeneratedTusProtocolContract.GeneratedTusProtocolOperation operation
                : GeneratedTusProtocolContract.OPERATIONS) {
            if (operation.operationId.equals(operationId)) {
                return operation;
            }
        }

        throw new AssertionError("Missing generated TUS operation: " + operationId);
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

    private static boolean hasRequiredHeader(
            GeneratedTusProtocolContract.GeneratedTusHeaderVariant variant,
            String headerName) {
        assertNotNull(variant);

        for (GeneratedTusProtocolContract.GeneratedTusHeaderField field : variant.fields) {
            if (field.required && field.name.equals(headerName)) {
                return true;
            }
        }

        return false;
    }

    private static void assertRequiredHeaderVariant(
            GeneratedTusProtocolContract.GeneratedTusHeaderVariant[] variants,
            String... headerNames) {
        for (GeneratedTusProtocolContract.GeneratedTusHeaderVariant variant : variants) {
            boolean hasAllHeaders = true;
            for (String headerName : headerNames) {
                if (!hasRequiredHeader(variant, headerName)) {
                    hasAllHeaders = false;
                    break;
                }
            }

            if (hasAllHeaders) {
                return;
            }
        }

        throw new AssertionError("Missing generated header variant");
    }

    private static void assertContains(String[] values, String expected) {
        for (String value : values) {
            if (value.equals(expected)) {
                return;
            }
        }

        throw new AssertionError("Missing generated value: " + expected);
    }
}
