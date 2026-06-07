package io.tus.java.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

final class Api2DevdockScenario {
    static JSONObject loadScenario() throws IOException {
        String scenarioPath = System.getenv("API2_SDK_EXAMPLE_SCENARIO");
        if (scenarioPath == null || scenarioPath.isEmpty()) {
            scenarioPath = "example/api2-scenario.json";
        }

        final byte[] contents = Files.readAllBytes(Paths.get(scenarioPath));
        return new JSONObject(new String(contents, StandardCharsets.UTF_8));
    }

    static void writeResult(JSONObject result) throws IOException {
        final String resultPath = System.getenv("API2_SDK_EXAMPLE_RESULT");
        if (resultPath == null || resultPath.isEmpty()) {
            return;
        }

        Files.write(
                Paths.get(resultPath),
                (result.toString(2) + "\n").getBytes(StandardCharsets.UTF_8)
        );
    }

    static JSONObject createResponse(JSONObject scenario) {
        return scenario.getJSONObject("prepared").getJSONObject("createResponse");
    }

    static byte[] scenarioBytes(JSONObject uploadConfig) {
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

    static int fixedChunkSizeBytes(JSONObject uploadConfig) {
        final JSONObject chunkSize = uploadConfig.getJSONObject("chunkSize");
        final String kind = chunkSize.getString("kind");
        if (!"fixed-bytes".equals(kind)) {
            throw new IllegalArgumentException("unsupported chunk size policy " + kind);
        }

        return chunkSize.getInt("bytes");
    }

    static void requireFullFileChunkSize(JSONObject uploadConfig) {
        final Object chunkSize = uploadConfig.get("chunkSize");
        if (!"full-file".equals(chunkSize)) {
            throw new IllegalArgumentException("unsupported chunk size policy " + chunkSize);
        }
    }

    static String tusUrl(
            JSONObject uploadConfig,
            JSONObject scenario,
            JSONObject createResponse
    ) {
        return scalarString(resolveValue(uploadConfig.getJSONObject("tusUrl"), scenario, createResponse));
    }

    static Map<String, String> uploadMetadata(
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

    private Api2DevdockScenario() {
        throw new IllegalStateException("Utility class");
    }
}
