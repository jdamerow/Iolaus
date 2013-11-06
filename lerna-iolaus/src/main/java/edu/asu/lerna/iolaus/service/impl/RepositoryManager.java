package edu.asu.lerna.iolaus.service.impl;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.domain.queryobject.IQuery;
import edu.asu.lerna.iolaus.service.ICacheManager;
import edu.asu.lerna.iolaus.service.IRepositoryManager;

@Service
public class RepositoryManager implements IRepositoryManager{

	@Autowired
	private ICacheManager cacheManager;

	@Override
	public void executeQuery(IQuery q)
	{
		//TODO: Break the query into smaller parts.
		
		//TODO: Create cypher for each sub-query
		
		//TODO: Execute cypher by calling the cache manager
		cacheManager.executeQuery("");
	}
	
	@Override
	public void breakdownQuery(IQuery q)
	{
		throw new NotImplementedException("Not yet implemented");
	}
	
	@Override
	public void queryToCypher(IQuery q)
	{
		throw new NotImplementedException("Not yet implemented");
	}
	
}
