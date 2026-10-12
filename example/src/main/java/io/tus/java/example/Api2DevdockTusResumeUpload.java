package io.tus.java.example;

import io.tus.java.client.FingerprintNotFoundException;
import io.tus.java.client.ProtocolException;
import io.tus.java.client.ResumingNotEnabledException;
import io.tus.java.client.TusClient;
import io.tus.java.client.TusURLStore;
import io.tus.java.client.TusUpload;
import io.tus.java.client.TusUploader;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public final class Api2DevdockTusResumeUpload {
    /**
     * Run the API2 devdock TUS resume example.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        try {
            System.setProperty("http.strictPostRedirect", "true");

            final JSONObject scenario = Api2DevdockScenario.loadScenario();
            final JSONObject createResponse = Api2DevdockScenario.createResponse(scenario);
            final JSONObject result = uploadWithStoredResume(scenario, createResponse);
            Api2DevdockScenario.writeResult(result);

            System.out.println(
                    "Java TUS SDK devdock scenario "
                            + scenario.getString("scenarioId")
                            + " resumed "
                            + result.getString("uploadUrl")
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static JSONObject uploadWithStoredResume(
            JSONObject scenario,
            JSONObject createResponse
    ) throws IOException, ProtocolException, FingerprintNotFoundException,
            ResumingNotEnabledException {
        final JSONObject uploadConfig = scenario.getJSONObject("upload");
        final JSONObject resume = uploadConfig.getJSONObject("resume");
        final byte[] content = Api2DevdockScenario.scenarioBytes(uploadConfig);
        final int chunkSize = Api2DevdockScenario.fixedChunkSizeBytes(uploadConfig);
        final String fingerprint = resume.getString("fingerprint");

        final CountingTusURLStore store = new CountingTusURLStore();
        final TusClient client = new TusClient();
        client.setUploadCreationURL(
                new URL(Api2DevdockScenario.tusUrl(uploadConfig, scenario, createResponse))
        );
        client.enableResuming(store);
        if (resume.getBoolean("removeFingerprintOnSuccess")) {
            client.enableRemoveFingerprintOnSuccess();
        }

        final TusUpload firstUpload = uploadFor(scenario, createResponse, content, fingerprint);
        final TusUploader firstUploader = client.createUpload(firstUpload);
        firstUploader.setChunkSize(chunkSize);
        final int firstAcceptedBytes = firstUploader.uploadChunk();
        if (firstAcceptedBytes != resume.getInt("stopAfterAcceptedBytes")) {
            throw new IllegalStateException(
                    "first upload accepted "
                            + firstAcceptedBytes
                            + " bytes, expected "
                            + resume.getInt("stopAfterAcceptedBytes")
            );
        }
        firstUploader.finish(false);
        final String firstUploadUrl = firstUploader.getUploadURL().toString();
        final int previousUploadCount = store.size();
        if (previousUploadCount != resume.getInt("expectedPreviousUploadCount")) {
            throw new IllegalStateException(
                    "stored upload count "
                            + previousUploadCount
                            + ", expected "
                            + resume.getInt("expectedPreviousUploadCount")
            );
        }

        final TusUpload secondUpload = uploadFor(scenario, createResponse, content, fingerprint);
        final TusUploader resumedUploader = client.resumeUpload(secondUpload);
        resumedUploader.setChunkSize(content.length);
        int uploadedChunkSize;
        do {
            uploadedChunkSize = resumedUploader.uploadChunk();
        } while (uploadedChunkSize > -1);
        resumedUploader.finish();

        final String uploadUrl = resumedUploader.getUploadURL().toString();
        if (!firstUploadUrl.equals(uploadUrl)) {
            throw new IllegalStateException(
                    "resumed upload URL " + uploadUrl + ", expected " + firstUploadUrl
            );
        }
        if (resumedUploader.getOffset() != content.length) {
            throw new IllegalStateException(
                    "remote offset " + resumedUploader.getOffset() + ", expected " + content.length
            );
        }

        final int remainingPreviousUploadCount = store.size();
        if (remainingPreviousUploadCount != resume.getInt("expectedRemainingPreviousUploadCount")) {
            throw new IllegalStateException(
                    "remaining stored upload count "
                            + remainingPreviousUploadCount
                            + ", expected "
                            + resume.getInt("expectedRemainingPreviousUploadCount")
            );
        }

        return new JSONObject()
                .put("firstAcceptedBytes", firstAcceptedBytes)
                .put("firstUploadUrl", firstUploadUrl)
                .put("previousUploadCount", previousUploadCount)
                .put("remainingPreviousUploadCount", remainingPreviousUploadCount)
                .put("uploadUrl", uploadUrl);
    }

    private static TusUpload uploadFor(
            JSONObject scenario,
            JSONObject createResponse,
            byte[] content,
            String fingerprint
    ) {
        final JSONObject uploadConfig = scenario.getJSONObject("upload");
        final TusUpload upload = new TusUpload();
        upload.setInputStream(new ByteArrayInputStream(content));
        upload.setSize(content.length);
        upload.setFingerprint(fingerprint);
        upload.setMetadata(
                Api2DevdockScenario.uploadMetadata(uploadConfig, scenario, createResponse)
        );
        return upload;
    }

    private static final class CountingTusURLStore implements TusURLStore {
        private final Map<String, URL> urls = new HashMap<String, URL>();

        @Override
        public void set(String fingerprint, URL url) {
            urls.put(fingerprint, url);
        }

        @Override
        public URL get(String fingerprint) {
            return urls.get(fingerprint);
        }

        @Override
        public void remove(String fingerprint) {
            urls.remove(fingerprint);
        }

        int size() {
            return urls.size();
        }
    }

    private Api2DevdockTusResumeUpload() {
        throw new IllegalStateException("Utility class");
    }
}
