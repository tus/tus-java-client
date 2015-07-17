package io.tus.java.client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class TusClient {
    public final static String TUS_VERSION = "1.0.0";

    private URL uploadCreationURL;
    private boolean resumingEnabled;
    private TusURLStore urlStore;

    public TusClient() {

    }

    public TusClient(URL uploadCreationURL) {
        setUploadCreationURL(uploadCreationURL);
    }

    public void setUploadCreationURL(URL uploadCreationURL) {
        this.uploadCreationURL = uploadCreationURL;
    }

    public URL getUploadCreationURL() {
        return uploadCreationURL;
    }

    public void enableResuming(TusURLStore urlStore) {
        resumingEnabled = true;
        this.urlStore = urlStore;
    }

    public void disableResuming() {
        resumingEnabled = false;
        this.urlStore = null;
    }
    
    public boolean resumingEnabled() {
    	return resumingEnabled;
    }

    public TusUploader createUpload(TusUpload upload) throws ProtocolException, IOException {
        HttpURLConnection connection = (HttpURLConnection) uploadCreationURL.openConnection();
        connection.setRequestMethod("POST");
        prepareConnection(connection);

        connection.addRequestProperty("Upload-Length", Long.toString(upload.getSize()));
        connection.connect();

        int responseCode = connection.getResponseCode();
        if(!(responseCode >= 200 && responseCode < 300)) {
            throw new ProtocolException("unexpected status code (" + responseCode + ") while creating upload");
        }

        String urlStr = connection.getHeaderField("Location");
        if(urlStr.length() == 0) {
            throw new ProtocolException("missing upload URL in response for creating upload");
        }

        URL uploadURL = new URL(urlStr);

        if(resumingEnabled) {
            urlStore.set(upload.getFingerprint(), uploadURL);
        }

        return new TusUploader(this, uploadURL, upload.getInputStream(), 0);
    }

    public TusUploader resumeUpload(TusUpload upload) throws FingerprintNotFoundException, ResumingNotEnabledException, ProtocolException, IOException {
        if(!resumingEnabled) {
            throw new ResumingNotEnabledException();
        }

        URL uploadURL = urlStore.get(upload.getFingerprint());
        if(uploadURL == null) {
            throw new FingerprintNotFoundException(upload.getFingerprint());
        }

        HttpURLConnection connection = (HttpURLConnection) uploadURL.openConnection();
        connection.setRequestMethod("HEAD");
        prepareConnection(connection);

        connection.connect();

        int responseCode = connection.getResponseCode();
        if(!(responseCode >= 200 && responseCode < 300)) {
            throw new ProtocolException("unexpected status code (" + responseCode + ") while resuming upload");
        }

        String offsetStr = connection.getHeaderField("Upload-Offset");
        if(offsetStr.length() == 0) {
            throw new ProtocolException("missing upload offset in response for resuming upload");
        }
        long offset = Long.parseLong(offsetStr);

        return new TusUploader(this, uploadURL, upload.getInputStream(), offset);
    }

    public TusUploader resumeOrCreateUpload(TusUpload upload) throws ProtocolException, IOException {
        try {
            return resumeUpload(upload);
        } catch(FingerprintNotFoundException e) {
            return createUpload(upload);
        } catch(ResumingNotEnabledException e) {
            return createUpload(upload);
        }
    }

    public void prepareConnection(URLConnection connection) {
        connection.addRequestProperty("Tus-Resumable", TUS_VERSION);
        // TODO: add custom headers
    }
}
