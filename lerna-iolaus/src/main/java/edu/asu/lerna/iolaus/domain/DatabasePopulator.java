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
	
	Node newNode1=new Node();
	DynamicProperties properties=new DynamicPropertiesContainer();
	properties.setProperty("First Name","John");
	properties.setProperty("Last Name","Maclean");
	
	newNode1.setId(1L);
	newNode1.setDataset("Marine Biology");
	newNode1.setType("Person");
	newNode1.setLabel("John");
	newNode1.setProperties(properties);
	template.save(newNode1);
	
	Node newNode2=new Node();
	newNode2.setId(2L);
	newNode2.setDataset("Marine Biology");
	newNode2.setType("Institute");
	newNode2.setLabel("Clark University");
	template.save(newNode2);
	
	Node newNode3=new Node();
	DynamicProperties properties2=new DynamicPropertiesContainer();
	properties2.setProperty("City","Tempe");
	properties2.setProperty("State","Arizona");
	newNode3.setId(3L);
	newNode3.setDataset("Marine Biology");
	newNode3.setType("Location");
	newNode3.setLabel("Tempe");
	newNode3.setProperties(properties2);
	template.save(newNode3);
	
	Node newNode4=new Node();
	newNode4.setId(4L);
	newNode4.setDataset("Marine Biology");
	newNode4.setType("Series");
	newNode4.setLabel("Botany");
	template.save(newNode4);
	
	Relation rel=new Relation(newNode1,newNode2,"AffiliatedTo");
	rel.setId(1L);
	DynamicProperties properties3=new DynamicPropertiesContainer();
	properties3.setProperty("Year",1991);
	rel.setProperties(properties3);
	newNode1.addRelationship(rel);
	template.save(newNode1);
	}
}
