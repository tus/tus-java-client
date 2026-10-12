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

public final class Api2DevdockTusUploadBodyHeaders {
    /**
     * Run the API2 devdock TUS upload body headers example.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        try {
            System.setProperty("http.strictPostRedirect", "true");

            final JSONObject scenario = Api2DevdockScenario.loadScenario();
            final JSONObject createResponse = Api2DevdockScenario.createResponse(scenario);
            final JSONObject result = uploadWithBodyHeaders(scenario, createResponse);
            Api2DevdockScenario.writeResult(result);

            System.out.println(
                    "Java TUS SDK devdock scenario "
                            + scenario.getString("scenarioId")
                            + " observed upload body headers for "
                            + result.getString("uploadUrl")
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static JSONObject uploadWithBodyHeaders(
            JSONObject scenario,
            JSONObject createResponse
    ) throws IOException, ProtocolException {
        final JSONObject uploadConfig = scenario.getJSONObject("upload");
        final byte[] content = Api2DevdockScenario.scenarioBytes(uploadConfig);
        final Map<String, Map<String, String>> expectedHeadersByMethod =
                Api2DevdockScenario.uploadBodyHeadersByMethod(uploadConfig);
        final Map<String, Map<String, String>> bodyHeadersByMethod =
                new LinkedHashMap<String, Map<String, String>>();
        Api2DevdockScenario.requireFullFileChunkSize(uploadConfig);

        final TusClient client = new TusClient();
        client.setUploadCreationURL(
                new URL(Api2DevdockScenario.tusUrl(uploadConfig, scenario, createResponse))
        );
        client.enableResuming(new TusURLMemoryStore());
        client.setRequestLifecycleHooks(new TusRequestLifecycleHooks(
                new TusRequestLifecycleHooks.BeforeRequest() {
                    @Override
                    public void beforeRequest(TusRequestLifecycleHooks.RequestContext context) {
                        final Map<String, String> expectedHeaders =
                                expectedHeadersByMethod.get(context.getMethod());
                        if (expectedHeaders == null) {
                            return;
                        }

                        bodyHeadersByMethod.put(
                                context.getMethod(),
                                observedBodyHeaders(
                                        context.getConnection(),
                                        context.getMethod(),
                                        expectedHeaders
                                )
                        );
                    }
                },
                null
        ));

        final TusUpload upload = new TusUpload();
        upload.setInputStream(new ByteArrayInputStream(content));
        upload.setSize(content.length);
        upload.setFingerprint(scenario.getString("scenarioId") + "-java-upload-body-headers");
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
                    "upload body headers upload offset "
                            + uploader.getOffset()
                            + ", expected "
                            + content.length
            );
        }
        if (uploader.getUploadURL() == null) {
            throw new IllegalStateException("upload body headers upload did not expose a URL");
        }
        for (String method : expectedHeadersByMethod.keySet()) {
            if (!bodyHeadersByMethod.containsKey(method)) {
                throw new IllegalStateException(
                        "upload body headers did not observe " + method + " request"
                );
            }
        }

        return new JSONObject()
                .put("bodyHeadersByMethod", new JSONObject(bodyHeadersByMethod))
                .put("uploadUrl", uploader.getUploadURL().toString());
    }

    private static Map<String, String> observedBodyHeaders(
            HttpURLConnection connection,
            String method,
            Map<String, String> expectedHeaders
    ) {
        final Map<String, String> headers = new LinkedHashMap<String, String>();
        for (Map.Entry<String, String> entry : expectedHeaders.entrySet()) {
            final String value = connection.getRequestProperty(entry.getKey());
            if (value == null) {
                throw new IllegalStateException(
                        "upload body headers did not observe " + entry.getKey() + " on " + method
                );
            }

            headers.put(entry.getKey(), value);
        }

        return headers;
    }

    private Api2DevdockTusUploadBodyHeaders() {
        throw new IllegalStateException("Utility class");
    }
}
