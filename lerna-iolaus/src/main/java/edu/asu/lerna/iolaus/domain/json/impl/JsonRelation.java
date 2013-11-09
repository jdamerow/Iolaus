package edu.asu.lerna.iolaus.domain.json.impl;

import java.util.HashMap;

import edu.asu.lerna.iolaus.domain.json.IJsonRelation;

public class JsonRelation implements IJsonRelation {
	
	private String id;
	private String type;
	private String startNode;
	private String endNode;
	private HashMap<String, String> data;

	public JsonRelation()
	{
		data = new HashMap<String, String>();
	}
	
	public String getEndNode() {
		return endNode;
	}
	public void setEndNode(String endNode) {
		this.endNode = endNode;
	}
	public String getStartNode() {
		return startNode;
	}
	public void setStartNode(String startNode) {
		this.startNode = startNode;
	}
	@Override
	public String getId() {
		return id;
	}
	@Override
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String getType() {
		return type;
	}
	@Override
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public HashMap<String, String> getData() {
		return data;
	}
	@Override
	public void setData(HashMap<String, String> data) {
		this.data = data;
	}

}
