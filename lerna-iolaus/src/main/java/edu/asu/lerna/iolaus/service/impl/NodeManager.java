package edu.asu.lerna.iolaus.service.impl;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.data.neo4j.fieldaccess.DynamicProperties;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.domain.Node;
import edu.asu.lerna.iolaus.domain.Relation;
import edu.asu.lerna.iolaus.repository.NodeRepository;
import edu.asu.lerna.iolaus.repository.RelationRepository;
import edu.asu.lerna.iolaus.service.INodeManager;

@Service
public class NodeManager implements INodeManager {


	private static final Logger logger = LoggerFactory
			.getLogger(NodeManager.class);
	
	@Autowired
	private NodeRepository nodeRepository;
	
	@Autowired
	private RelationRepository relationRepository;
	
	@Override
	public void saveRelation(Relation r){
		relationRepository.save(r);
	}

	/**
	 * @author Lohith Dwaraka
	 * 
	 * Stores node into Neo4j DB through repository proxy 
	 */
	@Override
	public void saveNode(Node n){
		nodeRepository.save(n);
	}
	
	/**
	 *  @author Lohith Dwaraka
	 *  
	 *  Queries Neo4j for Node type - Person
	 *  returns Node if found
	 *  else returns null
	 */
	@Override
	public Node checkGetPerson(String firstName, String lastName){
		
		EndResult<Node> nodeList = nodeRepository.findAllByPropertyValue("type", "Person");
		Iterator<Node> nodeIterator = nodeList.iterator();
		Node target=null;
		while(nodeIterator.hasNext()){
			Node node = nodeIterator.next();
			DynamicProperties properties =node.getProperties();
			String firstNameSample="";
			if(properties.hasProperty("firstName")){
				firstNameSample = (String) properties.getProperty("firstName");
			}
			String lastNameSample = (String) properties.getProperty("lastName");
			if((firstName.equals(firstNameSample))&&(lastName.equals(lastNameSample))){
				target=node;
				return target;
			}
		}
		return target;
	}
	
	/**
	 *  @author Lohith Dwaraka
	 *  
	 *  Queries Neo4j for Node type - Institute
	 *  returns Node if found
	 *  else returns null
	 */
	@Override
	public Node checkGetInstitute(String name){
		
		EndResult<Node> nodeList = nodeRepository.findAllByPropertyValue("type", "Institute");
		Iterator<Node> nodeIterator = nodeList.iterator();
		Node target=null;
		while(nodeIterator.hasNext()){
			Node node = nodeIterator.next();
			DynamicProperties properties =node.getProperties();
			String nameSample = (String) properties.getProperty("instituteName");
			if(name.equals(nameSample)){
				target=node;
				return target;
			}
		}
		return target;
	}
	
	/**
	 *  @author Lohith Dwaraka
	 *  
	 *  Queries Neo4j for Node type - Series
	 *  returns Node if found
	 *  else returns null
	 */
	@Override
	public Node checkGetSeries(String name){
		
		EndResult<Node> nodeList = nodeRepository.findAllByPropertyValue("type", "Series");
		Iterator<Node> nodeIterator = nodeList.iterator();
		Node target=null;
		while(nodeIterator.hasNext()){
			Node node = nodeIterator.next();
			DynamicProperties properties =node.getProperties();
			String nameSample = (String) properties.getProperty("seriesName");
			if(name.equals(nameSample)){
				target=node;
				return target;
			}
		}
		return target;
	}
	
	/**
	 *  @author Lohith Dwaraka
	 *  
	 *  Queries Neo4j for Node type - Location
	 *  returns Node if found
	 *  else returns null
	 */
	@Override
	public Node checkGetLocation(String city, String state){
		
		EndResult<Node> nodeList = nodeRepository.findAllByPropertyValue("type", "Location");
		Iterator<Node> nodeIterator = nodeList.iterator();
		Node target=null;
		while(nodeIterator.hasNext()){
			Node node = nodeIterator.next();
			DynamicProperties properties =node.getProperties();
			String citySample = (String) properties.getProperty("city");
			String stateSample = (String) properties.getProperty("state");
			if((state.equals(stateSample))&&(city.equals(citySample))){
				target=node;
				return target;
			}
		}
		return target;
	}
}
