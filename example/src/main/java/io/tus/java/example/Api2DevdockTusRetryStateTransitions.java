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
import java.util.Map;

public final class Api2DevdockTusRetryStateTransitions {
    private static final String UPLOAD_OFFSET_HEADER_NAME = "Upload-Offset";

    /**
     * Run the API2 devdock TUS retry-state transitions example.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        try {
            System.setProperty("http.strictPostRedirect", "true");

            final JSONObject scenario = Api2DevdockScenario.loadScenario();
            final JSONObject result = uploadWithRetryStateTransitions(scenario);
            Api2DevdockScenario.writeResult(result);

            System.out.println(
                    "Java TUS SDK devdock scenario "
                            + scenario.getString("scenarioId")
                            + " observed "
                            + result.getInt("eventCount")
                            + " retry event(s) for "
                            + result.getString("uploadUrl")
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static JSONObject uploadWithRetryStateTransitions(JSONObject scenario)
            throws IOException, ProtocolException {
        final JSONObject conformanceScenario = Api2DevdockScenario.conformanceScenario(scenario);
        final byte[] content = Api2DevdockScenario.conformanceInputSourceBytes(
                conformanceScenario
        );
        final URL endpointOrigin = new URL(Api2DevdockScenario.conformanceInputStringOption(
                conformanceScenario,
                "endpointUrl"
        ));
        final Map<String, String> metadata = Api2DevdockScenario.conformanceInputStringMapOption(
                conformanceScenario,
                "metadata"
        );
        final int[] retryDelays = conformanceRetryDelays(conformanceScenario);
        final RetryStateObserver observer = new RetryStateObserver(
                retryDecisions(conformanceScenario),
                retryDelays
        );
        final JSONObject completion = conformanceScenario.getJSONObject("completion");

        try (Api2DevdockTusConformanceServer conformanceServer =
                     new Api2DevdockTusConformanceServer(conformanceScenario, endpointOrigin)) {
            final TusClient client = new TusClient();
            client.setUploadCreationURL(conformanceServer.endpointUrl());
            client.enableResuming(new TusURLMemoryStore());

            final TusUpload upload = uploadFor(scenario, content, metadata);
            final long[] lastAcceptedOffset = new long[]{0};
            final RetryStateExecutor executor = new RetryStateExecutor(
                    client,
                    upload,
                    content.length,
                    observer
            );
            executor.setDelays(retryDelays);
            client.setRequestLifecycleHooks(new TusRequestLifecycleHooks(
                    null,
                    new TusRequestLifecycleHooks.AfterResponse() {
                        @Override
                        public void afterResponse(
                                TusRequestLifecycleHooks.RequestContext context
                        ) {
                            final long acceptedOffset = readAcceptedOffset(context.getConnection());
                            if (acceptedOffset <= lastAcceptedOffset[0]) {
                                return;
                            }

                            lastAcceptedOffset[0] = acceptedOffset;
                            executor.resetAfterProgress();
                        }
                    }
            ));

            if (!executor.makeAttempts()) {
                throw new IOException("retry state transition upload was interrupted");
            }
            observer.assertComplete();
            conformanceServer.assertExhausted();

            final JSONObject result = conformanceServer.result();
            result.put("completionKind", completion.getString("kind"));
            result.put("errorCalled", false);
            result.put("eventCount", observer.events().length());
            result.put("events", observer.events());
            result.put("requestCount", result.getJSONArray("requestMethods").length());
            result.put("successCalled", true);
            result.put("uploadUrl", conformanceServer.canonicalUrl(executor.uploadUrl()));

            return result;
        }
    }

    private static TusUpload uploadFor(
            JSONObject scenario,
            byte[] content,
            Map<String, String> metadata
    ) {
        final TusUpload upload = new TusUpload();
        upload.setInputStream(new ByteArrayInputStream(content));
        upload.setSize(content.length);
        upload.setFingerprint(scenario.getString("scenarioId") + "-java-retry-state-transitions");
        upload.setMetadata(metadata);
        return upload;
    }

    private static int[] conformanceRetryDelays(JSONObject conformanceScenario) {
        final JSONArray values = conformanceInputJSONArrayOption(conformanceScenario, "retryDelays");
        final int[] result = new int[values.length()];
        for (int index = 0; index < values.length(); index++) {
            result[index] = values.getInt(index);
        }

        return result;
    }

    private static JSONArray conformanceInputJSONArrayOption(
            JSONObject conformanceScenario,
            String key
    ) {
        final JSONArray entries = conformanceScenario.getJSONArray("inputOptionEntries");
        for (int index = 0; index < entries.length(); index++) {
            final JSONObject entry = entries.getJSONObject(index);
            if (key.equals(entry.getString("key"))) {
                return entry.getJSONArray("value");
            }
        }

        throw new IllegalArgumentException("missing conformance input array option " + key);
    }

    private static List<RetryDecision> retryDecisions(JSONObject conformanceScenario) {
        final JSONArray values = conformanceScenario.getJSONArray("retryDecisions");
        final List<RetryDecision> result = new ArrayList<RetryDecision>();
        for (int index = 0; index < values.length(); index++) {
            final JSONObject value = values.getJSONObject(index);
            result.add(new RetryDecision(
                    value.getBoolean("decision"),
                    value.getInt("retryAttempt")
            ));
        }

        return result;
    }

    private static long readAcceptedOffset(HttpURLConnection connection) {
        final String value = connection.getHeaderField(UPLOAD_OFFSET_HEADER_NAME);
        if (value == null || value.length() == 0) {
            return -1;
        }

        try {
            return Long.parseLong(value);
        } catch (NumberFormatException error) {
            return -1;
        }
    }

    private static final class RetryStateExecutor extends TusExecutor {
        private final TusClient client;
        private final TusUpload upload;
        private final int requestPayloadSize;
        private final RetryStateObserver observer;
        private String uploadUrl;

        RetryStateExecutor(
                TusClient client,
                TusUpload upload,
                int requestPayloadSize,
                RetryStateObserver observer
        ) {
            this.client = client;
            this.upload = upload;
            this.requestPayloadSize = requestPayloadSize;
            this.observer = observer;
        }

        @Override
        protected void makeAttempt() throws ProtocolException, IOException {
            final TusUploader uploader = client.resumeOrCreateUpload(upload);
            uploader.setChunkSize(requestPayloadSize);
            uploader.setRequestPayloadSize(requestPayloadSize);
            int uploadedChunkSize;
            do {
                uploadedChunkSize = uploader.uploadChunk();
            } while (uploadedChunkSize > -1);
            uploader.finish();
            uploadUrl = uploader.getUploadURL().toString();
        }

        @Override
        protected boolean shouldRetry(ProtocolException exception, int retryAttempt) {
            return observer.shouldRetry(retryAttempt);
        }

        @Override
        protected boolean shouldRetry(IOException exception, int retryAttempt) {
            return observer.shouldRetry(retryAttempt);
        }

        @Override
        protected void onRetryScheduled(int retryAttempt, int delayMillis) {
            observer.retryScheduled(retryAttempt, delayMillis);
        }

        void resetAfterProgress() {
            resetRetryAttempts();
        }

        String uploadUrl() {
            if (uploadUrl == null) {
                throw new IllegalStateException("retry state transition upload did not expose a URL");
            }

            return uploadUrl;
        }
    }

    private static final class RetryStateObserver {
        private final List<RetryDecision> decisions;
        private final JSONArray events;
        private final int[] retryDelays;
        private int decisionIndex;

        RetryStateObserver(List<RetryDecision> decisions, int[] retryDelays) {
            this.decisions = decisions;
            this.events = new JSONArray();
            this.retryDelays = retryDelays;
        }

        boolean shouldRetry(int retryAttempt) {
            if (decisionIndex >= decisions.size()) {
                throw new IllegalStateException(
                        "retry state transition received unexpected retry decision "
                                + decisionIndex
                );
            }

            final RetryDecision decision = decisions.get(decisionIndex);
            if (retryAttempt != decision.retryAttempt) {
                throw new IllegalStateException(
                        "retry state transition expected retry attempt "
                                + decision.retryAttempt
                                + ", got "
                                + retryAttempt
                );
            }

            events.put(new JSONObject()
                    .put("decision", decision.decision)
                    .put("kind", "should-retry")
                    .put("retryAttempt", retryAttempt));
            decisionIndex += 1;
            return decision.decision;
        }

        void retryScheduled(int retryAttempt, int delayMillis) {
            if (retryAttempt < 0 || retryAttempt >= retryDelays.length) {
                throw new IllegalStateException(
                        "retry state transition retry attempt "
                                + retryAttempt
                                + " has no retry delay"
                );
            }
            if (delayMillis != retryDelays[retryAttempt]) {
                throw new IllegalStateException(
                        "retry state transition expected retry delay "
                                + retryDelays[retryAttempt]
                                + ", got "
                                + delayMillis
                );
            }

            events.put(new JSONObject()
                    .put("delay", delayMillis)
                    .put("kind", "retry-schedule"));
        }

        void assertComplete() {
            if (decisionIndex == decisions.size()) {
                return;
            }

            throw new IllegalStateException(
                    "retry state transition expected "
                            + decisions.size()
                            + " retry decision(s), got "
                            + decisionIndex
            );
        }

        JSONArray events() {
            return events;
        }
    }

    private static final class RetryDecision {
        final boolean decision;
        final int retryAttempt;

        RetryDecision(boolean decision, int retryAttempt) {
            this.decision = decision;
            this.retryAttempt = retryAttempt;
        }
    }

    private Api2DevdockTusRetryStateTransitions() {
        throw new IllegalStateException("Utility class");
    }
}
