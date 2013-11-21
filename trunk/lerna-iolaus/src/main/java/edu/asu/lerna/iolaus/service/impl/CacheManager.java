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
import edu.asu.lerna.iolaus.domain.json.impl.JsonNode;
import edu.asu.lerna.iolaus.domain.json.impl.JsonRelation;
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
		json = "{\"query\":\"Start source=node:node_auto_index(type={param1}) match source-[r]->(target) Where (( source.dataset={param3} )) return source, r, target\", \"params\":{\"param1\":\"Person\",\"param3\":\"mblcourses\"}}";
		List<List> listOfNodesAndRelations = repoHandler.executeQuery(json);
		printList(listOfNodesAndRelations);

		Iterator<Neo4jConfFile> fileIterator = neo4jInstances.getfileList().iterator();
		while(fileIterator.hasNext())
		{

			//TODO: Add the result to the resultList	
		}

	}

	//TODO:Remove this method
	private void printList(List<List> listOfNodesAndRelations)
	{
		if(listOfNodesAndRelations != null)
		{
			for(List rowList: listOfNodesAndRelations)
			{
				System.out.println("--------------------------------------------");
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
							Map.Entry dataEntry = (Map.Entry) iterator.next();
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
							Map.Entry dataEntry = (Map.Entry) iterator.next();
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
