package edu.asu.lerna.iolaus.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.service.ICacheManager;
import edu.asu.lerna.iolaus.service.IRepositoryHandler;

@Service
public class CacheManager implements ICacheManager {

	//TODO: Change this implementation to use autowiring
//	private List<IRepositoryHandler> repoHanlders;
	@Autowired
	private IRepositoryHandler repoHandler;

	public CacheManager()
	{
		
//		repoHanlders = new ArrayList<IRepositoryHandler>();
	}
	
	@Override
	public void executeQuery(String json)
	{
		//TODO: Iterate through each repository and for each repository fetch the result.
		json = "start n=node(*) return n";
		repoHandler.executeQuery(json);
		//TODO: Add the result to the resultList	
		
		
		
	}
}
