package edu.asu.lerna.iolaus.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.domain.Node;
import edu.asu.lerna.iolaus.repository.NodeRepository;
import edu.asu.lerna.iolaus.service.INetworkManager;

@Service
public class NetworkManager implements INetworkManager {

	@Autowired
	private NodeRepository nodeRepository;
	
	@Override
	public void saveNode(Node n){
		nodeRepository.save(n);
	}
}
