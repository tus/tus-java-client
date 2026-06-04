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
                        "managedUploadDurableRetry"
                ),
                new GeneratedTusManagedUploadRuntimeCapabilities(
                        true,
                        false,
                        true,
                        false
                ),
                new GeneratedTusManagedUploadRuntimePlan(
                        "Location",
                        "pending",
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
                new GeneratedTusManagedUploadOutcomeExpectations(
                        false,
                        false,
                        true,
                        true
                ),
                new GeneratedTusManagedUploadExecution(
                        new GeneratedTusManagedUploadTerminalExecution(
                                true,
                                false,
                                false
                        ),
                        new GeneratedTusManagedUploadSchedulingExecution(
                                false,
                                true
                        ),
                        new GeneratedTusManagedUploadSourceExecution(
                                true,
                                false,
                                false
                        )
                ),
                new GeneratedTusManagedUploadStateExpectations(
                        true,
                        false,
                        false
                ),
                new GeneratedTusManagedUploadWorkload(
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
                                "running",
                                "failed",
                                new GeneratedTusManagedUploadFailure(
                                        true,
                                        false,
                                        false,
                                        "io-error",
                                        7
                                ),
                                new GeneratedTusManagedUploadRequest[] {
                                        new GeneratedTusManagedUploadRequest(
                                                "POST",
                                                "endpoint",
                                                0,
                                                201,
                                                new GeneratedTusManagedUploadHeaderSet(
                                                        true,
                                                        new GeneratedTusManagedUploadHeader[] {
                                                        new GeneratedTusManagedUploadHeader(
                                                                "Upload-Length",
                                                                "14"
                                                        ),
                                                        new GeneratedTusManagedUploadHeader(
                                                                "Upload-Metadata",
                                                                "filename bWFuYWdlZC50eHQ="
                                                        ),
                                                    }
                                                ),
                                                new GeneratedTusManagedUploadHeaderSet(
                                                        true,
                                                        new GeneratedTusManagedUploadHeader[] {
                                                        new GeneratedTusManagedUploadHeader(
                                                                "Location",
                                                                "https://tus.io/uploads/managed-durable-retry"
                                                        ),
                                                    }
                                                )
                                        ),
                                        new GeneratedTusManagedUploadRequest(
                                                "PATCH",
                                                "upload",
                                                7,
                                                204,
                                                new GeneratedTusManagedUploadHeaderSet(
                                                        true,
                                                        new GeneratedTusManagedUploadHeader[] {
                                                        new GeneratedTusManagedUploadHeader(
                                                                "Content-Type",
                                                                "application/offset+octet-stream"
                                                        ),
                                                        new GeneratedTusManagedUploadHeader(
                                                                "Upload-Offset",
                                                                "0"
                                                        ),
                                                    }
                                                ),
                                                new GeneratedTusManagedUploadHeaderSet(
                                                        true,
                                                        new GeneratedTusManagedUploadHeader[] {
                                                        new GeneratedTusManagedUploadHeader(
                                                                "Upload-Offset",
                                                                "7"
                                                        ),
                                                    }
                                                )
                                        ),
                                }
                        ),
                        new GeneratedTusManagedUploadAttempt(
                                1,
                                "running",
                                "succeeded",
                                null,
                                new GeneratedTusManagedUploadRequest[] {
                                        new GeneratedTusManagedUploadRequest(
                                                "HEAD",
                                                "upload",
                                                0,
                                                200,
                                                new GeneratedTusManagedUploadHeaderSet(
                                                        true,
                                                        new GeneratedTusManagedUploadHeader[0]
                                                ),
                                                new GeneratedTusManagedUploadHeaderSet(
                                                        true,
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
                                                )
                                        ),
                                        new GeneratedTusManagedUploadRequest(
                                                "PATCH",
                                                "upload",
                                                7,
                                                204,
                                                new GeneratedTusManagedUploadHeaderSet(
                                                        true,
                                                        new GeneratedTusManagedUploadHeader[] {
                                                        new GeneratedTusManagedUploadHeader(
                                                                "Content-Type",
                                                                "application/offset+octet-stream"
                                                        ),
                                                        new GeneratedTusManagedUploadHeader(
                                                                "Upload-Offset",
                                                                "7"
                                                        ),
                                                    }
                                                ),
                                                new GeneratedTusManagedUploadHeaderSet(
                                                        true,
                                                        new GeneratedTusManagedUploadHeader[] {
                                                        new GeneratedTusManagedUploadHeader(
                                                                "Upload-Offset",
                                                                "14"
                                                        ),
                                                    }
                                                )
                                        ),
                                }
                        ),
                        }
                )
        ),
        new GeneratedTusManagedUploadRuntimeCase(
                new GeneratedTusManagedUploadRuntimeProfile(
                        "managedUploadPermanentFailure"
                ),
                new GeneratedTusManagedUploadRuntimeCapabilities(
                        true,
                        false,
                        true,
                        false
                ),
                new GeneratedTusManagedUploadRuntimePlan(
                        "Location",
                        "pending",
                        new String[] {
                        "pending",
                        "running",
                        "failed",
                    },
                        new int[0]
                ),
                new GeneratedTusManagedUploadOutcomeExpectations(
                        false,
                        true,
                        true,
                        false
                ),
                new GeneratedTusManagedUploadExecution(
                        new GeneratedTusManagedUploadTerminalExecution(
                                false,
                                false,
                                true
                        ),
                        new GeneratedTusManagedUploadSchedulingExecution(
                                false,
                                true
                        ),
                        new GeneratedTusManagedUploadSourceExecution(
                                true,
                                false,
                                false
                        )
                ),
                new GeneratedTusManagedUploadStateExpectations(
                        true,
                        true,
                        false
                ),
                new GeneratedTusManagedUploadWorkload(
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
                                "running",
                                "failed",
                                new GeneratedTusManagedUploadFailure(
                                        false,
                                        false,
                                        true,
                                        "unretryable-protocol-error",
                                        -1
                                ),
                                new GeneratedTusManagedUploadRequest[] {
                                        new GeneratedTusManagedUploadRequest(
                                                "POST",
                                                "endpoint",
                                                0,
                                                400,
                                                new GeneratedTusManagedUploadHeaderSet(
                                                        true,
                                                        new GeneratedTusManagedUploadHeader[] {
                                                        new GeneratedTusManagedUploadHeader(
                                                                "Upload-Length",
                                                                "14"
                                                        ),
                                                        new GeneratedTusManagedUploadHeader(
                                                                "Upload-Metadata",
                                                                "filename bWFuYWdlZC1wZXJtYW5lbnQtZmFpbHVyZS50eHQ="
                                                        ),
                                                    }
                                                ),
                                                new GeneratedTusManagedUploadHeaderSet(
                                                        false,
                                                        new GeneratedTusManagedUploadHeader[0]
                                                )
                                        ),
                                }
                        ),
                        }
                )
        ),
        new GeneratedTusManagedUploadRuntimeCase(
                new GeneratedTusManagedUploadRuntimeProfile(
                        "managedUploadRetryPolicyExhausted"
                ),
                new GeneratedTusManagedUploadRuntimeCapabilities(
                        true,
                        false,
                        true,
                        false
                ),
                new GeneratedTusManagedUploadRuntimePlan(
                        "Location",
                        "pending",
                        new String[] {
                        "pending",
                        "running",
                        "failed",
                        "running",
                        "failed",
                        "running",
                        "failed",
                    },
                        new int[] {
                        0,
                        0,
                    }
                ),
                new GeneratedTusManagedUploadOutcomeExpectations(
                        false,
                        true,
                        true,
                        false
                ),
                new GeneratedTusManagedUploadExecution(
                        new GeneratedTusManagedUploadTerminalExecution(
                                false,
                                true,
                                true
                        ),
                        new GeneratedTusManagedUploadSchedulingExecution(
                                false,
                                true
                        ),
                        new GeneratedTusManagedUploadSourceExecution(
                                true,
                                false,
                                false
                        )
                ),
                new GeneratedTusManagedUploadStateExpectations(
                        true,
                        true,
                        false
                ),
                new GeneratedTusManagedUploadWorkload(
                        new GeneratedTusManagedUploadInput(
                        "hello retries!",
                        7,
                        "managed-retry-exhausted-fingerprint",
                        "managed-retry-exhausted",
                        new GeneratedTusManagedUploadMetadata[] {
                        new GeneratedTusManagedUploadMetadata(
                                "filename",
                                "managed-retry-exhausted.txt"
                        ),
                    }
                        ),
                        new GeneratedTusManagedUploadAttempt[] {
                        new GeneratedTusManagedUploadAttempt(
                                0,
                                "running",
                                "failed",
                                new GeneratedTusManagedUploadFailure(
                                        false,
                                        false,
                                        true,
                                        "retryable-protocol-error",
                                        -1
                                ),
                                new GeneratedTusManagedUploadRequest[] {
                                        new GeneratedTusManagedUploadRequest(
                                                "POST",
                                                "endpoint",
                                                0,
                                                500,
                                                new GeneratedTusManagedUploadHeaderSet(
                                                        true,
                                                        new GeneratedTusManagedUploadHeader[] {
                                                        new GeneratedTusManagedUploadHeader(
                                                                "Upload-Length",
                                                                "14"
                                                        ),
                                                        new GeneratedTusManagedUploadHeader(
                                                                "Upload-Metadata",
                                                                "filename bWFuYWdlZC1yZXRyeS1leGhhdXN0ZWQudHh0"
                                                        ),
                                                    }
                                                ),
                                                new GeneratedTusManagedUploadHeaderSet(
                                                        false,
                                                        new GeneratedTusManagedUploadHeader[0]
                                                )
                                        ),
                                }
                        ),
                        new GeneratedTusManagedUploadAttempt(
                                1,
                                "running",
                                "failed",
                                new GeneratedTusManagedUploadFailure(
                                        false,
                                        false,
                                        true,
                                        "retryable-protocol-error",
                                        -1
                                ),
                                new GeneratedTusManagedUploadRequest[] {
                                        new GeneratedTusManagedUploadRequest(
                                                "POST",
                                                "endpoint",
                                                0,
                                                500,
                                                new GeneratedTusManagedUploadHeaderSet(
                                                        true,
                                                        new GeneratedTusManagedUploadHeader[] {
                                                        new GeneratedTusManagedUploadHeader(
                                                                "Upload-Length",
                                                                "14"
                                                        ),
                                                        new GeneratedTusManagedUploadHeader(
                                                                "Upload-Metadata",
                                                                "filename bWFuYWdlZC1yZXRyeS1leGhhdXN0ZWQudHh0"
                                                        ),
                                                    }
                                                ),
                                                new GeneratedTusManagedUploadHeaderSet(
                                                        false,
                                                        new GeneratedTusManagedUploadHeader[0]
                                                )
                                        ),
                                }
                        ),
                        new GeneratedTusManagedUploadAttempt(
                                2,
                                "running",
                                "failed",
                                new GeneratedTusManagedUploadFailure(
                                        false,
                                        false,
                                        true,
                                        "retryable-protocol-error",
                                        -1
                                ),
                                new GeneratedTusManagedUploadRequest[] {
                                        new GeneratedTusManagedUploadRequest(
                                                "POST",
                                                "endpoint",
                                                0,
                                                500,
                                                new GeneratedTusManagedUploadHeaderSet(
                                                        true,
                                                        new GeneratedTusManagedUploadHeader[] {
                                                        new GeneratedTusManagedUploadHeader(
                                                                "Upload-Length",
                                                                "14"
                                                        ),
                                                        new GeneratedTusManagedUploadHeader(
                                                                "Upload-Metadata",
                                                                "filename bWFuYWdlZC1yZXRyeS1leGhhdXN0ZWQudHh0"
                                                        ),
                                                    }
                                                ),
                                                new GeneratedTusManagedUploadHeaderSet(
                                                        false,
                                                        new GeneratedTusManagedUploadHeader[0]
                                                )
                                        ),
                                }
                        ),
                        }
                )
        ),
        new GeneratedTusManagedUploadRuntimeCase(
                new GeneratedTusManagedUploadRuntimeProfile(
                        "managedUploadSourceUnavailable"
                ),
                new GeneratedTusManagedUploadRuntimeCapabilities(
                        true,
                        false,
                        true,
                        false
                ),
                new GeneratedTusManagedUploadRuntimePlan(
                        "Location",
                        "pending",
                        new String[] {
                        "pending",
                        "running",
                        "failed",
                    },
                        new int[0]
                ),
                new GeneratedTusManagedUploadOutcomeExpectations(
                        false,
                        true,
                        true,
                        false
                ),
                new GeneratedTusManagedUploadExecution(
                        new GeneratedTusManagedUploadTerminalExecution(
                                false,
                                true,
                                false
                        ),
                        new GeneratedTusManagedUploadSchedulingExecution(
                                false,
                                true
                        ),
                        new GeneratedTusManagedUploadSourceExecution(
                                false,
                                true,
                                true
                        )
                ),
                new GeneratedTusManagedUploadStateExpectations(
                        false,
                        false,
                        false
                ),
                new GeneratedTusManagedUploadWorkload(
                        new GeneratedTusManagedUploadInput(
                        "hello missing!",
                        7,
                        "managed-source-unavailable-fingerprint",
                        "managed-source-unavailable",
                        new GeneratedTusManagedUploadMetadata[] {
                        new GeneratedTusManagedUploadMetadata(
                                "filename",
                                "managed-source-unavailable.txt"
                        ),
                    }
                        ),
                        new GeneratedTusManagedUploadAttempt[] {
                        new GeneratedTusManagedUploadAttempt(
                                0,
                                "running",
                                "failed",
                                new GeneratedTusManagedUploadFailure(
                                        false,
                                        true,
                                        false,
                                        "source-unavailable",
                                        -1
                                ),
                                new GeneratedTusManagedUploadRequest[] {

                                }
                        ),
                        }
                )
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
            recordState(testCase, states, stateFile, testCase.initialState);

            final GeneratedTusManagedUploadUrlStore urlStore = new GeneratedTusManagedUploadUrlStore();
            final TusClient client = new TusClient();
            client.setUploadCreationURL(mockServerURL);
            client.enableResuming(urlStore);
            client.enableRemoveFingerprintOnSuccess();

            try {
                prepareSourceBeforeProtocol(testCase, source, ownedSource, states, stateFile);
                if (shouldDeferBeforeProtocol(testCase)) {
                    assertDeferredResult(testCase);
                } else {
                    TusExecutor executor =
                            managedExecutorFor(testCase, client, ownedSource, states, stateFile);
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
                }
            } catch (IOException error) {
                if (!isSourceUnavailableBeforeProtocol(testCase)) {
                    throw error;
                }
                assertTerminalFailure(testCase, error);
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
            assertInputSourceState(testCase, source);
            assertProtocolRequestCount(testCase);
            stateFile.delete();
        }
    }

    private void assertTerminalResult(
            GeneratedTusManagedUploadRuntimeCase testCase,
            Future<Boolean> future) throws Exception {
        if (!testCase.expectTerminalResult) {
            throw new AssertionError(testCase.scenarioId + " expected deferred outcome");
        }

        try {
            boolean result = future.get();
            if (!testCase.expectTerminalSuccess) {
                throw new AssertionError(testCase.scenarioId + " expected terminal failure");
            }
            assertTrue(testCase.scenarioId, result);
        } catch (ExecutionException error) {
            if (!testCase.expectTerminalFailure) {
                throw error;
            }
            assertTerminalFailure(testCase, error.getCause());
        }
    }

    private void assertTerminalFailure(
            GeneratedTusManagedUploadRuntimeCase testCase,
            Throwable error) {
        if (testCase.expectProtocolExceptionOnTerminalFailure && error instanceof ProtocolException) {
            assertTrue(testCase.scenarioId, error instanceof ProtocolException);
            return;
        }
        if (testCase.expectIoExceptionOnTerminalFailure && error instanceof IOException) {
            assertTrue(testCase.scenarioId, error instanceof IOException);
            return;
        }

        throw new AssertionError(
                testCase.scenarioId
                        + " observed unexpected generated terminal failure "
                        + error);
    }

    private void assertDeferredResult(GeneratedTusManagedUploadRuntimeCase testCase) {
        if (
                !testCase.expectDeferredNetworkResult
                || !testCase.deferBeforeProtocol
                || testCase.networkConstraintSatisfied) {
            throw new AssertionError(testCase.scenarioId + " expected deferred network outcome");
        }
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
                recordState(testCase, states, stateFile, attempt.stateBeforeAttempt);

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
                            throw new IOException(attempt.failure.failureMessage);
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
                && attempt.failure.failAfterAcceptedOffset;
    }

    private void recordDuringProtocolFailure(
            GeneratedTusManagedUploadRuntimeCase testCase,
            List<String> states,
            File stateFile,
            GeneratedTusManagedUploadAttempt attempt) throws IOException {
        if (attempt.failure == null || !attempt.failure.failDuringProtocolRequest) {
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
        if (!testCase.copySourceToOwnedStorage) {
            throw new AssertionError(
                    testCase.scenarioId
                            + " uses unsupported generated source durability capability");
        }

        Files.copy(source.toPath(), ownedSource.toPath(), StandardCopyOption.REPLACE_EXISTING);
        assertTrue(testCase.scenarioId, ownedSource.exists());
    }

    private void prepareSourceBeforeProtocol(
            GeneratedTusManagedUploadRuntimeCase testCase,
            File source,
            File ownedSource,
            List<String> states,
            File stateFile) throws IOException {
        if (testCase.prepareDurableSourceBeforeProtocol) {
            copyDurableSource(testCase, source, ownedSource);
            return;
        }
        if (testCase.simulateMissingSourceBeforeDurableCopy) {
            GeneratedTusManagedUploadAttempt attempt = testCase.attempts[0];
            if (source.exists() && !source.delete()) {
                throw new IOException("Could not remove generated input source " + source);
            }
            recordState(testCase, states, stateFile, attempt.stateBeforeAttempt);
            try {
                copyDurableSource(testCase, source, ownedSource);
            } catch (IOException error) {
                recordState(testCase, states, stateFile, attempt.stateAfterAttempt);
                throw error;
            }
            throw new AssertionError(testCase.scenarioId + " unexpectedly prepared missing source");
        }

        throw new AssertionError(
                testCase.scenarioId
                        + " uses unsupported generated source preparation expectations");
    }

    private boolean isSourceUnavailableBeforeProtocol(GeneratedTusManagedUploadRuntimeCase testCase) {
        return testCase.sourceUnavailableBeforeProtocol;
    }

    private boolean shouldDeferBeforeProtocol(GeneratedTusManagedUploadRuntimeCase testCase) {
        return testCase.deferBeforeProtocol;
    }

    private void cleanupAfterTerminalState(
            GeneratedTusManagedUploadRuntimeCase testCase,
            File ownedSource) throws IOException {
        if (!testCase.cleanupOwnedSourceAfterTerminalState) {
            return;
        }

        Files.deleteIfExists(ownedSource.toPath());
    }

    private void assertOwnedSourceState(
            GeneratedTusManagedUploadRuntimeCase testCase,
            File ownedSource) {
        if (testCase.expectOwnedSourceExists) {
            assertTrue(testCase.scenarioId, ownedSource.exists());
            ownedSource.delete();
            return;
        }

        assertFalse(testCase.scenarioId, ownedSource.exists());
    }

    private void assertInputSourceState(
            GeneratedTusManagedUploadRuntimeCase testCase,
            File source) {
        if (testCase.expectInputSourceExists) {
            assertTrue(testCase.scenarioId, source.exists());
            source.delete();
            return;
        }

        assertFalse(testCase.scenarioId, source.exists());
    }

    private void assertResumeUrlState(
            GeneratedTusManagedUploadRuntimeCase testCase,
            GeneratedTusManagedUploadUrlStore urlStore) {
        if (testCase.expectResumeUrlExists) {
            assertTrue(testCase.scenarioId, urlStore.get(testCase.input.fingerprint) != null);
            return;
        }

        assertNull(testCase.scenarioId, urlStore.get(testCase.input.fingerprint));
    }

    private void assertProtocolRequestCount(GeneratedTusManagedUploadRuntimeCase testCase) {
        HttpRequest[] requests = mockServer.retrieveRecordedRequests(new HttpRequest());
        assertTrue(
                testCase.scenarioId,
                requests.length == expectedProtocolRequestCount(testCase));
    }

    private int expectedProtocolRequestCount(GeneratedTusManagedUploadRuntimeCase testCase) {
        int count = 0;
        for (GeneratedTusManagedUploadAttempt attempt : testCase.attempts) {
            count += attempt.requests.length;
        }
        return count;
    }

    private void recordState(
            GeneratedTusManagedUploadRuntimeCase testCase,
            List<String> states,
            File stateFile,
            String state) throws IOException {
        if (!testCase.useFilesystemStateBackend) {
            throw new AssertionError(
                    testCase.scenarioId
                            + " uses unsupported generated state backend capability");
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
        if (request.requestHeaders.includesDefaultProtocolHeaders) {
            for (Map.Entry<String, String> entry : TusProtocol.DEFAULT_REQUEST_HEADERS.entrySet()) {
                httpRequest.withHeader(entry.getKey(), entry.getValue());
            }
        }
        for (GeneratedTusManagedUploadHeader header : request.requestHeaders.headers) {
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
        if (request.responseHeaders.includesDefaultProtocolHeaders) {
            for (Map.Entry<String, String> entry : TusProtocol.DEFAULT_RESPONSE_HEADERS.entrySet()) {
                response.withHeader(entry.getKey(), entry.getValue());
            }
        }
        for (GeneratedTusManagedUploadHeader header : request.responseHeaders.headers) {
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
        final boolean copySourceToOwnedStorage;
        final boolean useDurableOsScheduler;
        final boolean useFilesystemStateBackend;
        final boolean usePlatformKeyValueStateBackend;
        final String initialState;
        final String locationHeaderName;
        final boolean expectDeferredNetworkResult;
        final boolean expectTerminalFailure;
        final boolean expectTerminalResult;
        final boolean expectTerminalSuccess;
        final boolean cleanupOwnedSourceAfterTerminalState;
        final boolean deferBeforeProtocol;
        final boolean expectIoExceptionOnTerminalFailure;
        final boolean expectProtocolExceptionOnTerminalFailure;
        final boolean networkConstraintSatisfied;
        final boolean prepareDurableSourceBeforeProtocol;
        final boolean simulateMissingSourceBeforeDurableCopy;
        final boolean sourceUnavailableBeforeProtocol;
        final boolean expectInputSourceExists;
        final boolean expectOwnedSourceExists;
        final boolean expectResumeUrlExists;
        final String[] expectedStates;
        final int[] retryDelays;
        final String offsetDiscoveryMethod;
        final GeneratedTusManagedUploadInput input;
        final GeneratedTusManagedUploadAttempt[] attempts;

        GeneratedTusManagedUploadRuntimeCase(
                GeneratedTusManagedUploadRuntimeProfile profile,
                GeneratedTusManagedUploadRuntimeCapabilities runtimeCapabilities,
                GeneratedTusManagedUploadRuntimePlan runtimePlan,
                GeneratedTusManagedUploadOutcomeExpectations outcomeExpectations,
                GeneratedTusManagedUploadExecution execution,
                GeneratedTusManagedUploadStateExpectations stateExpectations,
                GeneratedTusManagedUploadWorkload workload) {
            this.scenarioId = profile.scenarioId;
            this.copySourceToOwnedStorage = runtimeCapabilities.copySourceToOwnedStorage;
            this.useDurableOsScheduler = runtimeCapabilities.useDurableOsScheduler;
            this.useFilesystemStateBackend = runtimeCapabilities.useFilesystemStateBackend;
            this.usePlatformKeyValueStateBackend =
                    runtimeCapabilities.usePlatformKeyValueStateBackend;
            this.initialState = runtimePlan.initialState;
            this.locationHeaderName = runtimePlan.locationHeaderName;
            this.expectDeferredNetworkResult = outcomeExpectations.expectDeferredNetworkResult;
            this.expectTerminalFailure = outcomeExpectations.expectTerminalFailure;
            this.expectTerminalResult = outcomeExpectations.expectTerminalResult;
            this.expectTerminalSuccess = outcomeExpectations.expectTerminalSuccess;
            this.cleanupOwnedSourceAfterTerminalState = execution.cleanupOwnedSourceAfterTerminalState;
            this.deferBeforeProtocol = execution.deferBeforeProtocol;
            this.expectIoExceptionOnTerminalFailure = execution.expectIoExceptionOnTerminalFailure;
            this.expectProtocolExceptionOnTerminalFailure = execution.expectProtocolExceptionOnTerminalFailure;
            this.networkConstraintSatisfied = execution.networkConstraintSatisfied;
            this.prepareDurableSourceBeforeProtocol = execution.prepareDurableSourceBeforeProtocol;
            this.simulateMissingSourceBeforeDurableCopy = execution.simulateMissingSourceBeforeDurableCopy;
            this.sourceUnavailableBeforeProtocol = execution.sourceUnavailableBeforeProtocol;
            this.expectInputSourceExists = stateExpectations.inputSourceExists;
            this.expectOwnedSourceExists = stateExpectations.ownedSourceExists;
            this.expectResumeUrlExists = stateExpectations.resumeUrlExists;
            this.expectedStates = runtimePlan.expectedStates;
            this.retryDelays = runtimePlan.retryDelays;
            this.offsetDiscoveryMethod = offsetDiscoveryMethod();
            this.input = workload.input;
            this.attempts = workload.attempts;
        }
    }

    private static final class GeneratedTusManagedUploadOutcomeExpectations {
        final boolean expectDeferredNetworkResult;
        final boolean expectTerminalFailure;
        final boolean expectTerminalResult;
        final boolean expectTerminalSuccess;

        GeneratedTusManagedUploadOutcomeExpectations(
                boolean expectDeferredNetworkResult,
                boolean expectTerminalFailure,
                boolean expectTerminalResult,
                boolean expectTerminalSuccess) {
            this.expectDeferredNetworkResult = expectDeferredNetworkResult;
            this.expectTerminalFailure = expectTerminalFailure;
            this.expectTerminalResult = expectTerminalResult;
            this.expectTerminalSuccess = expectTerminalSuccess;
        }
    }

    private static final class GeneratedTusManagedUploadRuntimeProfile {
        final String scenarioId;

        GeneratedTusManagedUploadRuntimeProfile(String scenarioId) {
            this.scenarioId = scenarioId;
        }
    }

    private static final class GeneratedTusManagedUploadRuntimeCapabilities {
        final boolean copySourceToOwnedStorage;
        final boolean useDurableOsScheduler;
        final boolean useFilesystemStateBackend;
        final boolean usePlatformKeyValueStateBackend;

        GeneratedTusManagedUploadRuntimeCapabilities(
                boolean copySourceToOwnedStorage,
                boolean useDurableOsScheduler,
                boolean useFilesystemStateBackend,
                boolean usePlatformKeyValueStateBackend) {
            this.copySourceToOwnedStorage = copySourceToOwnedStorage;
            this.useDurableOsScheduler = useDurableOsScheduler;
            this.useFilesystemStateBackend = useFilesystemStateBackend;
            this.usePlatformKeyValueStateBackend = usePlatformKeyValueStateBackend;
        }
    }

    private static final class GeneratedTusManagedUploadRuntimePlan {
        final String[] expectedStates;
        final String initialState;
        final String locationHeaderName;
        final int[] retryDelays;

        GeneratedTusManagedUploadRuntimePlan(
                String locationHeaderName,
                String initialState,
                String[] expectedStates,
                int[] retryDelays) {
            this.expectedStates = expectedStates;
            this.initialState = initialState;
            this.locationHeaderName = locationHeaderName;
            this.retryDelays = retryDelays;
        }
    }

    private static final class GeneratedTusManagedUploadExecution {
        final boolean cleanupOwnedSourceAfterTerminalState;
        final boolean deferBeforeProtocol;
        final boolean expectIoExceptionOnTerminalFailure;
        final boolean expectProtocolExceptionOnTerminalFailure;
        final boolean networkConstraintSatisfied;
        final boolean prepareDurableSourceBeforeProtocol;
        final boolean simulateMissingSourceBeforeDurableCopy;
        final boolean sourceUnavailableBeforeProtocol;

        GeneratedTusManagedUploadExecution(
                GeneratedTusManagedUploadTerminalExecution terminalExecution,
                GeneratedTusManagedUploadSchedulingExecution schedulingExecution,
                GeneratedTusManagedUploadSourceExecution sourceExecution) {
            this.cleanupOwnedSourceAfterTerminalState =
                    terminalExecution.cleanupOwnedSourceAfterTerminalState;
            this.deferBeforeProtocol = schedulingExecution.deferBeforeProtocol;
            this.expectIoExceptionOnTerminalFailure =
                    terminalExecution.expectIoExceptionOnTerminalFailure;
            this.expectProtocolExceptionOnTerminalFailure =
                    terminalExecution.expectProtocolExceptionOnTerminalFailure;
            this.networkConstraintSatisfied = schedulingExecution.networkConstraintSatisfied;
            this.prepareDurableSourceBeforeProtocol =
                    sourceExecution.prepareDurableSourceBeforeProtocol;
            this.simulateMissingSourceBeforeDurableCopy =
                    sourceExecution.simulateMissingSourceBeforeDurableCopy;
            this.sourceUnavailableBeforeProtocol = sourceExecution.sourceUnavailableBeforeProtocol;
        }
    }

    private static final class GeneratedTusManagedUploadTerminalExecution {
        final boolean cleanupOwnedSourceAfterTerminalState;
        final boolean expectIoExceptionOnTerminalFailure;
        final boolean expectProtocolExceptionOnTerminalFailure;

        GeneratedTusManagedUploadTerminalExecution(
                boolean cleanupOwnedSourceAfterTerminalState,
                boolean expectIoExceptionOnTerminalFailure,
                boolean expectProtocolExceptionOnTerminalFailure) {
            this.cleanupOwnedSourceAfterTerminalState = cleanupOwnedSourceAfterTerminalState;
            this.expectIoExceptionOnTerminalFailure = expectIoExceptionOnTerminalFailure;
            this.expectProtocolExceptionOnTerminalFailure = expectProtocolExceptionOnTerminalFailure;
        }
    }

    private static final class GeneratedTusManagedUploadSchedulingExecution {
        final boolean deferBeforeProtocol;
        final boolean networkConstraintSatisfied;

        GeneratedTusManagedUploadSchedulingExecution(
                boolean deferBeforeProtocol,
                boolean networkConstraintSatisfied) {
            this.deferBeforeProtocol = deferBeforeProtocol;
            this.networkConstraintSatisfied = networkConstraintSatisfied;
        }
    }

    private static final class GeneratedTusManagedUploadSourceExecution {
        final boolean prepareDurableSourceBeforeProtocol;
        final boolean simulateMissingSourceBeforeDurableCopy;
        final boolean sourceUnavailableBeforeProtocol;

        GeneratedTusManagedUploadSourceExecution(
                boolean prepareDurableSourceBeforeProtocol,
                boolean simulateMissingSourceBeforeDurableCopy,
                boolean sourceUnavailableBeforeProtocol) {
            this.prepareDurableSourceBeforeProtocol = prepareDurableSourceBeforeProtocol;
            this.simulateMissingSourceBeforeDurableCopy = simulateMissingSourceBeforeDurableCopy;
            this.sourceUnavailableBeforeProtocol = sourceUnavailableBeforeProtocol;
        }
    }

    private static final class GeneratedTusManagedUploadStateExpectations {
        final boolean inputSourceExists;
        final boolean ownedSourceExists;
        final boolean resumeUrlExists;

        GeneratedTusManagedUploadStateExpectations(
                boolean inputSourceExists,
                boolean ownedSourceExists,
                boolean resumeUrlExists) {
            this.inputSourceExists = inputSourceExists;
            this.ownedSourceExists = ownedSourceExists;
            this.resumeUrlExists = resumeUrlExists;
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

    private static final class GeneratedTusManagedUploadWorkload {
        final GeneratedTusManagedUploadAttempt[] attempts;
        final GeneratedTusManagedUploadInput input;

        GeneratedTusManagedUploadWorkload(
                GeneratedTusManagedUploadInput input,
                GeneratedTusManagedUploadAttempt[] attempts) {
            this.attempts = attempts;
            this.input = input;
        }
    }

    private static final class GeneratedTusManagedUploadAttempt {
        final int attemptIndex;
        final String stateAfterAttempt;
        final String stateBeforeAttempt;
        final GeneratedTusManagedUploadFailure failure;
        final GeneratedTusManagedUploadRequest[] requests;

        GeneratedTusManagedUploadAttempt(
                int attemptIndex,
                String stateBeforeAttempt,
                String stateAfterAttempt,
                GeneratedTusManagedUploadFailure failure,
                GeneratedTusManagedUploadRequest[] requests) {
            this.attemptIndex = attemptIndex;
            this.stateAfterAttempt = stateAfterAttempt;
            this.stateBeforeAttempt = stateBeforeAttempt;
            this.failure = failure;
            this.requests = requests;
        }
    }

    private static final class GeneratedTusManagedUploadFailure {
        final long afterAcceptedOffset;
        final boolean failAfterAcceptedOffset;
        final boolean failBeforeProtocolRequest;
        final boolean failDuringProtocolRequest;
        final String failureMessage;

        GeneratedTusManagedUploadFailure(
                boolean failAfterAcceptedOffset,
                boolean failBeforeProtocolRequest,
                boolean failDuringProtocolRequest,
                String failureMessage,
                long afterAcceptedOffset) {
            this.afterAcceptedOffset = afterAcceptedOffset;
            this.failAfterAcceptedOffset = failAfterAcceptedOffset;
            this.failBeforeProtocolRequest = failBeforeProtocolRequest;
            this.failDuringProtocolRequest = failDuringProtocolRequest;
            this.failureMessage = failureMessage;
        }
    }

    private static final class GeneratedTusManagedUploadRequest {
        final String method;
        final String url;
        final int bodySize;
        final int statusCode;
        final GeneratedTusManagedUploadHeaderSet requestHeaders;
        final GeneratedTusManagedUploadHeaderSet responseHeaders;

        GeneratedTusManagedUploadRequest(
                String method,
                String url,
                int bodySize,
                int statusCode,
                GeneratedTusManagedUploadHeaderSet requestHeaders,
                GeneratedTusManagedUploadHeaderSet responseHeaders) {
            this.method = method;
            this.url = url;
            this.bodySize = bodySize;
            this.statusCode = statusCode;
            this.requestHeaders = requestHeaders;
            this.responseHeaders = responseHeaders;
        }
    }

    private static final class GeneratedTusManagedUploadHeaderSet {
        final boolean includesDefaultProtocolHeaders;
        final GeneratedTusManagedUploadHeader[] headers;

        GeneratedTusManagedUploadHeaderSet(
                boolean includesDefaultProtocolHeaders,
                GeneratedTusManagedUploadHeader[] headers) {
            this.includesDefaultProtocolHeaders = includesDefaultProtocolHeaders;
            this.headers = headers;
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
