package io.tus.java.client;

import junit.framework.TestCase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class TestTusUpload extends TestCase {
    public void testTusUploadFile() throws IOException {
        String content = "hello world";
        Map<String, String> metadata = new LinkedHashMap<String, String>();
        metadata.put("foo", "hello");
        metadata.put("bar", "world");

        File file = File.createTempFile("tus-upload-test", ".tmp");
        OutputStream output = new FileOutputStream(file);
        output.write(content.getBytes());
        output.close();

        TusUpload upload = new TusUpload(file);

        upload.setMetadata(metadata);
        assertEquals(upload.getMetadata(), metadata);
        assertEquals(upload.getEncodedMetadata(), "foo aGVsbG8=,bar d29ybGQ=");

        assertEquals(upload.getSize(), content.length());
        assertNotSame(upload.getFingerprint(), "");
        byte[] readContent = new byte[content.length()];
        assertEquals(upload.getInputStream().read(readContent), content.length());
        assertEquals(new String(readContent), content);
    }
}
