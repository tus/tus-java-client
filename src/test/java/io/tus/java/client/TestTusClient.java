package io.tus.java.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.socket.PortFactory;

import junit.framework.TestCase;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;

public class TestTusClient extends TestCase {
	private MockServerClient mockServer;
    public URL mockServerURL;
	
	static URL creationUrl;
	
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		creationUrl = new URL("http://master.tus.io");
        int port = PortFactory.findFreePort();
        mockServerURL = new URL("http://localhost:" + port + "/files");
        mockServer = startClientAndServer(port);
	}

    @After
    protected void tearDown() {
        mockServer.stop();
    }

	@Test
	public void testTusClient() {
		TusClient client = new TusClient();
		assertEquals(client.getUploadCreationURL(), null);
	}

	@Test
	public void testTusClientURL() throws MalformedURLException {
		TusClient client = new TusClient(creationUrl);
		assertEquals(client.getUploadCreationURL(), creationUrl);
	}

	@Test
	public void testSetUploadCreationURL() throws MalformedURLException {
		TusClient client = new TusClient();
		client.setUploadCreationURL(new URL("http://master.tus.io"));
		assertEquals(client.getUploadCreationURL(), new URL("http://master.tus.io"));
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
            .withHeader("Upload-Length", "10"))
        .respond(new HttpResponse()
                .withStatusCode(201)
                .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                .withHeader("Location", "http://master.tus.io/files/foo"));

		TusClient client = new TusClient(mockServerURL);
	    TusUpload upload = new TusUpload();
        upload.setSize(10);
        upload.setInputStream(new ByteArrayInputStream(new byte[10]));
        TusUploader uploader = client.createUpload(upload);

        assertEquals(uploader.getUploadURL(), new URL("http://master.tus.io/files/foo"));
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

		TusClient client = new TusClient(mockServerURL);
        client.enableResuming(new TestResumeUploadStore(this));

		TusUpload upload = new TusUpload();
		upload.setSize(10);
		upload.setInputStream(new ByteArrayInputStream(new byte[10]));
		upload.setFingerprint("test-fingerprint");

		TusUploader uploader = client.resumeUpload(upload);

		assertEquals(uploader.getUploadURL(), new URL(mockServerURL.toString() + "/foo"));
		assertEquals(uploader.getOffset(), 3);
    }

	private class TestResumeUploadStore implements TusURLStore {
		private TestTusClient testCase;

		public TestResumeUploadStore(TestTusClient testCase) {
			this.testCase = testCase;
		}

		public void set(String fingerprint, URL url) {

		}

		public URL get(String fingerprint) {
			testCase.assertEquals(fingerprint, "test-fingerprint");

			try {
				return new URL(testCase.mockServerURL.toString() + "/foo");
			} catch(Exception e) {}
			return null;
		}

		public void remove(String fingerprint) {

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
                        .withHeader("Location", "http://master.tus.io/files/foo"));

        TusClient client = new TusClient(mockServerURL);
        TusUpload upload = new TusUpload();
        upload.setSize(10);
        upload.setInputStream(new ByteArrayInputStream(new byte[10]));
        TusUploader uploader = client.resumeOrCreateUpload(upload);

        assertEquals(uploader.getUploadURL(), new URL("http://master.tus.io/files/foo"));
	}

	@Test
	public void testPrepareConnection() throws IOException {
        HttpURLConnection connection = (HttpURLConnection) mockServerURL.openConnection();
        TusClient client = new TusClient();
        client.prepareConnection(connection);

        assertEquals(connection.getRequestProperty("Tus-Resumable"), TusClient.TUS_VERSION);
	}
}
