package edu.asu.lerna.iolaus.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.configuration.neo4j.impl.Neo4jRegistry;
import edu.asu.lerna.iolaus.domain.INeo4jInstance;
import edu.asu.lerna.iolaus.service.ICacheManager;
import edu.asu.lerna.iolaus.service.IRepositoryManager;
/**
 * This class implements {@link IRepositoryManager}. It has method for executing json query on multiple Neo4j instances.
 * @author Karan Kothari
 *
 */
@Service
public class RepositoryManager implements IRepositoryManager {

	private static final Logger logger = LoggerFactory
			.getLogger(RepositoryManager.class);

	@Autowired
	private ICacheManager cacheManager;
	
	@Autowired
	private Neo4jRegistry neo4jInstances;

	@Autowired
	@Qualifier("cypherEndPoint")
	private String cypherEndPoint;
	
	public RepositoryManager()
	{
			//repoHanlders = new ArrayList<IRepositoryHandler>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<List<Object>> executeQuery(String json, List<String> dbInstances)
	{
		logger.debug("\n\n");
		List<List<Object>> listOfNodesAndRelations = new ArrayList<List<Object>>();;
		List<String> instanceUrl = new ArrayList<String>();
		Iterator<String> iterator = dbInstances.iterator();
		while(iterator.hasNext())
		{
			String dbName = iterator.next();
			Iterator<INeo4jInstance> fileIterator = neo4jInstances.getfileList().iterator();
			while(fileIterator.hasNext())
			{
				INeo4jInstance dbFile = fileIterator.next();
				if (dbName.equals(dbFile.getId())&&dbFile.isActive())
				{
					instanceUrl.add("http://"+dbFile.getHost()+":"+dbFile.getPort()+"/"+dbFile.getDbPath()+"/"+cypherEndPoint);
				}
			}
		}
		List<List<Object>> queryResults;
		for (String instance: instanceUrl)
		{
			queryResults = cacheManager.executeQuery(json, instance);
			if(queryResults!=null)
				listOfNodesAndRelations.addAll(queryResults);
		}
		return listOfNodesAndRelations;
	}
}
