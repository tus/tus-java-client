package io.tus.java.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Class to test the tus-Client.
 */
public class TestTusClient extends MockServerProvider {

    /**
     * Tests if the client object is set up correctly.
     */
    @Test
    public void testTusClient() {
        TusClient client = new TusClient();
        assertNull(client.getUploadCreationURL());
    }

    /**
     * Checks if upload URLS are set correctly.
     * @throws MalformedURLException if the provided URL is malformed.
     */
    @Test
    public void testTusClientURL() throws MalformedURLException {
        TusClient client = new TusClient();
        client.setUploadCreationURL(creationUrl);
        assertEquals(client.getUploadCreationURL(), creationUrl);
    }

    /**
     * Checks if upload URLS are set correctly.
     * @throws MalformedURLException if the provided URL is malformed.
     */
    @Test
    public void testSetUploadCreationURL() throws MalformedURLException {
        TusClient client = new TusClient();
        client.setUploadCreationURL(new URL("http://tusd.tusdemo.net"));
        assertEquals(client.getUploadCreationURL(), new URL("http://tusd.tusdemo.net"));
    }

    /**
     * Tests if resumable uploads can be turned off and on.
     */
    @Test
    public void testEnableResuming() {
        TusClient client = new TusClient();
        assertFalse(client.resumingEnabled());

        TusURLStore store = new TusURLMemoryStore();
        client.enableResuming(store);
        assertTrue(client.resumingEnabled());

        client.disableResuming();
        assertFalse(client.resumingEnabled());
    }

    /**
     * Verifies if uploads can be created with the tus client.
     * @throws IOException if upload data cannot be read.
     * @throws ProtocolException if the upload cannot be constructed.
     */
    @Test
    public void testCreateUpload() throws IOException, ProtocolException {
        mockServer.when(withDefaultProtocolRequestHeaders(new HttpRequest()
                .withMethod("POST")
                .withPath("/files")
                .withHeader("Connection", "keep-alive")
                .withHeader("Upload-Metadata", "foo aGVsbG8=,bar d29ybGQ=")
                .withHeader("Upload-Length", "10")))
                .respond(withDefaultProtocolResponseHeaders(new HttpResponse()
                        .withStatusCode(201)
                        .withHeader("Location", mockServerURL + "/foo")));

        Map<String, String> metadata = new LinkedHashMap<String, String>();
        metadata.put("foo", "hello");
        metadata.put("bar", "world");

        TusClient client = new TusClient();
        client.setUploadCreationURL(mockServerURL);
        TusUpload upload = new TusUpload();
        upload.setSize(10);
        upload.setInputStream(new ByteArrayInputStream(new byte[10]));
        upload.setMetadata(metadata);
        TusUploader uploader = client.createUpload(upload);

        assertEquals(uploader.getUploadURL(), new URL(mockServerURL + "/foo"));
    }

    /**
     * Verifies if uploads can be created while sending data in the creation request.
     * @throws IOException if upload data cannot be read.
     * @throws ProtocolException if the upload cannot be constructed.
     */
    @Test
    public void testCreateUploadWithData() throws IOException, ProtocolException {
        byte[] content = new byte[] {
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j'
        };
        mockServer.when(withDefaultProtocolRequestHeaders(new HttpRequest()
                .withMethod("POST")
                .withPath("/files")
                .withHeader("Connection", "keep-alive")
                .withHeader("Content-Type", "application/offset+octet-stream")
                .withHeader("Upload-Length", "10")
                .withBody(Arrays.copyOfRange(content, 0, 4))))
                .respond(withDefaultProtocolResponseHeaders(new HttpResponse()
                        .withStatusCode(201)
                        .withHeader("Location", mockServerURL + "/foo")
                        .withHeader("Upload-Offset", "4")));
        mockServer.when(new HttpRequest()
                .withMethod("POST")
                .withPath("/files/foo"))
                .respond(withDefaultProtocolResponseHeaders(new HttpResponse()
                        .withStatusCode(204)
                        .withHeader("Upload-Offset", "10")));

        TusClient client = new TusClient();
        client.setUploadCreationURL(mockServerURL);
        TusUpload upload = new TusUpload();
        upload.setSize(content.length);
        upload.setInputStream(new ByteArrayInputStream(content));

        TusUploader uploader = client.createUploadWithData(upload, 4);
        assertEquals(uploader.getUploadURL(), new URL(mockServerURL + "/foo"));
        assertEquals(uploader.getOffset(), 4);

        uploader.setChunkSize(6);
        assertEquals(uploader.uploadChunk(), 6);
        uploader.finish();
        assertEquals(uploader.getOffset(), content.length);

        HttpRequest[] patchRequests = mockServer.retrieveRecordedRequests(new HttpRequest()
                .withMethod("POST")
                .withPath("/files/foo"));
        assertEquals(1, patchRequests.length);
        assertTrue(patchRequests[0].containsHeader("X-HTTP-Method-Override"));
        assertArrayEquals(Arrays.copyOfRange(content, 4, 10), patchRequests[0].getBodyAsRawBytes());
    }

