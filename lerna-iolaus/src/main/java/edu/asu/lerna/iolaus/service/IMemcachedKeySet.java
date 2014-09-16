package edu.asu.lerna.iolaus.service;

import java.util.Set;


public interface IMemcachedKeySet {
	Set<String> getKeySet();
	void addKey(String key);
	void removeAll();
	void remove(String key);
}
