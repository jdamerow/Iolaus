package edu.asu.lerna.iolaus.domain.json.impl;

import java.io.Serializable;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import edu.asu.lerna.iolaus.domain.json.IJsonNode;

@XmlRootElement
public class JsonNode implements IJsonNode, Serializable {
 
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
	
	@XmlElement
	@Override
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public String getType() {
		return type;
	}
	
	@XmlElement
	@Override
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public HashMap<String, String> getData() {
		return data;
	}
	
	@XmlElement
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
	
	public static class Adapter extends XmlAdapter<JsonNode, IJsonNode>
	{

		@Override
		public IJsonNode unmarshal(JsonNode v) throws Exception {
			return v;
		}

		@Override
		public JsonNode marshal(IJsonNode v) throws Exception {
			return (JsonNode)v;
		}
		
	}
	
}