    /**
     * Verifies if request lifecycle hooks run around upload creation.
     * @throws IOException if upload data cannot be read.
     * @throws ProtocolException if the upload cannot be constructed.
     */
    @Test
    public void testCreateUploadRequestLifecycleHooks() throws IOException, ProtocolException {
        mockServer.when(withDefaultProtocolRequestHeaders(new HttpRequest()
                .withMethod("POST")
                .withPath("/files")
                .withHeader("X-Hook", "before")
                .withHeader("Upload-Length", "10")))
                .respond(withDefaultProtocolResponseHeaders(new HttpResponse()
                        .withStatusCode(201)
                        .withHeader("Location", mockServerURL + "/foo")));

        final List<String> events = new ArrayList<String>();
        TusClient client = new TusClient();
        client.setUploadCreationURL(mockServerURL);
        client.setRequestLifecycleHooks(new TusRequestLifecycleHooks(
                new TusRequestLifecycleHooks.BeforeRequest() {
                    @Override
                    public void beforeRequest(TusRequestLifecycleHooks.RequestContext context) {
                        events.add("before:" + context.getMethod());
                        context.getConnection().addRequestProperty("X-Hook", "before");
                    }
                },
                new TusRequestLifecycleHooks.AfterResponse() {
                    @Override
                    public void afterResponse(TusRequestLifecycleHooks.RequestContext context) throws IOException {
                        events.add(
                                "after:"
                                        + context.getMethod()
                                        + ":"
                                        + context.getConnection().getResponseCode()
                        );
                    }
                }
        ));
        TusUpload upload = new TusUpload();
        upload.setSize(10);
        upload.setInputStream(new ByteArrayInputStream(new byte[10]));
        TusUploader uploader = client.createUpload(upload);

        assertEquals(uploader.getUploadURL(), new URL(mockServerURL + "/foo"));
        assertEquals("before:POST", events.get(0));
        assertEquals("after:POST:201", events.get(1));
    }

    /**
     * Verifies if uploads can be created with deferred upload length.
     * @throws IOException if upload data cannot be read.
     * @throws ProtocolException if the upload cannot be constructed.
     */
    @Test
    public void testCreateUploadWithDeferredLength() throws IOException, ProtocolException {
        mockServer.when(withDefaultProtocolRequestHeaders(new HttpRequest()
                .withMethod("POST")
                .withPath("/files")
                .withHeader("Upload-Defer-Length", "1")))
                .respond(withDefaultProtocolResponseHeaders(new HttpResponse()
                        .withStatusCode(201)
                        .withHeader("Location", mockServerURL + "/foo")));

        TusClient client = new TusClient();
        client.setUploadCreationURL(mockServerURL);
        TusUpload upload = new TusUpload();
        upload.setSize(10);
        upload.setUploadLengthDeferred(true);
        upload.setInputStream(new ByteArrayInputStream(new byte[10]));
        TusUploader uploader = client.createUpload(upload);
        HttpRequest[] requests = mockServer.retrieveRecordedRequests(new HttpRequest()
                .withMethod("POST")
                .withPath("/files"));

        assertEquals(uploader.getUploadURL(), new URL(mockServerURL + "/foo"));
        assertEquals(1, requests.length);
        assertFalse(requests[0].containsHeader("Upload-Length"));
    }

