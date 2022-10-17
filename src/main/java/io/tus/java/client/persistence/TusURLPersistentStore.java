package io.tus.java.client.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.io.FileUtils;

import io.tus.java.client.TusURLStore;

public class TusURLPersistentStore implements TusURLStore {
	
	private static volatile TusUploadFileSet filesWithFingerprints;
	private String pathOfPersistentXmlFiles = FileUtils.getTempDirectoryPath() + java.io.File.separator + "tus-client-uploads.xml";
	
	

	public TusURLPersistentStore() {
		
		super();
		
		
		unmarshall();
		marshall();
	}

	public TusURLPersistentStore(String pathOfPersistentXmlFiles) {
		super();
		this.pathOfPersistentXmlFiles = pathOfPersistentXmlFiles;
		unmarshall();
	}

	public void set(String fingerprint, URL url) {
		
		unmarshall();
		if(filesWithFingerprints.getFiles().containsKey(fingerprint))	{
			
			  TusUploadFile uf = filesWithFingerprints.getFiles().get(fingerprint);
			  if(uf != null)	{
				  uf.setUrl(url);
			  }
			  
		} else  {
			
			TusUploadFile uf = new TusUploadFile(); uf.setUrl(url);
			filesWithFingerprints.getFiles().put(fingerprint, uf);
		}
		marshall();
		
	}
	
	public void set(String fingerprint, URL url, String fileName) {
		
		unmarshall();
		if(filesWithFingerprints.getFiles().containsKey(fingerprint))	{
			
			  TusUploadFile uf = filesWithFingerprints.getFiles().get(fingerprint);
			  if(uf != null)	{
				  uf.setUrl(url);
				  uf.setFileName(fileName); 
			  }
			  
		} else  {
			
			TusUploadFile uf = new TusUploadFile(); 
			uf.setUrl(url);
			filesWithFingerprints.getFiles().put(fingerprint, uf);
		}
		marshall();
	}

	public URL get(String fingerprint) {
		
		unmarshall();
		TusUploadFile uf = filesWithFingerprints.getFiles().get(fingerprint);
		if(uf != null)	{
			 return uf.getUrl();
		}
		return null;
	}
	
	public TusUploadFile getTusUploadFile(String fingerprint) {
		
		unmarshall();
		return filesWithFingerprints.getFiles().get(fingerprint);
	}

	public void remove(String fingerprint) {
		
		unmarshall();
		filesWithFingerprints.getFiles().remove(fingerprint);
		marshall();
	}
	
	public boolean exists(String fingerprint)	{
		
		unmarshall();
		return filesWithFingerprints.getFiles().containsKey(fingerprint);
	}
	
	public boolean fileExists(String fileName)	{
		
		unmarshall();
		for(Map.Entry<String, TusUploadFile> entry : filesWithFingerprints.getFiles().entrySet() )	{
			
			if( entry.getValue().getFileName().equals(fileName) )	{
				return true;
			}
		}
		
		return false;
	}
	
	public String getFingerprintForFile(String fileName)	{
		
		unmarshall();
		for(Map.Entry<String, TusUploadFile> entry : filesWithFingerprints.getFiles().entrySet() )	{
			
			if( entry.getValue().getFileName().equals(fileName) )	{
				return entry.getKey();
			}
		}
		
		return null;
	}
	
	public Map<String, TusUploadFile> getMap()	{
		return filesWithFingerprints.getFiles();
	}

	private synchronized void unmarshall()	{
		
		try	{
			
			JAXBContext jaxbContext   = JAXBContext.newInstance( TusUploadFileSet.class );
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			
			InputStream inStream = new FileInputStream( pathOfPersistentXmlFiles );
			 
			filesWithFingerprints = (TusUploadFileSet) jaxbUnmarshaller.unmarshal( inStream );
		} catch( JAXBException e )	{
			throw new RuntimeException(e);
			
		} catch (FileNotFoundException e)	{
			filesWithFingerprints = new TusUploadFileSet();
			/*
			try {
				FileUtils.touch(new File(pathOfPersistentXmlFiles));
			} catch (IOException e1) {
				throw new RuntimeException(e1);
			}*/
		}	
	}
	
	private synchronized void marshall()	{
		
		try	{
			JAXBContext jaxbContext   = JAXBContext.newInstance( TusUploadFileSet.class );
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			File persistedFileOfFileListXml = new File( pathOfPersistentXmlFiles);
			if( !persistedFileOfFileListXml.exists() )	{
				
				FileUtils.touch( persistedFileOfFileListXml );
			}
			jaxbMarshaller.marshal(filesWithFingerprints, persistedFileOfFileListXml);
			
		}	catch( JAXBException | IOException e )	{
			throw new RuntimeException(e);
			
		}
		
	}
}
