package io.tus.java.example;

import io.tus.java.client.ProtocolException;
import io.tus.java.client.TusClient;
import io.tus.java.client.TusURLMemoryStore;
import io.tus.java.client.TusUpload;
import io.tus.java.client.TusUploader;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Api2DevdockTusUpload {
    /**
     * Run the API2 devdock TUS upload example.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        try {
            System.setProperty("http.strictPostRedirect", "true");

            final JSONObject scenario = loadScenario();
            final JSONObject createResponse = scenario.getJSONObject("prepared").getJSONObject("createResponse");
            final String uploadUrl = uploadWithTus(scenario, createResponse);

            System.out.println(
                    "Java TUS SDK devdock scenario "
                            + scenario.getString("scenarioId")
                            + " uploaded to "
                            + uploadUrl
            );
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static JSONObject loadScenario() throws IOException {
        String scenarioPath = System.getenv("API2_SDK_EXAMPLE_SCENARIO");
        if (scenarioPath == null || scenarioPath.isEmpty()) {
            scenarioPath = "example/api2-scenario.json";
        }

        final byte[] contents = Files.readAllBytes(Paths.get(scenarioPath));
        return new JSONObject(new String(contents, StandardCharsets.UTF_8));
    }

    private static String uploadWithTus(
            JSONObject scenario,
            JSONObject createResponse
    ) throws IOException, ProtocolException {
        final JSONObject uploadConfig = scenario.getJSONObject("upload");
        final Object endpointValue = resolveValue(uploadConfig.getJSONObject("tusUrl"), scenario, createResponse);
        final byte[] content = scenarioBytes(uploadConfig);

        final TusClient client = new TusClient();
        client.setUploadCreationURL(new URL(scalarString(endpointValue)));
        client.enableResuming(new TusURLMemoryStore());

        final TusUpload upload = new TusUpload();
        upload.setInputStream(new ByteArrayInputStream(content));
        upload.setSize(content.length);
        upload.setFingerprint(scenario.getString("scenarioId") + "-java-devdock-example");
        upload.setMetadata(uploadMetadata(uploadConfig, scenario, createResponse));

        final TusUploader uploader = client.resumeOrCreateUpload(upload);
        uploader.setChunkSize(content.length);
        int uploadedChunkSize;
        do {
            uploadedChunkSize = uploader.uploadChunk();
        } while (uploadedChunkSize > -1);
        uploader.finish();

        if (uploader.getOffset() != content.length) {
            throw new IllegalStateException(
                    "remote offset " + uploader.getOffset() + ", expected " + content.length
            );
        }
        if (uploader.getUploadURL() == null) {
            throw new IllegalStateException("upload did not return a URL");
        }

        return uploader.getUploadURL().toString();
    }

    private static byte[] scenarioBytes(JSONObject uploadConfig) {
        final JSONObject source = uploadConfig.getJSONObject("source");
        final String kind = source.getString("kind");
        if (!"bytes".equals(kind)) {
            throw new IllegalArgumentException("unsupported source kind " + kind);
        }

        final String encoding = source.getString("encoding");
        if (!"utf8".equals(encoding)) {
            throw new IllegalArgumentException("unsupported source encoding " + encoding);
        }

        return source.getString("value").getBytes(StandardCharsets.UTF_8);
    }

    private static Map<String, String> uploadMetadata(
            JSONObject uploadConfig,
            JSONObject scenario,
            JSONObject createResponse
    ) {
        final JSONArray fields = uploadConfig.getJSONArray("metadata");
        final Map<String, String> metadata = new LinkedHashMap<String, String>();
        for (int index = 0; index < fields.length(); index++) {
            final JSONObject field = fields.getJSONObject(index);
            metadata.put(
                    field.getString("name"),
                    scalarString(resolveValue(field.getJSONObject("value"), scenario, createResponse))
            );
        }

        return metadata;
    }

    private static Object resolveValue(
            JSONObject valueSpec,
            JSONObject scenario,
            JSONObject createResponse
    ) {
        if (valueSpec.has("value")) {
            return valueSpec.get("value");
        }

        final JSONObject source = valueSpec.getJSONObject("source");
        final String root = source.getString("root");
        final Object rootValue;
        if ("scenario".equals(root)) {
            rootValue = scenario;
        } else if ("createResponse".equals(root)) {
            rootValue = createResponse;
        } else {
            throw new IllegalArgumentException("unsupported scenario value root " + root);
        }

        return readPath(rootValue, source.getJSONArray("path"));
    }

    private static Object readPath(Object value, JSONArray pathParts) {
        Object current = value;
        for (int index = 0; index < pathParts.length(); index++) {
            final Object part = pathParts.get(index);
            if (current instanceof JSONObject && part instanceof String) {
                current = ((JSONObject) current).get((String) part);
                continue;
            }

            if (current instanceof JSONArray && part instanceof Number) {
                current = ((JSONArray) current).get(((Number) part).intValue());
                continue;
            }

            throw new IllegalArgumentException("cannot read scenario path part " + part);
        }

        return current;
    }

    private static String scalarString(Object value) {
        if (JSONObject.NULL.equals(value)) {
            return "null";
        }

        return String.valueOf(value);
    }

    private Api2DevdockTusUpload() {
        throw new IllegalStateException("Utility class");
    }
}
