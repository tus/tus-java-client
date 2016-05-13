package io.tus.java.client;

import org.junit.After;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.socket.PortFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.junit.Assert.*;

public class TestTusUploader{
    private MockServerClient mockServer;
    private URL mockServerURL;

    private boolean isOpenJDK6 = System.getProperty("java.version").startsWith("1.6") &&
            System.getProperty("java.vm.name").contains("OpenJDK");

    @Before
    public void setUp() throws Exception {
        int port = PortFactory.findFreePort();
        mockServerURL = new URL("http://localhost:" + port + "/files");
        mockServer = startClientAndServer(port);
    }

    @After
    public void tearDown() {
        mockServer.stop();
    }

    @Test
    public void testTusUploader() throws IOException, ProtocolException {
        byte[] content = "hello world".getBytes();

        mockServer.when(new HttpRequest()
                .withPath("/files/foo")
                .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                .withHeader("Upload-Offset", "3")
                .withHeader("Content-Type", "application/offset+octet-stream")
                .withHeader(isOpenJDK6 ? "": "Expect: 100-continue")
                .withBody(Arrays.copyOfRange(content, 3, 11)))
                .respond(new HttpResponse()
                        .withStatusCode(204)
                        .withHeader("Tus-Resumable", TusClient.TUS_VERSION));

        TusClient client = new TusClient();
        URL uploadUrl = new URL(mockServerURL + "/foo");
        InputStream input = new ByteArrayInputStream(content);
        long offset = 3;

        TusUploader uploader = new TusUploader(client, uploadUrl, input, offset);

        uploader.setChunkSize(5);
        assertEquals(uploader.getChunkSize(), 5);

        assertEquals(5, uploader.uploadChunk());
        assertEquals(3, uploader.uploadChunk(5));
        assertEquals(-1, uploader.uploadChunk());
        assertEquals(-1, uploader.uploadChunk(5));
        assertEquals(11, uploader.getOffset());
        uploader.finish();
    }

    @Test
    public void testTusUploaderFailedExpectation() throws IOException, ProtocolException {
        Assume.assumeFalse(isOpenJDK6);

        FailingExpectationServer server = new FailingExpectationServer();
        server.start();

        byte[] content = "hello world".getBytes();

        TusClient client = new TusClient();
        URL uploadUrl = new URL(server.getURL() + "/expect");
        InputStream input = new ByteArrayInputStream(content);
        long offset = 3;

        boolean exceptionThrown = false;
        TusUploader uploader = new TusUploader(client, uploadUrl, input, offset);
        try {
            uploader.uploadChunk();
        } catch(ProtocolException e) {
            assertTrue(e.getMessage().contains("500"));
            exceptionThrown = true;
        } finally {
            assertTrue(exceptionThrown);
        }
    }

    /**
     * FailingExpectationServer is a HTTP/1.1 server which will always respond with a 500 Internal
     * Error. This is meant to simulate failing expectations when the request contains the
     * Expect header. The org.mockserver packages do not support this and will always send the
     * 100 Continue status code. therefore we built our own stupid mocking server.
     */
    private class FailingExpectationServer extends Thread {
        private final byte[] response = "HTTP/1.1 500 Internal Server Error\r\n\r\n".getBytes();
        private ServerSocket serverSocket;
        private int port;

        public FailingExpectationServer() throws IOException {
            port = PortFactory.findFreePort();

            serverSocket = new ServerSocket(port);
        }

        @Override
        public void run() {
            try {
                Socket socket = serverSocket.accept();

                OutputStream output = socket.getOutputStream();
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                while (!input.readLine().isEmpty()) {
                    output.write(response);
                    break;
                }

                socket.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        public URL getURL() {
            try {
                return new URL("http://localhost:" + port);
            } catch(MalformedURLException e) {
                return null;
            }
        }
    }

    @Test
    public void testSetRequestPayloadSize() throws Exception {
        byte[] content = "hello world".getBytes();

        mockServer.when(new HttpRequest()
                .withPath("/files/payload")
                .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                .withHeader("Upload-Offset", "0")
                .withHeader("Content-Type", "application/offset+octet-stream")
                .withBody(Arrays.copyOfRange(content, 0, 5)))
                .respond(new HttpResponse()
                        .withStatusCode(204)
                        .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                        .withHeader("Upload-Offset", "5"));

        mockServer.when(new HttpRequest()
                .withPath("/files/payload")
                .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                .withHeader("Upload-Offset", "5")
                .withHeader("Content-Type", "application/offset+octet-stream")
                .withBody(Arrays.copyOfRange(content, 5, 10)))
                .respond(new HttpResponse()
                        .withStatusCode(204)
                        .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                        .withHeader("Upload-Offset", "10"));

        mockServer.when(new HttpRequest()
                .withPath("/files/payload")
                .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                .withHeader("Upload-Offset", "10")
                .withHeader("Content-Type", "application/offset+octet-stream")
                .withBody(Arrays.copyOfRange(content, 10, 11)))
                .respond(new HttpResponse()
                        .withStatusCode(204)
                        .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                        .withHeader("Upload-Offset", "11"));

        TusClient client = new TusClient();
        URL uploadUrl = new URL(mockServerURL + "/payload");
        InputStream input = new ByteArrayInputStream(content);

        TusUploader uploader = new TusUploader(client, uploadUrl, input, 0);

        assertEquals(uploader.getRequestPayloadSize(), 1024 * 1024);
        uploader.setRequestPayloadSize(5);
        assertEquals(uploader.getRequestPayloadSize(), 5);

        uploader.setChunkSize(4);

        // First request
        assertEquals(4, uploader.uploadChunk());
        assertEquals(1, uploader.uploadChunk());

        // Second request
        uploader.setChunkSize(100);
        assertEquals(5, uploader.uploadChunk());

        // Third request
        assertEquals(1, uploader.uploadChunk());
        uploader.finish();
    }

    @Test(expected = IllegalStateException.class)
    public void testSetRequestPayloadSizeThrows() throws Exception {
        byte[] content = "hello world".getBytes();

        TusClient client = new TusClient();
        URL uploadUrl = new URL(mockServerURL + "/payloadException");
        InputStream input = new ByteArrayInputStream(content);

        TusUploader uploader = new TusUploader(client, uploadUrl, input, 0);

        uploader.setChunkSize(4);
        uploader.uploadChunk();

        // Throws IllegalStateException
        uploader.setRequestPayloadSize(100);
    }
}
