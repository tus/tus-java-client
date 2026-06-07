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

public final class Api2DevdockTusRequestIdHeaders {
    /**
     * Run the API2 devdock TUS request ID headers example.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        try {
            System.setProperty("http.strictPostRedirect", "true");

            final JSONObject scenario = Api2DevdockScenario.loadScenario();
            final JSONObject createResponse = Api2DevdockScenario.createResponse(scenario);
            final JSONObject result = uploadWithRequestIdHeaders(scenario, createResponse);
            Api2DevdockScenario.writeResult(result);

            System.out.println(
                    "Java TUS SDK devdock scenario "
                            + scenario.getString("scenarioId")
                            + " observed request ID headers for "
                            + result.getString("uploadUrl")
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static JSONObject uploadWithRequestIdHeaders(
            JSONObject scenario,
            JSONObject createResponse
    ) throws IOException, ProtocolException {
        final JSONObject uploadConfig = scenario.getJSONObject("upload");
        final byte[] content = Api2DevdockScenario.scenarioBytes(uploadConfig);
        final String requestIdHeaderName =
                Api2DevdockScenario.uploadRequestIdHeaderName(uploadConfig);
        final Map<String, Map<String, String>> headersByMethod =
                new LinkedHashMap<String, Map<String, String>>();
        Api2DevdockScenario.requireFullFileChunkSize(uploadConfig);

        final TusClient client = new TusClient();
        client.setUploadCreationURL(
                new URL(Api2DevdockScenario.tusUrl(uploadConfig, scenario, createResponse))
        );
        client.enableResuming(new TusURLMemoryStore());
        client.setHeaders(Api2DevdockScenario.uploadHeaders(uploadConfig));
        if (Api2DevdockScenario.uploadAddRequestId(uploadConfig)) {
            client.enableRequestIdHeader();
        }
        client.setRequestLifecycleHooks(new TusRequestLifecycleHooks(
                new TusRequestLifecycleHooks.BeforeRequest() {
                    @Override
                    public void beforeRequest(TusRequestLifecycleHooks.RequestContext context) {
                        if ("POST".equals(context.getMethod()) || "PATCH".equals(context.getMethod())) {
                            final Map<String, String> headers = new LinkedHashMap<String, String>();
                            headers.put(
                                    requestIdHeaderName,
                                    observedRequestIdHeader(
                                            context.getConnection(),
                                            context.getMethod(),
                                            requestIdHeaderName
                                    )
                            );
                            headersByMethod.put(context.getMethod(), headers);
                        }
                    }
                },
                null
        ));

        final TusUpload upload = new TusUpload();
        upload.setInputStream(new ByteArrayInputStream(content));
        upload.setSize(content.length);
        upload.setFingerprint(scenario.getString("scenarioId") + "-java-request-id-headers");
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
                    "request ID headers upload offset "
                            + uploader.getOffset()
                            + ", expected "
                            + content.length
            );
        }
        if (uploader.getUploadURL() == null) {
            throw new IllegalStateException("request ID headers upload did not expose a URL");
        }

        return new JSONObject()
                .put("headersByMethod", new JSONObject(headersByMethod))
                .put("uploadUrl", uploader.getUploadURL().toString());
    }

    private static String observedRequestIdHeader(
            HttpURLConnection connection,
            String method,
            String requestIdHeaderName
    ) {
        final String value = connection.getRequestProperty(requestIdHeaderName);
        if (value == null || value.isEmpty()) {
            throw new IllegalStateException(
                    "request ID headers did not observe "
                            + requestIdHeaderName
                            + " on "
                            + method
            );
        }

        return value;
    }

    private Api2DevdockTusRequestIdHeaders() {
        throw new IllegalStateException("Utility class");
    }
}
