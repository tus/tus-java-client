package io.tus.java.example;

import io.tus.java.client.TusClient;
import io.tus.java.client.TusUpload;
import io.tus.java.client.TusUploader;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Map;

public final class Api2DevdockTusProtocolVersionSelection {
    /**
     * Run the API2 devdock TUS protocol-version selection example.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        try {
            System.setProperty("http.strictPostRedirect", "true");

            final JSONObject scenario = Api2DevdockScenario.loadScenario();
            final JSONObject result = uploadWithSelectedProtocol(scenario);
            Api2DevdockScenario.writeResult(result);

            System.out.println(
                    "Java TUS SDK devdock scenario "
                            + scenario.getString("scenarioId")
                            + " created upload "
                            + result.getString("uploadUrl")
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static JSONObject uploadWithSelectedProtocol(JSONObject scenario) throws Exception {
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
        final String protocol = Api2DevdockScenario.conformanceInputStringOption(
                conformanceScenario,
                "protocol"
        );
        final boolean uploadDataDuringCreation =
                Api2DevdockScenario.conformanceInputBooleanOption(
                        conformanceScenario,
                        "uploadDataDuringCreation",
                        false
                );
        if (!uploadDataDuringCreation) {
            throw new IllegalArgumentException(
                    "Java protocol-version selection proof expects creation with upload"
            );
        }

        final JSONObject completion = conformanceScenario.getJSONObject("completion");
        try (Api2DevdockTusConformanceServer conformanceServer =
                     new Api2DevdockTusConformanceServer(conformanceScenario, endpointOrigin)) {
            final TusClient client = new TusClient();
            client.setProtocol(protocol);
            client.setUploadCreationURL(conformanceServer.endpointUrl());

            final TusUpload upload = new TusUpload();
            upload.setInputStream(new ByteArrayInputStream(content));
            upload.setSize(content.length);
            upload.setFingerprint(
                    scenario.getString("scenarioId") + "-java-protocol-version-selection"
            );
            upload.setMetadata(metadata);

            final TusUploader uploader = client.createUploadWithData(upload, content.length);
            uploader.finish();

            conformanceServer.assertExhausted();
            final JSONObject result = conformanceServer.result();
            result.put("completionKind", completion.getString("kind"));
            result.put("errorCalled", false);
            result.put("requestCount", result.getJSONArray("requestMethods").length());
            result.put("successCalled", true);
            result.put(
                    "uploadUrl",
                    conformanceServer.canonicalUrl(uploader.getUploadURL().toString())
            );

            return result;
        }
    }

    private Api2DevdockTusProtocolVersionSelection() {
        throw new IllegalStateException("Utility class");
    }
}
