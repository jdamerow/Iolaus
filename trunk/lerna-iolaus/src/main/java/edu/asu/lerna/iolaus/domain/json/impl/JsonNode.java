package edu.asu.lerna.iolaus.domain.json.impl;

import java.util.HashMap;

import edu.asu.lerna.iolaus.domain.json.IJsonNode;

public class JsonNode implements IJsonNode {
 
	private String id;
	private String type;
	private HashMap<String, String> data;
	
	public JsonNode()
	{
		data = new HashMap<String, String>();
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
	@Override
	public void addData(String key, String value){
		if(this.data == null)
			this.data = new HashMap<String, String>();
		this.data.put(key, value);
	}
	
}
