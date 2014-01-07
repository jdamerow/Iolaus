package edu.asu.lerna.iolaus.domain;

import org.springframework.data.neo4j.annotation.Indexed;

public class PersonNode extends Node{
	
	@Indexed(unique=false)
	private String firstName;
	
	@Indexed(unique=false)					
	private String lastName;
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
}
