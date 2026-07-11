package io.tus.java.example;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

final class Api2DevdockTusConformanceServer implements AutoCloseable {
    interface RequestAbortHandler {
        void abortRequest(RequestAbortContext context) throws Exception;
    }

    static final class RequestAbortContext {
        private final int requestIndex;
        private final String method;
        private final String url;

        RequestAbortContext(int requestIndex, String method, String url) {
            this.requestIndex = requestIndex;
            this.method = method;
            this.url = url;
        }

        int requestIndex() {
            return requestIndex;
        }

        String method() {
            return method;
        }

        String url() {
            return url;
        }
    }

    private final URL endpointOrigin;
    private final byte[] inputSourceContent;
    private final List<JSONObject> requests;
    private final boolean[] observedRequests;
    private final List<RequestGate> requestGates;
    private final HttpServer server;
    private final ExecutorService executor;
    private final List<String> errors;
    private final JSONObject[] absentHeaderPresence;
    private final Integer[] requestBodySizes;
    private final Integer[] requestBodyStarts;
    private final JSONObject[] requestHeaders;
    private final String[] requestMethods;
    private final String[] requestUrls;
    private final RequestAbortHandler requestAbortHandler;
    private int nextSequentialRequestIndex;
    private int observedRequestCount;

    Api2DevdockTusConformanceServer(JSONObject conformanceScenario, URL endpointOrigin)
            throws IOException {
        this(conformanceScenario, endpointOrigin, null);
    }

