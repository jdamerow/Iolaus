package edu.asu.spring.iolaus.domain;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.RelationshipType;
import org.springframework.data.neo4j.annotation.StartNode;
import org.springframework.data.neo4j.fieldaccess.DynamicProperties;

@RelationshipEntity
public class Relation {

	@GraphId 
	private Long id;
	
	@Fetch @StartNode
	private Node startNode;
	
	@Fetch @EndNode
	private Node endNode;
	
	//TODO: Check if DynamicProperties work for Relationship too ! As per javadocs it works with @NodeEntity
	@GraphProperty
	private DynamicProperties properties;
	
	@RelationshipType
	private String relationshipType;
	
	public Relation()
	{
		super();
	}

	public Relation(Node startNode, Node endNode, String relationshipType)
	{
		super();
		this.startNode = startNode;
		this.endNode = endNode;
		this.relationshipType = relationshipType;
	}
}
