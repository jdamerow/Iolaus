package edu.asu.lerna.iolaus.domain.json.impl;

import java.util.Map;

import edu.asu.lerna.iolaus.domain.json.INode;

public class Node implements INode {

	private long id;
	private Map<String,String> properties;
	private StringBuffer jsonBody;
	private String type;
	
	/**
	 * Constructor for creating a node having properties.
	 * @param type is a type of the node.
	 * @param properties are the properties associated with the node.
	 */
	public Node(String type,Map<String, String> properties){
		this.type=type;
		this.properties=properties;
		createJsonBody(this.type,properties);
	}
	
	/**
	 * Constructor for creating a node without any properties. 
	 * @param type is a type of the node.
	 */
	public Node(String type){
		this.type=type;
		createJsonBody(this.type);
	}

	@Override
	public void setId(long id) {
		this.id=id;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public Map<String, String> getProperties() {
		return properties;
	}
	
	@Override
	public String getJsonBody() {
		return jsonBody.toString();
	}
	
	private void createJsonBody(String type, Map<String, String> properties){
		jsonBody.append("{\n\t");
		if(type!=null){
			jsonBody.append("\"type\" : "+"\""+type+"\"");
		}
		if(properties!=null){
			for(Map.Entry<String,String> property:properties.entrySet()){
					jsonBody.append(",\n\t\""+property.getKey()+"\" : \""+property.getValue()+"\" ");
			}
		}
		jsonBody.append("\n}");
	}
	
	private void createJsonBody(String type) {
		jsonBody.append("{\n\t");
		if(type!=null)
			jsonBody.append("\"type\" : "+"\""+type+"\"");
		jsonBody.append("\n}");
	}	
}
