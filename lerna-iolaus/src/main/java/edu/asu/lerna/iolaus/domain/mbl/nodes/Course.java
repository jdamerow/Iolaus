package edu.asu.lerna.iolaus.domain.mbl.nodes;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class Course extends Node {
	
	private String type = "Course";
	private String uri;
	private String name;
	private int year;
	private String dataset = "mblcourses";
	
	public Course(String uri, String name, int year) {
		this.uri = uri;
		this.name = name;
		this.year = year;
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

	public int getYear() {
		return year;
	}

	public String toJson() throws JsonGenerationException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(this);
		return json;
	}

}