    /**
     * Verifies if uploads can be created with the tus client through a proxy.
     * @throws IOException if upload data cannot be read.
     * @throws ProtocolException if the upload cannot be constructed.
     */
    @Test
    public void testCreateUploadWithProxy() throws IOException, ProtocolException {
        mockServer.when(withDefaultProtocolRequestHeaders(new HttpRequest()
                .withMethod("POST")
                .withPath("/files")
                .withHeader("Proxy-Connection", "keep-alive")
                .withHeader("Upload-Metadata", "foo aGVsbG8=,bar d29ybGQ=")
                .withHeader("Upload-Length", "11")))
                .respond(withDefaultProtocolResponseHeaders(new HttpResponse()
                        .withStatusCode(201)
                        .withHeader("Location", mockServerURL + "/foo")));

        Map<String, String> metadata = new LinkedHashMap<String, String>();
        metadata.put("foo", "hello");
        metadata.put("bar", "world");

        TusClient client = new TusClient();
        client.setUploadCreationURL(mockServerURL);
        client.setProxy(new Proxy(Type.HTTP, new InetSocketAddress("localhost", mockServer.getPort())));
        TusUpload upload = new TusUpload();
        upload.setSize(11);
        upload.setInputStream(new ByteArrayInputStream(new byte[11]));
        upload.setMetadata(metadata);
        TusUploader uploader = client.createUpload(upload);

        assertEquals(uploader.getUploadURL(), new URL(mockServerURL + "/foo"));
    }

    /**
     * Tests if a missing location header causes an exception as expected.
     * @throws Exception if unreachable code has been reached.
     */
    @Test
    public void testCreateUploadWithMissingLocationHeader() throws Exception {
        mockServer.when(withDefaultProtocolRequestHeaders(new HttpRequest()
                .withMethod("POST")
                .withPath("/files")
                .withHeader("Upload-Length", "10")))
                .respond(withDefaultProtocolResponseHeaders(new HttpResponse()
                        .withStatusCode(201)));

        TusClient client = new TusClient();
        client.setUploadCreationURL(mockServerURL);
        TusUpload upload = new TusUpload();
        upload.setSize(10);
        upload.setInputStream(new ByteArrayInputStream(new byte[10]));
        try {
            TusUploader uploader = client.createUpload(upload);
            throw new Exception("unreachable code reached");
        } catch (ProtocolException e) {
            assertEquals(e.getMessage(), "missing upload URL in response for creating upload");
        }
    }

    /**
     * Tests if create-upload response failures expose detailed request and response context.
     * @throws Exception if unreachable code has been reached.
     */
    @Test
    public void testCreateUploadWithDetailedResponseError() throws Exception {
        mockServer.when(withDefaultProtocolRequestHeaders(new HttpRequest()
                .withMethod("POST")
                .withPath("/files")
                .withHeader("Upload-Length", "10")
                .withHeader("X-Request-ID", "contract-request-id")))
                .respond(withDefaultProtocolResponseHeaders(new HttpResponse()
                        .withStatusCode(500)
                        .withBody("server_error")));

        Map<String, String> headers = new LinkedHashMap<String, String>();
        headers.put("X-Request-ID", "contract-request-id");

        TusClient client = new TusClient();
        client.setHeaders(headers);
        client.setUploadCreationURL(mockServerURL);
        TusUpload upload = new TusUpload();
        upload.setSize(10);
        upload.setInputStream(new ByteArrayInputStream(new byte[10]));
        try {
            client.createUpload(upload);
            throw new Exception("unreachable code reached");
        } catch (TusResponseException error) {
            assertEquals(
                    "tus: unexpected response while creating upload, originated from request "
                            + "(method: POST, url: "
                            + mockServerURL
                            + ", response code: 500, response text: server_error, request id: "
                            + "contract-request-id)",
                    error.getMessage()
            );
            assertNull(error.getCausingError());
            assertEquals("POST", error.getOriginalRequestMethod());
            assertEquals("contract-request-id", error.getOriginalRequestId());
            assertEquals(mockServerURL, error.getOriginalRequestURL());
            assertTrue(error.hasOriginalResponse());
            assertEquals("server_error", error.getOriginalResponseBody());
            assertEquals(500, error.getOriginalResponseStatus());
        }
    }

