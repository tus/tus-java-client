package io.tus.java.example;

import io.tus.java.client.ProtocolException;
import io.tus.java.client.TusClient;
import io.tus.java.client.TusDetailedError;
import io.tus.java.client.TusUpload;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public final class Api2DevdockTusDetailedError {
    /**
     * Run the API2 devdock detailed-error scenario.
     *
     * @param args Unused command-line arguments.
     * @throws Exception Thrown when the scenario cannot be executed.
     */
    public static void main(String[] args) throws Exception {
        final JSONObject scenario = Api2DevdockScenario.loadScenario();
        final JSONObject result = uploadExpectingDetailedError(
                Api2DevdockScenario.conformanceScenario(scenario)
        );

        Api2DevdockScenario.writeResult(result);
        System.out.println(
                "Java TUS SDK devdock scenario "
                        + scenario.getString("scenarioId")
                        + " observed detailed error "
                        + result.getString("errorMessage")
        );
    }

    private static JSONObject uploadExpectingDetailedError(JSONObject conformanceScenario)
            throws Exception {
        final JSONObject requestPlan = conformanceScenario
                .getJSONArray("requests")
                .getJSONObject(0);
        if (!requestPlan.isNull("errorMessage")) {
            return uploadExpectingRequestError(conformanceScenario, requestPlan);
        }

        return uploadExpectingResponseError(conformanceScenario);
    }

    private static JSONObject uploadExpectingResponseError(JSONObject conformanceScenario)
            throws Exception {
        final URL endpointUrl = new URL(
                Api2DevdockScenario.conformanceInputStringOption(conformanceScenario, "endpointUrl")
        );
        try (Api2DevdockTusConformanceServer conformanceServer =
                new Api2DevdockTusConformanceServer(conformanceScenario, endpointUrl)) {
            final TusClient client = clientFor(conformanceScenario);
            client.setUploadCreationURL(conformanceServer.endpointUrl());

            final Throwable error = createUploadExpectingError(client, conformanceScenario);
            conformanceServer.assertExhausted();

            final JSONObject serverResult = conformanceServer.result();
            return detailedResult(
                    error,
                    canonicalize(serverResult, conformanceServer),
                    conformanceServer
            );
        }
    }

    private static JSONObject uploadExpectingRequestError(
            JSONObject conformanceScenario,
            JSONObject requestPlan
    ) throws Exception {
        final URL endpointUrl = new URL(
                Api2DevdockScenario.conformanceInputStringOption(conformanceScenario, "endpointUrl")
        );
        final FailingTusClient client = new FailingTusClient(requestPlan.getString("errorMessage"));
        configureClient(client, conformanceScenario);
        client.setUploadCreationURL(endpointUrl);

        final Throwable error = createUploadExpectingError(client, conformanceScenario);
        final JSONObject requestResult = new JSONObject()
                .put("requestMethods", new JSONArray().put(requestPlan.getString("effectiveMethod")))
                .put("requestUrls", new JSONArray().put(requestPlan.getString("expectedUrl")));

        return detailedResult(error, requestResult, null);
    }

    private static TusClient clientFor(JSONObject conformanceScenario) {
        final TusClient client = new TusClient();
        configureClient(client, conformanceScenario);
        return client;
    }

    private static void configureClient(TusClient client, JSONObject conformanceScenario) {
        final Map<String, String> headers = Api2DevdockScenario.conformanceInputStringMapOption(
                conformanceScenario,
                "headers"
        );
        client.setHeaders(headers);
    }

    private static Throwable createUploadExpectingError(
            TusClient client,
            JSONObject conformanceScenario
    ) throws Exception {
        final TusUpload upload = uploadFor(conformanceScenario);
        try {
            client.createUpload(upload);
        } catch (IOException | ProtocolException error) {
            return error;
        }

        throw new IllegalStateException("detailed error scenario unexpectedly created an upload");
    }

    private static TusUpload uploadFor(JSONObject conformanceScenario) {
        final byte[] content = Api2DevdockScenario.conformanceInputSourceBytes(conformanceScenario);
        final Map<String, String> metadata = Api2DevdockScenario.conformanceInputStringMapOption(
                conformanceScenario,
                "metadata"
        );
        final TusUpload upload = new TusUpload();
        upload.setInputStream(new ByteArrayInputStream(content));
        upload.setMetadata(metadata);
        upload.setSize(content.length);
        return upload;
    }

    private static JSONObject detailedResult(
            Throwable error,
            JSONObject requestResult,
            Api2DevdockTusConformanceServer conformanceServer
    ) {
        final JSONObject result = new JSONObject()
                .put("errorCaught", true)
                .put("errorIsDetailed", error instanceof TusDetailedError)
                .put("errorMessage", canonicalValue(error.getMessage(), conformanceServer))
                .put("requestCount", requestResult.getJSONArray("requestMethods").length())
                .put("requestMethods", requestResult.getJSONArray("requestMethods"))
                .put("requestUrls", requestResult.getJSONArray("requestUrls"));

        if (!(error instanceof TusDetailedError)) {
            return result;
        }

        final TusDetailedError detailedError = (TusDetailedError) error;
        result.put("causingErrorPresent", detailedError.getCausingError() != null);
        if (detailedError.getCausingError() != null) {
            result.put("causingErrorMessage", detailedError.getCausingError().getMessage());
        }
        result.put("originalRequestMethod", detailedError.getOriginalRequestMethod());
        result.put("originalRequestRequestId", detailedError.getOriginalRequestId());
        result.put(
                "originalRequestUrl",
                canonicalValue(detailedError.getOriginalRequestURL().toString(), conformanceServer)
        );
        result.put("originalResponsePresent", detailedError.hasOriginalResponse());
        if (detailedError.hasOriginalResponse()) {
            result.put("originalResponseBody", detailedError.getOriginalResponseBody());
            result.put("originalResponseStatus", detailedError.getOriginalResponseStatus());
        }

        return result;
    }

    private static JSONObject canonicalize(
            JSONObject requestResult,
            Api2DevdockTusConformanceServer conformanceServer
    ) {
        final JSONArray requestUrls = requestResult.getJSONArray("requestUrls");
        final JSONArray canonicalUrls = new JSONArray();
        for (int index = 0; index < requestUrls.length(); index++) {
            canonicalUrls.put(conformanceServer.canonicalUrl(requestUrls.getString(index)));
        }

        return new JSONObject(requestResult.toString()).put("requestUrls", canonicalUrls);
    }

    private static String canonicalValue(
            String value,
            Api2DevdockTusConformanceServer conformanceServer
    ) {
        if (conformanceServer == null) {
            return value;
        }

        return conformanceServer.canonicalUrl(value);
    }

    private static final class FailingTusClient extends TusClient {
        private final String errorMessage;

        FailingTusClient(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        @Override
        protected HttpURLConnection openConnection(URL uploadUrl) {
            return new FailingHttpURLConnection(uploadUrl, errorMessage);
        }
    }

    private static final class FailingHttpURLConnection extends HttpURLConnection {
        private final String errorMessage;

        FailingHttpURLConnection(URL url, String errorMessage) {
            super(url);
            this.errorMessage = errorMessage;
        }

        @Override
        public void connect() throws IOException {
            throw new IOException(errorMessage);
        }

        @Override
        public void disconnect() {
        }

        @Override
        public boolean usingProxy() {
            return false;
        }
    }

    private Api2DevdockTusDetailedError() {
        throw new IllegalStateException("Utility class");
    }
}
