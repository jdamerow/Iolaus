package edu.asu.lerna.iolaus.service;

import edu.asu.lerna.iolaus.domain.Node;
import edu.asu.lerna.iolaus.domain.Relation;

public interface INodeManager {

	void saveRelation(Relation r);
	void saveNode(Node n);
	Node checkGetLocation(String city, String state);
	Node checkGetSeries(String name);
	Node checkGetInstitute(String name);
	Node checkGetPerson(String firstName, String lastName);

}