    /**
     * Tests if create-upload request failures expose detailed request context.
     * @throws Exception if unreachable code has been reached.
     */
    @Test
    public void testCreateUploadWithDetailedRequestError() throws Exception {
        Map<String, String> headers = new LinkedHashMap<String, String>();
        headers.put("X-Request-ID", "contract-request-id");

        TusClient client = new TusClient() {
            @Override
            protected HttpURLConnection openConnection(URL uploadURL) {
                return new FailingHttpURLConnection(uploadURL, "socket down");
            }
        };
        client.setHeaders(headers);
        client.setUploadCreationURL(mockServerURL);
        TusUpload upload = new TusUpload();
        upload.setSize(10);
        upload.setInputStream(new ByteArrayInputStream(new byte[10]));
        try {
            client.createUpload(upload);
            throw new Exception("unreachable code reached");
        } catch (TusRequestException error) {
            assertEquals(
                    "tus: failed to create upload, caused by Error: socket down, "
                            + "originated from request (method: POST, url: "
                            + mockServerURL
                            + ", response code: n/a, response text: n/a, request id: "
                            + "contract-request-id)",
                    error.getMessage()
            );
            assertEquals("socket down", error.getCausingError().getMessage());
            assertEquals("POST", error.getOriginalRequestMethod());
            assertEquals("contract-request-id", error.getOriginalRequestId());
            assertEquals(mockServerURL, error.getOriginalRequestURL());
            assertFalse(error.hasOriginalResponse());
            assertEquals(TusProtocol.DETAILED_ERROR_MISSING_VALUE, error.getOriginalResponseBody());
            assertEquals(-1, error.getOriginalResponseStatus());
        }
    }

    /**
     * Tests if start option validation rejects parallel uploads with an upload URL.
     * @throws MalformedURLException if the provided URL is malformed.
     */
    @Test
    public void testValidateStartOptionsRejectsParallelUploadsWithUploadURL()
            throws MalformedURLException {
        TusStartOptions options = validStartOptions();
        options.setParallelUploads(TusProtocol.MINIMUM_PARALLEL_UPLOADS);
        options.setUploadURL(new URL("https://tus.io/uploads/start-validation-upload-url"));

        try {
            new TusClient().validateStartOptions(options);
            fail("start option validation unexpectedly succeeded");
        } catch (IllegalArgumentException error) {
            assertEquals(
                    TusProtocol.START_OPTION_VALIDATION_PARALLEL_UPLOADS_WITH_UPLOAD_URL,
                    error.getMessage()
            );
        }
    }

    /**
     * Tests if start option validation rejects parallel uploads with creation data.
     * @throws MalformedURLException if the provided URL is malformed.
     */
    @Test
    public void testValidateStartOptionsRejectsParallelUploadsWithUploadDataDuringCreation()
            throws MalformedURLException {
        TusStartOptions options = validStartOptions();
        options.setParallelUploads(TusProtocol.MINIMUM_PARALLEL_UPLOADS);
        options.setUploadDataDuringCreation(true);

        try {
            new TusClient().validateStartOptions(options);
            fail("start option validation unexpectedly succeeded");
        } catch (IllegalArgumentException error) {
            assertEquals(
                    TusProtocol
                            .START_OPTION_VALIDATION_PARALLEL_UPLOADS_WITH_UPLOAD_DATA_DURING_CREATION,
                    error.getMessage()
            );
        }
    }

