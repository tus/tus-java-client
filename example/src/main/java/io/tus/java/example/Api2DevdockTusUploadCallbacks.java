package io.tus.java.example;

import io.tus.java.client.ProtocolException;
import io.tus.java.client.TusClient;
import io.tus.java.client.TusURLMemoryStore;
import io.tus.java.client.TusUpload;
import io.tus.java.client.TusUploader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class Api2DevdockTusUploadCallbacks {
    /**
     * Run the API2 devdock TUS upload callback example.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        try {
            System.setProperty("http.strictPostRedirect", "true");

            final JSONObject scenario = Api2DevdockScenario.loadScenario();
            final JSONObject createResponse = Api2DevdockScenario.createResponse(scenario);
            final JSONObject result = uploadWithCallbacks(scenario, createResponse);
            Api2DevdockScenario.writeResult(result);

            System.out.println(
                    "Java TUS SDK devdock scenario "
                            + scenario.getString("scenarioId")
                            + " observed upload callbacks for "
                            + result.getString("uploadUrl")
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static JSONObject uploadWithCallbacks(
            JSONObject scenario,
            JSONObject createResponse
    ) throws IOException, ProtocolException {
        final JSONObject uploadConfig = scenario.getJSONObject("upload");
        final byte[] content = Api2DevdockScenario.scenarioBytes(uploadConfig);
        final Api2DevdockScenario.UploadCallbacksPlan callbacks =
                Api2DevdockScenario.uploadCallbacks(scenario);
        final List<String> events = new ArrayList<String>();
        Api2DevdockScenario.requireFullFileChunkSize(uploadConfig);

        final TusClient client = new TusClient();
        client.setUploadCreationURL(
                new URL(Api2DevdockScenario.tusUrl(uploadConfig, scenario, createResponse))
        );
        client.enableResuming(new TusURLMemoryStore());

        final TusUpload upload = new TusUpload();
        upload.setInputStream(new EventRecordingByteArrayInputStream(content, callbacks, events));
        upload.setSize(content.length);
        upload.setFingerprint(scenario.getString("scenarioId") + "-java-upload-callbacks");
        upload.setMetadata(
                Api2DevdockScenario.uploadMetadata(uploadConfig, scenario, createResponse)
        );

        final TusUploader uploader = client.resumeOrCreateUpload(upload);
        events.add(Api2DevdockScenario.uploadCallbackEventKey(
                callbacks,
                callbacks.eventKinds.uploadUrlAvailable
        ));
        uploader.setChunkSize(content.length);
        uploader.setProgressListener(new TusUploader.ProgressListener() {
            @Override
            public void onProgress(long bytesSent, long bytesTotal) {
                events.add(Api2DevdockScenario.uploadCallbackEventKey(
                        callbacks,
                        callbacks.eventKinds.progress,
                        Api2DevdockScenario.uploadCallbackEventKeyNumber(bytesSent),
                        Api2DevdockScenario.uploadCallbackEventKeyNumber(bytesTotal)
                ));
            }
        });
        uploader.setChunkCompleteListener(new TusUploader.ChunkCompleteListener() {
            @Override
            public void onChunkComplete(long chunkSize, long bytesAccepted, long bytesTotal) {
                events.add(Api2DevdockScenario.uploadCallbackEventKey(
                        callbacks,
                        callbacks.eventKinds.chunkComplete,
                        Api2DevdockScenario.uploadCallbackEventKeyNumber(chunkSize),
                        Api2DevdockScenario.uploadCallbackEventKeyNumber(bytesAccepted),
                        Api2DevdockScenario.uploadCallbackEventKeyNumber(bytesTotal)
                ));
            }
        });

        int uploadedChunkSize;
        do {
            uploadedChunkSize = uploader.uploadChunk();
        } while (uploadedChunkSize > -1);

        if (uploader.getOffset() != content.length) {
            throw new IllegalStateException(
                    "upload callbacks offset " + uploader.getOffset() + ", expected " + content.length
            );
        }
        if (uploader.getUploadURL() == null) {
            throw new IllegalStateException("upload callbacks TUS upload did not expose a URL");
        }

        events.add(Api2DevdockScenario.uploadCallbackEventKey(
                callbacks,
                callbacks.eventKinds.success
        ));
        uploader.finish();

        final List<String> matchedEvents =
                Api2DevdockScenario.matchUploadCallbackEventKeys(callbacks, events);
        return new JSONObject()
                .put("eventKeys", new JSONArray(matchedEvents))
                .put("rawEventKeys", new JSONArray(events))
                .put("uploadUrl", uploader.getUploadURL().toString());
    }

    private static final class EventRecordingByteArrayInputStream extends ByteArrayInputStream {
        private final Api2DevdockScenario.UploadCallbacksPlan callbacks;
        private final List<String> events;

        EventRecordingByteArrayInputStream(
                byte[] content,
                Api2DevdockScenario.UploadCallbacksPlan callbacks,
                List<String> events
        ) {
            super(content);
            this.callbacks = callbacks;
            this.events = events;
        }

        @Override
        public void close() throws IOException {
            events.add(Api2DevdockScenario.uploadCallbackEventKey(
                    callbacks,
                    callbacks.eventKinds.sourceClose
            ));
            super.close();
        }
    }

    private Api2DevdockTusUploadCallbacks() {
        throw new IllegalStateException("Utility class");
    }
}
