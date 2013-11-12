package edu.asu.lerna.iolaus.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.asu.lerna.iolaus.domain.queryobject.INode;
import edu.asu.lerna.iolaus.domain.queryobject.IOperator;
import edu.asu.lerna.iolaus.domain.queryobject.IProperty;
import edu.asu.lerna.iolaus.domain.queryobject.IQuery;
import edu.asu.lerna.iolaus.domain.queryobject.IRelNode;
import edu.asu.lerna.iolaus.domain.queryobject.IRelationship;
import edu.asu.lerna.iolaus.domain.queryobject.PropertyOf;

public interface IObjectToCypher {
	
	public String objectToCypher(INode node);
	//public String objectToCypher(IRelNode node);

	/**
	 * direction=true for outgoing
	 * direction=false for incoming
	**/
	public void addToStart(Map<String,String> startMap,String label,String prop);
	//void nestedRelNodeObject(IRelNode node, PropertyOf propertyOf);
	
	void propertyObject(IProperty prop, PropertyOf propertyOf,
			Map<String, String> startMap, Map<String, List<String>> whereMap,
			int currentTarget, int currentRelationship);
	
	void addRelationToMatch(String node1, String node2, String relation,
			boolean direction, Map<String, String> matchMap);

	String nodeObject(INode node, PropertyOf propertyOf,
			Map<String, String> startMap, Map<String, List<String>> whereMap,
			Map<String, String> matchMap);

	List<Integer> operatorObject(IOperator op, PropertyOf propertyOf,
			Map<String, String> startMap, Map<String, List<String>> whereMap,
			Map<String, String> matchMap, int currentTarget,
			int currentRelationship);

	List<Integer> relationshipObject(IRelationship relationship,
			Map<String, String> startMap, Map<String, List<String>> whereMap,
			Map<String, String> matchMap, int currentTarget,
			int currentRelationship);

	void relNodeObject(IRelNode relNode, PropertyOf propertyOf,
			Map<String, String> startMap, Map<String, List<String>> whereMap,
			int currentTarget, int currentRelationship, String targetOperator);
}
