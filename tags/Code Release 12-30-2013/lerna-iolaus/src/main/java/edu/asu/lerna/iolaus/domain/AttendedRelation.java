package edu.asu.lerna.iolaus.domain;

import org.springframework.data.neo4j.annotation.Indexed;

public class AttendedRelation extends Relation {
	
	@Indexed(unique=false)
	private String role;
	
	@Indexed(unique=false)
	private int year;
	
	public AttendedRelation(){
		super();
	}
	
	public AttendedRelation(Node startNode, Node endNode)
	{
		super(startNode,endNode,"attended");
	}
	
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

}
