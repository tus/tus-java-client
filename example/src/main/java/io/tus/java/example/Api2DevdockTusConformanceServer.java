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

final class Api2DevdockTusConformanceServer implements AutoCloseable {
    private final URL endpointOrigin;
    private final List<JSONObject> requests;
    private final HttpServer server;
    private final List<String> errors;
    private final List<JSONObject> requestHeaders;
    private final List<String> requestMethods;
    private final List<String> requestUrls;
    private int nextRequestIndex;

    Api2DevdockTusConformanceServer(JSONObject conformanceScenario, URL endpointOrigin)
            throws IOException {
        this.endpointOrigin = endpointOrigin;
        this.requests = new ArrayList<JSONObject>();
        final JSONArray requestArray = conformanceScenario.getJSONArray("requests");
        for (int index = 0; index < requestArray.length(); index++) {
            requests.add(requestArray.getJSONObject(index));
        }
        this.errors = new ArrayList<String>();
        this.requestHeaders = new ArrayList<JSONObject>();
        this.requestMethods = new ArrayList<String>();
        this.requestUrls = new ArrayList<String>();
        this.server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                handleRequest(exchange);
            }
        });
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
        if (nextRequestIndex == requests.size()) {
            return;
        }

        throw new IllegalStateException(
                "expected "
                        + requests.size()
                        + " conformance request(s), got "
                        + nextRequestIndex
        );
    }

    void assertNoErrors() {
        if (errors.isEmpty()) {
            return;
        }

        throw new IllegalStateException(errorSummary());
    }

    String canonicalUrl(String actualUrl) {
        return actualUrl.replace(localOrigin(), canonicalOrigin());
    }

    String errorSummary() {
        if (errors.isEmpty()) {
            return "no conformance server errors";
        }

        return String.join("; ", errors);
    }

    JSONObject result() {
        return new JSONObject()
                .put("requestHeaders", new JSONArray(requestHeaders))
                .put("requestMethods", new JSONArray(requestMethods))
                .put("requestUrls", new JSONArray(requestUrls));
    }

    @Override
    public void close() {
        server.stop(0);
    }

    private void handleRequest(HttpExchange exchange) throws IOException {
        try {
            final byte[] body = readRequestBody(exchange);
            final int requestIndex = observeRequest(exchange, body);
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

    private int observeRequest(HttpExchange exchange, byte[] body) throws IOException {
        if (nextRequestIndex >= requests.size()) {
            throw new IllegalStateException(
                    "unexpected request "
                            + exchange.getRequestMethod()
                            + " "
                            + exchange.getRequestURI()
            );
        }

        final int requestIndex = nextRequestIndex;
        final JSONObject requestPlan = requests.get(requestIndex);
        final String actualUrl = canonicalRequestUrl(exchange.getRequestURI());
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
        final JSONObject expectedHeaders = requestPlan.getJSONObject("effectiveHeaders");
        assertHeaders(requestIndex, expectedHeaders, exchange.getRequestHeaders());

        requestMethods.add(exchange.getRequestMethod());
        requestUrls.add(actualUrl);
        requestHeaders.add(capturedHeaders(expectedHeaders, exchange.getRequestHeaders()));
        nextRequestIndex += 1;

        return requestIndex;
    }

    private void assertHeaders(int requestIndex, JSONObject expectedHeaders, Headers actualHeaders) {
        for (String name : expectedHeaders.keySet()) {
            final String expectedValue = expectedHeaders.getString(name);
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
                result.put(name, value);
            }
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

    private Api2DevdockTusConformanceServer() {
        throw new IllegalStateException("Utility class");
    }
}
