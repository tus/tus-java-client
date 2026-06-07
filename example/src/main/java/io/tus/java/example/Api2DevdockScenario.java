package io.tus.java.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class Api2DevdockScenario {
    static final class UploadCallbackEventKinds {
        final String chunkComplete;
        final String progress;
        final String sourceClose;
        final String success;
        final String uploadUrlAvailable;

        UploadCallbackEventKinds(JSONObject eventKinds) {
            chunkComplete = eventKinds.getString("chunkComplete");
            progress = eventKinds.getString("progress");
            sourceClose = eventKinds.getString("sourceClose");
            success = eventKinds.getString("success");
            uploadUrlAvailable = eventKinds.getString("uploadUrlAvailable");
        }
    }

    static final class UploadCallbacksPlan {
        final List<String> allowedExtraEventKeyPrefixes;
        final List<List<String>> eventKeyAlternativeGroups;
        final UploadCallbackEventKinds eventKinds;
        final String eventKeyPartSeparator;
        final List<String> eventKeys;
        final String eventPolicyMatching;

        UploadCallbacksPlan(JSONObject uploadCallbacks) {
            allowedExtraEventKeyPrefixes = stringList(
                    uploadCallbacks.getJSONArray("allowedExtraEventKeyPrefixes")
            );
            eventKeyAlternativeGroups = stringListList(
                    uploadCallbacks.getJSONArray("eventKeyAlternativeGroups")
            );
            eventKinds = new UploadCallbackEventKinds(uploadCallbacks.getJSONObject("eventKinds"));
            eventKeyPartSeparator = uploadCallbacks.getString("eventKeyPartSeparator");
            eventKeys = stringList(uploadCallbacks.getJSONArray("eventKeys"));
            eventPolicyMatching = uploadCallbacks.getString("eventPolicyMatching");
        }
    }

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

    static Map<String, String> uploadHeaders(JSONObject uploadConfig) {
        return stringMap(uploadConfig.getJSONObject("headers"));
    }

    static UploadCallbacksPlan uploadCallbacks(JSONObject scenario) {
        return new UploadCallbacksPlan(
                scenario.getJSONObject("upload").getJSONObject("uploadCallbacks")
        );
    }

    static String uploadCallbackEventKey(UploadCallbacksPlan plan, String... parts) {
        final StringBuilder key = new StringBuilder();
        for (int index = 0; index < parts.length; index++) {
            if (index > 0) {
                key.append(plan.eventKeyPartSeparator);
            }
            key.append(parts[index]);
        }

        return key.toString();
    }

    static String uploadCallbackEventKeyNumber(long value) {
        return Long.toString(value);
    }

    static List<String> matchUploadCallbackEventKeys(
            UploadCallbacksPlan plan,
            List<String> actual
    ) {
        if (!"exact".equals(plan.eventPolicyMatching)
                && !"exact-except-allowed-extra-events".equals(plan.eventPolicyMatching)) {
            throw new IllegalArgumentException(
                    "unsupported upload callback event policy " + plan.eventPolicyMatching
            );
        }

        final List<String> matched = new ArrayList<String>();
        int expectedIndex = 0;
        for (String event : actual) {
            if (expectedIndex < plan.eventKeys.size()
                    && uploadCallbackEventMatchesExpected(plan, expectedIndex, event)) {
                matched.add(plan.eventKeys.get(expectedIndex));
                expectedIndex += 1;
                continue;
            }

            if ("exact-except-allowed-extra-events".equals(plan.eventPolicyMatching)
                    && hasAllowedUploadCallbackExtraEventPrefix(plan, event)) {
                continue;
            }

            throw new IllegalStateException(
                    "unexpected upload callback event "
                            + event
                            + " at expected index "
                            + expectedIndex
                            + "; expected "
                            + plan.eventKeys
                            + ", actual "
                            + actual
            );
        }

        if (expectedIndex != plan.eventKeys.size()) {
            throw new IllegalStateException(
                    "missing upload callback events after index "
                            + expectedIndex
                            + "; expected "
                            + plan.eventKeys
                            + ", actual "
                            + actual
            );
        }

        return matched;
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

    private static List<String> stringList(JSONArray values) {
        final List<String> result = new ArrayList<String>();
        for (int index = 0; index < values.length(); index++) {
            result.add(values.getString(index));
        }

        return result;
    }

    private static Map<String, String> stringMap(JSONObject values) {
        final Map<String, String> result = new LinkedHashMap<String, String>();
        for (String key : values.keySet()) {
            result.put(key, values.getString(key));
        }

        return result;
    }

    private static List<List<String>> stringListList(JSONArray values) {
        final List<List<String>> result = new ArrayList<List<String>>();
        for (int index = 0; index < values.length(); index++) {
            result.add(stringList(values.getJSONArray(index)));
        }

        return result;
    }

    private static boolean uploadCallbackEventMatchesExpected(
            UploadCallbacksPlan plan,
            int expectedIndex,
            String event
    ) {
        if (plan.eventKeys.get(expectedIndex).equals(event)) {
            return true;
        }

        final List<String> alternatives = plan.eventKeyAlternativeGroups.get(expectedIndex);
        for (String alternative : alternatives) {
            if (alternative.equals(event)) {
                return true;
            }
        }

        return false;
    }

    private static boolean hasAllowedUploadCallbackExtraEventPrefix(
            UploadCallbacksPlan plan,
            String event
    ) {
        for (String prefix : plan.allowedExtraEventKeyPrefixes) {
            if (event.startsWith(prefix)) {
                return true;
            }
        }

        return false;
    }

    private Api2DevdockScenario() {
        throw new IllegalStateException("Utility class");
    }
}
