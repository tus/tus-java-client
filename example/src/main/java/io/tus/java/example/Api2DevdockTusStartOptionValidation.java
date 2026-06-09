package io.tus.java.example;

import io.tus.java.client.TusClient;
import io.tus.java.client.TusStartOptions;
import io.tus.java.client.TusUpload;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.net.URL;

public final class Api2DevdockTusStartOptionValidation {
    /**
     * Run the API2 devdock start-option validation scenario.
     *
     * @param args Unused command-line arguments.
     * @throws Exception Thrown when the scenario cannot be executed.
     */
    public static void main(String[] args) throws Exception {
        final JSONObject scenario = Api2DevdockScenario.loadScenario();
        final JSONObject conformanceScenario = Api2DevdockScenario.conformanceScenario(scenario);
        final JSONObject result = validateStartOptions(conformanceScenario);

        Api2DevdockScenario.writeResult(result);
        System.out.println(
                "Java TUS SDK devdock scenario "
                        + scenario.getString("scenarioId")
                        + " rejected "
                        + conformanceScenario.getJSONObject("completion").getString("reason")
        );
    }

    private static JSONObject validateStartOptions(JSONObject conformanceScenario)
            throws Exception {
        final TusClient client = new TusClient();
        final TusStartOptions options = startOptionsFor(conformanceScenario);

        try {
            client.validateStartOptions(options);
        } catch (IllegalArgumentException error) {
            final String expectedMessage = conformanceScenario
                    .getJSONObject("completion")
                    .getString("message");
            if (!expectedMessage.equals(error.getMessage())) {
                throw new IllegalStateException(
                        "expected start option validation error "
                                + expectedMessage
                                + ", got "
                                + error.getMessage()
                );
            }

            return new JSONObject()
                    .put("errorCaught", true)
                    .put("errorMessage", error.getMessage())
                    .put("requestCount", 0);
        }

        throw new IllegalStateException("start option validation scenario unexpectedly succeeded");
    }

    private static TusStartOptions startOptionsFor(JSONObject conformanceScenario)
            throws Exception {
        final TusStartOptions options = new TusStartOptions();
        final String endpointURL = Api2DevdockScenario.conformanceInputStringOptionOrNull(
                conformanceScenario,
                "endpointUrl"
        );
        final String uploadURL = Api2DevdockScenario.conformanceInputStringOptionOrNull(
                conformanceScenario,
                "uploadUrl"
        );
        final int parallelUploads = Api2DevdockScenario.conformanceInputIntegerOption(
                conformanceScenario,
                "parallelUploads",
                1
        );
        final boolean uploadDataDuringCreation =
                Api2DevdockScenario.conformanceInputBooleanOption(
                        conformanceScenario,
                        "uploadDataDuringCreation",
                        false
                );
        final boolean uploadLengthDeferred = Api2DevdockScenario.conformanceInputBooleanOption(
                conformanceScenario,
                "uploadLengthDeferred",
                false
        );

        options.setUpload(uploadFor(conformanceScenario, uploadLengthDeferred));
        options.setParallelUploads(parallelUploads);
        options.setUploadDataDuringCreation(uploadDataDuringCreation);
        options.setUploadLengthDeferred(uploadLengthDeferred);
        if (endpointURL != null) {
            options.setEndpointURL(new URL(endpointURL));
        }
        if (uploadURL != null) {
            options.setUploadURL(new URL(uploadURL));
        }

        return options;
    }

    private static TusUpload uploadFor(
            JSONObject conformanceScenario,
            boolean uploadLengthDeferred
    ) {
        final byte[] content = Api2DevdockScenario.conformanceInputSourceBytes(conformanceScenario);
        final TusUpload upload = new TusUpload();
        upload.setInputStream(new ByteArrayInputStream(content));
        upload.setSize(content.length);
        upload.setUploadLengthDeferred(uploadLengthDeferred);
        return upload;
    }

    private Api2DevdockTusStartOptionValidation() {
        throw new IllegalStateException("Utility class");
    }
}
