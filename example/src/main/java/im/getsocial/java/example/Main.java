package im.getsocial.java.example;


import java.io.File;
import java.io.IOException;
import java.net.URL;

import io.tus.java.client.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Create a new GetsocialTusClient instance
            final GetsocialTusClient client = new GetsocialTusClient();

            // Existing URL for the resource. This resource needs to be
            // in an "empty" state; created with a POST request but not
            // yet written to.
            final String existingURL = "http://example.com/57fd65sd7";

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
                    TusUploader uploader = client.resumeOrCreateUpload(upload, existingURL);

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