    /**
     * Tests if uploads with relative upload destinations are working.
     * @throws Exception
     */
    @Test
    public void testCreateUploadWithRelativeLocation() throws Exception {
        // We need to enable strict following for POST requests first
        System.setProperty("http.strictPostRedirect", "true");

        // Attempt a real redirect
        mockServer.when(withDefaultProtocolRequestHeaders(new HttpRequest()
                .withMethod("POST")
                .withPath("/filesRedirect")
                .withHeader("Upload-Length", "10")))
                .respond(new HttpResponse()
                        .withStatusCode(301)
                        .withHeader("Location", mockServerURL + "Redirected/"));

        mockServer.when(withDefaultProtocolRequestHeaders(new HttpRequest()
                .withMethod("POST")
                .withPath("/filesRedirected/")
                .withHeader("Upload-Length", "10")))
                .respond(withDefaultProtocolResponseHeaders(new HttpResponse()
                        .withStatusCode(201)
                        .withHeader("Location", "foo")));

        TusClient client = new TusClient();
        client.setUploadCreationURL(new URL(mockServerURL + "Redirect"));
        TusUpload upload = new TusUpload();
        upload.setSize(10);
        upload.setInputStream(new ByteArrayInputStream(new byte[10]));
        TusUploader uploader = client.createUpload(upload);

        // The upload URL must be relative to the URL of the request by which it was returned,
        // not the upload creation URL. In most cases, there is no difference between those two,
        // but it's still important to be correct here.
        assertEquals(uploader.getUploadURL(), new URL(mockServerURL + "Redirected/foo"));
    }

    /**
     * Tests if {@link TusClient#resumeUpload(TusUpload)} works.
     * @throws ResumingNotEnabledException
     * @throws FingerprintNotFoundException
     * @throws IOException
     * @throws ProtocolException
     */
    @Test
    public void testResumeUpload() throws ResumingNotEnabledException, FingerprintNotFoundException, IOException,
            ProtocolException {
        mockServer.when(withDefaultProtocolRequestHeaders(new HttpRequest()
                .withMethod("HEAD")
                .withPath("/files/foo")))
                .respond(withDefaultProtocolResponseHeaders(new HttpResponse()
                        .withStatusCode(204)
                        .withHeader("Upload-Offset", "3")));

        TusClient client = new TusClient();
        client.setUploadCreationURL(mockServerURL);
        client.enableResuming(new TestResumeUploadStore());

        TusUpload upload = new TusUpload();
        upload.setSize(10);
        upload.setInputStream(new ByteArrayInputStream(new byte[10]));
        upload.setFingerprint("test-fingerprint");

        TusUploader uploader = client.resumeUpload(upload);

        assertEquals(uploader.getUploadURL(), new URL(mockServerURL.toString() + "/foo"));
        assertEquals(uploader.getOffset(), 3);
    }

    /**
     * Verifies if request lifecycle hooks run around offset discovery.
     * @throws ResumingNotEnabledException if resuming is disabled.
     * @throws FingerprintNotFoundException if the stored URL is missing.
     * @throws IOException if the request cannot be issued.
     * @throws ProtocolException if the upload cannot be resumed.
     */
    @Test
    public void testResumeUploadRequestLifecycleHooks() throws ResumingNotEnabledException,
            FingerprintNotFoundException, IOException, ProtocolException {
        mockServer.when(withDefaultProtocolRequestHeaders(new HttpRequest()
                .withMethod("HEAD")
                .withPath("/files/foo")
                .withHeader("X-Hook", "before")))
                .respond(withDefaultProtocolResponseHeaders(new HttpResponse()
                        .withStatusCode(204)
                        .withHeader("Upload-Offset", "3")));

        final List<String> events = new ArrayList<String>();
        TusClient client = new TusClient();
        client.setUploadCreationURL(mockServerURL);
        client.enableResuming(new TestResumeUploadStore());
        client.setRequestLifecycleHooks(new TusRequestLifecycleHooks(
                new TusRequestLifecycleHooks.BeforeRequest() {
                    @Override
                    public void beforeRequest(TusRequestLifecycleHooks.RequestContext context) {
                        events.add("before:" + context.getMethod());
                        context.getConnection().addRequestProperty("X-Hook", "before");
                    }
                },
                new TusRequestLifecycleHooks.AfterResponse() {
                    @Override
                    public void afterResponse(TusRequestLifecycleHooks.RequestContext context) throws IOException {
                        events.add(
                                "after:"
                                        + context.getMethod()
                                        + ":"
                                        + context.getConnection().getResponseCode()
                        );
                    }
                }
        ));

        TusUpload upload = new TusUpload();
        upload.setSize(10);
        upload.setInputStream(new ByteArrayInputStream(new byte[10]));
        upload.setFingerprint("test-fingerprint");

        TusUploader uploader = client.resumeUpload(upload);

        assertEquals(uploader.getUploadURL(), new URL(mockServerURL.toString() + "/foo"));
        assertEquals(uploader.getOffset(), 3);
        assertEquals("before:HEAD", events.get(0));
        assertEquals("after:HEAD:204", events.get(1));
    }

