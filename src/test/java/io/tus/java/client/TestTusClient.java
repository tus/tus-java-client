package io.tus.java.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

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
        assertEquals(client.getUploadCreationURL(), null);
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
        assertEquals(client.resumingEnabled(), false);

        TusURLStore store = new TusURLMemoryStore();
        client.enableResuming(store);
        assertEquals(client.resumingEnabled(), true);

        client.disableResuming();
        assertEquals(client.resumingEnabled(), false);
    }

    /**
     * Verifies if uploads can be created with the tus client.
     * @throws IOException if upload data cannot be read.
     * @throws ProtocolException if the upload cannot be constructed.
     */
    @Test
    public void testCreateUpload() throws IOException, ProtocolException {
        mockServer.when(new HttpRequest()
                .withMethod("POST")
                .withPath("/files")
                .withHeader("Connection", "keep-alive")
                .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                .withHeader("Upload-Metadata", "foo aGVsbG8=,bar d29ybGQ=")
                .withHeader("Upload-Length", "10"))
                .respond(new HttpResponse()
                        .withStatusCode(201)
                        .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                        .withHeader("Location", mockServerURL + "/foo"));

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
     * Verifies if uploads can be created with the tus client through a proxy.
     * @throws IOException if upload data cannot be read.
     * @throws ProtocolException if the upload cannot be constructed.
     */
    @Test
    public void testCreateUploadWithProxy() throws IOException, ProtocolException {
        mockServer.when(new HttpRequest()
                .withMethod("POST")
                .withPath("/files")
                .withHeader("Proxy-Connection", "keep-alive")
                .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                .withHeader("Upload-Metadata", "foo aGVsbG8=,bar d29ybGQ=")
                .withHeader("Upload-Length", "11"))
                .respond(new HttpResponse()
                        .withStatusCode(201)
                        .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                        .withHeader("Location", mockServerURL + "/foo"));

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
        mockServer.when(new HttpRequest()
                .withMethod("POST")
                .withPath("/files")
                .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                .withHeader("Upload-Length", "10"))
                .respond(new HttpResponse()
                        .withStatusCode(201)
                        .withHeader("Tus-Resumable", TusClient.TUS_VERSION));

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
     * Tests if uploads with relative upload destinations are working.
     * @throws Exception
     */
    @Test
    public void testCreateUploadWithRelativeLocation() throws Exception {
        // We need to enable strict following for POST requests first
        System.setProperty("http.strictPostRedirect", "true");

        // Attempt a real redirect
        mockServer.when(new HttpRequest()
                .withMethod("POST")
                .withPath("/filesRedirect")
                .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                .withHeader("Upload-Length", "10"))
                .respond(new HttpResponse()
                        .withStatusCode(301)
                        .withHeader("Location", mockServerURL + "Redirected/"));

        mockServer.when(new HttpRequest()
                .withMethod("POST")
                .withPath("/filesRedirected/")
                .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                .withHeader("Upload-Length", "10"))
                .respond(new HttpResponse()
                        .withStatusCode(201)
                        .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                        .withHeader("Location", "foo"));

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
        mockServer.when(new HttpRequest()
                .withMethod("HEAD")
                .withPath("/files/foo")
                .withHeader("Tus-Resumable", TusClient.TUS_VERSION))
                .respond(new HttpResponse()
                        .withStatusCode(204)
                        .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                        .withHeader("Upload-Offset", "3"));

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
     * Test Implementation for a {@link TusURLStore}.
     */
    private class TestResumeUploadStore implements TusURLStore {
        public void set(String fingerprint, URL url) {
            assertTrue("set method must not be called", false);
        }

        public URL get(String fingerprint) {
            assertEquals(fingerprint, "test-fingerprint");

            try {
                return new URL(mockServerURL.toString() + "/foo");
            } catch (Exception e) { }
            return null;
        }

        public void remove(String fingerprint) {
            assertTrue("remove method must not be called", false);
        }
    }

    /**
     * Tests if an upload gets started if {@link TusClient#resumeOrCreateUpload(TusUpload)} gets called.
     * @throws IOException
     * @throws ProtocolException
     */
    @Test
    public void testResumeOrCreateUpload() throws IOException, ProtocolException {
        mockServer.when(new HttpRequest()
                .withMethod("POST")
                .withPath("/files")
                .withHeader("Connection", "keep-alive")
                .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                .withHeader("Upload-Length", "10"))
                .respond(new HttpResponse()
                        .withStatusCode(201)
                        .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                        .withHeader("Location", mockServerURL + "/foo"));

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
        mockServer.when(new HttpRequest()
                .withMethod("POST")
                .withPath("/files")
                .withHeader("Proxy-Connection", "keep-alive")
                .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                .withHeader("Upload-Length", "11"))
                .respond(new HttpResponse()
                        .withStatusCode(201)
                        .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                        .withHeader("Location", mockServerURL + "/foo"));

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
        mockServer.when(new HttpRequest()
                .withMethod("HEAD")
                .withPath("/files/not_found")
                .withHeader("Tus-Resumable", TusClient.TUS_VERSION))
                .respond(new HttpResponse()
                        .withStatusCode(404));

        mockServer.when(new HttpRequest()
                .withMethod("POST")
                .withPath("/files")
                .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                .withHeader("Upload-Length", "10"))
                .respond(new HttpResponse()
                        .withStatusCode(201)
                        .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                        .withHeader("Location", mockServerURL + "/foo"));

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
        mockServer.when(new HttpRequest()
                .withMethod("HEAD")
                .withPath("/files/fooFromURL")
                .withHeader("Tus-Resumable", TusClient.TUS_VERSION))
                .respond(new HttpResponse()
                        .withStatusCode(204)
                        .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                        .withHeader("Upload-Offset", "3"));

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

        assertEquals(connection.getRequestProperty("Tus-Resumable"), TusClient.TUS_VERSION);
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
        mockServer.when(new HttpRequest()
                .withMethod("POST")
                .withPath("/filesRedirect")
                .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                .withHeader("Upload-Length", "10"))
                .respond(new HttpResponse()
                        .withStatusCode(301)
                        .withHeader("Location", mockServerURL + "Redirected"));

        mockServer.when(new HttpRequest()
                .withMethod("POST")
                .withPath("/filesRedirected")
                .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                .withHeader("Upload-Length", "10"))
                .respond(new HttpResponse()
                        .withStatusCode(201)
                        .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                        .withHeader("Location", mockServerURL + "/foo"));

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

        assertTrue(!client.removeFingerprintOnSuccessEnabled());

        TusUpload upload = new TusUpload();
        upload.setFingerprint("fingerprint");

        client.uploadFinished(upload);

        assertTrue(dummyURL.equals(store.get("fingerprint")));

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

        assertTrue(store.get("fingerprint") == null);

    }
}
