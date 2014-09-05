package edu.asu.lerna.iolaus.domain.mbl.nodes;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

public abstract class Node {
	public abstract String toJson() throws JsonGenerationException, JsonMappingException, IOException;
	public abstract String getUri();
	public abstract List<String> getPropertyJson(String nodeId);
	
	protected String createJson(String nodeId, String key, String value) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		json.append("\"key\" : \"" + key +"\",");
		json.append("\"uri\" : \"" + nodeId + "\",");
		json.append("\"value\" : \"" + value +"\"");
		json.append("}");
		return json.toString();
	}
	
	protected String createJson(String nodeId, String key, int value) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		json.append("\"key\" : \"" + key +"\",");
		json.append("\"uri\" : \"" + nodeId + "\",");
		json.append("\"value\" : " + value);
		json.append("}");
		return json.toString();
	}
}
