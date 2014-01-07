package edu.asu.lerna.iolaus.service;

import java.util.List;

public interface IRepositoryManager {

	public abstract List<List<Object>> executeQuery(String json, List<String> dbInstances);


}
