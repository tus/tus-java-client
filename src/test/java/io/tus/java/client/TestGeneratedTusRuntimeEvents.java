/*
 * Code generated from Transloadit API2 TUS protocol contracts; DO NOT EDIT.
 * If it looks wrong, please report the issue instead of editing this file by hand;
 * the source fix belongs in the protocol contract generator so all TUS clients stay in sync.
 */

package io.tus.java.client;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests generated TUS client runtime event fixtures against the real uploader.
 */
public class TestGeneratedTusRuntimeEvents extends MockServerProvider {
    private static final GeneratedTusRuntimeEventCase[] CASES =
            new GeneratedTusRuntimeEventCase[] {
        new GeneratedTusRuntimeEventCase(
                "singleUploadLifecycle",
                "exact-except-extra-progress",
                false,
                new GeneratedTusRuntimeBeforeStartAction[0],
                new GeneratedTusRuntimeEventInput(
                        "hello world",
                        "generated-contract",
                        "absolute",
                        false,
                        11,
                        null,
                        new GeneratedTusRuntimeEventMetadata[] {
                        new GeneratedTusRuntimeEventMetadata(
                                "filename",
                                "hello.txt"
                        ),
                    }
                ),
                new GeneratedTusRuntimeEventRequest[] {
                        new GeneratedTusRuntimeEventRequest(
                                "POST",
                                "endpoint",
                                201,
                                new GeneratedTusRuntimeEventHeader[] {
                                new GeneratedTusRuntimeEventHeader(
                                        "Upload-Length",
                                        "11"
                                ),
                            },
                                new GeneratedTusRuntimeEventHeader[] {
                                new GeneratedTusRuntimeEventHeader(
                                        "Location",
                                        "https://tus.io/uploads/generated-contract"
                                ),
                            }
                        ),
                        new GeneratedTusRuntimeEventRequest(
                                "PATCH",
                                "upload",
                                204,
                                new GeneratedTusRuntimeEventHeader[] {
                                new GeneratedTusRuntimeEventHeader(
                                        "Upload-Offset",
                                        "0"
                                ),
                            },
                                new GeneratedTusRuntimeEventHeader[] {
                                new GeneratedTusRuntimeEventHeader(
                                        "Upload-Offset",
                                        "11"
                                ),
                            }
                        ),
                },
                new String[] {
                "progress:0:11",
                "progress:11:11",
                "chunk-complete:11:11:11",
            }
        ),
        new GeneratedTusRuntimeEventCase(
                "resumeFromPreviousUpload",
                "exact-except-extra-progress",
                false,
                new GeneratedTusRuntimeBeforeStartAction[] {
                new GeneratedTusRuntimeBeforeStartAction(
                        "resume-from-previous-upload",
                        1,
                        0
                ),
            },
                new GeneratedTusRuntimeEventInput(
                        "hello world",
                        "resume-contract",
                        "stored",
                        false,
                        6,
                        new GeneratedTusRuntimeEventStoredUpload(
                                "contract-resume-fingerprint",
                                true
                        ),
                        new GeneratedTusRuntimeEventMetadata[0]
                ),
                new GeneratedTusRuntimeEventRequest[] {
                        new GeneratedTusRuntimeEventRequest(
                                "HEAD",
                                "upload",
                                200,
                                new GeneratedTusRuntimeEventHeader[0],
                                new GeneratedTusRuntimeEventHeader[] {
                                new GeneratedTusRuntimeEventHeader(
                                        "Upload-Length",
                                        "11"
                                ),
                                new GeneratedTusRuntimeEventHeader(
                                        "Upload-Offset",
                                        "5"
                                ),
                            }
                        ),
                        new GeneratedTusRuntimeEventRequest(
                                "PATCH",
                                "upload",
                                204,
                                new GeneratedTusRuntimeEventHeader[] {
                                new GeneratedTusRuntimeEventHeader(
                                        "Upload-Offset",
                                        "5"
                                ),
                            },
                                new GeneratedTusRuntimeEventHeader[] {
                                new GeneratedTusRuntimeEventHeader(
                                        "Upload-Offset",
                                        "11"
                                ),
                            }
                        ),
                },
                new String[] {
                "progress:5:11",
                "progress:11:11",
                "chunk-complete:6:11:11",
            }
        ),
        new GeneratedTusRuntimeEventCase(
                "relativeLocationResolution",
                "exact-except-extra-progress",
                false,
                new GeneratedTusRuntimeBeforeStartAction[0],
                new GeneratedTusRuntimeEventInput(
                        "hello world",
                        "relative-contract",
                        "relative",
                        true,
                        11,
                        null,
                        new GeneratedTusRuntimeEventMetadata[] {
                        new GeneratedTusRuntimeEventMetadata(
                                "filename",
                                "hello.txt"
                        ),
                    }
                ),
                new GeneratedTusRuntimeEventRequest[] {
                        new GeneratedTusRuntimeEventRequest(
                                "POST",
                                "endpoint",
                                201,
                                new GeneratedTusRuntimeEventHeader[] {
                                new GeneratedTusRuntimeEventHeader(
                                        "Upload-Length",
                                        "11"
                                ),
                            },
                                new GeneratedTusRuntimeEventHeader[] {
                                new GeneratedTusRuntimeEventHeader(
                                        "Location",
                                        "relative-contract"
                                ),
                            }
                        ),
                        new GeneratedTusRuntimeEventRequest(
                                "PATCH",
                                "upload",
                                204,
                                new GeneratedTusRuntimeEventHeader[] {
                                new GeneratedTusRuntimeEventHeader(
                                        "Upload-Offset",
                                        "0"
                                ),
                            },
                                new GeneratedTusRuntimeEventHeader[] {
                                new GeneratedTusRuntimeEventHeader(
                                        "Upload-Offset",
                                        "11"
                                ),
                            }
                        ),
                },
                new String[] {
                "progress:0:11",
                "progress:11:11",
                "chunk-complete:11:11:11",
            }
        ),
        new GeneratedTusRuntimeEventCase(
                "deferredLengthUpload",
                "exact-except-extra-progress",
                true,
                new GeneratedTusRuntimeBeforeStartAction[0],
                new GeneratedTusRuntimeEventInput(
                        "hello world",
                        "deferred-contract",
                        "absolute",
                        false,
                        100,
                        null,
                        new GeneratedTusRuntimeEventMetadata[] {
                        new GeneratedTusRuntimeEventMetadata(
                                "filename",
                                "hello.txt"
                        ),
                    }
                ),
                new GeneratedTusRuntimeEventRequest[] {
                        new GeneratedTusRuntimeEventRequest(
                                "POST",
                                "endpoint",
                                201,
                                new GeneratedTusRuntimeEventHeader[] {
                                new GeneratedTusRuntimeEventHeader(
                                        "Upload-Defer-Length",
                                        "1"
                                ),
                            },
                                new GeneratedTusRuntimeEventHeader[] {
                                new GeneratedTusRuntimeEventHeader(
                                        "Location",
                                        "https://tus.io/uploads/deferred-contract"
                                ),
                            }
                        ),
                        new GeneratedTusRuntimeEventRequest(
                                "PATCH",
                                "upload",
                                204,
                                new GeneratedTusRuntimeEventHeader[] {
                                new GeneratedTusRuntimeEventHeader(
                                        "Upload-Length",
                                        "11"
                                ),
                                new GeneratedTusRuntimeEventHeader(
                                        "Upload-Offset",
                                        "0"
                                ),
                            },
                                new GeneratedTusRuntimeEventHeader[] {
                                new GeneratedTusRuntimeEventHeader(
                                        "Upload-Offset",
                                        "11"
                                ),
                            }
                        ),
                },
                new String[] {
                "progress:0:11",
                "progress:11:11",
                "chunk-complete:11:11:11",
            }
        ),
    };
    private static final GeneratedTusMethodOverride[] METHOD_OVERRIDES =
            new GeneratedTusMethodOverride[] {
        new GeneratedTusMethodOverride(
                "PATCH",
                "POST",
                "X-HTTP-Method-Override",
                "PATCH"
        ),
    };

