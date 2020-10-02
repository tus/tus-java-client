package io.tus.java.example;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import io.tus.java.client.ProtocolException;
import io.tus.java.client.TusClient;
import io.tus.java.client.TusExecutor;
import io.tus.java.client.TusURLMemoryStore;
import io.tus.java.client.TusUpload;
import io.tus.java.client.TusUploader;

public class Main {
    public static void main(String[] args) {
        try {
            // When Java's HTTP client follows a redirect for a POST request, it will change the
            // method from POST to GET which can be disabled using following system property.
            // If you do not enable strict redirects, the tus-java-client will not follow any
            // redirects but still work correctly.
            System.setProperty("http.strictPostRedirect", "true");

            // Create a new TusClient instance
            final TusClient client = new TusClient();

            // Configure tus HTTP endpoint. This URL will be used for creating new uploads
            // using the Creation extension
            client.setUploadCreationURL(new URL("https://tusd.tusdemo.net/files/"));

            // Enable resumable uploads by storing the upload URL in memory
            client.enableResuming(new TusURLMemoryStore());

            // Open a file using which we will then create a TusUpload. If you do not have
            // a File object, you can manually construct a TusUpload using an InputStream.
            // See the documentation for more information.
            File file = new File("./example/assets/prairie.jpg");
            final TusUpload upload = new TusUpload(file);

            // You can also upload from an InputStream directly using a bit more work:
            // InputStream stream = …;
            // TusUpload upload = new TusUpload();
            // upload.setInputStream(stream);
            // upload.setSize(sizeOfStream);
            // upload.setFingerprint("stream");


            System.out.println("Starting upload...");

            // We wrap our uploading code in the TusExecutor class which will automatically catch
            // exceptions and issue retries with small delays between them and take fully
            // advantage of tus' resumability to offer more reliability.
            // This step is optional but highly recommended.
            TusExecutor executor = new TusExecutor() {
                @Override
                protected void makeAttempt() throws ProtocolException, IOException {
                    // First try to resume an upload. If that's not possible we will create a new
                    // upload and get a TusUploader in return. This class is responsible for opening
                    // a connection to the remote server and doing the uploading.
                    TusUploader uploader = client.resumeOrCreateUpload(upload);

                    // Upload the file in chunks of 1KB sizes.
                    uploader.setChunkSize(1024);

                    // Upload the file as long as data is available. Once the
                    // file has been fully uploaded the method will return -1
                    do {
                        // Calculate the progress using the total size of the uploading file and
                        // the current offset.
                        long totalBytes = upload.getSize();
                        long bytesUploaded = uploader.getOffset();
                        double progress = (double) bytesUploaded / totalBytes * 100;

                        System.out.printf("Upload at %06.2f%%.\n", progress);
                    } while(uploader.uploadChunk() > -1);

                    // Allow the HTTP connection to be closed and cleaned up
                    uploader.finish();

                    System.out.println("Upload finished.");
                    System.out.format("Upload available at: %s", uploader.getUploadURL().toString());
                }
            };
            executor.makeAttempts();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }
}
