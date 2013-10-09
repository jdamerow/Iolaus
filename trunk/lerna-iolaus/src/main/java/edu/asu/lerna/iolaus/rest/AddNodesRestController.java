package edu.asu.lerna.iolaus.rest;

import java.security.Principal;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.fieldaccess.DynamicProperties;
import org.springframework.data.neo4j.fieldaccess.DynamicPropertiesContainer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.lerna.iolaus.domain.Node;
import edu.asu.lerna.iolaus.domain.Relation;
import edu.asu.lerna.iolaus.repository.NodeRepository;
import edu.asu.lerna.iolaus.repository.RelationRepository;
import edu.asu.lerna.iolaus.service.INetworkManager;


/**
 * Controller for Iolaus related rest api's exposed to other clients
 * 
 * @author Lohith Dwaraka
 * 
 */
@Controller
public class AddNodesRestController {

	private static final Logger logger = LoggerFactory
			.getLogger(AddNodesRestController.class);
	
	@Autowired
	private INetworkManager networkManager;
	

//	@RequestMapping(value = "/", method = RequestMethod.GET)
//	public String validUserHandle(ModelMap model, Principal principal) {
//
//		System.out.println("Inside home controller mapping.......");
//		return "home";
//
//	}
//	
	
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/addnodes", method = RequestMethod.GET)
	public String addNodes(Model model) {
		
		Node person = new Node();
		
		//node.setId( UUID.randomUUID().getMostSignificantBits());
		person.setLabel("Lohith");
		person.setDataset("Marine Biology");
		person.setType("Person");
		DynamicProperties properties = new DynamicPropertiesContainer();
		properties.setProperty("firstName","Dwaraka");
		properties.setProperty("lastName","Lohith");
		person.setProperties(properties);
		
		Node location=new Node();
		properties=new DynamicPropertiesContainer();
		properties.setProperty("City","Tempe");
		properties.setProperty("State","Arizona");
		location.setDataset("Marine Biology");
		location.setType("Location");
		location.setLabel("Tempe");
		location.setProperties(properties);
		
		Relation rel=new Relation(person, location,"StaysIn");
		properties=new DynamicPropertiesContainer();
		properties.setProperty("Year",1991);
		rel.setProperties(properties); 
		
		//person.addRelationship(rel);
		networkManager.saveNode(person);
		networkManager.saveNode(location);
		networkManager.saveRelation(rel);
		return "home";
	}

}
