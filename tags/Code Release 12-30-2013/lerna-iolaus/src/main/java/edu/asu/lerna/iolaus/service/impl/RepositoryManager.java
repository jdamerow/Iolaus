package edu.asu.lerna.iolaus.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.configuration.neo4j.iml.Neo4jRegistry;
import edu.asu.lerna.iolaus.domain.INeo4jConfFile;
import edu.asu.lerna.iolaus.domain.json.IJsonNode;
import edu.asu.lerna.iolaus.domain.json.IJsonRelation;
import edu.asu.lerna.iolaus.service.ICacheManager;
import edu.asu.lerna.iolaus.service.IRepositoryManager;

@Service
public class RepositoryManager implements IRepositoryManager {

	private static final Logger logger = LoggerFactory
			.getLogger(RepositoryManager.class);
//	private List<IRepositoryHandler> repoHanlders;
	@Autowired
	private ICacheManager cacheManager;
	
	@Autowired
	private Neo4jRegistry neo4jInstances;

	public RepositoryManager()
	{
			//repoHanlders = new ArrayList<IRepositoryHandler>();
	}

	@Override
	public List<List<Object>> executeQuery(String json, List<String> dbInstances)
	{
		logger.debug("\n\n");
		List<List<Object>> listOfNodesAndRelations = new ArrayList<List<Object>>();
		List<String> instanceUrl = new ArrayList<String>();
		Iterator<String> iterator = dbInstances.iterator();
		while(iterator.hasNext())
		{
			String dbName = iterator.next();
			Iterator<INeo4jConfFile> fileIterator = neo4jInstances.getfileList().iterator();
			while(fileIterator.hasNext())
			{
				INeo4jConfFile dbFile = fileIterator.next();
				if (dbName.equals(dbFile.getId())&&dbFile.isActive())
				{
					instanceUrl.add("http://localhost:"+dbFile.getPort()+"/db/data/cypher");
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

	//TODO:Remove this method
	private void printList(List<List<Object>> listOfNodesAndRelations)
	{
		int count=0;
		if(listOfNodesAndRelations != null)
		{
			for(List<Object> rowList: listOfNodesAndRelations)
			{
				System.out.println("--------------------------------------------"+count++);
				for(Object obj: rowList)
				{
					if(obj instanceof IJsonNode)
					{
						IJsonNode jsonNode = (IJsonNode) obj;
						System.out.println(">> Node <<");
						System.out.println("Id: "+jsonNode.getId());
						System.out.println("Type: "+jsonNode.getType());
						System.out.println("Data size: "+jsonNode.getData().size());
						Iterator<Entry<String, String>> iterator = jsonNode.getData().entrySet().iterator();
						while(iterator.hasNext())
						{
							Entry<String, String> dataEntry = iterator.next();
							System.out.println(dataEntry.getKey()+" : "+dataEntry.getValue());
						}
						System.out.println();
					}
					else if(obj instanceof IJsonRelation)
					{
						IJsonRelation jsonRelation = (IJsonRelation) obj;
						System.out.println(">> Relation <<");
						System.out.println("Id: "+jsonRelation.getId());
						System.out.println("Start: "+jsonRelation.getStartNode());
						System.out.println("End: "+jsonRelation.getEndNode());
						System.out.println("Type: "+jsonRelation.getType());
						System.out.println("Data size: "+jsonRelation.getData().size());
						Iterator<Entry<String, String>> iterator = jsonRelation.getData().entrySet().iterator();
						while(iterator.hasNext())
						{
							Entry<String, String> dataEntry = iterator.next();
							System.out.println(dataEntry.getKey()+" : "+dataEntry.getValue());
						}
						System.out.println();
					}
				}
				System.out.println("--------------------------------------------");
			}
		}
	}
}
