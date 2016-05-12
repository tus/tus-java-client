package io.tus.java.example;

import java.io.File;
import java.net.URL;

import io.tus.java.client.TusClient;
import io.tus.java.client.TusURLMemoryStore;
import io.tus.java.client.TusUpload;
import io.tus.java.client.TusUploader;

public class Main {
    public static void main(String[] args) {
        try {
            // Create a new TusClient instance
            TusClient client = new TusClient();

            // Configure tus HTTP endpoint. This URL will be used for creating new uploads
            // using the Creation extension
            client.setUploadCreationURL(new URL("http://master.tus.io/files/"));

            // Enable resumable uploads by storing the upload URL in memory
            client.enableResuming(new TusURLMemoryStore());

            // Open a file using which we will then create a TusUpload. If you do not have
            // a File object, you can manually construct a TusUpload using an InputStream.
            // See the documentation for more information.
            File file = new File("./example/assets/prairie.jpg");
            TusUpload upload = new TusUpload(file);

            // First try to resume an upload. If that's not possible we will create a new
            // upload and get a TusUploader in return. This class is responsible for opening
            // a connection to the remote server and doing the uploading.
            TusUploader uploader = client.resumeOrCreateUpload(upload);

            System.out.println("Starting upload...");

            // Upload the file in chunks of 1KB sizes.
            uploader.setChunkSize(1024);

            // Upload the file as long as data is available. Once the
            // file has been fully uploaded the method will return -1
            while(uploader.uploadChunk() > -1) {
                // Calculate the progress using the total size of the uploading file and
                // the current offset.
                long totalBytes = upload.getSize();
                long bytesUploaded = uploader.getOffset();
                double progress = (double) bytesUploaded / totalBytes * 100;

                System.out.printf("Upload at %06.2f%%.\n", progress);
            }

            // Allow the HTTP connection to be closed and cleaned up
            uploader.finish();

            System.out.println("Upload finished.");
            System.out.format("Upload available at: %s", uploader.getUploadURL().toString());
        } catch(Exception e) {
            e.printStackTrace();
        }

    }
}
