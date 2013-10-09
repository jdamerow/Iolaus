package edu.asu.lerna.iolaus.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import edu.asu.lerna.iolaus.domain.Node;
import edu.asu.lerna.iolaus.domain.Relation;
import edu.asu.lerna.iolaus.repository.NodeRepository;
import edu.asu.lerna.iolaus.service.INetworkManager;

@Service
public class NetworkManager implements INetworkManager {

	@Autowired
	private Neo4jTemplate template;
	
	@Override
	public void saveRelation(Relation r){
		template.save(r);
	}
		
		@Override
		public void saveNode(Node n){
			template.save(n);
	}
}