    /**
     * Test Implementation for a {@link TusURLStore}.
     */
    private final class TestResumeUploadStore implements TusURLStore {
        public void set(String fingerprint, URL url) {
            fail("set method must not be called");
        }

        public URL get(String fingerprint) {
            assertEquals(fingerprint, "test-fingerprint");

            try {
                return new URL(mockServerURL.toString() + "/foo");
            } catch (Exception ignored) { }
            return null;
        }

        public void remove(String fingerprint) {
            fail("remove method must not be called");
        }
    }

    /**
     * Tests if an upload gets started if {@link TusClient#resumeOrCreateUpload(TusUpload)} gets called.
     * @throws IOException
     * @throws ProtocolException
     */
    @Test
    public void testResumeOrCreateUpload() throws IOException, ProtocolException {
        mockServer.when(withDefaultProtocolRequestHeaders(new HttpRequest()
                .withMethod("POST")
                .withPath("/files")
                .withHeader("Connection", "keep-alive")
                .withHeader("Upload-Length", "10")))
                .respond(withDefaultProtocolResponseHeaders(new HttpResponse()
                        .withStatusCode(201)
                        .withHeader("Location", mockServerURL + "/foo")));

        TusClient client = new TusClient();
        client.setUploadCreationURL(mockServerURL);
        TusUpload upload = new TusUpload();
        upload.setSize(10);
        upload.setInputStream(new ByteArrayInputStream(new byte[10]));
        TusUploader uploader = client.resumeOrCreateUpload(upload);

        assertEquals(uploader.getUploadURL(), new URL(mockServerURL + "/foo"));
    }

    /**
     * Tests if an upload gets started when {@link TusClient#resumeOrCreateUpload(TusUpload)} gets called with
     * a proxy set.
     * @throws IOException
     * @throws ProtocolException
     */
    @Test
    public void testResumeOrCreateUploadWithProxy() throws IOException, ProtocolException {
        mockServer.when(withDefaultProtocolRequestHeaders(new HttpRequest()
                .withMethod("POST")
                .withPath("/files")
                .withHeader("Proxy-Connection", "keep-alive")
                .withHeader("Upload-Length", "11")))
                .respond(withDefaultProtocolResponseHeaders(new HttpResponse()
                        .withStatusCode(201)
                        .withHeader("Location", mockServerURL + "/foo")));

        TusClient client = new TusClient();
        client.setUploadCreationURL(mockServerURL);
        Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress("localhost", mockServer.getPort()));
        client.setProxy(proxy);
        TusUpload upload = new TusUpload();
        upload.setSize(11);
        upload.setInputStream(new ByteArrayInputStream(new byte[11]));
        TusUploader uploader = client.resumeOrCreateUpload(upload);

