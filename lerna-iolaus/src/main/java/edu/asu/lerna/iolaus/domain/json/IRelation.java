package edu.asu.lerna.iolaus.domain.json;

import java.util.Map;

public interface IRelation {
	String getEndNode();
	String getStartNode();
	Map<String,String> getData();
	String getType();
	long getId();
	void setId(long id);
	String getJsonBody();
}
