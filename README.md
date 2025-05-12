# tus-java-client [![Tests](https://github.com/tus/tus-java-client/actions/workflows/tests.yml/badge.svg?branch=main)](https://github.com/tus/tus-java-client/actions/workflows/tests.yml)

> **tus** is a protocol based on HTTP for *resumable file uploads*. Resumable
> means that an upload can be interrupted at any moment and can be resumed without
> re-uploading the previous data again. An interruption may happen willingly, if
> the user wants to pause, or by accident in case of a network issue or server
> outage.

**tus-java-client** is a library for uploading files using the *tus* protocol to any remote server supporting it.

This library is also compatible with the Android platform and can be used without any modifications using the API. The [tus-android-client](https://github.com/tus/tus-android-client) provides additional classes which can be used in addition the Java library.

## Usage

```java
// Create a new TusClient instance
TusClient client = new TusClient();

// Configure tus HTTP endpoint. This URL will be used for creating new uploads
// using the Creation extension
client.setUploadCreationURL(new URL("https://tusd.tusdemo.net/files"));

// Enable resumable uploads by storing the upload URL in memory
client.enableResuming(new TusURLMemoryStore());

// Open a file using which we will then create a TusUpload. If you do not have
// a File object, you can manually construct a TusUpload using an InputStream.
// See the documentation for more information.
File file = new File("./cute_kitten.png");
final TusUpload upload = new TusUpload(file);

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
        
        // Alternatively, if your tus server does not support the Creation extension
        // and you obtained an upload URL from another service, you can instruct
        // tus-java-client to upload to a specific URL. Please note that this is usually
        // _not_ necessary and only if the tus server does not support the Creation
        // extension. The Vimeo API would be an example where this method is needed.
        // TusUploader uploader = client.beginOrResumeUploadFromURL(upload, new URL("https://tus.server.net/files/my_file"));

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

```

## Installation

The JARs can be downloaded manually from our [Maven Central Repo](https://central.sonatype.com/namespace/io.tus.java.client).

**Gradle:**

```groovy
implementation 'io.tus.java.client:tus-java-client:0.5.1'
```

**Maven:**

```xml
<dependency>
  <groupId>io.tus.java.client</groupId>
  <artifactId>tus-java-client</artifactId>
  <version>0.5.1</version>
</dependency>
```

## Documentation

The documentation of the latest versions can be found online at [javadoc.io](https://javadoc.io/doc/io.tus.java.client/tus-java-client).

## FAQ

### Can I use my own custom SSLSocketFactory?

Yes, you can! Create a subclass of `TusClient` and override the `prepareConnection` method to attach your `SSLSocketFactory`. After this use your custom `TusClient` subclass as you would normally use it. Here is an example:

```java
@Override
public void prepareConnection(@NotNull HttpURLConnection connection) {
    super.prepareConnection(connection);
    
    if(connection instanceof HttpsURLConnection) {
        HttpsURLConnection secureConnection = (HttpsURLConnection) connection;
        secureConnection.setSSLSocketFactory(mySSLSocketFactory);
    }
}
```

### Can I use a proxy that will be used for uploading files?

Yes, just add a proxy to the TusClient as shown below (1 line added to the above [usage](#usage)):

```java
TusClient client = new TusClient();
Proxy myProxy = new Proxy(...);
client.setProxy(myProxy);
```

## License

MIT
