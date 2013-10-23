package edu.asu.lerna.iolaus.domain;

import org.springframework.data.neo4j.annotation.Indexed;

public class AffiliatedWithRelation extends Relation{
	
	@Indexed(unique=false)
	private String role;
	
	@Indexed(unique=false)
	private int year;
	
	public AffiliatedWithRelation(){
		super();
	}
	
	public AffiliatedWithRelation(Node startNode, Node endNode)
	{
		super(startNode,endNode,"affiliatedWith");
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
