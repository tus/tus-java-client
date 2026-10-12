package io.tus.java.example;

import io.tus.java.client.ProtocolException;
import io.tus.java.client.TusClient;
import io.tus.java.client.TusUpload;
import io.tus.java.client.TusUploader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;

public final class Api2DevdockTusNodePathInputSource {
    /**
     * Run the API2 devdock TUS node path input source example.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        try {
            System.setProperty("http.strictPostRedirect", "true");

            final JSONObject scenario = Api2DevdockScenario.loadScenario();
            final JSONObject conformanceScenario = Api2DevdockScenario.conformanceScenario(scenario);
            final JSONObject result = uploadWithNodePathInputSource(conformanceScenario);
            Api2DevdockScenario.writeResult(result);

            System.out.println(
                    "Java TUS SDK devdock scenario "
                            + scenario.getString("scenarioId")
                            + " read "
                            + Api2DevdockScenario.conformanceInputSourceKind(conformanceScenario)
                            + " for "
                            + result.getString("uploadUrl")
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static JSONObject uploadWithNodePathInputSource(JSONObject conformanceScenario)
            throws IOException, ProtocolException {
        final byte[] content = Api2DevdockScenario.conformanceInputSourceBytes(conformanceScenario);
        final String inputKind = Api2DevdockScenario.conformanceInputSourceKind(conformanceScenario);
        final URL endpointUrl = new URL(
                Api2DevdockScenario.conformanceInputStringOption(
                        conformanceScenario,
                        "endpointUrl"
                )
        );
        final JSONArray events = new JSONArray();

        try (Api2DevdockTusConformanceServer conformanceServer =
                     new Api2DevdockTusConformanceServer(conformanceScenario, endpointUrl)) {
            final File source = File.createTempFile("api2-java-tus-node-path-input", ".bin");
            try {
                Files.write(source.toPath(), content);

                final TusClient client = new TusClient();
                client.setUploadCreationURL(conformanceServer.endpointUrl());

                final TusUpload upload = new TusUpload(source);
                upload.setMetadata(
                        Api2DevdockScenario.conformanceInputStringMapOption(
                                conformanceScenario,
                                "metadata"
                        )
                );

                events.put(new JSONObject()
                        .put("inputKind", inputKind)
                        .put("kind", "source-open")
                        .put("size", upload.getSize()));

                final TusUploader uploader = client.createUpload(upload);
                uploader.setChunkSize(content.length);
                boolean uploadFinished = false;
                try {
                    int uploadedChunkSize;
                    do {
                        uploadedChunkSize = uploader.uploadChunk();
                    } while (uploadedChunkSize > -1);
                    uploader.finish();
                    uploadFinished = true;
                } catch (IOException | ProtocolException error) {
                    throw new IOException(
                            "node path input source conformance failed: "
                                    + conformanceServer.errorSummary(),
                            error
                    );
                } finally {
                    if (uploadFinished) {
                        events.put(new JSONObject().put("kind", "success"));
                    }
                    events.put(new JSONObject().put("kind", "source-close"));
                }

                if (uploader.getUploadURL() == null) {
                    throw new IllegalStateException(
                            "node path input source upload did not expose a URL"
                    );
                }

                conformanceServer.assertExhausted();
                return conformanceServer.result()
                        .put("events", events)
                        .put(
                                "uploadUrl",
                                conformanceServer.canonicalUrl(uploader.getUploadURL().toString())
                        );
            } finally {
                Files.deleteIfExists(source.toPath());
            }
        }
    }

    private Api2DevdockTusNodePathInputSource() {
        throw new IllegalStateException("Utility class");
    }
}
