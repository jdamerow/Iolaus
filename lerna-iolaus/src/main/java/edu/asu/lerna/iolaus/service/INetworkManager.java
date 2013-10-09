package edu.asu.lerna.iolaus.service;

import edu.asu.lerna.iolaus.domain.Node;
import edu.asu.lerna.iolaus.domain.Relation;

public interface INetworkManager {

	void saveRelation(Relation r);
	void saveNode(Node n);

}
