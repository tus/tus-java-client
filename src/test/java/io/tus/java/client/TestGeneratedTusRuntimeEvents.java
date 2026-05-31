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
                new GeneratedTusRuntimeEventInput(
                        "hello world",
                        "generated-contract",
                        "absolute",
                        false,
                        11,
                        null,
                        false,
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
                new GeneratedTusRuntimeEventInput(
                        "hello world",
                        "resume-contract",
                        "stored",
                        false,
                        6,
                        "contract-resume-fingerprint",
                        true,
                        new GeneratedTusRuntimeEventMetadata[0]
                ),
                new GeneratedTusRuntimeEventRequest[] {
                        new GeneratedTusRuntimeEventRequest(
                                "HEAD",
                                "upload",
                                200,
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
                new GeneratedTusRuntimeEventInput(
                        "hello world",
                        "relative-contract",
                        "relative",
                        true,
                        11,
                        null,
                        false,
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
            if (urlStore != null) {
                client.enableResuming(urlStore);
            }
            if (testCase.input.removeFingerprintOnSuccess) {
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
            }
            uploader.finish();

            assertArrayEquals(
                    testCase.scenarioId,
                    testCase.eventKeys,
                    events.toArray(new String[events.size()]));
            assertStoredUploadState(testCase, urlStore);
        }
    }

    private TusUploader uploaderFor(TusClient client, GeneratedTusRuntimeEventCase testCase)
            throws Exception {
        if (testCase.input.fingerprint != null) {
            return client.resumeUpload(uploadFor(testCase));
        }

        return client.createUpload(uploadFor(testCase));
    }

    private TusUpload uploadFor(GeneratedTusRuntimeEventCase testCase) {
        byte[] content = testCase.input.content.getBytes(StandardCharsets.UTF_8);
        TusUpload upload = new TusUpload();
        upload.setSize(content.length);
        upload.setInputStream(new ByteArrayInputStream(content));
        upload.setMetadata(metadataFor(testCase.input.metadata));
        if (testCase.input.fingerprint != null) {
            upload.setFingerprint(testCase.input.fingerprint);
        }
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
            HttpRequest httpRequest = new HttpRequest()
                    .withPath(pathFor(testCase, request));
            if (!"upload".equals(request.url) || "HEAD".equals(request.method)) {
                httpRequest.withMethod(request.method);
            }

            mockServer.when(httpRequest).respond(responseFor(testCase, request));
        }
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
        for (GeneratedTusRuntimeEventHeader header : request.headers) {
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
        if (testCase.input.fingerprint == null) {
            return null;
        }

        GeneratedTusRuntimeEventUrlStore store = new GeneratedTusRuntimeEventUrlStore();
        store.set(testCase.input.fingerprint, uploadUrlFor(testCase));
        return store;
    }

    private void assertStoredUploadState(
            GeneratedTusRuntimeEventCase testCase,
            GeneratedTusRuntimeEventUrlStore urlStore) {
        if (urlStore == null) {
            return;
        }

        URL storedUrl = urlStore.get(testCase.input.fingerprint);
        if (testCase.input.removeFingerprintOnSuccess) {
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

    private static final class GeneratedTusRuntimeEventCase {
        final String scenarioId;
        final GeneratedTusRuntimeEventInput input;
        final GeneratedTusRuntimeEventRequest[] requests;
        final String[] eventKeys;

        GeneratedTusRuntimeEventCase(
                String scenarioId,
                GeneratedTusRuntimeEventInput input,
                GeneratedTusRuntimeEventRequest[] requests,
                String[] eventKeys) {
            this.scenarioId = scenarioId;
            this.input = input;
            this.requests = requests;
            this.eventKeys = eventKeys;
        }
    }

    private static final class GeneratedTusRuntimeEventInput {
        final String content;
        final String uploadPath;
        final String locationHeaderKind;
        final boolean endpointHasTrailingSlash;
        final int chunkSize;
        final String fingerprint;
        final boolean removeFingerprintOnSuccess;
        final GeneratedTusRuntimeEventMetadata[] metadata;

        GeneratedTusRuntimeEventInput(
                String content,
                String uploadPath,
                String locationHeaderKind,
                boolean endpointHasTrailingSlash,
                int chunkSize,
                String fingerprint,
                boolean removeFingerprintOnSuccess,
                GeneratedTusRuntimeEventMetadata[] metadata) {
            this.content = content;
            this.uploadPath = uploadPath;
            this.locationHeaderKind = locationHeaderKind;
            this.endpointHasTrailingSlash = endpointHasTrailingSlash;
            this.chunkSize = chunkSize;
            this.fingerprint = fingerprint;
            this.removeFingerprintOnSuccess = removeFingerprintOnSuccess;
            this.metadata = metadata;
        }
    }

    private static final class GeneratedTusRuntimeEventRequest {
        final String method;
        final String url;
        final int statusCode;
        final GeneratedTusRuntimeEventHeader[] headers;

        GeneratedTusRuntimeEventRequest(
                String method,
                String url,
                int statusCode,
                GeneratedTusRuntimeEventHeader[] headers) {
            this.method = method;
            this.url = url;
            this.statusCode = statusCode;
            this.headers = headers;
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
