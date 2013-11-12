package edu.asu.lerna.iolaus.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public CacheManager()
	{
		
//		repoHanlders = new ArrayList<IRepositoryHandler>();
	}
	
	@Override
	public void executeQuery(String json)
	{
		//TODO: Iterate through each repository and for each repository fetch the result.
		
		System.out.println("\n\n");
		json = "http://localhost:7474/db/data/node/4237";
		HashMap<String, List> listOfNodesAndRelations = repoHandler.executeQuery(json);
		printList(listOfNodesAndRelations);
		
		System.out.println("\n\n");
		json = "http://localhost:7474/db/data/node/4237/relationships/all";
		listOfNodesAndRelations = repoHandler.executeQuery(json);
		printList(listOfNodesAndRelations);
		
		//TODO: Add the result to the resultList	
		
				
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
					System.out.println(jsonNode.getId());
					System.out.println(jsonNode.getType());
					System.out.println(jsonNode.getData().size());
				}

			List<IJsonRelation> relationList = listOfNodesAndRelations.get("relationList");
			if(relationList != null)
				for(IJsonRelation jsonRelation: relationList)
				{
					System.out.println(jsonRelation.getId());
					System.out.println(jsonRelation.getStartNode());
					System.out.println(jsonRelation.getEndNode());
					System.out.println(jsonRelation.getType());
					System.out.println(jsonRelation.getData().size());
				}
		}
	}
}
