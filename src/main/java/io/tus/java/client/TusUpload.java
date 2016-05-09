package io.tus.java.client;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * This class contains information about a file which will be uploaded later. Uploading is not
 * done using this class but using {@link TusUploader} whose instances are returned by
 * {@link TusClient#createUpload(TusUpload)}, {@link TusClient#createUpload(TusUpload)} and
 * {@link TusClient#resumeOrCreateUpload(TusUpload)}.
 */
public class TusUpload {
    private long size;
    private InputStream input;
    private String fingerprint;
    private Map<String, String> metadata;

    /**
     * Create a new TusUpload object.
     */
    public TusUpload() {
    }

    /**
     * Create a new TusUpload object using the supplied File object. The corresponding {@link
     * InputStream}, size and fingerprint will be automatically set.
     *
     * @param file The file whose content should be later uploaded.
     * @throws FileNotFoundException Thrown if the file cannot be found.
     */
    public TusUpload(@NotNull File file) throws FileNotFoundException {
        size = file.length();
        input = new FileInputStream(file);

        fingerprint = String.format("%s-%d", file.getAbsolutePath(), size);

        metadata = new HashMap<String, String>();
        metadata.put("filename", file.getName());
    }

    public long getSize() {
        return size;
    }

    /**
     * Set the file's size in bytes.
     *
     * @param size File's size in bytes.
     */
    public void setSize(long size) {
        this.size = size;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public InputStream getInputStream() {
        return input;
    }

    /**
     * Set the source from which will be read if the file will be later uploaded.
     *
     * @param inputStream The stream which will be read.
     */
    public void setInputStream(InputStream inputStream) {
        input = inputStream;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    /**
     * Encode the metadata into a string according to the specification, so it can be
     * used as the value for the Upload-Metadata header.
     *
     * @return Encoded metadata
     */
    public String getEncodedMetadata() {
        if(metadata == null || metadata.size() == 0) {
            return "";
        }

        String encoded = "";

        boolean firstElement = true;
        for(Map.Entry<String, String> entry : metadata.entrySet()) {
            if(!firstElement) {
                encoded += ",";
            }
            encoded += entry.getKey() + " " + base64Encode(entry.getValue().getBytes());

            firstElement = false;
        }

        return encoded;
    }

    /**
     * Encode a byte-array using Base64. This is a sligtly modified version from an implementation
     * published on Wikipedia (https://en.wikipedia.org/wiki/Base64#Sample_Implementation_in_Java)
     * under the Creative Commons Attribution-ShareAlike License.
     */
    static String base64Encode(byte[] in)       {
        StringBuilder out = new StringBuilder((in.length * 4) / 3);
        String codes = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

        int b;
        for (int i = 0; i < in.length; i += 3)  {
            b = (in[i] & 0xFC) >> 2;
            out.append(codes.charAt(b));
            b = (in[i] & 0x03) << 4;
            if (i + 1 < in.length)      {
                b |= (in[i + 1] & 0xF0) >> 4;
                out.append(codes.charAt(b));
                b = (in[i + 1] & 0x0F) << 2;
                if (i + 2 < in.length)  {
                    b |= (in[i + 2] & 0xC0) >> 6;
                    out.append(codes.charAt(b));
                    b = in[i + 2] & 0x3F;
                    out.append(codes.charAt(b));
                } else  {
                    out.append(codes.charAt(b));
                    out.append('=');
                }
            } else      {
                out.append(codes.charAt(b));
                out.append("==");
            }
        }

        return out.toString();
    }
}
