package edu.asu.lerna.iolaus.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.fieldaccess.DynamicProperties;
import org.springframework.data.neo4j.fieldaccess.DynamicPropertiesContainer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import edu.asu.lerna.iolaus.domain.Node;
import edu.asu.lerna.iolaus.domain.Relation;
import edu.asu.lerna.iolaus.service.INodeManager;


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
	private INodeManager nodeManager;

	
	/**
	 * @author Lohith Dwaraka
	 * 
	 * Upload a file of MBL object into Neo4J
	 * @throws IOException 
	 */
	@RequestMapping(value = "/uploadnodes", method = RequestMethod.POST)
	public String addNodesFile(@RequestParam("file") MultipartFile file,Model model) throws IOException {
		
        logger.info("file Name "+file.getOriginalFilename());
        BufferedReader  b=null;
        try {
			b = new BufferedReader (new InputStreamReader(file.getInputStream()));
		} catch (IOException e) {
			logger.error("IO Error, File not uploaded",e);
		}
        
        String line;
        try {
			while((line=b.readLine())!=null){
				createMBLObject(line);
			}
		} catch (IOException e) {
			logger.error("IO Error, File not uploaded",e);
		}
        b.close();
        logger.info("Added all the nodes from the input");
		
		return "home";
	}
	
	
	
	/**
	 * @author Lohith Dwaraka
	 * @param singleData
	 * 
	 * Adds a MBL Object
	 */
	public void createMBLObject(String singleData){
		String data [] = singleData.split(",");
		String year=data[0];
		String firstName=data[1];
		String lastName=data[2];
		String instituteName=data[3];
		String instituteRole=data[4];
		String seriesName=data[5];
		String seriesRole=data[6];
		String locationStreet=data[7];
		String locationCity=data[8];
		String locationState=data[9];
		String dataset=data[10];
		
		
		
		Node person = nodeManager.checkGetPerson(firstName,lastName);
		if(person ==null){
			person = new Node();
			//node.setId( UUID.randomUUID().getMostSignificantBits());
			person.setLabel(lastName);
			person.setDataset(dataset);
			person.setType("Person");
			DynamicProperties properties = new DynamicPropertiesContainer();
			properties.setProperty("firstName",firstName);
			properties.setProperty("lastName",lastName);
			person.setProperties(properties);
			nodeManager.saveNode(person);
		}else{
			logger.debug("Person Already exist ");
		}
		
		
		Node location = nodeManager.checkGetLocation(locationCity, locationState);
		if(location == null){
			location=new Node();
			DynamicProperties properties = new DynamicPropertiesContainer();
			properties=new DynamicPropertiesContainer();
			properties.setProperty("city",locationCity);
			properties.setProperty("state",locationState);
			location.setDataset(dataset);
			location.setType("Location");
			location.setLabel(locationCity);
			location.setProperties(properties);
			nodeManager.saveNode(location);
		}else{
			logger.debug("Location Already exist ");
		}
		
		Node series = nodeManager.checkGetSeries(seriesName);
		if(series == null){
			series=new Node();
			DynamicProperties properties = new DynamicPropertiesContainer();
			properties=new DynamicPropertiesContainer();
			properties.setProperty("seriesName",seriesName);
			series.setDataset(dataset);
			series.setType("Series");
			series.setLabel(seriesName);
			series.setProperties(properties);
			nodeManager.saveNode(series);
		}else{
			logger.debug("series Already exist ");
		}
		
		
		Node institute = nodeManager.checkGetInstitute(instituteName);
		if(institute == null){
			institute=new Node();
			DynamicProperties properties = new DynamicPropertiesContainer();
			properties=new DynamicPropertiesContainer();
			properties.setProperty("instituteName",instituteName);
			institute.setDataset(dataset);
			institute.setType("Institute");
			institute.setLabel(instituteName);
			institute.setProperties(properties);
			nodeManager.saveNode(institute);
		}else{
			logger.debug("institute Already exist ");
		}
		
		/**
		 * Below part is for relation and node serving Person <- StaysIn-> location
		 */
		Relation staysIn=new Relation(person, location,"StaysIn");
		DynamicProperties properties = new DynamicPropertiesContainer();
		properties=new DynamicPropertiesContainer();
		properties.setProperty("Year",year);
		staysIn.setDataset(dataset);
		staysIn.setLabel("StaysIn");
		staysIn.setType("StaysIn");
		staysIn.setProperties(properties); 
		nodeManager.saveRelation(staysIn);
		
		/**
		 * Below part is for relation and node serving Institute <- StaysIn-> location
		 */
		Relation locatedAt=new Relation(institute, location,"LocatedAt");
		properties = new DynamicPropertiesContainer();
		properties=new DynamicPropertiesContainer();
		properties.setProperty("Year",year);
		locatedAt.setDataset(dataset);
		locatedAt.setLabel("LocatedAt");
		locatedAt.setType("LocatedAt");
		locatedAt.setProperties(properties); 
		nodeManager.saveRelation(locatedAt);
		
		
		/**
		 * Below part is for relation and node serving Person <- attends-> Series
		 */
		Relation attends=new Relation(person, series,"Attends");
		properties = new DynamicPropertiesContainer();
		properties=new DynamicPropertiesContainer();
		properties.setProperty("role",seriesRole);
		properties.setProperty("Year",year);
		attends.setDataset(dataset);
		attends.setLabel("Attends");
		attends.setType("Attends");
		attends.setProperties(properties); 
		nodeManager.saveRelation(attends);
		
		
		/**
		 * Below part is for relation and node serving Person <- Affliates-> Institute
		 */
		Relation affliates=new Relation(person, institute,"Affliates");
		properties = new DynamicPropertiesContainer();
		properties=new DynamicPropertiesContainer();
		properties.setProperty("role",instituteRole);
		properties.setProperty("Year",year);
		affliates.setDataset(dataset);
		affliates.setLabel("Affliates");
		affliates.setType("Affliates");
		affliates.setProperties(properties); 
		nodeManager.saveRelation(affliates);
	}

	/**
	 * Hard coded data in the rest API for testing
	 */
	@RequestMapping(value = "/addnodes", method = RequestMethod.GET)
	public String addNodes(Model model) {
		
		String allData = "1892,W.A.,Setchell,Yale University,,Botany ,Instructor,,New Haven,CT,Marine Biology";
		String data [] = allData.split(",");
		String year=data[0];
		String firstName=data[1];
		String lastName=data[2];
		String instituteName=data[3];
		String instituteRole=data[4];
		String seriesName=data[5];
		String seriesRole=data[6];
		String locationStreet=data[7];
		String locationCity=data[8];
		String locationState=data[9];
		String dataset=data[10];
		
		
		
		Node person = nodeManager.checkGetPerson(firstName,lastName);
		if(person ==null){
			person = new Node();
			//node.setId( UUID.randomUUID().getMostSignificantBits());
			person.setLabel(lastName);
			person.setDataset(dataset);
			person.setType("Person");
			DynamicProperties properties = new DynamicPropertiesContainer();
			properties.setProperty("firstName",firstName);
			properties.setProperty("lastName",lastName);
			person.setProperties(properties);
			nodeManager.saveNode(person);
		}else{
			logger.info("Person Already exist ");
		}
		
		
		Node location = nodeManager.checkGetLocation(locationCity, locationState);
		if(location == null){
			location=new Node();
			DynamicProperties properties = new DynamicPropertiesContainer();
			properties=new DynamicPropertiesContainer();
			properties.setProperty("city",locationCity);
			properties.setProperty("state",locationState);
			location.setDataset(dataset);
			location.setType("Location");
			location.setLabel("Tempe");
			location.setProperties(properties);
			nodeManager.saveNode(location);
		}else{
			logger.info("Location Already exist ");
		}
		
		Node series = nodeManager.checkGetSeries(seriesName);
		if(series == null){
			series=new Node();
			DynamicProperties properties = new DynamicPropertiesContainer();
			properties=new DynamicPropertiesContainer();
			properties.setProperty("seriesName",seriesName);
			series.setDataset(dataset);
			series.setType("Series");
			series.setLabel(seriesName);
			series.setProperties(properties);
			nodeManager.saveNode(series);
		}else{
			logger.info("series Already exist ");
		}
		
		
		Node institute = nodeManager.checkGetInstitute(instituteName);
		if(institute == null){
			institute=new Node();
			DynamicProperties properties = new DynamicPropertiesContainer();
			properties=new DynamicPropertiesContainer();
			properties.setProperty("instituteName",instituteName);
			institute.setDataset(dataset);
			institute.setType("Institute");
			institute.setLabel(instituteName);
			institute.setProperties(properties);
			nodeManager.saveNode(institute);
		}else{
			logger.info("institute Already exist ");
		}
		
		/**
		 * Below part is for relation and node serving Person <- StaysIn-> location
		 */
		Relation staysIn=new Relation(person, location,"StaysIn");
		DynamicProperties properties = new DynamicPropertiesContainer();
		properties=new DynamicPropertiesContainer();
		properties.setProperty("Year",year);
		staysIn.setDataset(dataset);
		staysIn.setLabel("StaysIn");
		staysIn.setType("StaysIn");
		staysIn.setProperties(properties); 
		nodeManager.saveRelation(staysIn);
		
		/**
		 * Below part is for relation and node serving Institute <- StaysIn-> location
		 */
		Relation locatedAt=new Relation(institute, location,"LocatedAt");
		properties = new DynamicPropertiesContainer();
		properties=new DynamicPropertiesContainer();
		properties.setProperty("Year",year);
		locatedAt.setDataset(dataset);
		locatedAt.setLabel("LocatedAt");
		locatedAt.setType("LocatedAt");
		locatedAt.setProperties(properties); 
		nodeManager.saveRelation(locatedAt);
		
		
		/**
		 * Below part is for relation and node serving Person <- attends-> Series
		 */
		Relation attends=new Relation(person, series,"Attends");
		properties = new DynamicPropertiesContainer();
		properties=new DynamicPropertiesContainer();
		properties.setProperty("role",seriesRole);
		properties.setProperty("Year",year);
		attends.setDataset(dataset);
		attends.setLabel("Attends");
		attends.setType("Attends");
		attends.setProperties(properties); 
		nodeManager.saveRelation(attends);
		
		
		/**
		 * Below part is for relation and node serving Person <- Affliates-> Institute
		 */
		Relation affliates=new Relation(person, institute,"Affliates");
		properties = new DynamicPropertiesContainer();
		properties=new DynamicPropertiesContainer();
		properties.setProperty("role",instituteRole);
		properties.setProperty("Year",year);
		affliates.setDataset(dataset);
		affliates.setLabel("Affliates");
		affliates.setType("Affliates");
		affliates.setProperties(properties); 
		nodeManager.saveRelation(affliates);
		
		
		
		return "home";
	}
	
}
