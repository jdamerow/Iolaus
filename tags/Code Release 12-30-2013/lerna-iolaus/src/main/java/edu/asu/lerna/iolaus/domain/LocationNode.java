package edu.asu.lerna.iolaus.domain;

import org.springframework.data.neo4j.annotation.Indexed;

public class LocationNode extends Node{
	
	@Indexed(unique=false)
	private String address;
	
	@Indexed(unique=false)
	private String street;
	
	@Indexed(unique=false)
	private String city;
	
	@Indexed(unique=false)
	private String state;
	
	@Indexed(unique=false)
	private String country;
	
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}