    /**
     * Verifies the sync uploader emits generated progress and chunk-complete events.
     */
    @Test
    public void testSyncUploaderEmitsGeneratedProgressAndChunkEvents() throws Exception {
        for (GeneratedTusRuntimeEventCase testCase : CASES) {
            mockServer.reset();

            final List<String> events = new ArrayList<String>();
            TusClient client = new TusClient();
            client.setUploadCreationURL(endpointUrlFor(testCase));
            GeneratedTusRuntimeEventUrlStore urlStore = urlStoreFor(testCase);
            if (hasResumeBeforeStartAction(testCase)) {
                if (urlStore == null) {
                    throw new AssertionError(
                            testCase.scenarioId + " cannot resume without generated URL storage");
                }
                client.enableResuming(urlStore);
            }
            if (
                    testCase.input.storedUpload != null
                    && testCase.input.storedUpload.removeFingerprintOnSuccess) {
                client.enableRemoveFingerprintOnSuccess();
            }

            registerResponses(testCase);

            TusUploader uploader = uploaderFor(client, testCase);
            uploader.setChunkSize(testCase.input.chunkSize);
            uploader.setProgressListener(new TusUploader.ProgressListener() {
                @Override
                public void onProgress(long bytesSent, long bytesTotal) {
                    events.add("progress:" + bytesSent + ":" + bytesTotal);
                }
            });
            uploader.setChunkCompleteListener(new TusUploader.ChunkCompleteListener() {
                @Override
                public void onChunkComplete(long chunkSize, long bytesAccepted, long bytesTotal) {
                    events.add("chunk-complete:" + chunkSize + ":" + bytesAccepted + ":" + bytesTotal);
                }
            });

            while (uploader.uploadChunk() > -1) {
                continue;
            }
            uploader.finish();

            assertEvents(testCase, events);
            assertStoredUploadState(testCase, urlStore);
        }
    }

