package edu.asu.lerna.iolaus.domain;

import org.springframework.data.neo4j.annotation.Indexed;

public class HasLocationRelation extends Relation {
	
	@Indexed(unique=false)
	private int year;
	
	public HasLocationRelation(){
		super();
	}
	
	public HasLocationRelation(Node startNode, Node endNode)
	{
		super(startNode,endNode,"hasLocation");
	}
	
	
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

}
