package edu.asu.lerna.iolaus.service;

import java.util.List;

public interface ICacheManager {

	public abstract List<List> executeQuery(String json, List<String> dbInstances);


}
