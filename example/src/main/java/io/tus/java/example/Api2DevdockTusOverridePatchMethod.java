package io.tus.java.example;

import io.tus.java.client.ProtocolException;
import io.tus.java.client.TusClient;
import io.tus.java.client.TusUpload;
import io.tus.java.client.TusUploader;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;

public final class Api2DevdockTusOverridePatchMethod {
    /**
     * Run the API2 devdock TUS PATCH method override example.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        try {
            final JSONObject scenario = Api2DevdockScenario.loadScenario();
            final JSONObject conformanceScenario = Api2DevdockScenario.conformanceScenario(scenario);
            final JSONObject result = uploadWithMethodOverride(conformanceScenario);
            Api2DevdockScenario.writeResult(result);

            System.out.println(
                    "Java TUS SDK devdock scenario "
                            + scenario.getString("scenarioId")
                            + " uploaded to "
                            + result.getString("uploadUrl")
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static JSONObject uploadWithMethodOverride(JSONObject conformanceScenario)
            throws IOException, ProtocolException {
        final byte[] content = Api2DevdockScenario.conformanceInputSourceBytes(conformanceScenario);
        final URL endpointUrl = new URL(
                Api2DevdockScenario.conformanceInputStringOption(
                        conformanceScenario,
                        "endpointUrl"
                )
        );
        final String uploadUrl = Api2DevdockScenario.conformanceInputStringOption(
                conformanceScenario,
                "uploadUrl"
        );

        try (Api2DevdockTusConformanceServer conformanceServer =
                     new Api2DevdockTusConformanceServer(conformanceScenario, endpointUrl)) {
            final TusClient client = new TusClient();

            final TusUpload upload = new TusUpload();
            upload.setInputStream(new ByteArrayInputStream(content));
            upload.setSize(content.length);

            final TusUploader uploader = client.beginOrResumeUploadFromURL(
                    upload,
                    conformanceServer.localUrlFor(uploadUrl)
            );
            uploader.setChunkSize(content.length);
            try {
                int uploadedChunkSize;
                do {
                    uploadedChunkSize = uploader.uploadChunk();
                } while (uploadedChunkSize > -1);
                uploader.finish();
            } catch (IOException | ProtocolException error) {
                throw new IOException(
                        "PATCH method override conformance failed: "
                                + conformanceServer.errorSummary(),
                        error
                );
            }

            conformanceServer.assertExhausted();
            return conformanceServer.result()
                    .put(
                            "uploadUrl",
                            conformanceServer.canonicalUrl(uploader.getUploadURL().toString())
                    );
        }
    }

    private Api2DevdockTusOverridePatchMethod() {
        throw new IllegalStateException("Utility class");
    }
}