    private TusUploader uploaderFor(TusClient client, GeneratedTusRuntimeEventCase testCase)
            throws Exception {
        GeneratedTusRuntimeBeforeStartAction resumeAction = resumeBeforeStartAction(testCase);
        if (resumeAction != null) {
            assertStoredUploadAvailableForResume(testCase, resumeAction);
            return client.resumeUpload(uploadFor(testCase));
        }

        return client.createUpload(uploadFor(testCase));
    }

    private boolean hasResumeBeforeStartAction(GeneratedTusRuntimeEventCase testCase) {
        return resumeBeforeStartAction(testCase) != null;
    }

    private GeneratedTusRuntimeBeforeStartAction resumeBeforeStartAction(
            GeneratedTusRuntimeEventCase testCase) {
        GeneratedTusRuntimeBeforeStartAction action = null;
        for (GeneratedTusRuntimeBeforeStartAction candidate : testCase.beforeStartActions) {
            if (!"resume-from-previous-upload".equals(candidate.kind)) {
                throw new AssertionError(
                        testCase.scenarioId
                                + " uses unsupported generated beforeStart action "
                                + candidate.kind);
            }

            if (action != null) {
                throw new AssertionError(
                        testCase.scenarioId + " defines more than one resume beforeStart action");
            }

            action = candidate;
        }

        return action;
    }

    private void assertStoredUploadAvailableForResume(
            GeneratedTusRuntimeEventCase testCase,
            GeneratedTusRuntimeBeforeStartAction action) {
        if (testCase.input.storedUpload == null) {
            throw new AssertionError(
                    testCase.scenarioId + " cannot resume without a generated stored upload");
        }
        assertEquals(testCase.scenarioId, 0, action.selectedPreviousUploadIndex);
        assertEquals(testCase.scenarioId, 1, action.expectedPreviousUploadCount);
    }