    Api2DevdockTusConformanceServer(
            JSONObject conformanceScenario,
            URL endpointOrigin,
            RequestAbortHandler requestAbortHandler
    )
            throws IOException {
        this.endpointOrigin = endpointOrigin;
        this.requestAbortHandler = requestAbortHandler;
        this.inputSourceContent = inputSourceContent(conformanceScenario);
        this.requests = new ArrayList<JSONObject>();
        final JSONArray requestArray = conformanceScenario.getJSONArray("requests");
        for (int index = 0; index < requestArray.length(); index++) {
            requests.add(requestArray.getJSONObject(index));
        }
        this.observedRequests = new boolean[requests.size()];
        this.requestGates = requestGates(conformanceScenario);
        this.errors = new ArrayList<String>();
        this.absentHeaderPresence = new JSONObject[requests.size()];
        this.requestBodySizes = new Integer[requests.size()];
        this.requestBodyStarts = new Integer[requests.size()];
        this.requestHeaders = new JSONObject[requests.size()];
        this.requestMethods = new String[requests.size()];
        this.requestUrls = new String[requests.size()];
        this.server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        this.executor = Executors.newCachedThreadPool();
        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                handleRequest(exchange);
            }
        });
        server.setExecutor(executor);
        server.start();
    }

    URL endpointUrl() throws IOException {
        return localUrl(endpointOrigin.toString());
    }

    URL localUrlFor(String canonicalUrl) throws IOException {
        return localUrl(canonicalUrl);
    }

    void assertExhausted() {
        assertNoErrors();
        if (observedRequestCount == requests.size()) {
            return;
        }

        throw new IllegalStateException(
                "expected "
                        + requests.size()
                        + " conformance request(s), got "
                        + observedRequestCount
                        + "; observed methods "
                        + observedStrings(requestMethods)
                        + "; observed URLs "
                        + observedStrings(requestUrls)
        );
    }

    void assertNoErrors() {
        if (errors.isEmpty()) {
            return;
        }

        throw new IllegalStateException(errorSummary());
    }

    String canonicalUrl(String actualUrl) {
        return canonicalValue(actualUrl);
    }

    String errorSummary() {
        if (errors.isEmpty()) {
            return "no conformance server errors";
        }

        return String.join("; ", errors);
    }

    JSONObject result() {
        return new JSONObject()
                .put("absentHeaderPresence", observedObjects(absentHeaderPresence))
                .put("requestBodySizes", observedIntegers(requestBodySizes))
                .put("requestBodyStarts", observedIntegers(requestBodyStarts))
                .put("requestHeaders", observedObjects(requestHeaders))
                .put("requestMethods", observedStrings(requestMethods))
                .put("requestUrls", observedStrings(requestUrls));
    }

    @Override
    public void close() {
        server.stop(0);
        executor.shutdownNow();
    }

    private static List<RequestGate> requestGates(JSONObject conformanceScenario) {
        final List<RequestGate> result = new ArrayList<RequestGate>();
        final JSONObject execution = conformanceScenario.optJSONObject("execution");
        if (execution == null) {
            return result;
        }

        final JSONArray gates = execution.optJSONArray("serverRequestGates");
        if (gates == null) {
            return result;
        }

        for (int index = 0; index < gates.length(); index++) {
            result.add(new RequestGate(gates.getJSONObject(index)));
        }

        return result;
    }

    private JSONArray observedIntegers(Integer[] values) {
        final JSONArray result = new JSONArray();
        for (int index = 0; index < values.length; index++) {
            if (observedRequests[index]) {
                result.put(values[index] == null ? JSONObject.NULL : values[index]);
            }
        }

        return result;
    }

    private static byte[] inputSourceContent(JSONObject conformanceScenario) {
        final JSONObject inputSource = conformanceScenario.optJSONObject("inputSource");
        if (inputSource == null || !inputSource.has("content")) {
            return null;
        }

        return inputSource.getString("content").getBytes(StandardCharsets.UTF_8);
    }

    private void awaitRequestGate(int requestIndex) throws InterruptedException {
        for (RequestGate gate : requestGates) {
            gate.awaitIfHeld(requestIndex);
        }
    }

    private JSONArray observedStrings(String[] values) {
        final JSONArray result = new JSONArray();
        for (int index = 0; index < values.length; index++) {
            if (observedRequests[index]) {
                result.put(values[index]);
            }
        }

        return result;
    }

    private JSONArray observedObjects(JSONObject[] values) {
        final JSONArray result = new JSONArray();
        for (int index = 0; index < values.length; index++) {
            if (observedRequests[index]) {
                result.put(values[index]);
            }
        }

        return result;
    }

    private void handleRequest(HttpExchange exchange) throws IOException {
        try {
            final byte[] body = readRequestBody(exchange);
            final int requestIndex = observeRequest(exchange, body);
            if (requests.get(requestIndex).optBoolean("abort", false)) {
                abortRequest(exchange, requestIndex);
                return;
            }
            awaitRequestGate(requestIndex);
            writeResponse(exchange, requests.get(requestIndex));
        } catch (Exception error) {
            errors.add(error.getMessage());
            final byte[] body = errorSummary().getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(500, body.length);
            try (OutputStream responseBody = exchange.getResponseBody()) {
                responseBody.write(body);
            }
        }
    }

    private void abortRequest(HttpExchange exchange, int requestIndex) throws Exception {
        if (requestAbortHandler == null) {
            throw new IllegalStateException("request " + requestIndex + " expected abort handler");
        }

        requestAbortHandler.abortRequest(new RequestAbortContext(
                requestIndex,
                requestMethods[requestIndex],
                requestUrls[requestIndex]
        ));
        exchange.close();
    }

    private synchronized int observeRequest(HttpExchange exchange, byte[] body) throws IOException {
        if (observedRequestCount >= requests.size()) {
            throw new IllegalStateException(
                    "unexpected request "
                            + exchange.getRequestMethod()
                            + " "
                            + exchange.getRequestURI()
            );
        }

        final String actualUrl = canonicalRequestUrl(exchange.getRequestURI());
        final int requestIndex = matchingRequestIndex(exchange, actualUrl, body);
        if (requestIndex < 0) {
            throw new IllegalStateException(
                    "unexpected request "
                            + exchange.getRequestMethod()
                            + " "
                            + actualUrl
                            + "; next planned request "
                            + nextSequentialRequestIndex
            );
        }
        final JSONObject requestPlan = requests.get(requestIndex);
        assertRequestMatchesPlan(requestIndex, requestPlan, exchange, actualUrl, body);
        assertRequestBodyContent(requestIndex, requestPlan, body);
        assertAbsentHeaders(requestIndex, requestPlan, exchange.getRequestHeaders());
        final JSONObject expectedHeaders = requestPlan.getJSONObject("effectiveHeaders");
        assertHeaders(requestIndex, expectedHeaders, exchange.getRequestHeaders());

        absentHeaderPresence[requestIndex] = capturedAbsentHeaderPresence(
                requestPlan,
                exchange.getRequestHeaders()
        );
        requestBodySizes[requestIndex] = requestPlan.isNull("bodySize")
                ? null
                : Integer.valueOf(body.length);
        requestBodyStarts[requestIndex] = requestPlan.has("bodyStart") && !requestPlan.isNull("bodyStart")
                ? Integer.valueOf(requestPlan.getInt("bodyStart"))
                : null;
        requestMethods[requestIndex] = exchange.getRequestMethod();
        requestUrls[requestIndex] = actualUrl;
        requestHeaders[requestIndex] = capturedHeaders(expectedHeaders, exchange.getRequestHeaders());
        observedRequests[requestIndex] = true;
        observedRequestCount += 1;
        while (
                nextSequentialRequestIndex < observedRequests.length
                        && observedRequests[nextSequentialRequestIndex]
        ) {
            nextSequentialRequestIndex += 1;
        }

        return requestIndex;
    }

    private int matchingRequestIndex(
            HttpExchange exchange,
            String actualUrl,
            byte[] body
    ) {
        if (requestMatchesPlan(nextSequentialRequestIndex, exchange, actualUrl, body)) {
            return nextSequentialRequestIndex;
        }

        for (RequestGate gate : requestGates) {
            final int requestIndex = gate.matchingHeldRequestIndex(
                    observedRequests,
                    requests,
                    exchange,
                    actualUrl,
                    body
            );
            if (requestIndex >= 0) {
                return requestIndex;
            }
        }

        return -1;
    }

    private boolean requestMatchesPlan(
            int requestIndex,
            HttpExchange exchange,
            String actualUrl,
            byte[] body
    ) {
        if (requestIndex < 0 || requestIndex >= requests.size() || observedRequests[requestIndex]) {
            return false;
        }

        final JSONObject requestPlan = requests.get(requestIndex);
        return requestPlan.getString("effectiveMethod").equals(exchange.getRequestMethod())
                && requestPlan.getString("expectedUrl").equals(actualUrl)
                && (requestPlan.isNull("bodySize") || body.length == requestPlan.getInt("bodySize"));
    }

    private void assertRequestMatchesPlan(
            int requestIndex,
            JSONObject requestPlan,
            HttpExchange exchange,
            String actualUrl,
            byte[] body
    ) {
        final String expectedUrl = requestPlan.getString("expectedUrl");
        final String expectedMethod = requestPlan.getString("effectiveMethod");
        if (!expectedMethod.equals(exchange.getRequestMethod())) {
            throw new IllegalStateException(
                    "request "
                            + requestIndex
                            + " expected method "
                            + expectedMethod
                            + ", got "
                            + exchange.getRequestMethod()
            );
        }
        if (!expectedUrl.equals(actualUrl)) {
            throw new IllegalStateException(
                    "request "
                            + requestIndex
                            + " expected URL "
                            + expectedUrl
                            + ", got "
                            + actualUrl
            );
        }
        if (!requestPlan.isNull("bodySize") && body.length != requestPlan.getInt("bodySize")) {
            throw new IllegalStateException(
                    "request "
                            + requestIndex
                            + " expected body size "
                            + requestPlan.getInt("bodySize")
                            + ", got "
                            + body.length
            );
        }
    }

    private void assertRequestBodyContent(int requestIndex, JSONObject requestPlan, byte[] body) {
        if (!requestPlan.has("bodyStart") || requestPlan.isNull("bodyStart") || inputSourceContent == null) {
            return;
        }

        final int bodyStart = requestPlan.getInt("bodyStart");
        if (bodyStart + body.length > inputSourceContent.length) {
            throw new IllegalStateException(
                    "request "
                            + requestIndex
                            + " body range "
                            + bodyStart
                            + ".."
                            + (bodyStart + body.length)
                            + " exceeds input source length "
                            + inputSourceContent.length
            );
        }
        for (int index = 0; index < body.length; index++) {
            final byte expected = inputSourceContent[bodyStart + index];
            if (body[index] == expected) {
                continue;
            }

            throw new IllegalStateException(
                    "request "
                            + requestIndex
                            + " body byte "
                            + index
                            + " expected "
                            + expected
                            + ", got "
                            + body[index]
            );
        }
    }

    private void assertAbsentHeaders(int requestIndex, JSONObject requestPlan, Headers actualHeaders) {
        final JSONArray absentHeaders = requestPlan.getJSONArray("absentHeaders");
        for (int index = 0; index < absentHeaders.length(); index++) {
            final String name = absentHeaders.getString(index);
            if (actualHeaders.getFirst(name) == null) {
                continue;
            }

            throw new IllegalStateException(
                    "request " + requestIndex + " expected header " + name + " to be absent"
            );
        }
    }

    private void assertHeaders(int requestIndex, JSONObject expectedHeaders, Headers actualHeaders) {
        for (String name : expectedHeaders.keySet()) {
            final String expectedValue = localValue(expectedHeaders.getString(name));
            final String actualValue = actualHeaders.getFirst(name);
            if (expectedValue.equals(actualValue)) {
                continue;
            }

            throw new IllegalStateException(
                    "request "
                            + requestIndex
                            + " expected header "
                            + name
                            + "="
                            + expectedValue
                            + ", got "
                            + actualValue
            );
        }
    }

    private JSONObject capturedHeaders(JSONObject expectedHeaders, Headers actualHeaders) {
        final JSONObject result = new JSONObject();
        for (String name : expectedHeaders.keySet()) {
            final String value = actualHeaders.getFirst(name);
            if (value != null) {
                result.put(name, canonicalValue(value));
            }
        }

        return result;
    }

    private JSONObject capturedAbsentHeaderPresence(
            JSONObject requestPlan,
            Headers actualHeaders
    ) {
        final JSONObject result = new JSONObject();
        final JSONArray absentHeaders = requestPlan.getJSONArray("absentHeaders");
        for (int index = 0; index < absentHeaders.length(); index++) {
            final String name = absentHeaders.getString(index);
            result.put(name, actualHeaders.getFirst(name) != null);
        }

        return result;
    }

    private void writeResponse(HttpExchange exchange, JSONObject requestPlan) throws IOException {
        final JSONObject responsePlan = requestPlan.getJSONObject("response");
        final Headers headers = exchange.getResponseHeaders();
        final JSONObject responseHeaders = responsePlan.getJSONObject("effectiveHeaders");
        for (String name : responseHeaders.keySet()) {
            headers.set(name, localValue(responseHeaders.getString(name)));
        }

        final byte[] body;
        if (responsePlan.isNull("body")) {
            body = new byte[0];
        } else {
            body = responsePlan.getString("body").getBytes(StandardCharsets.UTF_8);
        }
        final long responseBodyLength = body.length == 0 ? -1 : body.length;
        exchange.sendResponseHeaders(responsePlan.getInt("statusCode"), responseBodyLength);
        try (OutputStream responseBody = exchange.getResponseBody()) {
            responseBody.write(body);
        }
    }

    private URL localUrl(String canonicalUrl) throws IOException {
        return new URL(localValue(canonicalUrl));
    }

    private String localValue(String value) {
        return value.replace(canonicalOrigin(), localOrigin());
    }

    private String canonicalValue(String value) {
        return value.replace(localOrigin(), canonicalOrigin());
    }

    private String canonicalRequestUrl(URI requestUri) throws IOException {
        return new URL(endpointOrigin, requestUri.toString()).toString();
    }

    private static byte[] readRequestBody(HttpExchange exchange) throws IOException {
        final ByteArrayOutputStream body = new ByteArrayOutputStream();
        final byte[] buffer = new byte[8192];
        int read;
        while ((read = exchange.getRequestBody().read(buffer)) != -1) {
            body.write(buffer, 0, read);
        }

        return body.toByteArray();
    }

    private String canonicalOrigin() {
        return endpointOrigin.getProtocol() + "://" + endpointOrigin.getHost();
    }

    private String localOrigin() {
        final InetSocketAddress address = server.getAddress();
        return "http://" + address.getHostString() + ":" + address.getPort();
    }

    private static final class RequestGate {
        private final List<Integer> heldRequestIndexes;
        private final List<Integer> releaseAfterRequestIndexes;
        private final long timeoutMs;
        private final List<Integer> arrivedRequestIndexes;
        private boolean released;

        RequestGate(JSONObject gate) {
            if (!"release-after-all-started".equals(gate.getString("kind"))) {
                throw new IllegalArgumentException(
                        "unsupported conformance server request gate " + gate.getString("kind")
                );
            }

            heldRequestIndexes = integerList(gate.getJSONArray("heldRequestIndexes"));
            releaseAfterRequestIndexes = integerList(gate.getJSONArray("releaseAfterRequestIndexes"));
            timeoutMs = gate.getLong("timeoutMs");
            arrivedRequestIndexes = new ArrayList<Integer>();
        }

        int matchingHeldRequestIndex(
                boolean[] observedRequests,
                List<JSONObject> requests,
                HttpExchange exchange,
                String actualUrl,
                byte[] body
        ) {
            for (Integer requestIndex : heldRequestIndexes) {
                final int index = requestIndex.intValue();
                if (index < 0 || index >= requests.size() || observedRequests[index]) {
                    continue;
                }

                final JSONObject requestPlan = requests.get(index);
                if (!requestPlan.getString("effectiveMethod").equals(exchange.getRequestMethod())) {
                    continue;
                }
                if (!requestPlan.getString("expectedUrl").equals(actualUrl)) {
                    continue;
                }
                if (!requestPlan.isNull("bodySize") && body.length != requestPlan.getInt("bodySize")) {
                    continue;
                }

                return index;
            }

            return -1;
        }

        synchronized void awaitIfHeld(int requestIndex) throws InterruptedException {
            if (!contains(heldRequestIndexes, requestIndex)) {
                return;
            }

            if (!contains(arrivedRequestIndexes, requestIndex)) {
                arrivedRequestIndexes.add(Integer.valueOf(requestIndex));
            }
            if (containsAll(arrivedRequestIndexes, releaseAfterRequestIndexes)) {
                released = true;
                notifyAll();
                return;
            }

            final long deadline = System.currentTimeMillis() + timeoutMs;
            while (!released) {
                final long remainingMs = deadline - System.currentTimeMillis();
                if (remainingMs <= 0) {
                    throw new IllegalStateException(
                            "timed out waiting for conformance request gate; arrived "
                                    + arrivedRequestIndexes
                                    + ", expected "
                                    + releaseAfterRequestIndexes
                    );
                }
                wait(remainingMs);
            }
        }

        private static List<Integer> integerList(JSONArray values) {
            final List<Integer> result = new ArrayList<Integer>();
            for (int index = 0; index < values.length(); index++) {
                result.add(Integer.valueOf(values.getInt(index)));
            }

            return result;
        }

        private static boolean contains(List<Integer> values, int expected) {
            for (Integer value : values) {
                if (value.intValue() == expected) {
                    return true;
                }
            }

            return false;
        }

        private static boolean containsAll(List<Integer> values, List<Integer> expectedValues) {
            for (Integer expected : expectedValues) {
                if (!contains(values, expected.intValue())) {
                    return false;
                }
            }

            return true;
        }
    }

    private Api2DevdockTusConformanceServer() {
        throw new IllegalStateException("Utility class");
    }
}
