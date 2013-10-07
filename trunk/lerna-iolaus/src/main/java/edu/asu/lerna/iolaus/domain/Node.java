package edu.asu.lerna.iolaus.domain;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedToVia;
import org.springframework.data.neo4j.fieldaccess.DynamicProperties;

@NodeEntity
public class Node {

	@GraphId 
	private Long id;

	@Indexed(unique=false)
	private String type;

	@Indexed(unique=false)
	private String label;

	private String dataset;

	@GraphProperty
	private DynamicProperties properties;
	
//	@RelatedToVia
//	private Set<Relation> relationships;

	//TODO: Add proper constructors and initialize the class objects
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDataset() {
		return dataset;
	}

	public void setDataset(String dataset) {
		this.dataset = dataset;
	}

	public DynamicProperties getProperties() {
		return properties;
	}

	public void setProperties(DynamicProperties properties) {
		this.properties = properties;
	}

//	public Set<Relation> getRelationships() {
//		return relationships;
//	}
//
//	public void setRelationships(Set<Relation> relationships) {
//		this.relationships = relationships;
//	}
//	
//	public void addRelationship(Relation relation)
//	{
//		if(this.relationships == null)
//		{
//			this.relationships = new HashSet<Relation>();
//		}
//		this.relationships.add(relation);
//	}

}