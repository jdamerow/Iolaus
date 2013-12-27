package edu.asu.lerna.iolaus.service;

import java.util.List;

public interface ICacheManager {
	public abstract List<List<Object>> executeQuery(String json,String instance);

	public abstract String getKey(String json, String instance);
}
