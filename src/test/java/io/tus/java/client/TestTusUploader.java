package io.tus.java.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.Arrays;

import org.junit.Assume;
import org.junit.Test;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.socket.PortFactory;

/**
 * Test class for {@link TusUploader}.
 */
public class TestTusUploader extends MockServerProvider {
    private boolean isOpenJDK6 = System.getProperty("java.version").startsWith("1.6")
            && System.getProperty("java.vm.name").contains("OpenJDK");

    /**
     * Tests if the {@link TusUploader} actually uploads files and fixed chunk sizes.
     * @throws IOException
     * @throws ProtocolException
     */
    @Test
    public void testTusUploader() throws IOException, ProtocolException {
        byte[] content = "hello world".getBytes();

        mockServer.when(new HttpRequest()
                .withPath("/files/foo")
                .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                .withHeader("Upload-Offset", "3")
                .withHeader("Content-Type", "application/offset+octet-stream")
                .withHeader("Connection", "keep-alive")
                .withBody(Arrays.copyOfRange(content, 3, 11)))
                .respond(new HttpResponse()
                        .withStatusCode(204)
                        .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                        .withHeader("Upload-Offset", "11"));

        TusClient client = new TusClient();
        URL uploadUrl = new URL(mockServerURL + "/foo");
        TusInputStream input = new TusInputStream(new ByteArrayInputStream(content));
        long offset = 3;

        TusUpload upload = new TusUpload();

        TusUploader uploader = new TusUploader(client, upload, uploadUrl, input, offset);

        uploader.setChunkSize(5);
        assertEquals(uploader.getChunkSize(), 5);

        assertEquals(5, uploader.uploadChunk());
        assertEquals(3, uploader.uploadChunk(5));
        assertEquals(-1, uploader.uploadChunk());
        assertEquals(-1, uploader.uploadChunk(5));
        assertEquals(11, uploader.getOffset());
        uploader.finish();
    }

    /**
     * Tests if the {@link TusUploader} actually uploads files through a proxy.
     * @throws IOException
     * @throws ProtocolException
     */
    @Test
    public void testTusUploaderWithProxy() throws IOException, ProtocolException {
        byte[] content = "hello world with proxy".getBytes();

        mockServer.when(new HttpRequest()
                .withPath("/files/foo")
                .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                .withHeader("Upload-Offset", "0")
                .withHeader("Content-Type", "application/offset+octet-stream")
                .withHeader("Proxy-Connection", "keep-alive")
                .withBody(Arrays.copyOf(content, content.length)))
            .respond(new HttpResponse()
                .withStatusCode(204)
                .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                .withHeader("Upload-Offset", "22"));

        TusClient client = new TusClient();
        URL uploadUrl = new URL(mockServerURL + "/foo");
        Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress("localhost", mockServer.getPort()));
        TusInputStream input = new TusInputStream(new ByteArrayInputStream(content));
        long offset = 0;

        TusUpload upload = new TusUpload();

        TusUploader uploader = new TusUploader(client, upload, uploadUrl, input, offset);
        uploader.setProxy(proxy);

