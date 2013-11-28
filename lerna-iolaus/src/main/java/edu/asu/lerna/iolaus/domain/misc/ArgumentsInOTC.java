package edu.asu.lerna.iolaus.domain.misc;

import java.util.List;
import java.util.Map;

import edu.asu.lerna.iolaus.domain.queryobject.PropertyOf;

public class ArgumentsInOTC {
	
	Map<String,String> startMap;
	Map<String,String> matchMap ;
	Map<String,List<String>> whereMap;
	PropertyOf propertyOf;
	Map<Object,String> objectToLabelMap;
	String dataSet;
	String targetOperator;
	int currentTarget;
	int currentRelationship;
	
	public String getTargetOperator() {
		return targetOperator;
	}
	public void setTargetOperator(String targetOperator) {
		this.targetOperator = targetOperator;
	}
	public int getCurrentTarget() {
		return currentTarget;
	}
	public void setCurrentTarget(int currentTarget) {
		this.currentTarget = currentTarget;
	}
	public int getCurrentRelationship() {
		return currentRelationship;
	}
	public void setCurrentRelationship(int currentRelationship) {
		this.currentRelationship = currentRelationship;
	}
	
	public Map<String, String> getStartMap() {
		return startMap;
	}
	public void setStartMap(Map<String, String> startMap) {
		this.startMap = startMap;
	}
	public Map<String, String> getMatchMap() {
		return matchMap;
	}
	public void setMatchMap(Map<String, String> matchMap) {
		this.matchMap = matchMap;
	}
	public Map<String, List<String>> getWhereMap() {
		return whereMap;
	}
	public void setWhereMap(Map<String, List<String>> whereMap) {
		this.whereMap = whereMap;
	}
	public PropertyOf getPropertyOf() {
		return propertyOf;
	}
	public void setPropertyOf(PropertyOf propertyOf) {
		this.propertyOf = propertyOf;
	}
	public Map<Object, String> getObjectToLabelMap() {
		return objectToLabelMap;
	}
	public void setObjectToLabelMap(Map<Object, String> objectToLabelMap) {
		this.objectToLabelMap = objectToLabelMap;
	}
	public String getDataSet() {
		return dataSet;
	}
	public void setDataSet(String dataSet) {
		this.dataSet = dataSet;
	}
}
