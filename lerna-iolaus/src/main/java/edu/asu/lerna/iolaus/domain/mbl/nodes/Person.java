package edu.asu.lerna.iolaus.domain.mbl.nodes;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class Person extends Node {

	private String type = "Person";
	private String uri;
	private String firstname;
	private String lastname;
	private String dataset = "mblcourses";
	
	public Person(String uri, String firstname, String lastname) {
		this.uri = uri;
		this.firstname = firstname;
		this.lastname = lastname;
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

	public String getFirstname() {
		return firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public String toJson() throws JsonGenerationException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(this);
		return json;
	}
	
}
