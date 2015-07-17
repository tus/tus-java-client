package io.tus.java.client;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class TusURLMemoryStore implements TusURLStore {

	private Map<String, URL> store = new HashMap<String, URL>();
	
	@Override
	public void set(String fingerprint, URL url) {
		store.put(fingerprint, url);
	}

	@Override
	public URL get(String fingerprint) {
		return store.get(fingerprint);
	}

	@Override
	public void remove(String fingerprint) {
		store.remove(fingerprint);
	}
}
