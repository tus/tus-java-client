package io.tus.java.example;

import io.tus.java.client.ProtocolException;
import io.tus.java.client.TusClient;
import io.tus.java.client.TusUpload;
import io.tus.java.client.TusUploader;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;

public final class Api2DevdockTusCreationWithUpload {
    /**
     * Run the API2 devdock TUS creation-with-upload example.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        try {
            System.setProperty("http.strictPostRedirect", "true");

            final JSONObject scenario = Api2DevdockScenario.loadScenario();
            final JSONObject createResponse = Api2DevdockScenario.createResponse(scenario);
            final String uploadUrl = uploadWithCreationData(scenario, createResponse);
            Api2DevdockScenario.writeResult(new JSONObject().put("uploadUrl", uploadUrl));

            System.out.println(
                    "Java TUS SDK devdock scenario "
                            + scenario.getString("scenarioId")
                            + " uploaded to "
                            + uploadUrl
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static String uploadWithCreationData(
            JSONObject scenario,
            JSONObject createResponse
    ) throws IOException, ProtocolException {
        final JSONObject uploadConfig = scenario.getJSONObject("upload");
        final byte[] content = Api2DevdockScenario.scenarioBytes(uploadConfig);
        Api2DevdockScenario.requireFullFileChunkSize(uploadConfig);
        if (!uploadConfig.getBoolean("uploadDataDuringCreation")) {
            throw new IllegalStateException(
                    "creation-with-upload scenario must set uploadDataDuringCreation"
            );
        }

        final TusClient client = new TusClient();
        client.setUploadCreationURL(
                new URL(Api2DevdockScenario.tusUrl(uploadConfig, scenario, createResponse))
        );

        final TusUpload upload = new TusUpload();
        upload.setInputStream(new ByteArrayInputStream(content));
        upload.setSize(content.length);
        upload.setFingerprint(scenario.getString("scenarioId") + "-java-creation-with-upload");
        upload.setMetadata(
                Api2DevdockScenario.uploadMetadata(uploadConfig, scenario, createResponse)
        );

        final TusUploader uploader = client.createUploadWithData(upload, content.length);
        uploader.finish();

        if (uploader.getOffset() != content.length) {
            throw new IllegalStateException(
                    "creation-with-upload accepted "
                            + uploader.getOffset()
                            + " bytes, expected "
                            + content.length
            );
        }
        if (uploader.getUploadURL() == null) {
            throw new IllegalStateException("creation-with-upload did not expose a URL");
        }

        return uploader.getUploadURL().toString();
    }

    private Api2DevdockTusCreationWithUpload() {
        throw new IllegalStateException("Utility class");
    }
}
