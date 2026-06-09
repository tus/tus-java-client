package io.tus.java.example;

import io.tus.java.client.ProtocolException;
import io.tus.java.client.TusClient;
import io.tus.java.client.TusUpload;
import io.tus.java.client.TusUploader;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;

public final class Api2DevdockTusRelativeLocationResolution {
    /**
     * Run the API2 devdock TUS relative Location resolution example.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        try {
            System.setProperty("http.strictPostRedirect", "true");

            final JSONObject scenario = Api2DevdockScenario.loadScenario();
            final JSONObject conformanceScenario = Api2DevdockScenario.conformanceScenario(scenario);
            final JSONObject result = uploadWithRelativeLocationResolution(conformanceScenario);
            Api2DevdockScenario.writeResult(result);

            System.out.println(
                    "Java TUS SDK devdock scenario "
                            + scenario.getString("scenarioId")
                            + " resolved "
                            + result.getString("uploadUrl")
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static JSONObject uploadWithRelativeLocationResolution(JSONObject conformanceScenario)
            throws IOException, ProtocolException {
        final byte[] content = Api2DevdockScenario.conformanceInputSourceBytes(conformanceScenario);
        final URL endpointUrl = new URL(
                Api2DevdockScenario.conformanceInputStringOption(
                        conformanceScenario,
                        "endpointUrl"
                )
        );

        try (Api2DevdockTusConformanceServer conformanceServer =
                     new Api2DevdockTusConformanceServer(conformanceScenario, endpointUrl)) {
            final TusClient client = new TusClient();
            client.setUploadCreationURL(conformanceServer.endpointUrl());

            final TusUpload upload = new TusUpload();
            upload.setInputStream(new ByteArrayInputStream(content));
            upload.setSize(content.length);
            upload.setFingerprint("api2-java-relative-location-conformance-fingerprint");
            upload.setMetadata(
                    Api2DevdockScenario.conformanceInputStringMapOption(
                            conformanceScenario,
                            "metadata"
                    )
            );

            final TusUploader uploader = client.createUpload(upload);
            uploader.setChunkSize(content.length);
            try {
                int uploadedChunkSize;
                do {
                    uploadedChunkSize = uploader.uploadChunk();
                } while (uploadedChunkSize > -1);
                uploader.finish();
            } catch (IOException | ProtocolException error) {
                throw new IOException(
                        "relative Location conformance failed: "
                                + conformanceServer.errorSummary(),
                        error
                );
            }

            if (uploader.getUploadURL() == null) {
                throw new IllegalStateException(
                        "relative Location resolution upload did not expose a URL"
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

    private Api2DevdockTusRelativeLocationResolution() {
        throw new IllegalStateException("Utility class");
    }
}
