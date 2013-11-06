package edu.asu.lerna.iolaus.service.impl;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.domain.queryobject.IQuery;
import edu.asu.lerna.iolaus.service.ICacheManager;
import edu.asu.lerna.iolaus.service.IRepositoryManager;

@Service
public class RepositoryManager implements IRepositoryManager{


	private ICacheManager cacheManager;

	private void executeQuery(IQuery q)
	{
		throw new NotImplementedException("Not yet implemented");
	}
	
	private void breakdownQuery(IQuery q)
	{
		throw new NotImplementedException("Not yet implemented");
	}
	
	private void queryToCypher(IQuery q)
	{
		throw new NotImplementedException("Not yet implemented");
	}
	
	private void executeCypher(String cypher)
	{
		throw new NotImplementedException("Not yet implemented");
	}
}
