package edu.asu.lerna.iolaus.service;

import java.util.List;

public interface ICacheManager {
	List<List<Object>> executeQuery(String json,String instance);
}