    private TusUpload uploadFor(GeneratedTusRuntimeEventCase testCase) {
        byte[] content = testCase.input.content.getBytes(StandardCharsets.UTF_8);
        TusUpload upload = new TusUpload();
        upload.setSize(content.length);
        upload.setInputStream(new ByteArrayInputStream(content));
        upload.setMetadata(metadataFor(testCase.input.metadata));
        if (testCase.input.storedUpload != null) {
            upload.setFingerprint(testCase.input.storedUpload.fingerprint);
        }
        upload.setUploadLengthDeferred(testCase.uploadLengthDeferred);
        return upload;
    }

    private Map<String, String> metadataFor(GeneratedTusRuntimeEventMetadata[] metadata) {
        Map<String, String> result = new LinkedHashMap<String, String>();
        for (GeneratedTusRuntimeEventMetadata entry : metadata) {
            result.put(entry.name, entry.value);
        }
        return result;
    }

    private void registerResponses(GeneratedTusRuntimeEventCase testCase) throws Exception {
        for (GeneratedTusRuntimeEventRequest request : testCase.requests) {
            mockServer.when(requestFor(testCase, request, request.method, null))
                    .respond(responseFor(testCase, request));
            GeneratedTusMethodOverride methodOverride = methodOverrideFor(request.method);
            if (methodOverride != null) {
                mockServer.when(requestFor(testCase, request, methodOverride.method, methodOverride))
                        .respond(responseFor(testCase, request));
            }
        }
    }

    private HttpRequest requestFor(
            GeneratedTusRuntimeEventCase testCase,
            GeneratedTusRuntimeEventRequest request,
            String method,
            GeneratedTusMethodOverride methodOverride) throws Exception {
        HttpRequest httpRequest = new HttpRequest()
                .withMethod(method)
                .withPath(pathFor(testCase, request));
        for (GeneratedTusRuntimeEventHeader header : request.requestHeaders) {
            httpRequest.withHeader(header.name, header.value);
        }
        if (methodOverride != null) {
            httpRequest.withHeader(methodOverride.headerName, methodOverride.headerValue);
        }
        return httpRequest;
    }

    private GeneratedTusMethodOverride methodOverrideFor(String originalMethod) {
        for (GeneratedTusMethodOverride methodOverride : METHOD_OVERRIDES) {
            if (methodOverride.originalMethod.equals(originalMethod)) {
                return methodOverride;
            }
        }

        return null;
    }

    private String pathFor(
            GeneratedTusRuntimeEventCase testCase,
            GeneratedTusRuntimeEventRequest request) throws Exception {
        if ("endpoint".equals(request.url)) {
            return endpointUrlFor(testCase).getPath();
        }

        return uploadUrlFor(testCase).getPath();
    }

    private HttpResponse responseFor(
            GeneratedTusRuntimeEventCase testCase,
            GeneratedTusRuntimeEventRequest request) throws Exception {
        HttpResponse response = new HttpResponse().withStatusCode(request.statusCode);
        for (GeneratedTusRuntimeEventHeader header : request.responseHeaders) {
            response.withHeader(header.name, headerValueFor(testCase, header));
        }
        return response;
    }

    private String headerValueFor(
            GeneratedTusRuntimeEventCase testCase,
            GeneratedTusRuntimeEventHeader header) throws Exception {
        if (!"Location".equals(header.name)) {
            return header.value;
        }

        if ("relative".equals(testCase.input.locationHeaderKind)) {
            return testCase.input.uploadPath;
        }

        return uploadUrlFor(testCase).toString();
    }

    private URL uploadUrlFor(GeneratedTusRuntimeEventCase testCase) throws Exception {
        return new URL(mockServerURL.toString() + "/" + testCase.input.uploadPath);
    }

