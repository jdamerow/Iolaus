package edu.asu.lerna.iolaus.domain.json.impl;

import java.util.Map;

import edu.asu.lerna.iolaus.domain.json.IRelation;

public class Relation implements IRelation {

	private long id;
	private String startNode;
	private String type;
	private String endNode;
	private Map<String,String> data;
	private StringBuffer jsonBody;
	
	/**
	 * Constructor for creating a relationship without properties.
	 * @param startNode is id of start node.
	 * @param endNode is id of end node.
	 * @param type is a type of the relationship.
	 */
	public Relation(String startNode,String endNode,String type){
		this.startNode=startNode;
		this.endNode=endNode;
		this.type=type;
		createJsonBody(endNode,type);
	}

	/**
	 * Constructor for creating relationship having some properties. 
	 * @param startNode is id of start node.
	 * @param endNode is id of end node.
	 * @param type is a type of the relationship.
	 * @param properties is the properties of the relationships.
	 */
	public Relation(String startNode,String endNode,String type,Map<String,String> properties){
		this.startNode=startNode;
		this.endNode=endNode;
		this.type=type;
		this.data=properties;
		createJsonBody(endNode, type,properties);
	}

	@Override
	public void setId(long id) {
		this.id=id;
	}
	
	@Override
	public String getType() {
		return type;
	}


	@Override
	public long getId() {
		return id;
	}
	
	@Override
	public String getEndNode() {
		return endNode;
	}
	
	@Override
	public Map<String, String> getData() {
		return data;
	}
	
	@Override
	public String getStartNode() {
		return startNode;
	}
	
	@Override
	public String getJsonBody() {
		return jsonBody.toString();
	}
	
	private void createJsonBody(String endNode, String type) {
		if(endNode==null || type==null)
			jsonBody=null;
		else{
			jsonBody.append("{\n");
			jsonBody.append("\t\"to : "+"\""+endNode+"\" ,\n");
			jsonBody.append("\t\"type : "+"\""+type+"\"\n");
			jsonBody.append("}");
		}
	}
	
	private void createJsonBody(String endNode, String type,Map<String, String> properties) {
		if(endNode==null || type==null)
			jsonBody=null;
		else{
			jsonBody.append("{\n");
			jsonBody.append("\t\"to : "+"\""+endNode+"\"");
			jsonBody.append(" ,\n\t\"type : "+"\""+type+"\" ,");
			if(properties!=null){
				boolean firstProperty=true;
				jsonBody.append("\"data\" : {");
				for(Map.Entry<String,String> property:properties.entrySet()){
					if(firstProperty){
						jsonBody.append("\n\t\""+property.getKey()+" : "+"\""+property.getValue()+"\"");
						firstProperty=false;
					}else{
						jsonBody.append(" ,\n\t\""+property.getKey()+" : "+"\""+property.getValue()+"\"");
					}
				}
				jsonBody.append("}");
			}
			jsonBody.append("\n}");
		}
	}
}
