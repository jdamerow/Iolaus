package edu.asu.lerna.iolaus.domain;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.Indexed;
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
	
	@Indexed(unique=false)
	private String uri;
	
	@Indexed(unique=false)
	private String serviceId;
	
	private String label;
	
	private String dataset;
	
	
	//TODO: Check if DynamicProperties work for Relationship too ! As per javadocs it works with @NodeEntity
	/*@GraphProperty
	private DynamicProperties properties;*/
	
	@RelationshipType
	private String relationshipType;
	
	public Relation(){
		super();
	}
	
	public Relation(Node startNode, Node endNode,String relationshipType)
	{
		super();
		this.startNode = startNode;
		this.endNode = endNode;
		this.relationshipType = relationshipType;
	}
	
	/*public void setProperties(DynamicProperties properties){
		this.properties=properties;
	}
	
	public DynamicProperties getProperties(){
		return properties;
	}*/
	
	public void setId(Long id){
		this.id=id;
	}
	
	public Long getId(){
		return id;
	}
	
	public String getServiceId(){
		return serviceId;
	}
	
	public void setServiceId(String serviceId){
		this.serviceId=serviceId;
	}
	
	public String getUri(){
		return uri;
	}
	
	public void setUri(String uri){
		this.uri=uri;
	}
	
	public String getRelationshipType(){
		return relationshipType;
	}
	
	/*public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}*/

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
	
}
