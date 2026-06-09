package io.tus.java.example;

import io.tus.java.client.TusClient;
import io.tus.java.client.TusURLMemoryStore;
import io.tus.java.client.TusUpload;
import io.tus.java.client.TusUploader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public final class Api2DevdockTusAbortUpload {
    /**
     * Run the API2 devdock TUS abort-upload example.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        try {
            System.setProperty("http.strictPostRedirect", "true");

            final JSONObject scenario = Api2DevdockScenario.loadScenario();
            final JSONObject result = uploadAndAbort(scenario);
            Api2DevdockScenario.writeResult(result);

            System.out.println(
                    "Java TUS SDK devdock scenario "
                            + scenario.getString("scenarioId")
                            + " aborted the upload"
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static JSONObject uploadAndAbort(JSONObject scenario) throws Exception {
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
        final Map<String, String> headers = Api2DevdockScenario.conformanceInputStringMapOptionOrEmpty(
                conformanceScenario,
                "headers"
        );
        final boolean terminateUploadOnAbort = conformanceScenario
                .getJSONObject("runtimeSetup")
                .getJSONObject("abort")
                .getBoolean("terminateUpload");
        final String fingerprint = runtimeFingerprint(conformanceScenario, scenario);
        final JSONObject completion = conformanceScenario.getJSONObject("completion");

        final TusClient client = new TusClient();
        final TusURLMemoryStore urlStore = new TusURLMemoryStore();
        final List<JSONObject> events = new ArrayList<JSONObject>();
        final AtomicReference<TusUploader> activeUploader = new AtomicReference<TusUploader>();
        final AtomicBoolean aborted = new AtomicBoolean(false);
        final AtomicBoolean successCalled = new AtomicBoolean(false);
        final AtomicReference<Exception> uploadError = new AtomicReference<Exception>();

        try (Api2DevdockTusConformanceServer conformanceServer =
                     new Api2DevdockTusConformanceServer(
                             conformanceScenario,
                             endpointOrigin,
                             new Api2DevdockTusConformanceServer.RequestAbortHandler() {
                                 @Override
                                 public void abortRequest(
                                         Api2DevdockTusConformanceServer.RequestAbortContext context
                                 ) throws Exception {
                                     aborted.set(true);
                                     events.add(new JSONObject()
                                             .put("kind", "request-abort")
                                             .put("method", context.method())
                                             .put("requestIndex", context.requestIndex())
                                             .put("url", context.url()));

                                     final TusUploader uploader = activeUploader.get();
                                     if (uploader == null) {
                                         if (terminateUploadOnAbort) {
                                             throw new IllegalStateException(
                                                     "abort scenario requested termination "
                                                             + "before uploader was available"
                                             );
                                         }
                                         client.abortUpload();
                                         return;
                                     }

                                     client.abortUpload(uploader, false);
                                 }
                             })) {
            client.setUploadCreationURL(conformanceServer.endpointUrl());
            client.enableResuming(urlStore);
            client.setHeaders(headers);

            final TusUpload upload = uploadFor(content, fingerprint, metadata);
            final Thread uploadThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        final TusUploader uploader = client.resumeOrCreateUpload(upload);
                        activeUploader.set(uploader);
                        uploader.setChunkSize(content.length);
                        uploader.setRequestPayloadSize(content.length);
                        while (uploader.uploadChunk() > -1) { }
                        uploader.finish();
                        successCalled.set(true);
                    } catch (Exception error) {
                        if (!aborted.get()) {
                            uploadError.set(error);
                        }
                    }
                }
            });
            uploadThread.start();
            uploadThread.join(5000);
            if (uploadThread.isAlive()) {
                uploadThread.interrupt();
                throw new IllegalStateException("timed out waiting for abort upload thread");
            }
            if (uploadError.get() != null) {
                throw uploadError.get();
            }
            if (!aborted.get()) {
                throw new IllegalStateException("abort scenario completed without aborting");
            }
            if (terminateUploadOnAbort) {
                final TusUploader uploader = activeUploader.get();
                if (uploader == null) {
                    throw new IllegalStateException(
                            "abort scenario requested termination before uploader was available"
                    );
                }
                client.abortUpload(uploader, true);
            }

            conformanceServer.assertExhausted();
            final JSONObject result = conformanceServer.result();
            result.put("completionKind", completion.getString("kind"));
            result.put("errorCalled", false);
            result.put("events", new JSONArray(events));
            result.put("requestCount", result.getJSONArray("requestMethods").length());
            result.put("successCalled", successCalled.get());
            result.put("uploadUrl", uploadUrlResult(conformanceServer, activeUploader.get()));

            return result;
        }
    }

    private static TusUpload uploadFor(
            byte[] content,
            String fingerprint,
            Map<String, String> metadata
    ) {
        final TusUpload upload = new TusUpload();
        upload.setInputStream(new ByteArrayInputStream(content));
        upload.setSize(content.length);
        upload.setFingerprint(fingerprint);
        upload.setMetadata(metadata);
        return upload;
    }

    private static Object uploadUrlResult(
            Api2DevdockTusConformanceServer conformanceServer,
            TusUploader uploader
    ) {
        if (uploader == null || uploader.getUploadURL() == null) {
            return JSONObject.NULL;
        }

        return conformanceServer.canonicalUrl(uploader.getUploadURL().toString());
    }

    private static String runtimeFingerprint(JSONObject conformanceScenario, JSONObject scenario) {
        final JSONObject runtimeSetup = conformanceScenario.getJSONObject("runtimeSetup");
        if (!runtimeSetup.has("fingerprint")) {
            return scenario.getString("scenarioId") + "-java-abort-upload";
        }

        final JSONObject fingerprint = runtimeSetup.getJSONObject("fingerprint");
        if (!fingerprint.optBoolean("install", false)) {
            return scenario.getString("scenarioId") + "-java-abort-upload";
        }

        return fingerprint.getString("value");
    }

    private Api2DevdockTusAbortUpload() {
        throw new IllegalStateException("Utility class");
    }
}
