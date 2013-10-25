package edu.asu.lerna.iolaus.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.EndResult;
import org.springframework.data.neo4j.fieldaccess.DynamicProperties;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.domain.LocationNode;
import edu.asu.lerna.iolaus.domain.Node;
import edu.asu.lerna.iolaus.domain.PersonNode;
import edu.asu.lerna.iolaus.domain.Relation;
import edu.asu.lerna.iolaus.repository.NodeRepository;
import edu.asu.lerna.iolaus.repository.RelationRepository;
import edu.asu.lerna.iolaus.service.INodeManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.context.support.FileSystemXmlApplicationContext;

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
	public Node saveNode(Node n){
		Node savedNode=nodeRepository.save(n);
		return savedNode;
	}
	
	/**
	 *  @author Karan Kothari
	 *  Initialize HashMaps for every single type
	 */
	@Override
	public void initializeNodeTypeMap(int count){
		for(int i=1;i<=count;i++)
			nodeTypeList.add(new HashMap<String,List<Node>>());
	}
	
	/**
	 *  @author Karan Kothari
	 *  First check if node is already in Hash Map
	 *  If it is there then check those with the nodeList for that key
	 *  if that node is not found then add it to the Hash Map
	 */
	
	@Override
	public void saveNodeInMap(int index,Node node,String key){
		Map<String,List<Node>> nodeMap=nodeTypeList.get(index);
		
		if(nodeMap.containsKey(key)){
			List<Node> nodeList=nodeMap.get(key);
			nodeList.add(node);
		}
		else{
			List<Node> nodeList=new ArrayList<Node>();
			nodeList.add(node);
			nodeMap.put(key, nodeList);	
		}
	}
	
	/**
	 *  @author Lohith Dwaraka
	 *  
	 *  Queries Neo4j for Node type - Person
	 *  returns Node if found
	 *  else returns null
	 */
	
	/**
	 *  @author Karan Kothari
	 *  Compare key with the map and if key exists
	 *  then compare it with all of the nodes for that key 
	 *  otherwise return null
	 */
	@Override
	public PersonNode checkGetPerson(String firstName, String lastName){
		
		Map<String,List<Node>> nodeMap=nodeTypeList.get(0);
		PersonNode target=null;
		List<Node> nodeList;
		if(nodeMap.containsKey(firstName))
			nodeList=nodeMap.get(firstName);
		else
			return null;
		Iterator<Node> nodeIterator = nodeList.iterator();
		while(nodeIterator.hasNext()){
			PersonNode node=(PersonNode)nodeIterator.next();
			String lastNameSample=node.getLastName();
			if(lastName.equals(lastNameSample)){
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
	/**
	 *  @author Karan Kothari
	 *  if key is there then return corresponding node
	 *  otherwise return null
	 */
	@Override
	public Node checkGetInstitute(String name){
		
		Map<String,List<Node>> nodeMap=nodeTypeList.get(1);
		List<Node> nodeList=nodeMap.get(name);
		/*EndResult<Node> nodeList = nodeRepository.findAllByPropertyValue("type", "Institute");*/
		Node target=null;
		if(nodeList!=null)
			target=nodeList.get(0);
		return target;
	}
	
	/**
	 *  @author Lohith Dwaraka
	 *  
	 *  Queries Neo4j for Node type - Series
	 *  returns Node if found
	 *  else returns null
	 */
	/**
	 *  @author Karan Kothari
	 *  if key is there then return corresponding node
	 *  otherwise return null
	 */
	@Override
	public Node checkGetSeries(String name){
		
		Map<String,List<Node>> nodeMap=nodeTypeList.get(2);
		List<Node> nodeList=nodeMap.get(name);
		/*EndResult<Node> nodeList = nodeRepository.findAllByPropertyValue("type", "Series");*/
		Node target=null;
		if(nodeList!=null)
			target=nodeList.get(0);
		return target;
	}
	
	/**
	 *  @author Lohith Dwaraka
	 *  
	 *  Queries Neo4j for Node type - Location
	 *  returns Node if found
	 *  else returns null
	 */
	
	/**
	 *  @author Karan Kothari
	 *  Compare key with the map and if key exists
	 *  then compare it with all of the nodes for that key 
	 *  otherwise return null
	 */
	@Override
	public LocationNode checkGetLocation(String address,String street,String city, String state,String country){
		
		Map<String,List<Node>> nodeMap=nodeTypeList.get(3);
		LocationNode target=null;
		List<Node> nodeList;
		if(nodeMap.containsKey(city))
			nodeList=nodeMap.get(city);
		else
			return null;
		Iterator<Node> nodeIterator = nodeList.iterator();
		while(nodeIterator.hasNext()){
			LocationNode node=(LocationNode)nodeIterator.next();
			String addressSample=node.getAddress();
			String streetSample=node.getStreet();
			String stateSample=node.getState();
			String countrySample=node.getCountry();
			
			if(address.equals(addressSample)&&street.equals(streetSample)&&state.equals(stateSample)&&country.equals(countrySample)){
				target=node;
			}
		}
		return target;
	}
}