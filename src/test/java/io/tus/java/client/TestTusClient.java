package io.tus.java.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;


public class TestTusClient extends MockServerProvider {
    @Test
    public void testTusClient() {
        TusClient client = new TusClient();
        assertEquals(client.getUploadCreationURL(), null);
    }

    @Test
    public void testTusClientURL() throws MalformedURLException {
        TusClient client = new TusClient();
        client.setUploadCreationURL(creationUrl);
        assertEquals(client.getUploadCreationURL(), creationUrl);
    }

    @Test
    public void testSetUploadCreationURL() throws MalformedURLException {
        TusClient client = new TusClient();
        client.setUploadCreationURL(new URL("http://tusd.tusdemo.net"));
        assertEquals(client.getUploadCreationURL(), new URL("http://tusd.tusdemo.net"));
    }

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

    @Test
    public void testCreateUpload() throws IOException, ProtocolException {
        mockServer.when(new HttpRequest()
                .withMethod("POST")
                .withPath("/files")
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

    @Test
    public void testCreateUploadWithMissingLocationHeader() throws IOException, Exception {
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
        } catch(ProtocolException e) {
            assertEquals(e.getMessage(), "missing upload URL in response for creating upload");
        }
    }

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

        // The upload URL must be relative to the URL of the request by which is was returned,
        // not the upload creation URL. In most cases, there is no difference between those two
        // but it's still important to be correct here.
        assertEquals(uploader.getUploadURL(), new URL(mockServerURL + "Redirected/foo"));
    }

    @Test
    public void testResumeUpload() throws ResumingNotEnabledException, FingerprintNotFoundException, IOException, ProtocolException {
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

    private class TestResumeUploadStore implements TusURLStore {
        public void set(String fingerprint, URL url) {
            assertTrue("set method must not be called", false);
        }

        public URL get(String fingerprint) {
            assertEquals(fingerprint, "test-fingerprint");

            try {
                return new URL(mockServerURL.toString() + "/foo");
            } catch(Exception e) {}
            return null;
        }

        public void remove(String fingerprint) {
            assertTrue("remove method must not be called", false);
        }
    }

    @Test
    public void testResumeOrCreateUpload() throws IOException, ProtocolException {
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
        TusUpload upload = new TusUpload();
        upload.setSize(10);
        upload.setInputStream(new ByteArrayInputStream(new byte[10]));
        TusUploader uploader = client.resumeOrCreateUpload(upload);

        assertEquals(uploader.getUploadURL(), new URL(mockServerURL + "/foo"));
    }

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

    @Test
    public void testPrepareConnection() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) mockServerURL.openConnection();
        TusClient client = new TusClient();
        client.prepareConnection(connection);

        assertEquals(connection.getRequestProperty("Tus-Resumable"), TusClient.TUS_VERSION);
    }

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