        assertEquals(proxy, uploader.getProxy());
        assertEquals(22, uploader.uploadChunk());
        uploader.finish();
    }

    /**
     * Verifies, that {@link TusClient#uploadFinished(TusUpload)} gets called after a proper upload has been finished.
     * @throws IOException
     * @throws ProtocolException
     */
    @Test
    public void testTusUploaderClientUploadFinishedCalled() throws IOException, ProtocolException {

        TusClient client = mock(TusClient.class);

        byte[] content = "hello world".getBytes();

        URL uploadUrl = new URL("http://dummy-url/foo");
        TusInputStream input = new TusInputStream(new ByteArrayInputStream(content));
        long offset = 10;

        TusUpload upload = new TusUpload();
        upload.setSize(10);

        TusUploader uploader = new TusUploader(client, upload, uploadUrl, input, offset);
        uploader.finish();

        // size and offset are the same, so uploadfinished() should be called
        verify(client).uploadFinished(upload);
    }

    /**
     * Verifies, that {@link TusClient#uploadFinished(TusUpload)} doesn't get called if the actual upload size is
     * greater than the offset.
     * @throws IOException
     * @throws ProtocolException
     */
    @Test
    public void testTusUploaderClientUploadFinishedNotCalled() throws IOException, ProtocolException {

        TusClient client = mock(TusClient.class);

        byte[] content = "hello world".getBytes();

        URL uploadUrl = new URL("http://dummy-url/foo");
        TusInputStream input = new TusInputStream(new ByteArrayInputStream(content));
        long offset = 0;

        TusUpload upload = new TusUpload();
        upload.setSize(10);

        TusUploader uploader = new TusUploader(client, upload, uploadUrl, input, offset);
        uploader.finish();

        // size is greater than offset, so uploadfinished() should not be called
        verify(client, times(0)).uploadFinished(upload);
    }

    /**
     * Verifies, that an Exception gets thrown, if the upload server isn't satisfied with the client's headers.
     * @throws IOException
     * @throws ProtocolException
     */
    @Test
    public void testTusUploaderFailedExpectation() throws IOException, ProtocolException {
        Assume.assumeFalse(isOpenJDK6);

        FailingExpectationServer server = new FailingExpectationServer();
        server.start();

        byte[] content = "hello world".getBytes();

        TusClient client = new TusClient();
        URL uploadUrl = new URL(server.getURL() + "/expect");
        TusInputStream input = new TusInputStream(new ByteArrayInputStream(content));
        long offset = 3;
        TusUpload upload = new TusUpload();
        boolean exceptionThrown = false;
        TusUploader uploader = new TusUploader(client, upload, uploadUrl, input, offset);
        try {
            uploader.uploadChunk();
        } catch (ProtocolException e) {
            assertTrue(e.getMessage().contains("500"));
            exceptionThrown = true;
        } finally {
            assertTrue(exceptionThrown);
        }
    }

    /**
     * FailingExpectationServer is a HTTP/1.1 server which will always respond with a 500 Internal
     * Error. This is meant to simulate failing expectations when the request contains the
     * expected header. The org.mockserver packages do not support this and will always send the
     * 100 Continue status code. therefore, we built our own stupid mocking server.
     */
    private class FailingExpectationServer extends Thread {
        private final byte[] response = "HTTP/1.1 500 Internal Server Error\r\n\r\n".getBytes();
        private ServerSocket serverSocket;
        private int port;

        FailingExpectationServer() throws IOException {
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public URL getURL() {
            try {
                return new URL("http://localhost:" + port);
            } catch (MalformedURLException e) {
                return null;
            }
        }
    }

    /**
     * Verifies, that {@link TusUploader#setRequestPayloadSize(int)} effectively limits the size  a payload.
     * @throws Exception
     */
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
        TusInputStream input = new TusInputStream(new ByteArrayInputStream(content));
        TusUpload upload = new TusUpload();

        TusUploader uploader = new TusUploader(client, upload, uploadUrl, input, 0);

        assertEquals(uploader.getRequestPayloadSize(), 10 * 1024 * 1024);
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


    /**
     * Verifies, that an exception is thrown if {@link TusUploader#setRequestPayloadSize(int)} is called while the
     * client has already an upload connection opened.
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void testSetRequestPayloadSizeThrows() throws Exception {
        byte[] content = "hello world".getBytes();

        TusClient client = new TusClient();
        URL uploadUrl = new URL(mockServerURL + "/payloadException");
        TusInputStream input = new TusInputStream(new ByteArrayInputStream(content));
        TusUpload upload = new TusUpload();

        TusUploader uploader = new TusUploader(client, upload, uploadUrl, input, 0);

        uploader.setChunkSize(4);
        uploader.uploadChunk();

        // Throws IllegalStateException
        uploader.setRequestPayloadSize(100);
    }

    /**
     * Verifies, that an Exception is thrown if the UploadOffsetHeader is missing.
     * @throws Exception
     */
    @Test
    public void testMissingUploadOffsetHeader() throws Exception {
        byte[] content = "hello world".getBytes();

        mockServer.when(new HttpRequest()
                .withPath("/files/missingHeader"))
                .respond(new HttpResponse()
                        .withStatusCode(204)
                        .withHeader("Tus-Resumable", TusClient.TUS_VERSION));

        TusClient client = new TusClient();
        URL uploadUrl = new URL(mockServerURL + "/missingHeader");
        TusInputStream input = new TusInputStream(new ByteArrayInputStream(content));
        TusUpload upload = new TusUpload();

        TusUploader uploader = new TusUploader(client, upload, uploadUrl, input, 0);

        boolean exceptionThrown = false;
        try {
            assertEquals(11, uploader.uploadChunk());
            uploader.finish();
        } catch (ProtocolException e) {
            assertTrue(e.getMessage().contains("no or invalid Upload-Offset header"));
            exceptionThrown = true;
        } finally {
            assertTrue(exceptionThrown);
        }
    }

    /**
     * Verifies, that an Exception is thrown if the UploadOffsetHeader of the server's response does not match the
     * clients upload offset value.
     * @throws Exception
     */
    @Test
    public void testUnmatchingUploadOffsetHeader() throws Exception {
        byte[] content = "hello world".getBytes();

        mockServer.when(new HttpRequest()
                .withPath("/files/unmatchingHeader"))
                .respond(new HttpResponse()
                        .withStatusCode(204)
                        .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                        .withHeader("Upload-Offset", "44"));

        TusClient client = new TusClient();
        URL uploadUrl = new URL(mockServerURL + "/unmatchingHeader");
        TusInputStream input = new TusInputStream(new ByteArrayInputStream(content));
        TusUpload upload = new TusUpload();

        TusUploader uploader = new TusUploader(client, upload, uploadUrl, input, 0);

        boolean exceptionThrown = false;
        try {
            assertEquals(11, uploader.uploadChunk());
            uploader.finish();
        } catch (ProtocolException e) {
            assertTrue(e.getMessage().contains("different Upload-Offset value (44) than expected (11)"));
            exceptionThrown = true;
        } finally {
            assertTrue(exceptionThrown);
        }
    }
}
