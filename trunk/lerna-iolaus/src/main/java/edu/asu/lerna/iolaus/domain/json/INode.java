package edu.asu.lerna.iolaus.domain.json;

import java.util.Map;

public interface INode {
	
	void setId(long id);
	long getId();
	Map<String,String> getProperties();
	String getJsonBody();
}
