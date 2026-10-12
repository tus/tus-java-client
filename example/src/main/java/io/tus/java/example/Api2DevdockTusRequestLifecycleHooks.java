package io.tus.java.example;

import io.tus.java.client.ProtocolException;
import io.tus.java.client.TusClient;
import io.tus.java.client.TusRequestLifecycleHooks;
import io.tus.java.client.TusURLMemoryStore;
import io.tus.java.client.TusUpload;
import io.tus.java.client.TusUploader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class Api2DevdockTusRequestLifecycleHooks {
    /**
     * Run the API2 devdock TUS request lifecycle hooks example.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        try {
            System.setProperty("http.strictPostRedirect", "true");

            final JSONObject scenario = Api2DevdockScenario.loadScenario();
            final JSONObject createResponse = Api2DevdockScenario.createResponse(scenario);
            final JSONObject result = uploadWithRequestLifecycleHooks(scenario, createResponse);
            Api2DevdockScenario.writeResult(result);

            System.out.println(
                    "Java TUS SDK devdock scenario "
                            + scenario.getString("scenarioId")
                            + " observed lifecycle hooks for "
                            + result.getString("uploadUrl")
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static JSONObject uploadWithRequestLifecycleHooks(
            JSONObject scenario,
            JSONObject createResponse
    ) throws IOException, ProtocolException {
        final JSONObject uploadConfig = scenario.getJSONObject("upload");
        final JSONObject hooksConfig = uploadConfig.getJSONObject("requestLifecycleHooks");
        final byte[] content = Api2DevdockScenario.scenarioBytes(uploadConfig);
        final List<String> beforeRequestMethods = new ArrayList<String>();
        final List<String> afterResponseMethods = new ArrayList<String>();
        final List<Integer> afterResponseStatusCodes = new ArrayList<Integer>();
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
                        beforeRequestMethods.add(context.getMethod());
                    }
                },
                new TusRequestLifecycleHooks.AfterResponse() {
                    @Override
                    public void afterResponse(
                            TusRequestLifecycleHooks.RequestContext context
                    ) throws IOException {
                        afterResponseMethods.add(context.getMethod());
                        afterResponseStatusCodes.add(context.getConnection().getResponseCode());
                    }
                }
        ));

        final TusUpload upload = new TusUpload();
        upload.setInputStream(new ByteArrayInputStream(content));
        upload.setSize(content.length);
        upload.setFingerprint(scenario.getString("scenarioId") + "-java-request-hooks");
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
                    "request lifecycle hooks upload offset "
                            + uploader.getOffset()
                            + ", expected "
                            + content.length
            );
        }
        if (uploader.getUploadURL() == null) {
            throw new IllegalStateException("request lifecycle hooks upload did not return a URL");
        }
        assertStringArray(
                beforeRequestMethods,
                hooksConfig.getJSONArray("expectedBeforeRequestMethods"),
                "before request methods"
        );
        assertStringArray(
                afterResponseMethods,
                hooksConfig.getJSONArray("expectedAfterResponseMethods"),
                "after response methods"
        );
        assertIntegerArray(
                afterResponseStatusCodes,
                hooksConfig.getJSONArray("expectedAfterResponseStatusCodes"),
                "after response status codes"
        );

        return new JSONObject()
                .put("afterResponseMethods", new JSONArray(afterResponseMethods))
                .put("afterResponseStatusCodes", new JSONArray(afterResponseStatusCodes))
                .put("beforeRequestMethods", new JSONArray(beforeRequestMethods))
                .put("uploadUrl", uploader.getUploadURL().toString());
    }

    private static void assertStringArray(List<String> actual, JSONArray expected, String label) {
        if (actual.size() != expected.length()) {
            throw new IllegalStateException(
                    "request lifecycle hooks expected "
                            + label
                            + " "
                            + expected
                            + ", got "
                            + actual
            );
        }

        for (int index = 0; index < expected.length(); index++) {
            if (!actual.get(index).equals(expected.getString(index))) {
                throw new IllegalStateException(
                        "request lifecycle hooks expected "
                                + label
                                + " "
                                + expected.getString(index)
                                + " at index "
                                + index
                                + ", got "
                                + actual.get(index)
                );
            }
        }
    }

    private static void assertIntegerArray(
            List<Integer> actual,
            JSONArray expected,
            String label
    ) {
        if (actual.size() != expected.length()) {
            throw new IllegalStateException(
                    "request lifecycle hooks expected "
                            + label
                            + " "
                            + expected
                            + ", got "
                            + actual
            );
        }

        for (int index = 0; index < expected.length(); index++) {
            if (actual.get(index) != expected.getInt(index)) {
                throw new IllegalStateException(
                        "request lifecycle hooks expected "
                                + label
                                + " "
                                + expected.getInt(index)
                                + " at index "
                                + index
                                + ", got "
                                + actual.get(index)
                );
            }
        }
    }

    private Api2DevdockTusRequestLifecycleHooks() {
        throw new IllegalStateException("Utility class");
    }
}
