package io.tus.java.example;

import io.tus.java.client.ProtocolException;
import io.tus.java.client.TusClient;
import io.tus.java.client.TusRequestLifecycleHooks;
import io.tus.java.client.TusURLMemoryStore;
import io.tus.java.client.TusUpload;
import io.tus.java.client.TusUploader;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Api2DevdockTusCustomRequestHeaders {
    /**
     * Run the API2 devdock TUS custom request headers example.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        try {
            System.setProperty("http.strictPostRedirect", "true");

            final JSONObject scenario = Api2DevdockScenario.loadScenario();
            final JSONObject createResponse = Api2DevdockScenario.createResponse(scenario);
            final JSONObject result = uploadWithCustomHeaders(scenario, createResponse);
            Api2DevdockScenario.writeResult(result);

            System.out.println(
                    "Java TUS SDK devdock scenario "
                            + scenario.getString("scenarioId")
                            + " observed custom request headers for "
                            + result.getString("uploadUrl")
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static JSONObject uploadWithCustomHeaders(
            JSONObject scenario,
            JSONObject createResponse
    ) throws IOException, ProtocolException {
        final JSONObject uploadConfig = scenario.getJSONObject("upload");
        final byte[] content = Api2DevdockScenario.scenarioBytes(uploadConfig);
        final Map<String, String> expectedHeaders = Api2DevdockScenario.uploadHeaders(uploadConfig);
        final Map<String, Map<String, String>> headersByMethod =
                new LinkedHashMap<String, Map<String, String>>();
        Api2DevdockScenario.requireFullFileChunkSize(uploadConfig);

        final TusClient client = new TusClient();
        client.setUploadCreationURL(
                new URL(Api2DevdockScenario.tusUrl(uploadConfig, scenario, createResponse))
        );
        client.enableResuming(new TusURLMemoryStore());
        client.setHeaders(expectedHeaders);
        client.setRequestLifecycleHooks(new TusRequestLifecycleHooks(
                new TusRequestLifecycleHooks.BeforeRequest() {
                    @Override
                    public void beforeRequest(TusRequestLifecycleHooks.RequestContext context) {
                        if ("POST".equals(context.getMethod()) || "PATCH".equals(context.getMethod())) {
                            headersByMethod.put(
                                    context.getMethod(),
                                    observedCustomHeaders(context.getConnection(), expectedHeaders)
                            );
                        }
                    }
                },
                null
        ));

        final TusUpload upload = new TusUpload();
        upload.setInputStream(new ByteArrayInputStream(content));
        upload.setSize(content.length);
        upload.setFingerprint(scenario.getString("scenarioId") + "-java-custom-request-headers");
        upload.setMetadata(
                Api2DevdockScenario.uploadMetadata(uploadConfig, scenario, createResponse)
        );

        final TusUploader uploader = client.resumeOrCreateUpload(upload);
        uploader.setChunkSize(content.length);
        int uploadedChunkSize;
        do {
            uploadedChunkSize = uploader.uploadChunk();
        } while (uploadedChunkSize > -1);
        uploader.finish();

        if (uploader.getOffset() != content.length) {
            throw new IllegalStateException(
                    "custom request headers upload offset "
                            + uploader.getOffset()
                            + ", expected "
                            + content.length
            );
        }
        if (uploader.getUploadURL() == null) {
            throw new IllegalStateException("custom request headers upload did not expose a URL");
        }

        return new JSONObject()
                .put("headersByMethod", new JSONObject(headersByMethod))
                .put("uploadUrl", uploader.getUploadURL().toString());
    }

    private static Map<String, String> observedCustomHeaders(
            HttpURLConnection connection,
            Map<String, String> expectedHeaders
    ) {
        final Map<String, String> headers = new LinkedHashMap<String, String>();
        for (Map.Entry<String, String> entry : expectedHeaders.entrySet()) {
            final String value = connection.getRequestProperty(entry.getKey());
            if (value == null) {
                throw new IllegalStateException(
                        "custom request headers did not observe " + entry.getKey()
                );
            }

            headers.put(entry.getKey(), value);
        }

        return headers;
    }

    private Api2DevdockTusCustomRequestHeaders() {
        throw new IllegalStateException("Utility class");
    }
}
