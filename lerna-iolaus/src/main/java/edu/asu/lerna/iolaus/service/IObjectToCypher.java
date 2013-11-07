package edu.asu.lerna.iolaus.service;

import edu.asu.lerna.iolaus.domain.queryobject.INode;
import edu.asu.lerna.iolaus.domain.queryobject.IOperator;
import edu.asu.lerna.iolaus.domain.queryobject.IProperty;
import edu.asu.lerna.iolaus.domain.queryobject.IQuery;
import edu.asu.lerna.iolaus.domain.queryobject.IRelNode;
import edu.asu.lerna.iolaus.domain.queryobject.IRelationship;
import edu.asu.lerna.iolaus.domain.queryobject.PropertyOf;

public interface IObjectToCypher {
	
	public String objectToCypher(IQuery query);
	public void nodeObject(INode node,PropertyOf propertyOf);
	public void relationshipObject(IRelationship relationship);
	public void operatorObject(IOperator op,PropertyOf propertyOf);
	public void propertyObject(IProperty prop,PropertyOf propertyOf);
	public void relNodeObject(IRelNode relNode,PropertyOf propertyOf);
	public void incrementTarget();
	public void incrementRelationship();
	public void incrementSource();
	/**
	 * direction=true for outgoing
	 * direction=false for incoming
	**/
	public void addRelationToMatch(String node1,String node2,String relation,boolean direction);
}
