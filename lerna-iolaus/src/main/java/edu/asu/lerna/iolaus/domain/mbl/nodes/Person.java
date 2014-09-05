package edu.asu.lerna.iolaus.domain.mbl.nodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

	@Override
	public List<String> getPropertyJson(String nodeId) {
		List<String> propertyJson = new ArrayList<String>();
		propertyJson.add(createJson(nodeId, "type", type));
		propertyJson.add(createJson(nodeId, "uri", uri));
		propertyJson.add(createJson(nodeId, "firstname", firstname));
		propertyJson.add(createJson(nodeId, "lastname", lastname));
		propertyJson.add(createJson(nodeId, "dataset", dataset));
		return propertyJson;
	}

}
