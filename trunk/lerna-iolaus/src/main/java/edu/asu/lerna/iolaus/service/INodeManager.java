package edu.asu.lerna.iolaus.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.asu.lerna.iolaus.domain.LocationNode;
import edu.asu.lerna.iolaus.domain.Node;
import edu.asu.lerna.iolaus.domain.PersonNode;
import edu.asu.lerna.iolaus.domain.Relation;

public interface INodeManager {

	List<Map<String,List<Node>>> nodeTypeList = new ArrayList<Map<String,List<Node>>>();
	
	public Node saveNode(Node n);

	public void initializeNodeTypeMap(int count);

	public PersonNode checkGetPerson(String firstName, String lastName);
	
	public void saveNodeInMap(int index,Node node,String key);

	public Node checkGetInstitute(String name);

	public Node checkGetSeries(String name);

	public LocationNode checkGetLocation(String address, String street, String city,
			String state, String country);

	void saveRelation(Relation r);

	

}
