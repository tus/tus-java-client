/*
 * Code generated from Transloadit API2 TUS protocol contracts; DO NOT EDIT.
 * If it looks wrong, please report the issue instead of editing this file by hand;
 * the source fix belongs in the protocol contract generator so all TUS clients stay in sync.
 */

package io.tus.java.client;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests generated managed-upload scenarios against the real Java client pieces.
 */
public class TestGeneratedTusManagedUploadRuntime extends MockServerProvider {
    private static final GeneratedTusManagedUploadRuntimeCase[] CASES =
            new GeneratedTusManagedUploadRuntimeCase[] {
        new GeneratedTusManagedUploadRuntimeCase(
                new GeneratedTusManagedUploadRuntimeProfile(
                        "managedUploadDurableRetry",
                        "java",
                        "process-lifetime-worker-pool",
                        "copy-to-owned-storage",
                        "filesystem"
                ),
                new GeneratedTusManagedUploadTransport(
                        "Location"
                ),
                new GeneratedTusManagedUploadTerminal(
                        "succeeded",
                        ""
                ),
                new GeneratedTusManagedUploadCleanup(
                        "remove-owned-source-after-success",
                        "remove-after-success"
                ),
                new GeneratedTusManagedUploadRetryPlan(
                        new String[] {
                        "pending",
                        "running",
                        "failed",
                        "running",
                        "succeeded",
                    },
                        new int[] {
                        0,
                    }
                ),
                new GeneratedTusManagedUploadInput(
                        "hello managed!",
                        7,
                        "managed-durable-retry-fingerprint",
                        "managed-durable-retry",
                        new GeneratedTusManagedUploadMetadata[] {
                        new GeneratedTusManagedUploadMetadata(
                                "filename",
                                "managed.txt"
                        ),
                    }
                ),
                new GeneratedTusManagedUploadAttempt[] {
                        new GeneratedTusManagedUploadAttempt(
                                0,
                                "failed",
                                new GeneratedTusManagedUploadFailure(
                                        "after-accepted-offset",
                                        "io-error",
                                        7
                                ),
                                new GeneratedTusManagedUploadRequest[] {
                                        new GeneratedTusManagedUploadRequest(
                                                "POST",
                                                "endpoint",
                                                0,
                                                201,
                                                new GeneratedTusManagedUploadHeader[] {
                                                new GeneratedTusManagedUploadHeader(
                                                        "Upload-Length",
                                                        "14"
                                                ),
                                            },
                                                new GeneratedTusManagedUploadHeader[] {
                                                new GeneratedTusManagedUploadHeader(
                                                        "Location",
                                                        "https://tus.io/uploads/managed-durable-retry"
                                                ),
                                            }
                                        ),
                                        new GeneratedTusManagedUploadRequest(
                                                "PATCH",
                                                "upload",
                                                7,
                                                204,
                                                new GeneratedTusManagedUploadHeader[] {
                                                new GeneratedTusManagedUploadHeader(
                                                        "Upload-Offset",
                                                        "0"
                                                ),
                                            },
                                                new GeneratedTusManagedUploadHeader[] {
                                                new GeneratedTusManagedUploadHeader(
                                                        "Upload-Offset",
                                                        "7"
                                                ),
                                            }
                                        ),
                                }
                        ),
                        new GeneratedTusManagedUploadAttempt(
                                1,
                                "succeeded",
                                null,
                                new GeneratedTusManagedUploadRequest[] {
                                        new GeneratedTusManagedUploadRequest(
                                                "HEAD",
                                                "upload",
                                                0,
                                                200,
                                                new GeneratedTusManagedUploadHeader[0],
                                                new GeneratedTusManagedUploadHeader[] {
                                                new GeneratedTusManagedUploadHeader(
                                                        "Upload-Length",
                                                        "14"
                                                ),
                                                new GeneratedTusManagedUploadHeader(
                                                        "Upload-Offset",
                                                        "7"
                                                ),
                                            }
                                        ),
                                        new GeneratedTusManagedUploadRequest(
                                                "PATCH",
                                                "upload",
                                                7,
                                                204,
                                                new GeneratedTusManagedUploadHeader[] {
                                                new GeneratedTusManagedUploadHeader(
                                                        "Upload-Offset",
                                                        "7"
                                                ),
                                            },
                                                new GeneratedTusManagedUploadHeader[] {
                                                new GeneratedTusManagedUploadHeader(
                                                        "Upload-Offset",
                                                        "14"
                                                ),
                                            }
                                        ),
                                }
                        ),
                }
        ),
        new GeneratedTusManagedUploadRuntimeCase(
                new GeneratedTusManagedUploadRuntimeProfile(
                        "managedUploadPermanentFailure",
                        "java",
                        "process-lifetime-worker-pool",
                        "copy-to-owned-storage",
                        "filesystem"
                ),
                new GeneratedTusManagedUploadTransport(
                        "Location"
                ),
                new GeneratedTusManagedUploadTerminal(
                        "failed",
                        "unretryable-protocol-error"
                ),
                new GeneratedTusManagedUploadCleanup(
                        "retain-owned-source-after-permanent-failure",
                        "absent-after-permanent-failure"
                ),
                new GeneratedTusManagedUploadRetryPlan(
                        new String[] {
                        "pending",
                        "running",
                        "failed",
                    },
                        new int[0]
                ),
                new GeneratedTusManagedUploadInput(
                        "hello failure!",
                        7,
                        "managed-permanent-failure-fingerprint",
                        "managed-permanent-failure",
                        new GeneratedTusManagedUploadMetadata[] {
                        new GeneratedTusManagedUploadMetadata(
                                "filename",
                                "managed-permanent-failure.txt"
                        ),
                    }
                ),
                new GeneratedTusManagedUploadAttempt[] {
                        new GeneratedTusManagedUploadAttempt(
                                0,
                                "failed",
                                new GeneratedTusManagedUploadFailure(
                                        "during-protocol-request",
                                        "unretryable-protocol-error",
                                        -1
                                ),
                                new GeneratedTusManagedUploadRequest[] {
                                        new GeneratedTusManagedUploadRequest(
                                                "POST",
                                                "endpoint",
                                                0,
                                                400,
                                                new GeneratedTusManagedUploadHeader[] {
                                                new GeneratedTusManagedUploadHeader(
                                                        "Upload-Length",
                                                        "14"
                                                ),
                                            },
                                                new GeneratedTusManagedUploadHeader[0]
                                        ),
                                }
                        ),
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
     * Verifies a durable source can retry, resume, finish, and clean up from contract data.
     */
    @Test
    public void testManagedUploadDurableRetryRuntime() throws Exception {
        for (GeneratedTusManagedUploadRuntimeCase testCase : CASES) {
            mockServer.reset();
            registerResponses(testCase);

            List<String> states = new ArrayList<String>();
            File source = writeSourceFile(testCase);
            File ownedSource = ownedSourceFile(testCase, source);
            File stateFile = stateFile(testCase, source);
            copyDurableSource(testCase, source, ownedSource);
            recordState(testCase, states, stateFile, "pending");

            final GeneratedTusManagedUploadUrlStore urlStore = new GeneratedTusManagedUploadUrlStore();
            final TusClient client = new TusClient();
            client.setUploadCreationURL(mockServerURL);
            client.enableResuming(urlStore);
            client.enableRemoveFingerprintOnSuccess();

            TusExecutor executor = managedExecutorFor(testCase, client, ownedSource, states, stateFile);
            ExecutorService worker = Executors.newSingleThreadExecutor();
            try {
                Future<Boolean> future = worker.submit(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return executor.makeAttempts();
                    }
                });
                assertTerminalResult(testCase, future);
            } finally {
                worker.shutdownNow();
            }

            cleanupAfterTerminalState(testCase, ownedSource);

            assertArrayEquals(
                    testCase.scenarioId,
                    testCase.expectedStates,
                    states.toArray(new String[states.size()]));
            assertArrayEquals(
                    testCase.scenarioId,
                    testCase.expectedStates,
                    Files.readAllLines(stateFile.toPath(), StandardCharsets.UTF_8)
                            .toArray(new String[testCase.expectedStates.length]));
            assertResumeUrlState(testCase, urlStore);
            assertOwnedSourceState(testCase, ownedSource);
            assertTrue(testCase.scenarioId, source.exists());
            source.delete();
            stateFile.delete();
        }
    }

    private void assertTerminalResult(
            GeneratedTusManagedUploadRuntimeCase testCase,
            Future<Boolean> future) throws Exception {
        try {
            boolean result = future.get();
            if (!"succeeded".equals(testCase.terminalState)) {
                throw new AssertionError(testCase.scenarioId + " expected terminal failure");
            }
            assertTrue(testCase.scenarioId, result);
        } catch (ExecutionException error) {
            if (!"failed".equals(testCase.terminalState)) {
                throw error;
            }
            assertTerminalFailure(testCase, error.getCause());
        }
    }

    private void assertTerminalFailure(
            GeneratedTusManagedUploadRuntimeCase testCase,
            Throwable error) {
        if ("unretryable-protocol-error".equals(testCase.terminalFailure)) {
            assertTrue(testCase.scenarioId, error instanceof ProtocolException);
            return;
        }
        if ("source-unavailable".equals(testCase.terminalFailure)) {
            assertTrue(testCase.scenarioId, error instanceof IOException);
            return;
        }
        if ("retry-policy-exhausted".equals(testCase.terminalFailure)) {
            assertTrue(
                    testCase.scenarioId,
                    error instanceof ProtocolException || error instanceof IOException);
            return;
        }

        throw new AssertionError(
                testCase.scenarioId
                        + " uses unsupported generated terminal failure "
                        + testCase.terminalFailure);
    }

    private TusExecutor managedExecutorFor(
            final GeneratedTusManagedUploadRuntimeCase testCase,
            final TusClient client,
            final File ownedSource,
            final List<String> states,
            final File stateFile) {
        TusExecutor executor = new TusExecutor() {
            private int attemptIndex;

            @Override
            protected void makeAttempt() throws ProtocolException, IOException {
                GeneratedTusManagedUploadAttempt attempt = testCase.attempts[attemptIndex];
                attemptIndex += 1;
                recordState(testCase, states, stateFile, "running");

                try {
                    TusUpload upload = uploadFor(testCase, ownedSource);
                    TusUploader uploader = client.resumeOrCreateUpload(upload);
                    uploader.setChunkSize(testCase.input.chunkSize);
                    uploader.setRequestPayloadSize(testCase.input.chunkSize);
                    while (uploader.getOffset() < upload.getSize()) {
                        uploader.uploadChunk();
                        if (
                                isAfterAcceptedOffsetFailure(attempt)
                                && uploader.getOffset() == attempt.failure.afterAcceptedOffset) {
                            uploader.finish(false);
                            recordState(testCase, states, stateFile, attempt.stateAfterAttempt);
                            throw new IOException(attempt.failure.kind);
                        }
                    }
                    uploader.finish();
                    recordState(testCase, states, stateFile, attempt.stateAfterAttempt);
                } catch (ProtocolException error) {
                    recordDuringProtocolFailure(testCase, states, stateFile, attempt);
                    throw error;
                } catch (IOException error) {
                    recordDuringProtocolFailure(testCase, states, stateFile, attempt);
                    throw error;
                }
            }
        };
        executor.setDelays(testCase.retryDelays);
        return executor;
    }

    private boolean isAfterAcceptedOffsetFailure(GeneratedTusManagedUploadAttempt attempt) {
        return attempt.failure != null
                && "after-accepted-offset".equals(attempt.failure.phase);
    }

    private void recordDuringProtocolFailure(
            GeneratedTusManagedUploadRuntimeCase testCase,
            List<String> states,
            File stateFile,
            GeneratedTusManagedUploadAttempt attempt) throws IOException {
        if (attempt.failure == null || !"during-protocol-request".equals(attempt.failure.phase)) {
            return;
        }

        recordState(testCase, states, stateFile, attempt.stateAfterAttempt);
    }

    private TusUpload uploadFor(
            GeneratedTusManagedUploadRuntimeCase testCase,
            File ownedSource) throws IOException {
        TusUpload upload = new TusUpload(ownedSource);
        upload.setFingerprint(testCase.input.fingerprint);
        upload.setMetadata(metadataFor(testCase.input.metadata));
        return upload;
    }

    private Map<String, String> metadataFor(GeneratedTusManagedUploadMetadata[] metadata) {
        Map<String, String> result = new LinkedHashMap<String, String>();
        for (GeneratedTusManagedUploadMetadata entry : metadata) {
            result.put(entry.name, entry.value);
        }
        return result;
    }

    private void copyDurableSource(
            GeneratedTusManagedUploadRuntimeCase testCase,
            File source,
            File ownedSource) throws IOException {
        if (!"copy-to-owned-storage".equals(testCase.sourceDurability)) {
            throw new AssertionError(
                    testCase.scenarioId
                            + " uses unsupported generated source durability "
                            + testCase.sourceDurability);
        }

        Files.copy(source.toPath(), ownedSource.toPath(), StandardCopyOption.REPLACE_EXISTING);
        assertTrue(testCase.scenarioId, ownedSource.exists());
    }

    private void cleanupAfterTerminalState(
            GeneratedTusManagedUploadRuntimeCase testCase,
            File ownedSource) throws IOException {
        if (!"remove-owned-source-after-success".equals(testCase.ownedSourceCleanup)) {
            return;
        }

        Files.deleteIfExists(ownedSource.toPath());
    }

    private void assertOwnedSourceState(
            GeneratedTusManagedUploadRuntimeCase testCase,
            File ownedSource) {
        if ("remove-owned-source-after-success".equals(testCase.ownedSourceCleanup)) {
            assertFalse(testCase.scenarioId, ownedSource.exists());
            return;
        }
        if ("retain-owned-source-after-permanent-failure".equals(testCase.ownedSourceCleanup)) {
            assertTrue(testCase.scenarioId, ownedSource.exists());
            ownedSource.delete();
            return;
        }

        throw new AssertionError(
                testCase.scenarioId
                        + " uses unsupported generated owned-source cleanup "
                        + testCase.ownedSourceCleanup);
    }

    private void assertResumeUrlState(
            GeneratedTusManagedUploadRuntimeCase testCase,
            GeneratedTusManagedUploadUrlStore urlStore) {
        if (
                "remove-after-success".equals(testCase.resumeUrlCleanup)
                || "absent-after-permanent-failure".equals(testCase.resumeUrlCleanup)) {
            assertNull(testCase.scenarioId, urlStore.get(testCase.input.fingerprint));
            return;
        }

        throw new AssertionError(
                testCase.scenarioId
                        + " uses unsupported generated resume URL cleanup "
                        + testCase.resumeUrlCleanup);
    }

    private void recordState(
            GeneratedTusManagedUploadRuntimeCase testCase,
            List<String> states,
            File stateFile,
            String state) throws IOException {
        if (!"filesystem".equals(testCase.stateBackend)) {
            throw new AssertionError(
                    testCase.scenarioId
                            + " uses unsupported generated state backend "
                            + testCase.stateBackend);
        }

        states.add(state);
        Files.write(stateFile.toPath(), states, StandardCharsets.UTF_8);
    }

    private File writeSourceFile(GeneratedTusManagedUploadRuntimeCase testCase) throws IOException {
        File source = File.createTempFile(testCase.scenarioId, "-source.bin");
        Files.write(
                source.toPath(),
                testCase.input.content.getBytes(StandardCharsets.UTF_8));
        return source;
    }

    private File ownedSourceFile(
            GeneratedTusManagedUploadRuntimeCase testCase,
            File source) {
        return new File(source.getParentFile(), testCase.scenarioId + "-owned.bin");
    }

    private File stateFile(
            GeneratedTusManagedUploadRuntimeCase testCase,
            File source) {
        return new File(source.getParentFile(), testCase.scenarioId + "-state.txt");
    }

    private void registerResponses(GeneratedTusManagedUploadRuntimeCase testCase) throws Exception {
        for (GeneratedTusManagedUploadAttempt attempt : testCase.attempts) {
            for (GeneratedTusManagedUploadRequest request : attempt.requests) {
                mockServer.when(requestFor(testCase, request, request.method, null))
                        .respond(responseFor(testCase, request));
                GeneratedTusMethodOverride methodOverride = methodOverrideFor(request.method);
                if (methodOverride != null) {
                    mockServer.when(requestFor(testCase, request, methodOverride.method, methodOverride))
                            .respond(responseFor(testCase, request));
                }
            }
        }
    }

    private HttpRequest requestFor(
            GeneratedTusManagedUploadRuntimeCase testCase,
            GeneratedTusManagedUploadRequest request,
            String method,
            GeneratedTusMethodOverride methodOverride) throws Exception {
        HttpRequest httpRequest = new HttpRequest()
                .withMethod(method)
                .withPath(pathFor(testCase, request));
        for (GeneratedTusManagedUploadHeader header : request.requestHeaders) {
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
            GeneratedTusManagedUploadRuntimeCase testCase,
            GeneratedTusManagedUploadRequest request) throws Exception {
        if ("endpoint".equals(request.url)) {
            return mockServerURL.getPath();
        }

        return uploadUrlFor(testCase).getPath();
    }

    private HttpResponse responseFor(
            GeneratedTusManagedUploadRuntimeCase testCase,
            GeneratedTusManagedUploadRequest request) throws Exception {
        HttpResponse response = new HttpResponse().withStatusCode(request.statusCode);
        for (GeneratedTusManagedUploadHeader header : request.responseHeaders) {
            response.withHeader(header.name, headerValueFor(testCase, header));
        }
        return response;
    }

    private String headerValueFor(
            GeneratedTusManagedUploadRuntimeCase testCase,
            GeneratedTusManagedUploadHeader header) throws Exception {
        if (!testCase.locationHeaderName.equals(header.name)) {
            return header.value;
        }

        return uploadUrlFor(testCase).toString();
    }

    private URL uploadUrlFor(GeneratedTusManagedUploadRuntimeCase testCase) throws Exception {
        return new URL(mockServerURL.toString() + "/" + testCase.input.uploadPath);
    }

    private static String offsetDiscoveryMethod() {
        for (GeneratedTusProtocolContract.GeneratedTusProtocolOperation operation
                : GeneratedTusProtocolContract.OPERATIONS) {
            if ("offset-discovery".equals(operation.role)) {
                return operation.method;
            }
        }

        throw new AssertionError("Missing generated offset-discovery operation");
    }

    private static final class GeneratedTusManagedUploadRuntimeCase {
        final String scenarioId;
        final String runtime;
        final String scheduler;
        final String sourceDurability;
        final String stateBackend;
        final String locationHeaderName;
        final String terminalState;
        final String terminalFailure;
        final String ownedSourceCleanup;
        final String resumeUrlCleanup;
        final String[] expectedStates;
        final int[] retryDelays;
        final String offsetDiscoveryMethod;
        final GeneratedTusManagedUploadInput input;
        final GeneratedTusManagedUploadAttempt[] attempts;

        GeneratedTusManagedUploadRuntimeCase(
                GeneratedTusManagedUploadRuntimeProfile profile,
                GeneratedTusManagedUploadTransport transport,
                GeneratedTusManagedUploadTerminal terminal,
                GeneratedTusManagedUploadCleanup cleanup,
                GeneratedTusManagedUploadRetryPlan retryPlan,
                GeneratedTusManagedUploadInput input,
                GeneratedTusManagedUploadAttempt[] attempts) {
            this.scenarioId = profile.scenarioId;
            this.runtime = profile.runtime;
            this.scheduler = profile.scheduler;
            this.sourceDurability = profile.sourceDurability;
            this.stateBackend = profile.stateBackend;
            this.locationHeaderName = transport.locationHeaderName;
            this.terminalState = terminal.state;
            this.terminalFailure = terminal.failure;
            this.ownedSourceCleanup = cleanup.ownedSource;
            this.resumeUrlCleanup = cleanup.resumeUrl;
            this.expectedStates = retryPlan.expectedStates;
            this.retryDelays = retryPlan.retryDelays;
            this.offsetDiscoveryMethod = offsetDiscoveryMethod();
            this.input = input;
            this.attempts = attempts;
        }
    }

    private static final class GeneratedTusManagedUploadTerminal {
        final String state;
        final String failure;

        GeneratedTusManagedUploadTerminal(String state, String failure) {
            this.state = state;
            this.failure = failure;
        }
    }

    private static final class GeneratedTusManagedUploadRuntimeProfile {
        final String scenarioId;
        final String runtime;
        final String scheduler;
        final String sourceDurability;
        final String stateBackend;

        GeneratedTusManagedUploadRuntimeProfile(
                String scenarioId,
                String runtime,
                String scheduler,
                String sourceDurability,
                String stateBackend) {
            this.scenarioId = scenarioId;
            this.runtime = runtime;
            this.scheduler = scheduler;
            this.sourceDurability = sourceDurability;
            this.stateBackend = stateBackend;
        }
    }

    private static final class GeneratedTusManagedUploadTransport {
        final String locationHeaderName;

        GeneratedTusManagedUploadTransport(String locationHeaderName) {
            this.locationHeaderName = locationHeaderName;
        }
    }

    private static final class GeneratedTusManagedUploadCleanup {
        final String ownedSource;
        final String resumeUrl;

        GeneratedTusManagedUploadCleanup(String ownedSource, String resumeUrl) {
            this.ownedSource = ownedSource;
            this.resumeUrl = resumeUrl;
        }
    }

    private static final class GeneratedTusManagedUploadRetryPlan {
        final String[] expectedStates;
        final int[] retryDelays;

        GeneratedTusManagedUploadRetryPlan(String[] expectedStates, int[] retryDelays) {
            this.expectedStates = expectedStates;
            this.retryDelays = retryDelays;
        }
    }

    private static final class GeneratedTusManagedUploadInput {
        final String content;
        final int chunkSize;
        final String fingerprint;
        final String uploadPath;
        final GeneratedTusManagedUploadMetadata[] metadata;

        GeneratedTusManagedUploadInput(
                String content,
                int chunkSize,
                String fingerprint,
                String uploadPath,
                GeneratedTusManagedUploadMetadata[] metadata) {
            this.content = content;
            this.chunkSize = chunkSize;
            this.fingerprint = fingerprint;
            this.uploadPath = uploadPath;
            this.metadata = metadata;
        }
    }

    private static final class GeneratedTusManagedUploadAttempt {
        final int attemptIndex;
        final String stateAfterAttempt;
        final GeneratedTusManagedUploadFailure failure;
        final GeneratedTusManagedUploadRequest[] requests;

        GeneratedTusManagedUploadAttempt(
                int attemptIndex,
                String stateAfterAttempt,
                GeneratedTusManagedUploadFailure failure,
                GeneratedTusManagedUploadRequest[] requests) {
            this.attemptIndex = attemptIndex;
            this.stateAfterAttempt = stateAfterAttempt;
            this.failure = failure;
            this.requests = requests;
        }
    }

    private static final class GeneratedTusManagedUploadFailure {
        final String phase;
        final String kind;
        final long afterAcceptedOffset;

        GeneratedTusManagedUploadFailure(String phase, String kind, long afterAcceptedOffset) {
            this.phase = phase;
            this.kind = kind;
            this.afterAcceptedOffset = afterAcceptedOffset;
        }
    }

    private static final class GeneratedTusManagedUploadRequest {
        final String method;
        final String url;
        final int bodySize;
        final int statusCode;
        final GeneratedTusManagedUploadHeader[] requestHeaders;
        final GeneratedTusManagedUploadHeader[] responseHeaders;

        GeneratedTusManagedUploadRequest(
                String method,
                String url,
                int bodySize,
                int statusCode,
                GeneratedTusManagedUploadHeader[] requestHeaders,
                GeneratedTusManagedUploadHeader[] responseHeaders) {
            this.method = method;
            this.url = url;
            this.bodySize = bodySize;
            this.statusCode = statusCode;
            this.requestHeaders = requestHeaders;
            this.responseHeaders = responseHeaders;
        }
    }

    private static final class GeneratedTusManagedUploadHeader {
        final String name;
        final String value;

        GeneratedTusManagedUploadHeader(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }

    private static final class GeneratedTusManagedUploadMetadata {
        final String name;
        final String value;

        GeneratedTusManagedUploadMetadata(String name, String value) {
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

    private static final class GeneratedTusManagedUploadUrlStore implements TusURLStore {
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
