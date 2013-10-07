package edu.asu.lerna.iolaus.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import edu.asu.lerna.iolaus.domain.Node;
import edu.asu.lerna.iolaus.repository.NodeRepository;
import edu.asu.lerna.iolaus.service.INetworkManager;

public class NetworkManager implements INetworkManager {

	@Autowired
	public NodeRepository nodeRepository;
	
	@Override
	public void saveNode(Node n){
		nodeRepository.save(n);		
	}
}