    private URL endpointUrlFor(GeneratedTusRuntimeEventCase testCase) throws Exception {
        if (testCase.input.endpointHasTrailingSlash) {
            return new URL(mockServerURL.toString() + "/");
        }

        return mockServerURL;
    }

    private GeneratedTusRuntimeEventUrlStore urlStoreFor(
            GeneratedTusRuntimeEventCase testCase) throws Exception {
        if (testCase.input.storedUpload == null) {
            return null;
        }

        GeneratedTusRuntimeEventUrlStore store = new GeneratedTusRuntimeEventUrlStore();
        store.set(testCase.input.storedUpload.fingerprint, uploadUrlFor(testCase));
        return store;
    }

    private void assertStoredUploadState(
            GeneratedTusRuntimeEventCase testCase,
            GeneratedTusRuntimeEventUrlStore urlStore) {
        if (urlStore == null) {
            return;
        }

        URL storedUrl = urlStore.get(testCase.input.storedUpload.fingerprint);
        if (testCase.input.storedUpload.removeFingerprintOnSuccess) {
            assertNull(testCase.scenarioId, storedUrl);
            return;
        }

        assertEquals(testCase.scenarioId, uploadUrlForUnchecked(testCase), storedUrl);
    }

    private URL uploadUrlForUnchecked(GeneratedTusRuntimeEventCase testCase) {
        try {
            return uploadUrlFor(testCase);
        } catch (Exception error) {
            throw new AssertionError(error);
        }
    }

    private void assertEvents(GeneratedTusRuntimeEventCase testCase, List<String> events) {
        if ("exact".equals(testCase.eventPolicyMatching)) {
            assertArrayEquals(
                    testCase.scenarioId,
                    testCase.eventKeys,
                    events.toArray(new String[events.size()]));
            return;
        }

        if ("exact-except-extra-progress".equals(testCase.eventPolicyMatching)) {
            assertEventsExactExceptExtraProgress(testCase, events);
            return;
        }

        throw new AssertionError(
                "Unsupported generated event policy "
                        + testCase.eventPolicyMatching
                        + " for "
                        + testCase.scenarioId);
    }

    private void assertEventsExactExceptExtraProgress(
            GeneratedTusRuntimeEventCase testCase,
            List<String> events) {
        int expectedIndex = 0;
        for (String event : events) {
            if (
                    expectedIndex < testCase.eventKeys.length
                    && event.equals(testCase.eventKeys[expectedIndex])) {
                expectedIndex += 1;
                continue;
            }

            if (event.startsWith("progress:")) {
                continue;
            }

            throw new AssertionError(
                    testCase.scenarioId
                            + " emitted unexpected non-progress event "
                            + event
                            + "; expected "
                            + java.util.Arrays.toString(testCase.eventKeys));
        }

        if (expectedIndex == testCase.eventKeys.length) {
            return;
        }

        throw new AssertionError(
                testCase.scenarioId
                        + " did not emit every expected non-extra event; observed "
                        + events
                        + "; expected "
                        + java.util.Arrays.toString(testCase.eventKeys));
    }

    private static final class GeneratedTusRuntimeEventCase {
        final String scenarioId;
        final String eventPolicyMatching;
        final boolean uploadLengthDeferred;
        final GeneratedTusRuntimeBeforeStartAction[] beforeStartActions;
        final GeneratedTusRuntimeEventInput input;
        final GeneratedTusRuntimeEventRequest[] requests;
        final String[] eventKeys;

        GeneratedTusRuntimeEventCase(
                String scenarioId,
                String eventPolicyMatching,
                boolean uploadLengthDeferred,
                GeneratedTusRuntimeBeforeStartAction[] beforeStartActions,
                GeneratedTusRuntimeEventInput input,
                GeneratedTusRuntimeEventRequest[] requests,
                String[] eventKeys) {
            this.scenarioId = scenarioId;
            this.eventPolicyMatching = eventPolicyMatching;
            this.uploadLengthDeferred = uploadLengthDeferred;
            this.beforeStartActions = beforeStartActions;
            this.input = input;
            this.requests = requests;
            this.eventKeys = eventKeys;
        }
    }

