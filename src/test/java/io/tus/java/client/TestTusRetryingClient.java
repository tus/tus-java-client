package io.tus.java.client;

import org.junit.Test;
import org.mockserver.matchers.Times;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.socket.PortFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;

import static org.junit.Assert.*;

public class TestTusRetryingClient extends MockServerProvider {
    @Test
    public void testSetDelays() {
        TusRetryingClient client = new TusRetryingClient();
        assertArrayEquals(client.getDelays(), new int[]{500, 1000, 2000, 3000});
        client.setDelays(new int[]{1, 2, 3});
        assertArrayEquals(client.getDelays(), new int[]{1, 2, 3});
    }

    @Test
    public void testCreateUploadFail() throws Exception {
        mockServer.when(new HttpRequest()
                .withMethod("POST")
                .withPath("/files")
                .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                .withHeader("Upload-Length", "10"))
                .respond(new HttpResponse()
                        .withStatusCode(500));

        TusRetryingClient client = new TusRetryingClient();
        client.setDelays(new int[]{1, 2, 3});
        client.setUploadCreationURL(mockServerURL);
        TusUpload upload = new TusUpload();
        upload.setSize(10);
        upload.setInputStream(new ByteArrayInputStream(new byte[10]));

        boolean exceptionThrown = false;
        try {
            client.createUpload(upload);
        } catch(ProtocolException e) {
            assertTrue(e.getMessage().contains("500"));
            exceptionThrown = true;
        } finally {
            assertTrue(exceptionThrown);
        }
    }

    @Test
    public void testCreateUploadFailWithoutRetrying() throws Exception {
        mockServer.when(new HttpRequest()
                .withMethod("POST")
                .withPath("/files")
                .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                .withHeader("Upload-Length", "10"))
                .respond(new HttpResponse()
                        .withStatusCode(400));

        TusRetryingClient client = new TusRetryingClient();
        client.setUploadCreationURL(mockServerURL);
        TusUpload upload = new TusUpload();
        upload.setSize(10);
        upload.setInputStream(new ByteArrayInputStream(new byte[10]));

        boolean exceptionThrown = false;
        try {
            client.createUpload(upload);
        } catch(ProtocolException e) {
            assertTrue(e.getMessage().contains("400"));
            exceptionThrown = true;
        } finally {
            assertTrue(exceptionThrown);
        }
    }

    @Test
    public void testCreateUploadSuccess() throws Exception {
        mockServer.when(new HttpRequest()
                .withMethod("POST")
                .withPath("/files")
                .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                .withHeader("Upload-Length", "10"),
                Times.exactly(2))
                .respond(new HttpResponse()
                        .withStatusCode(500));


        mockServer.when(new HttpRequest()
                .withMethod("POST")
                .withPath("/files")
                .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                .withHeader("Upload-Length", "10"))
                .respond(new HttpResponse()
                        .withStatusCode(201)
                        .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                        .withHeader("Location", mockServerURL + "/foo"));

        TusRetryingClient client = new TusRetryingClient();
        client.setDelays(new int[]{1, 2, 3});
        client.setUploadCreationURL(mockServerURL);
        TusUpload upload = new TusUpload();
        upload.setSize(10);
        upload.setInputStream(new ByteArrayInputStream(new byte[10]));


        TusUploader uploader = client.createUpload(upload);
        assertEquals(uploader.getUploadURL(), new URL(mockServerURL + "/foo"));
    }

    @Test
    public void testCreateUploadInterrupting() throws Exception {
        mockServer.when(new HttpRequest()
                .withMethod("POST")
                .withPath("/files")
                .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                .withHeader("Upload-Length", "10"))
                .respond(new HttpResponse()
                        .withStatusCode(500));

        TusRetryingClient client = new TusRetryingClient();
        client.setDelays(new int[]{100000});
        client.setUploadCreationURL(mockServerURL);
        TusUpload upload = new TusUpload();
        upload.setSize(10);
        upload.setInputStream(new ByteArrayInputStream(new byte[10]));

        final Thread uploaderThread = Thread.currentThread();
        Thread waiterThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    uploaderThread.interrupt();
                } catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        waiterThread.start();

        TusUploader uploader = client.createUpload(upload);
        assertNull(uploader);
    }

    @Test(expected = IOException.class)
    public void testCreateUploadIOException() throws Exception {
        TusRetryingClient client = new TusRetryingClient();
        client.setDelays(new int[]{1, 2, 3});

        // Try to connect to a port, on which nobody listens in order to cause an IOException
        client.setUploadCreationURL(new URL("http://localhost:" + PortFactory.findFreePort() + "/files/"));
        TusUpload upload = new TusUpload();
        upload.setSize(10);
        upload.setInputStream(new ByteArrayInputStream(new byte[10]));

        client.createUpload(upload);
    }

    @Test
    public void testResumeOrCreateUpload() throws Exception {
        mockServer.when(new HttpRequest()
                .withMethod("HEAD")
                .withPath("/files/hash")
                .withHeader("Tus-Resumable", TusClient.TUS_VERSION))
                .respond(new HttpResponse()
                        .withStatusCode(500));

        mockServer.when(new HttpRequest()
                .withMethod("POST")
                .withPath("/files")
                .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                .withHeader("Upload-Length", "10"),
                Times.exactly(2))
                .respond(new HttpResponse()
                        .withStatusCode(500));


        mockServer.when(new HttpRequest()
                .withMethod("POST")
                .withPath("/files")
                .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                .withHeader("Upload-Length", "10"))
                .respond(new HttpResponse()
                        .withStatusCode(201)
                        .withHeader("Tus-Resumable", TusClient.TUS_VERSION)
                        .withHeader("Location", mockServerURL + "/foo"));

        TusRetryingClient client = new TusRetryingClient();

        TusURLStore store = new TusURLMemoryStore();
        store.set("fingerprint", new URL(mockServerURL + "/hash"));

        client.setDelays(new int[]{1, 2, 3});
        client.setUploadCreationURL(mockServerURL);
        client.enableResuming(store);

        TusUpload upload = new TusUpload();
        upload.setSize(10);
        upload.setInputStream(new ByteArrayInputStream(new byte[10]));
        upload.setFingerprint("fingerprint");

        TusUploader uploader = client.resumeOrCreateUpload(upload);
        assertEquals(uploader.getUploadURL(), new URL(mockServerURL + "/foo"));
    }
}
