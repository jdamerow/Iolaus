package edu.asu.lerna.iolaus.domain.mbl.nodes;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class Location extends Node {
	
	private String type = "Location";
	private String uri;
	private String location;
	private String dataset = "mblcourses";
	
	public Location(String uri, String location) {
		this.uri = uri;
		this.location = location;
	}
	
	public String getType() {
		return type;
	}
	
	public String getDataset() {
		return dataset;
	}
	
	public String getUri() {
		return uri;
	}

	public String getLocation() {
		return location;
	}

	public String toJson() throws JsonGenerationException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(this);
		return json;
	}
}
