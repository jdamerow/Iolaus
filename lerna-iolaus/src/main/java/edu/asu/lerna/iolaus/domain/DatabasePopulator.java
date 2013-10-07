package edu.asu.lerna.iolaus.domain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.fieldaccess.DynamicProperties;
import org.springframework.data.neo4j.fieldaccess.DynamicPropertiesContainer;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.transaction.annotation.Transactional;


public class DatabasePopulator {
	
	@Autowired Neo4jTemplate template;
	@Transactional
	public void populateDatabase() {
	
	Node person=new Node();
	DynamicProperties properties=new DynamicPropertiesContainer();
	properties.setProperty("First Name","John");
	properties.setProperty("Last Name","Maclean");
	
	person.setId(1L);
	person.setDataset("Marine Biology");
	person.setType("Person");
	person.setLabel("John");
	person.setProperties(properties);
	
	Node institute=new Node();
	institute.setId(2L);
	institute.setDataset("Marine Biology");
	institute.setType("Institute");
	institute.setLabel("Clark University");
	
	Node location=new Node();
	properties=new DynamicPropertiesContainer();
	properties.setProperty("City","Tempe");
	properties.setProperty("State","Arizona");
	location.setId(3L);
	location.setDataset("Marine Biology");
	location.setType("Location");
	location.setLabel("Tempe");
	location.setProperties(properties);
	
	Node series=new Node();
	series.setId(4L);
	series.setDataset("Marine Biology");
	series.setType("Series");
	series.setLabel("Botany");

	
	Relation rel=new Relation(person,institute,"AffiliatedTo");
	rel.setId(1L);
	properties=new DynamicPropertiesContainer();
	properties.setProperty("Role","Chairman");
	properties.setProperty("Year", 1989);
	rel.setProperties(properties);
	//person.addRelationship(rel);
	
	rel=new Relation(person, location, "StaysIn");
	rel.setId(2L);
	properties=new DynamicPropertiesContainer();
	properties.setProperty("Year",1991);
	rel.setProperties(properties);
	//person.addRelationship(rel);
	
	rel=new Relation(person,series,"Attends");
	rel.setId(3L);
	properties=new DynamicPropertiesContainer();
	properties.setProperty("Role", "Instructor");
	properties.setProperty("Year", 1989);
	rel.setProperties(properties);
	//person.addRelationship(rel);
	
	template.save(person);
	
	rel=new Relation(institute, location, "LocatesAt");
	rel.setId(4L);
	properties=new DynamicPropertiesContainer();
	properties.setProperty("Year", 1989);
	
	template.save(institute);
	
	}
}
