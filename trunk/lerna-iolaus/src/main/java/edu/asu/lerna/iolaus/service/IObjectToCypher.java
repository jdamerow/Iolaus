package edu.asu.lerna.iolaus.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.asu.lerna.iolaus.domain.misc.ReturnParametersOfOTC;
import edu.asu.lerna.iolaus.domain.queryobject.INode;
import edu.asu.lerna.iolaus.domain.queryobject.IOperator;
import edu.asu.lerna.iolaus.domain.queryobject.IProperty;
import edu.asu.lerna.iolaus.domain.queryobject.IQuery;
import edu.asu.lerna.iolaus.domain.queryobject.IRelNode;
import edu.asu.lerna.iolaus.domain.queryobject.IRelationship;
import edu.asu.lerna.iolaus.domain.queryobject.PropertyOf;

public interface IObjectToCypher {
	
	ReturnParametersOfOTC objectToCypher(INode node);
	ReturnParametersOfOTC objectToCypher(IRelNode node);
	//public String objectToCypher(IRelNode node);
	
	String s="Start ";
	String m="Match ";
	String w="Where ";
	String r="return ";

	/**
	 * direction=true for outgoing
	 * direction=false for incoming
	*/
	/*void addToStart(Map<String,String> startMap,String label,String prop);
	
	void propertyObject(IProperty prop, PropertyOf propertyOf,
			Map<String, String> startMap, Map<String, List<String>> whereMap,
			int currentTarget, int currentRelationship);
	
	void addRelationToMatch(String node1, String node2, String relation,
			boolean direction, Map<String, String> matchMap);


	void relNodeObject(IRelNode relNode, PropertyOf propertyOf,
			Map<String, String> startMap, Map<String, List<String>> whereMap,
			int currentTarget, int currentRelationship, String targetOperator,String dataSet);

	String nodeObject(INode node, PropertyOf propertyOf,
			Map<String, String> startMap, Map<String, List<String>> whereMap,
			Map<String, String> matchMap, Map<Object, String> objectToLabelMap,
			String dataSet);
	
	List<Integer> relationshipObject(IRelationship relationship,
			Map<String, String> startMap, Map<String, List<String>> whereMap,
			Map<String, String> matchMap, Map<Object, String> objectToLabelMap,
			int currentTarget, int currentRelationship, String dataSet);
	String nestedRelNodeObject(IRelNode relNode, PropertyOf propertyOf,
			Map<String, String> startMap, Map<String, List<String>> whereMap,
			Map<String, String> matchMap, Map<Object, String> objectToLabelMap,
			String dataSet);
	List<Integer> operatorObject(IOperator op, PropertyOf propertyOf,
			Map<String, String> startMap, Map<String, List<String>> whereMap,
			Map<String, String> matchMap, Map<Object, String> objectToLabelMap,
			String targetOperator, int currentTarget, int currentRelationship,
			String dataSet);*/
}
