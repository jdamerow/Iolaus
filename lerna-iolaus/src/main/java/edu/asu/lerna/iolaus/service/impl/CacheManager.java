package edu.asu.lerna.iolaus.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.configuration.neo4j.iml.Neo4jConfFile;
import edu.asu.lerna.iolaus.configuration.neo4j.iml.Neo4jRegistry;
import edu.asu.lerna.iolaus.domain.json.IJsonNode;
import edu.asu.lerna.iolaus.domain.json.IJsonRelation;
import edu.asu.lerna.iolaus.service.ICacheManager;
import edu.asu.lerna.iolaus.service.IRepositoryHandler;

@Service
public class CacheManager implements ICacheManager {

	//TODO: Change this implementation to use autowiring
//	private List<IRepositoryHandler> repoHanlders;
	@Autowired
	private IRepositoryHandler repoHandler;
	@Autowired
	private Neo4jRegistry neo4jInstances;

	public CacheManager()
	{
		
//		repoHanlders = new ArrayList<IRepositoryHandler>();
	}
	
	@Override
	public void executeQuery(String json)
	{
		//TODO: Iterate through each repository and for each repository fetch the result.
		System.out.println("\n\n");
		Iterator<Neo4jConfFile> fileIterator = neo4jInstances.getfileList().iterator();
		while(fileIterator.hasNext())
		{

		json = fileIterator.next().getPath();
		HashMap<String, List> listOfNodesAndRelations = repoHandler.executeQuery(json);
		printList(listOfNodesAndRelations);
		
		json = "http://localhost:7474/db/data/node/4237/relationships/all";
		listOfNodesAndRelations = repoHandler.executeQuery(json);
		listOfNodesAndRelations = repoHandler.executeQuery(json);
		printList(listOfNodesAndRelations);
		
		//TODO: Add the result to the resultList	
		}
				
	}
	
	//TODO:Remove this method
	private void printList(HashMap<String, List> listOfNodesAndRelations)
	{
		if(listOfNodesAndRelations != null)
		{
			List<IJsonNode> nodeList = listOfNodesAndRelations.get("nodesList");
			if(nodeList != null)
				for(IJsonNode jsonNode: nodeList)
				{
					System.out.println("Id: "+jsonNode.getId());
					System.out.println("Type: "+jsonNode.getType());
					System.out.println("Data size: "+jsonNode.getData().size());
					Iterator<Entry<String, String>> iterator = jsonNode.getData().entrySet().iterator();
					while(iterator.hasNext())
					{
						Map.Entry dataEntry = (Map.Entry) iterator.next();
						System.out.println(dataEntry.getKey()+" : "+dataEntry.getValue());
					}
					System.out.println();
				}

			List<IJsonRelation> relationList = listOfNodesAndRelations.get("relationList");
			if(relationList != null)
				for(IJsonRelation jsonRelation: relationList)
				{
					System.out.println("Id: "+jsonRelation.getId());
					System.out.println("Start: "+jsonRelation.getStartNode());
					System.out.println("End: "+jsonRelation.getEndNode());
					System.out.println("Type: "+jsonRelation.getType());
					System.out.println("Data size: "+jsonRelation.getData().size());
					Iterator<Entry<String, String>> iterator = jsonRelation.getData().entrySet().iterator();
					while(iterator.hasNext())
					{
						Map.Entry dataEntry = (Map.Entry) iterator.next();
						System.out.println(dataEntry.getKey()+" : "+dataEntry.getValue());
					}
					System.out.println();
				}
		}
	}
}
