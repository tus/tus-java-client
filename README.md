# tus-java-client

> **tus** is a protocol based on HTTP for *resumable file uploads*. Resumable
> means that an upload can be interrupted at any moment and can be resumed without
> re-uploading the previous data again. An interruption may happen willingly, if
> the user wants to pause, or by accident in case of an network issue or server
> outage.

**tus-java-client** is a library for uploading files using the *tus* protocol to any remote server supporting it.

## Usage

```java
TusClient client = new TusClient();
client.setUploadCreationURL(new URL("http://master.tus.io/files"));
client.enableResuming(new TusURLMemoryStore());

File file = new File("./cute_kitten.png");
TusUpload upload = new TusUpload(file);

TusUploader uploader = client.resumeOrCreateUpload(upload);

while(uploader.uploadChunk(1024 * 1024) > -1) {
  long totalBytes = upload.getSize();
  long bytesUploaded = uploader.getOffset();
  double progress = (double) bytesUploaded / totalBytes * 100
}

uploader.finish();

```
