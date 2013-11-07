package edu.asu.lerna.iolaus.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.domain.Node;
import edu.asu.lerna.iolaus.repository.NodeRepository;
import edu.asu.lerna.iolaus.service.IRepositoryHandler;

@Service
public class RepositoryHandler implements IRepositoryHandler {
	
	@Autowired
	private NodeRepository nodeRepository;
	
	@Override
	public void executeQuery(String json)
	{
		List<Node> nodeList = new ArrayList<Node>();
		EndResult<Node> nodeResult = nodeRepository.query(json,null);
		Iterator<Node> iterator = nodeResult.iterator();

		while (iterator.hasNext()) {
			nodeList.add(iterator.next());
		}
		
		System.out.println("Size of nodes fetched from database: "+nodeList.size());
		for(Node node: nodeList)
		{
			System.out.println("_________________________________________");
			System.out.println(node.getType());
			System.out.println(node.getDataset());
			System.out.println(node.getLabel());
		}
	}
}