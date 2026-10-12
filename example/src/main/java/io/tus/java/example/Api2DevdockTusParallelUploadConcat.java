package io.tus.java.example;

import io.tus.java.client.TusClient;
import io.tus.java.client.TusUpload;
import io.tus.java.client.TusUploader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public final class Api2DevdockTusParallelUploadConcat {
    /**
     * Run the API2 devdock TUS parallel-upload concat example.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        try {
            System.setProperty("http.strictPostRedirect", "true");

            final JSONObject scenario = Api2DevdockScenario.loadScenario();
            final JSONObject result = uploadWithParallelConcat(scenario);
            Api2DevdockScenario.writeResult(result);

            System.out.println(
                    "Java TUS SDK devdock scenario "
                            + scenario.getString("scenarioId")
                            + " concatenated parallel uploads into "
                            + result.getString("uploadUrl")
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static JSONObject uploadWithParallelConcat(JSONObject scenario) throws Exception {
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
        final Map<String, String> partialMetadata =
                Api2DevdockScenario.conformanceInputStringMapOption(
                        conformanceScenario,
                        "metadataForPartialUploads"
                );
        final int parallelUploads = Api2DevdockScenario.conformanceInputIntegerOption(
                conformanceScenario,
                "parallelUploads",
                1
        );
        final JSONObject completion = conformanceScenario.getJSONObject("completion");
        final EventKinds eventKinds = EventKinds.from(conformanceScenario);
        final List<JSONObject> events = Collections.synchronizedList(new ArrayList<JSONObject>());

        try (Api2DevdockTusConformanceServer conformanceServer =
                     new Api2DevdockTusConformanceServer(conformanceScenario, endpointOrigin)) {
            final TusClient client = new TusClient();
            client.setUploadCreationURL(conformanceServer.endpointUrl());

            final List<PartUpload> parts = partUploads(
                    conformanceScenario,
                    scenario.getString("scenarioId"),
                    content,
                    partialMetadata
            );
            if (parts.size() != parallelUploads) {
                throw new IllegalStateException(
                        "parallel concat expected "
                                + parallelUploads
                                + " part(s), got "
                                + parts.size()
                );
            }

            for (PartUpload part : parts) {
                part.uploader = client.createPartialUpload(part.upload);
            }

            runPartialUploads(parts, events, eventKinds, content.length);
            final List<URL> partialURLs = new ArrayList<URL>();
            for (PartUpload part : parts) {
                partialURLs.add(part.uploader.getUploadURL());
            }
            final URL finalUploadURL = client.concatenateUploads(partialURLs, metadata);

            conformanceServer.assertExhausted();
            final JSONArray sortedEvents = sortedEvents(events, eventKinds);
            final JSONObject result = conformanceServer.result();
            result.put("completionKind", completion.getString("kind"));
            result.put("errorCalled", false);
            result.put("eventCount", sortedEvents.length());
            result.put("events", sortedEvents);
            result.put("requestCount", result.getJSONArray("requestMethods").length());
            result.put("successCalled", true);
            result.put("uploadUrl", conformanceServer.canonicalUrl(finalUploadURL.toString()));

            return result;
        }
    }

    private static List<PartUpload> partUploads(
            JSONObject conformanceScenario,
            String scenarioId,
            byte[] content,
            Map<String, String> metadata
    ) {
        final JSONArray requests = conformanceScenario.getJSONArray("requests");
        final List<PartUpload> result = new ArrayList<PartUpload>();
        for (int requestIndex = 0; requestIndex < requests.length(); requestIndex++) {
            final JSONObject request = requests.getJSONObject(requestIndex);
            if (!"upload-partial-chunk".equals(request.optString("role"))) {
                continue;
            }

            final int bodyStart = request.optInt("bodyStart", 0);
            final int bodySize = request.getInt("bodySize");
            final byte[] partContent = new byte[bodySize];
            System.arraycopy(content, bodyStart, partContent, 0, bodySize);

            final TusUpload upload = new TusUpload();
            upload.setInputStream(new ByteArrayInputStream(partContent));
            upload.setSize(bodySize);
            upload.setFingerprint(
                    scenarioId + "-java-parallel-upload-concat-" + result.size()
            );
            upload.setMetadata(metadata);
            result.add(new PartUpload(bodyStart, bodySize, upload));
        }

        return result;
    }

    private static void runPartialUploads(
            List<PartUpload> parts,
            List<JSONObject> events,
            EventKinds eventKinds,
            int totalSize
    ) throws Exception {
        final List<Thread> threads = new ArrayList<Thread>();
        final AtomicReference<Exception> uploadError = new AtomicReference<Exception>();
        for (final PartUpload part : parts) {
            final Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        uploadPart(part, events, eventKinds, totalSize);
                    } catch (Exception error) {
                        uploadError.compareAndSet(null, error);
                    }
                }
            });
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join(5000);
            if (thread.isAlive()) {
                thread.interrupt();
                throw new IllegalStateException("timed out waiting for parallel concat upload");
            }
        }
        if (uploadError.get() != null) {
            throw uploadError.get();
        }
    }

    private static void uploadPart(
            final PartUpload part,
            final List<JSONObject> events,
            final EventKinds eventKinds,
            final int totalSize
    ) throws Exception {
        part.uploader.setChunkSize(part.bodySize);
        part.uploader.setRequestPayloadSize(part.bodySize);
        part.uploader.setProgressListener(new TusUploader.ProgressListener() {
            @Override
            public void onProgress(long bytesSent, long bytesTotal) {
                if (bytesSent <= 0) {
                    return;
                }

                events.add(new JSONObject()
                        .put("bytesSent", part.bodyStart + bytesSent)
                        .put("bytesTotal", totalSize)
                        .put("kind", eventKinds.progress));
            }
        });
        part.uploader.setChunkCompleteListener(new TusUploader.ChunkCompleteListener() {
            @Override
            public void onChunkComplete(long chunkSize, long bytesAccepted, long bytesTotal) {
                events.add(new JSONObject()
                        .put("bytesAccepted", part.bodyStart + bytesAccepted)
                        .put("bytesTotal", totalSize)
                        .put("chunkSize", chunkSize)
                        .put("kind", eventKinds.chunkComplete));
            }
        });

        int uploadProgress = part.uploader.uploadChunk();
        while (uploadProgress > -1) {
            uploadProgress = part.uploader.uploadChunk();
        }
        part.uploader.finish();
    }

    private static JSONArray sortedEvents(List<JSONObject> events, final EventKinds eventKinds) {
        final List<JSONObject> sorted = new ArrayList<JSONObject>(events);
        Collections.sort(sorted, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject left, JSONObject right) {
                final int positionCompare = Long.compare(eventPosition(left), eventPosition(right));
                if (positionCompare != 0) {
                    return positionCompare;
                }

                return Integer.compare(eventKindRank(left, eventKinds), eventKindRank(right, eventKinds));
            }
        });

        return new JSONArray(sorted);
    }

    private static long eventPosition(JSONObject event) {
        if (event.has("bytesSent")) {
            return event.getLong("bytesSent");
        }
        return event.getLong("bytesAccepted");
    }

    private static int eventKindRank(JSONObject event, EventKinds eventKinds) {
        if (eventKinds.progress.equals(event.getString("kind"))) {
            return 0;
        }
        return 1;
    }

    private static final class EventKinds {
        final String chunkComplete;
        final String progress;

        EventKinds(String chunkComplete, String progress) {
            this.chunkComplete = chunkComplete;
            this.progress = progress;
        }

        static EventKinds from(JSONObject conformanceScenario) {
            final JSONArray events = conformanceScenario.getJSONArray("events");
            String chunkComplete = null;
            String progress = null;
            for (int index = 0; index < events.length(); index++) {
                final JSONObject event = events.getJSONObject(index);
                if (event.has("bytesSent")) {
                    progress = event.getString("kind");
                }
                if (event.has("chunkSize")) {
                    chunkComplete = event.getString("kind");
                }
            }
            if (chunkComplete == null || progress == null) {
                throw new IllegalArgumentException("parallel concat scenario is missing event kinds");
            }

            return new EventKinds(chunkComplete, progress);
        }
    }

    private static final class PartUpload {
        final int bodySize;
        final int bodyStart;
        final TusUpload upload;
        TusUploader uploader;

        PartUpload(int bodyStart, int bodySize, TusUpload upload) {
            this.bodyStart = bodyStart;
            this.bodySize = bodySize;
            this.upload = upload;
        }
    }

    private Api2DevdockTusParallelUploadConcat() {
        throw new IllegalStateException("Utility class");
    }
}
