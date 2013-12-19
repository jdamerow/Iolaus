package edu.asu.lerna.iolaus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.service.ICacheManager;
import edu.asu.lerna.iolaus.service.IRepositoryHandler;


@Service
public class CacheManager implements ICacheManager {

	@Autowired 
	private IRepositoryHandler repositoryHandler;
	
	@Override
	public List<List<Object>> executeQuery(String json,String instance) {
		List<List<Object>> resultSet=null;
		resultSet=getCachedResults(json,instance);
		if(resultSet==null){
			resultSet=repositoryHandler.executeQuery(json, instance);
			cacheResults(json,instance,resultSet);
		}
		return resultSet;
	}

	private void cacheResults(String json, String instance,List<List<Object>> resultSet) {
		// TODO implement this method
		
	}

	private List<List<Object>> getCachedResults(String json, String instance) {
		// TODO implement this method
		return null;
	}

}