        assertEquals(proxy, client.getProxy());
        assertEquals(uploader.getUploadURL(), new URL(mockServerURL + "/foo"));
    }

    /**
     * Checks if a new upload attempt is started in case of a serverside 404-error, without having an Exception thrown.
     * @throws IOException
     * @throws ProtocolException
     */
    @Test
    public void testResumeOrCreateUploadNotFound() throws IOException, ProtocolException {
        mockServer.when(withDefaultProtocolRequestHeaders(new HttpRequest()
                .withMethod("HEAD")
                .withPath("/files/not_found")))
                .respond(new HttpResponse()
                        .withStatusCode(404));

        mockServer.when(withDefaultProtocolRequestHeaders(new HttpRequest()
                .withMethod("POST")
                .withPath("/files")
                .withHeader("Upload-Length", "10")))
                .respond(withDefaultProtocolResponseHeaders(new HttpResponse()
                        .withStatusCode(201)
                        .withHeader("Location", mockServerURL + "/foo")));

        TusClient client = new TusClient();
        client.setUploadCreationURL(mockServerURL);

        TusURLStore store = new TusURLMemoryStore();
        store.set("fingerprint", new URL(mockServerURL + "/not_found"));
        client.enableResuming(store);

        TusUpload upload = new TusUpload();
        upload.setSize(10);
        upload.setInputStream(new ByteArrayInputStream(new byte[10]));
        upload.setFingerprint("fingerprint");
        TusUploader uploader = client.resumeOrCreateUpload(upload);

        assertEquals(uploader.getUploadURL(), new URL(mockServerURL + "/foo"));
    }

    /**
     * Tests if {@link TusClient#beginOrResumeUploadFromURL(TusUpload, URL)} works.
     * @throws IOException
     * @throws ProtocolException
     */
    @Test
    public void testBeginOrResumeUploadFromURL() throws IOException, ProtocolException {
        mockServer.when(withDefaultProtocolRequestHeaders(new HttpRequest()
                .withMethod("HEAD")
                .withPath("/files/fooFromURL")))
                .respond(withDefaultProtocolResponseHeaders(new HttpResponse()
                        .withStatusCode(204)
                        .withHeader("Upload-Offset", "3")));

        TusClient client = new TusClient();
        URL uploadURL = new URL(mockServerURL.toString() + "/fooFromURL");

        TusUpload upload = new TusUpload();
        upload.setSize(10);
        upload.setInputStream(new ByteArrayInputStream(new byte[10]));

        TusUploader uploader = client.beginOrResumeUploadFromURL(upload, uploadURL);

        assertEquals(uploader.getUploadURL(), uploadURL);
        assertEquals(uploader.getOffset(), 3);
    }

    /**
     * Tests if connections are prepared correctly, which means all header are getting set.
     * @throws IOException
     */
    @Test
    public void testPrepareConnection() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) mockServerURL.openConnection();
        TusClient client = new TusClient();
        client.prepareConnection(connection);

        for (Map.Entry<String, String> entry : TusProtocol.DEFAULT_REQUEST_HEADERS.entrySet()) {
            assertEquals(entry.getValue(), connection.getRequestProperty(entry.getKey()));
        }
    }

    /**
     * Tests if HTTP - Headers are set correctly.
     * @throws IOException
     */
    @Test
    public void testSetHeaders() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) mockServerURL.openConnection();
        TusClient client = new TusClient();

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Greeting", "Hello");
        headers.put("Important", "yes");
        headers.put("Tus-Resumable", "evil");

        assertNull(client.getHeaders());
        client.setHeaders(headers);
        assertEquals(headers, client.getHeaders());

        client.prepareConnection(connection);

        assertEquals(connection.getRequestProperty("Greeting"), "Hello");
        assertEquals(connection.getRequestProperty("Important"), "yes");
    }

    /**
     * Tests if connection timeouts are set correctly.
     * @throws IOException
     */
    @Test
    public void testSetConnectionTimeout() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) mockServerURL.openConnection();
        TusClient client = new TusClient();

        assertEquals(client.getConnectTimeout(), 5000);
        client.setConnectTimeout(3000);
        assertEquals(client.getConnectTimeout(), 3000);

        client.prepareConnection(connection);

        assertEquals(connection.getConnectTimeout(), 3000);
    }

    /**
     * Tests whether the connection follows redirects only after explicitly enabling this feature.
     * @throws Exception
     */
    @Test
    public void testFollowRedirects() throws Exception {
        HttpURLConnection connection = (HttpURLConnection) mockServerURL.openConnection();
        TusClient client = new TusClient();

        // Should not follow by default
        client.prepareConnection(connection);
        assertFalse(connection.getInstanceFollowRedirects());

        // Only follow if we enable strict redirects
        System.setProperty("http.strictPostRedirect", "true");
        client.prepareConnection(connection);
        assertTrue(connection.getInstanceFollowRedirects());

        // Attempt a real redirect
        mockServer.when(withDefaultProtocolRequestHeaders(new HttpRequest()
                .withMethod("POST")
                .withPath("/filesRedirect")
                .withHeader("Upload-Length", "10")))
                .respond(new HttpResponse()
                        .withStatusCode(301)
                        .withHeader("Location", mockServerURL + "Redirected"));

        mockServer.when(withDefaultProtocolRequestHeaders(new HttpRequest()
                .withMethod("POST")
                .withPath("/filesRedirected")
                .withHeader("Upload-Length", "10")))
                .respond(withDefaultProtocolResponseHeaders(new HttpResponse()
                        .withStatusCode(201)
                        .withHeader("Location", mockServerURL + "/foo")));

        client.setUploadCreationURL(new URL(mockServerURL + "Redirect"));
        TusUpload upload = new TusUpload();
        upload.setSize(10);
        upload.setInputStream(new ByteArrayInputStream(new byte[10]));
        TusUploader uploader = client.createUpload(upload);

        assertEquals(uploader.getUploadURL(), new URL(mockServerURL + "/foo"));
    }

    /**
     * Tests if the fingerprint in the {@link TusURLStore} does not get removed after upload success.
     * @throws IOException
     * @throws ProtocolException
     */
    @Test
    public void testRemoveFingerprintOnSuccessDisabled() throws IOException, ProtocolException {

        TusClient client = new TusClient();

        TusURLStore store = new TusURLMemoryStore();
        URL dummyURL = new URL("http://dummy-url/files/dummy");
        store.set("fingerprint", dummyURL);
        client.enableResuming(store);

        assertFalse(client.removeFingerprintOnSuccessEnabled());

        TusUpload upload = new TusUpload();
        upload.setFingerprint("fingerprint");

        client.uploadFinished(upload);

        assertEquals(dummyURL, store.get("fingerprint"));

    }

    /**
     * Tests if the fingerprint in the {@link TusURLStore} does get removed after upload success,
     * after this feature has been enabled via the {@link TusClient#enableRemoveFingerprintOnSuccess()} - method.
     * @throws IOException
     * @throws ProtocolException
     */
    @Test
    public void testRemoveFingerprintOnSuccessEnabled() throws IOException, ProtocolException {

        TusClient client = new TusClient();

        TusURLStore store = new TusURLMemoryStore();
        URL dummyURL = new URL("http://dummy-url/files/dummy");
        store.set("fingerprint", dummyURL);
        client.enableResuming(store);
        client.enableRemoveFingerprintOnSuccess();

        assertTrue(client.removeFingerprintOnSuccessEnabled());

        TusUpload upload = new TusUpload();
        upload.setFingerprint("fingerprint");

        client.uploadFinished(upload);

        assertNull(store.get("fingerprint"));

    }

    /**
     * A mocked HttpURLConnection which fails while connecting.
     */
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

    private static TusStartOptions validStartOptions() throws MalformedURLException {
        TusUpload upload = new TusUpload();
        upload.setSize(11);
        upload.setInputStream(new ByteArrayInputStream(new byte[11]));

        TusStartOptions options = new TusStartOptions();
        options.setEndpointURL(new URL("https://tus.io/uploads"));
        options.setUpload(upload);
        return options;
    }
}
