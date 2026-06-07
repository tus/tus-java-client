package io.tus.java.example;

import io.tus.java.client.ProtocolException;
import io.tus.java.client.TusClient;
import io.tus.java.client.TusExecutor;
import io.tus.java.client.TusRequestLifecycleHooks;
import io.tus.java.client.TusURLMemoryStore;
import io.tus.java.client.TusUpload;
import io.tus.java.client.TusUploader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class Api2DevdockTusRetryOffsetRecovery {
    /**
     * Run the API2 devdock TUS retry-offset recovery example.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        try {
            System.setProperty("http.strictPostRedirect", "true");

            final JSONObject scenario = Api2DevdockScenario.loadScenario();
            final JSONObject createResponse = Api2DevdockScenario.createResponse(scenario);
            final JSONObject result = uploadWithRetryOffsetRecovery(scenario, createResponse);
            Api2DevdockScenario.writeResult(result);

            System.out.println(
                    "Java TUS SDK devdock scenario "
                            + scenario.getString("scenarioId")
                            + " recovered offset for "
                            + result.getString("uploadUrl")
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static JSONObject uploadWithRetryOffsetRecovery(
            JSONObject scenario,
            JSONObject createResponse
    ) throws IOException, ProtocolException {
        final JSONObject uploadConfig = scenario.getJSONObject("upload");
        final JSONObject retry = uploadConfig.getJSONObject("retryOffsetRecovery");
        final JSONObject failAfterResponse = retry.getJSONObject("failAfterResponse");
        final JSONObject recoveryResponse = retry.getJSONObject("recoveryResponse");
        final byte[] content = Api2DevdockScenario.scenarioBytes(uploadConfig);
        final int chunkSize = Api2DevdockScenario.fixedChunkSizeBytes(uploadConfig);
        final List<Integer> recoveredOffsets = new ArrayList<Integer>();
        final List<String> requestMethods = new ArrayList<String>();
        final int[] failureCandidateCount = new int[]{0};
        final int[] simulatedFailureCount = new int[]{0};

        final TusClient client = new TusClient();
        client.setUploadCreationURL(
                new URL(Api2DevdockScenario.tusUrl(uploadConfig, scenario, createResponse))
        );
        client.enableResuming(new TusURLMemoryStore());
        client.setRequestLifecycleHooks(new TusRequestLifecycleHooks(
                new TusRequestLifecycleHooks.BeforeRequest() {
                    @Override
                    public void beforeRequest(TusRequestLifecycleHooks.RequestContext context) {
                        requestMethods.add(context.getMethod());
                    }
                },
                new TusRequestLifecycleHooks.AfterResponse() {
                    @Override
                    public void afterResponse(
                            TusRequestLifecycleHooks.RequestContext context
                    ) throws IOException {
                        if (context.getMethod().equals(recoveryResponse.getString("method"))) {
                            recoveredOffsets.add(readHeaderInt(
                                    context.getConnection(),
                                    recoveryResponse.getString("offsetHeader")
                            ));
                        }

                        if (!context.getMethod().equals(failAfterResponse.getString("method"))) {
                            return;
                        }

                        failureCandidateCount[0] += 1;
                        if (failureCandidateCount[0] != failAfterResponse.getInt("occurrence")) {
                            return;
                        }

                        simulatedFailureCount[0] += 1;
                        throw new IOException(failAfterResponse.getString("message"));
                    }
                }
        ));

        final TusUpload upload = uploadFor(scenario, createResponse, content);
        final String[] uploadUrl = new String[]{null};
        final long[] finalOffset = new long[]{0};
        final TusExecutor executor = new TusExecutor() {
            @Override
            protected void makeAttempt() throws ProtocolException, IOException {
                final TusUploader uploader = client.resumeOrCreateUpload(upload);
                uploader.setChunkSize(chunkSize);
                uploader.setRequestPayloadSize(chunkSize);
                int uploadedChunkSize;
                do {
                    uploadedChunkSize = uploader.uploadChunk();
                } while (uploadedChunkSize > -1);
                uploader.finish();
                uploadUrl[0] = uploader.getUploadURL().toString();
                finalOffset[0] = uploader.getOffset();
            }
        };
        executor.setDelays(new int[uploadConfig.getInt("retries")]);
        if (!executor.makeAttempts()) {
            throw new IOException("retry offset recovery was interrupted");
        }

        if (uploadUrl[0] == null) {
            throw new IllegalStateException("retry offset recovery TUS upload did not expose a URL");
        }
        if (finalOffset[0] != content.length) {
            throw new IllegalStateException(
                    "retry offset recovery upload offset "
                            + finalOffset[0]
                            + ", expected "
                            + content.length
            );
        }
        if (simulatedFailureCount[0] != retry.getInt("expectedFailureCount")) {
            throw new IllegalStateException(
                    "retry offset recovery expected "
                            + retry.getInt("expectedFailureCount")
                            + " simulated failure(s), got "
                            + simulatedFailureCount[0]
            );
        }
        if (recoveredOffsets.size() != retry.getInt("expectedRecoveryRequestCount")) {
            throw new IllegalStateException(
                    "retry offset recovery expected "
                            + retry.getInt("expectedRecoveryRequestCount")
                            + " recovery request(s), got "
                            + recoveredOffsets.size()
            );
        }
        if (recoveredOffsets.get(0) != retry.getInt("expectedRecoveredOffset")) {
            throw new IllegalStateException(
                    "retry offset recovery expected recovered offset "
                            + retry.getInt("expectedRecoveredOffset")
                            + ", got "
                            + recoveredOffsets.get(0)
            );
        }
        assertRequestMethods(requestMethods, retry.getJSONArray("expectedRequestMethods"));

        return new JSONObject()
                .put("recoveredOffsets", new JSONArray(recoveredOffsets))
                .put("recoveryRequestCount", recoveredOffsets.size())
                .put("requestMethods", new JSONArray(requestMethods))
                .put("simulatedFailureCount", simulatedFailureCount[0])
                .put("uploadUrl", uploadUrl[0]);
    }

    private static TusUpload uploadFor(
            JSONObject scenario,
            JSONObject createResponse,
            byte[] content
    ) {
        final JSONObject uploadConfig = scenario.getJSONObject("upload");
        final TusUpload upload = new TusUpload();
        upload.setInputStream(new ByteArrayInputStream(content));
        upload.setSize(content.length);
        upload.setFingerprint(scenario.getString("scenarioId") + "-java-retry-offset-recovery");
        upload.setMetadata(
                Api2DevdockScenario.uploadMetadata(uploadConfig, scenario, createResponse)
        );
        return upload;
    }

    private static int readHeaderInt(HttpURLConnection connection, String headerName) {
        final String value = connection.getHeaderField(headerName);
        final int offset;
        try {
            offset = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalStateException(
                    "retry offset recovery expected numeric "
                            + headerName
                            + " response header, got "
                            + value
            );
        }
        if (offset < 0) {
            throw new IllegalStateException(
                    "retry offset recovery expected non-negative offset, got " + offset
            );
        }

        return offset;
    }

    private static void assertRequestMethods(List<String> actual, JSONArray expected) {
        if (actual.size() != expected.length()) {
            throw new IllegalStateException(
                    "retry offset recovery expected request methods "
                            + expected
                            + ", got "
                            + actual
            );
        }

        for (int index = 0; index < expected.length(); index++) {
            if (!actual.get(index).equals(expected.getString(index))) {
                throw new IllegalStateException(
                        "retry offset recovery expected request method "
                                + expected.getString(index)
                                + " at index "
                                + index
                                + ", got "
                                + actual.get(index)
                );
            }
        }
    }

    private Api2DevdockTusRetryOffsetRecovery() {
        throw new IllegalStateException("Utility class");
    }
}
