package io.tus.java.client.persistence;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "upload-persistence")
@XmlAccessorType(XmlAccessType.FIELD)
public class TusUploadFileSet {

	@XmlElementWrapper(name="files")
	private Map<String, TusUploadFile> files;

	public TusUploadFileSet() {
		super();
		files = new HashMap<>();
	}

	public Map<String, TusUploadFile> getFiles() {
		return files;
	}
	
	
}