    private static final class GeneratedTusRuntimeBeforeStartAction {
        final String kind;
        final int expectedPreviousUploadCount;
        final int selectedPreviousUploadIndex;

        GeneratedTusRuntimeBeforeStartAction(
                String kind,
                int expectedPreviousUploadCount,
                int selectedPreviousUploadIndex) {
            this.kind = kind;
            this.expectedPreviousUploadCount = expectedPreviousUploadCount;
            this.selectedPreviousUploadIndex = selectedPreviousUploadIndex;
        }
    }

    private static final class GeneratedTusRuntimeEventInput {
        final String content;
        final String uploadPath;
        final String locationHeaderKind;
        final boolean endpointHasTrailingSlash;
        final int chunkSize;
        final GeneratedTusRuntimeEventStoredUpload storedUpload;
        final GeneratedTusRuntimeEventMetadata[] metadata;

        GeneratedTusRuntimeEventInput(
                String content,
                String uploadPath,
                String locationHeaderKind,
                boolean endpointHasTrailingSlash,
                int chunkSize,
                GeneratedTusRuntimeEventStoredUpload storedUpload,
                GeneratedTusRuntimeEventMetadata[] metadata) {
            this.content = content;
            this.uploadPath = uploadPath;
            this.locationHeaderKind = locationHeaderKind;
            this.endpointHasTrailingSlash = endpointHasTrailingSlash;
            this.chunkSize = chunkSize;
            this.storedUpload = storedUpload;
            this.metadata = metadata;
        }
    }

    private static final class GeneratedTusRuntimeEventStoredUpload {
        final String fingerprint;
        final boolean removeFingerprintOnSuccess;

        GeneratedTusRuntimeEventStoredUpload(
                String fingerprint,
                boolean removeFingerprintOnSuccess) {
            this.fingerprint = fingerprint;
            this.removeFingerprintOnSuccess = removeFingerprintOnSuccess;
        }
    }

    private static final class GeneratedTusRuntimeEventRequest {
        final String method;
        final String url;
        final int statusCode;
        final GeneratedTusRuntimeEventHeader[] requestHeaders;
        final GeneratedTusRuntimeEventHeader[] responseHeaders;

        GeneratedTusRuntimeEventRequest(
                String method,
                String url,
                int statusCode,
                GeneratedTusRuntimeEventHeader[] requestHeaders,
                GeneratedTusRuntimeEventHeader[] responseHeaders) {
            this.method = method;
            this.url = url;
            this.statusCode = statusCode;
            this.requestHeaders = requestHeaders;
            this.responseHeaders = responseHeaders;
        }
    }

    private static final class GeneratedTusRuntimeEventHeader {
        final String name;
        final String value;

        GeneratedTusRuntimeEventHeader(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }

    private static final class GeneratedTusRuntimeEventMetadata {
        final String name;
        final String value;

        GeneratedTusRuntimeEventMetadata(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }

    private static final class GeneratedTusMethodOverride {
        final String originalMethod;
        final String method;
        final String headerName;
        final String headerValue;

        GeneratedTusMethodOverride(
                String originalMethod,
                String method,
                String headerName,
                String headerValue) {
            this.originalMethod = originalMethod;
            this.method = method;
            this.headerName = headerName;
            this.headerValue = headerValue;
        }
    }

    private static final class GeneratedTusRuntimeEventUrlStore implements TusURLStore {
        private final Map<String, URL> values = new LinkedHashMap<String, URL>();

        @Override
        public URL get(String fingerprint) {
            return values.get(fingerprint);
        }

        @Override
        public void set(String fingerprint, URL url) {
            values.put(fingerprint, url);
        }

        @Override
        public void remove(String fingerprint) {
            values.remove(fingerprint);
        }
    }
}
