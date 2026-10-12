package io.tus.java.example;

import io.tus.java.client.ProtocolException;
import io.tus.java.client.TusClient;
import io.tus.java.client.TusRequestLifecycleHooks;
import io.tus.java.client.TusUpload;
import io.tus.java.client.TusUploader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public final class Api2DevdockTusTerminateUpload {
    /**
     * Run the API2 devdock TUS terminate-upload example.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        try {
            System.setProperty("http.strictPostRedirect", "true");

            final JSONObject scenario = Api2DevdockScenario.loadScenario();
            final JSONObject createResponse = Api2DevdockScenario.createResponse(scenario);
            final JSONObject result = uploadAndTerminate(scenario, createResponse);
            Api2DevdockScenario.writeResult(result);

            System.out.println(
                    "Java TUS SDK devdock scenario "
                            + scenario.getString("scenarioId")
                            + " terminated "
                            + result.getString("uploadUrl")
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static JSONObject uploadAndTerminate(
            JSONObject scenario,
            JSONObject createResponse
    ) throws IOException, ProtocolException {
        final JSONObject uploadConfig = scenario.getJSONObject("upload");
        final Api2DevdockScenario.TerminationPlan termination =
                Api2DevdockScenario.termination(uploadConfig);
        final byte[] content = Api2DevdockScenario.scenarioBytes(uploadConfig);
        final int chunkSize = Api2DevdockScenario.fixedChunkSizeBytes(uploadConfig);
        final List<String> requestMethods = new ArrayList<String>();

        if (termination.stopAfterAcceptedBytes > content.length) {
            throw new IllegalStateException(
                    "terminate upload stop-after bytes "
                            + termination.stopAfterAcceptedBytes
                            + " exceeds content length "
                            + content.length
            );
        }

        final TusClient client = new TusClient();
        client.setUploadCreationURL(
                new URL(Api2DevdockScenario.tusUrl(uploadConfig, scenario, createResponse))
        );
        client.setRequestLifecycleHooks(new TusRequestLifecycleHooks(
                new TusRequestLifecycleHooks.BeforeRequest() {
                    @Override
                    public void beforeRequest(TusRequestLifecycleHooks.RequestContext context) {
                        requestMethods.add(context.getMethod());
                    }
                },
                null
        ));

        final TusUpload upload = new TusUpload();
        upload.setInputStream(new ByteArrayInputStream(content));
        upload.setSize(content.length);
        upload.setFingerprint(scenario.getString("scenarioId") + "-java-terminate-upload");
        upload.setMetadata(
                Api2DevdockScenario.uploadMetadata(uploadConfig, scenario, createResponse)
        );

        final TusUploader uploader = client.createUpload(upload);
        uploader.setChunkSize(chunkSize);
        uploader.setRequestPayloadSize(termination.stopAfterAcceptedBytes);
        final int uploadedChunkSize = uploader.uploadChunk();
        uploader.finish();

        if (uploadedChunkSize != termination.stopAfterAcceptedBytes) {
            throw new IllegalStateException(
                    "terminate upload wrote "
                            + uploadedChunkSize
                            + " bytes, expected "
                            + termination.stopAfterAcceptedBytes
            );
        }
        if (uploader.getOffset() != termination.stopAfterAcceptedBytes) {
            throw new IllegalStateException(
                    "terminate upload accepted "
                            + uploader.getOffset()
                            + " bytes, expected "
                            + termination.stopAfterAcceptedBytes
            );
        }
        if (uploader.getUploadURL() == null) {
            throw new IllegalStateException("terminate upload did not expose a URL");
        }

        final URL uploadUrl = uploader.getUploadURL();
        client.terminateUpload(uploadUrl).disconnect();
        final int verificationStatus = verifyTerminatedUpload(
                client,
                termination.verificationMethod,
                uploadUrl
        );
        if (verificationStatus != termination.expectedVerificationStatus) {
            throw new IllegalStateException(
                    "terminate upload verification status "
                            + verificationStatus
                            + ", expected "
                            + termination.expectedVerificationStatus
            );
        }

        return new JSONObject()
                .put("acceptedBytes", (int) uploader.getOffset())
                .put("deleteRequestCount", countMethod(requestMethods, termination.method))
                .put("requestMethods", new JSONArray(requestMethods))
                .put("terminated", true)
                .put("uploadUrl", uploadUrl.toString())
                .put("verificationStatus", verificationStatus);
    }

    private static int verifyTerminatedUpload(
            TusClient client,
            String method,
            URL uploadUrl
    ) throws IOException {
        final HttpURLConnection connection = (HttpURLConnection) uploadUrl.openConnection();
        try {
            connection.setRequestMethod(method);
            client.prepareConnection(connection);
            connection.connect();
            return connection.getResponseCode();
        } finally {
            connection.disconnect();
        }
    }

    private static int countMethod(List<String> methods, String expectedMethod) {
        int count = 0;
        for (String method : methods) {
            if (method.equals(expectedMethod)) {
                count += 1;
            }
        }

        return count;
    }

    private Api2DevdockTusTerminateUpload() {
        throw new IllegalStateException("Utility class");
    }
}
