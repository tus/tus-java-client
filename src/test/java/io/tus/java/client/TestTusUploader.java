package io.tus.java.client;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.socket.PortFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;

public class TestTusUploader extends TestCase {
    private MockServerClient mockServer;
    public URL mockServerURL;

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        int port = PortFactory.findFreePort();
        mockServerURL = new URL("http://localhost:" + port + "/files");
        mockServer = startClientAndServer(port);
    }

    @After
    protected void tearDown() {
        mockServer.stop();
    }

    public void testTusUploader() throws IOException, ProtocolException {
        byte[] content = "hello world".getBytes();

        mockServer.when(new HttpRequest()
                .withPath("/files/foo")
                .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                .withHeader("Upload-Offset", "3")
                .withBody(Arrays.copyOfRange(content, 3, 11)))
                .respond(new HttpResponse()
                        .withStatusCode(204)
                        .withHeader("Tus-Resumable", TusClient.TUS_VERSION));

        TusClient client = new TusClient();
        URL uploadUrl = new URL(mockServerURL + "/foo");
        InputStream input = new ByteArrayInputStream(content);
        long offset = 3;

        TusUploader uploader = new TusUploader(client, uploadUrl, input, offset);
        assertEquals(5, uploader.uploadChunk(5));
        assertEquals(3, uploader.uploadChunk(5));
        assertEquals(-1, uploader.uploadChunk(5));
        assertEquals(11, uploader.getOffset());
        uploader.finish();
    }
}
