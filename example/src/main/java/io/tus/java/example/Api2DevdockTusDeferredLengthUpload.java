package io.tus.java.example;

import io.tus.java.client.ProtocolException;
import io.tus.java.client.TusClient;
import io.tus.java.client.TusUpload;
import io.tus.java.client.TusUploader;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;

public final class Api2DevdockTusDeferredLengthUpload {
    /**
     * Run the API2 devdock TUS deferred-length upload example.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        try {
            System.setProperty("http.strictPostRedirect", "true");

            final JSONObject scenario = Api2DevdockScenario.loadScenario();
            final JSONObject createResponse = Api2DevdockScenario.createResponse(scenario);
            final String uploadUrl = uploadWithDeferredLength(scenario, createResponse);
            Api2DevdockScenario.writeResult(new JSONObject().put("uploadUrl", uploadUrl));

            System.out.println(
                    "Java TUS SDK devdock scenario "
                            + scenario.getString("scenarioId")
                            + " uploaded with deferred length to "
                            + uploadUrl
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static String uploadWithDeferredLength(
            JSONObject scenario,
            JSONObject createResponse
    ) throws IOException, ProtocolException {
        final JSONObject uploadConfig = scenario.getJSONObject("upload");
        final byte[] content = Api2DevdockScenario.scenarioBytes(uploadConfig);
        final int chunkSize = Api2DevdockScenario.fixedChunkSizeBytes(uploadConfig);
        if (!uploadConfig.getBoolean("uploadLengthDeferred")) {
            throw new IllegalStateException(
                    "deferred-length scenario must set uploadLengthDeferred"
            );
        }

        final TusClient client = new TusClient();
        client.setUploadCreationURL(
                new URL(Api2DevdockScenario.tusUrl(uploadConfig, scenario, createResponse))
        );

        final TusUpload upload = new TusUpload();
        upload.setInputStream(new ByteArrayInputStream(content));
        upload.setSize(content.length);
        upload.setFingerprint(scenario.getString("scenarioId") + "-java-deferred-length");
        upload.setMetadata(
                Api2DevdockScenario.uploadMetadata(uploadConfig, scenario, createResponse)
        );
        upload.setUploadLengthDeferred(true);

        final TusUploader uploader = client.createUpload(upload);
        uploader.setChunkSize(chunkSize);
        int uploadedChunkSize;
        do {
            uploadedChunkSize = uploader.uploadChunk();
        } while (uploadedChunkSize > -1);
        uploader.finish();

        if (uploader.getOffset() != content.length) {
            throw new IllegalStateException(
                    "remote offset " + uploader.getOffset() + ", expected " + content.length
            );
        }
        if (uploader.getUploadURL() == null) {
            throw new IllegalStateException("deferred-length upload did not return a URL");
        }

        return uploader.getUploadURL().toString();
    }

    private Api2DevdockTusDeferredLengthUpload() {
        throw new IllegalStateException("Utility class");
    }
}
