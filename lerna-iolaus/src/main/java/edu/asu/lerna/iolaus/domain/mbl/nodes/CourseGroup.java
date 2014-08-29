package edu.asu.lerna.iolaus.domain.mbl.nodes;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class CourseGroup extends Node {
	
	String type = "CourseGroup";
	private String uri;
	private String name;
	private String dataset = "mblcourses";
	
	public CourseGroup(String uri, String name) {
		this.uri = uri;
		this.name = name;
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

	public String getName() {
		return name;
	}

	public String toJson() throws JsonGenerationException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(this);
		return json;
	}
	
}
