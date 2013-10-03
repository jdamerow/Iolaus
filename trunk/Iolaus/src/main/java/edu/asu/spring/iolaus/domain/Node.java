package edu.asu.spring.iolaus.domain;

import java.util.Set;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.annotation.RelatedToVia;
import org.springframework.data.neo4j.fieldaccess.DynamicProperties;
import org.springframework.data.neo4j.fieldaccess.DynamicPropertiesContainer;

@NodeEntity
public class Node {
	
  @GraphId 
  Long id;

  @Indexed(unique=false)
  private String type;

  @Indexed(unique=false)
  private String label;
  
  private String dataset;
  
  @GraphProperty
  DynamicProperties properties;
  
}