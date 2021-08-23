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
    private TusInputStream tusInputStream;
    private String fingerprint;
    private Map<String, String> metadata;

    /**
     * Create a new TusUpload object.
     */
    public TusUpload() {
    }

    /**
     * Create a new TusUpload object using the supplied file object. The corresponding {@link
     * InputStream}, size and fingerprint will be automatically set.
     *
     * @param file The file whose content should be later uploaded.
     * @throws FileNotFoundException Thrown if the file cannot be found.
     */
    public TusUpload(@NotNull File file) throws FileNotFoundException {
        size = file.length();
        setInputStream(new FileInputStream(file));

        fingerprint = String.format("%s-%d", file.getAbsolutePath(), size);

        metadata = new HashMap<String, String>();
        metadata.put("filename", file.getName());
    }

    /**
     * Returns the file size of the upload.
     * @return File size in bytes
     */
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

    /**
     * Returns the file specific fingerprint.
     * @return Fingerprint as String.
     */
    public String getFingerprint() {
        return fingerprint;
    }

    /**
     * Sets a fingerprint for this upload. This fingerprint must be unique and file specific, because it is used
     * for upload identification.
     * @param fingerprint String of fingerprint information.
     */
    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    /**
     * Returns the input stream of the file to upload.
     * @return {@link InputStream}
     */
    public InputStream getInputStream() {
        return input;
    }

    /**
     * This method returns the {@link TusInputStream}, which was derived from the file's {@link InputStream}.
     * @return {@link TusInputStream}
     */
    TusInputStream getTusInputStream() {
        return tusInputStream;
    }

    /**
     * Set the source from which will be read if the file will be later uploaded.
     *
     * @param inputStream The stream which will be read.
     */
    public void setInputStream(InputStream inputStream) {
        input = inputStream;
        tusInputStream = new TusInputStream(inputStream);
    }

    /**
     * This methods allows it to send Metadata alongside with the upload. The Metadata must be provided as
     * a Map with Key - Value pairs of Type String.
     * @param metadata Key-value pairs of Type String
     */
    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    /**
     * This method returns the upload's metadata as Map.
     * @return {@link Map} of metadata Key - Value pairs.
     */
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
        if (metadata == null || metadata.size() == 0) {
            return "";
        }

        String encoded = "";

        boolean firstElement = true;
        for (Map.Entry<String, String> entry : metadata.entrySet()) {
            if (!firstElement) {
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
     * @param in input Byte array for Base64 encoding.
     * @return Base64 encoded String derived from input Bytes.
     */
    static String base64Encode(byte[] in) {
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
