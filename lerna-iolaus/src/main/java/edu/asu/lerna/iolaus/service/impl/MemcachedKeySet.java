package edu.asu.lerna.iolaus.service.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.service.IMemcachedKeySet;

@Service
public class MemcachedKeySet implements IMemcachedKeySet {
	
	private Set<String> keySet;
	
	public MemcachedKeySet() {
		keySet = new HashSet<String>();
	}

	@Override
	public Set<String> getKeySet() {
		return Collections.unmodifiableSet(keySet);
	}

	@Override
	public void addKey(String key) {
		if(key != null) {
			keySet.add(key);
		}
	}

	@Override
	public void removeAll() {
		keySet.removeAll(keySet);
	}

	@Override
	public void remove(String key) {
		if(keySet.contains(key)) {
			keySet.remove(key);
		}
	}
	
}
