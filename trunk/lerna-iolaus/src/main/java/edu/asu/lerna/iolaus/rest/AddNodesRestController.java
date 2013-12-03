package edu.asu.lerna.iolaus.rest;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.fieldaccess.DynamicProperties;
import org.springframework.data.neo4j.fieldaccess.DynamicPropertiesContainer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import edu.asu.lerna.iolaus.domain.AffiliatedWithRelation;
import edu.asu.lerna.iolaus.domain.AttendedRelation;
import edu.asu.lerna.iolaus.domain.HasLocationRelation;
import edu.asu.lerna.iolaus.domain.LocationNode;
import edu.asu.lerna.iolaus.domain.Node;
import edu.asu.lerna.iolaus.domain.PersonNode;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Query;
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
	 * Initializing the hash Maps
	 * Upload a file of MBL object into Neo4J
	 */
	@RequestMapping(value = "/uploadnodes", method = RequestMethod.POST)
	public String addNodesFile(@RequestParam("file") MultipartFile file,Model model) {
		
        logger.info("file Name "+file.getOriginalFilename());
        BufferedReader  b=null;
        int typesOfObjects=4;
        try {
			b = new BufferedReader (new InputStreamReader(file.getInputStream()));
		} catch (IOException e) {
			logger.error("IO Error, File not uploaded",e);
		}
        
        
        nodeManager.initializeNodeTypeMap(typesOfObjects);
        
        
        String line;
        try {
			while((line=b.readLine())!=null){
				createMBLObject(line);
			}
		} catch (IOException e) {
			logger.error("IO Error, File not uploaded",e);
		}
		
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
		int year=Integer.parseInt(data[0]);
		String firstName=data[1];
		String lastName=data[2];
		String instituteName=data[3];
		String instituteRole=data[4];
		String seriesName=data[5];
		String seriesRole=data[6];
		String locationAddress=data[7];
		String locationStreet=data[8];
		String locationCity=data[9];
		String locationState=data[10];
		String locationCountry=data[11];
		
		String dataset="mblcourses";
		
		
		
		PersonNode person = nodeManager.checkGetPerson(firstName,lastName);
		if(person ==null){
			person = new PersonNode();
			//node.setId( UUID.randomUUID().getMostSignificantBits());
			person.setLabel(firstName);
			person.setDataset(dataset);
			person.setServiceId(" ");
			person.setUri(" ");
			person.setType("Person");
			person.setFirstName(firstName);
			person.setLastName(lastName);
			person=(PersonNode) nodeManager.saveNode(person);
			nodeManager.saveNodeInMap(0, person,firstName);
		}else{
			logger.info("Person Already exist ");
		}
		
		
		LocationNode location = nodeManager.checkGetLocation(locationAddress, locationStreet,locationCity,locationState,locationCountry);
		if(location == null){
			location=new LocationNode();
			location.setServiceId(" ");
			location.setUri(" ");
			location.setAddress(locationAddress);
			location.setStreet(locationStreet);
			location.setCity(locationCity);
			location.setState(locationState);
			location.setCountry(locationCountry);
			location.setDataset(dataset);
			location.setType("Location");
			location.setLabel(locationCity);
			location=(LocationNode)nodeManager.saveNode(location);
			nodeManager.saveNodeInMap(3, location,locationCity);
		}else{
			logger.info("Location Already exist ");
		}
		
		Node series = nodeManager.checkGetSeries(seriesName);
		if(series == null){
			series=new Node();
			series.setServiceId(" ");
			series.setUri(" ");
			DynamicProperties properties = new DynamicPropertiesContainer();
			properties=new DynamicPropertiesContainer();
			series.setDataset(dataset);
			series.setType("Series");
			series.setLabel(seriesName);
			series=nodeManager.saveNode(series);
			nodeManager.saveNodeInMap(2, series,seriesName);
		}else{
			logger.info("series Already exist ");
		}
		
		
		Node institute = nodeManager.checkGetInstitute(instituteName);
		if(institute == null){
			institute=new Node();
			institute.setServiceId(" ");
			institute.setUri(" ");
			institute.setDataset(dataset);
			institute.setType("Institute");
			institute.setLabel(instituteName);
			institute=nodeManager.saveNode(institute);
			nodeManager.saveNodeInMap(1, institute,instituteName);
		}else{
			logger.info("institute Already exist ");
		}
		
		/**
		 * Below part is for relation and node serving Person <- StaysIn-> location
		 */
		HasLocationRelation hasLocation=new HasLocationRelation(person, location);
		hasLocation.setServiceId(" ");
		hasLocation.setUri(" ");
		hasLocation.setYear(year);
		hasLocation.setDataset(dataset);
		hasLocation.setLabel("hasLocation");
		nodeManager.saveRelation(hasLocation);
		
		/**
		 * Below part is for relation and node serving Institute <- StaysIn-> location
		 */
		HasLocationRelation locatedAt=new HasLocationRelation(institute, location);
		locatedAt.setServiceId(" ");
		locatedAt.setUri(" ");
		locatedAt.setDataset(dataset);
		locatedAt.setLabel("hasLocation");
		locatedAt.setYear(year); 
		nodeManager.saveRelation(locatedAt);
		
		
		/**
		 * Below part is for relation and node serving Person <- attends-> Series
		 */
		AttendedRelation attended=new AttendedRelation(person, series);
		attended.setServiceId(" ");
		attended.setUri(" ");
		attended.setDataset(dataset);
		attended.setLabel("attended");
		attended.setRole(seriesRole);
		attended.setYear(year);
		nodeManager.saveRelation(attended);
		
		
		/**
		 * Below part is for relation and node serving Person <- Affliates-> Institute
		 */
		AffiliatedWithRelation affiliatedWith=new AffiliatedWithRelation(person, institute);
		affiliatedWith.setServiceId(" ");
		affiliatedWith.setUri(" ");
		affiliatedWith.setRole(instituteRole);
		affiliatedWith.setYear(year);
		affiliatedWith.setDataset(dataset);
		affiliatedWith.setLabel("affiliatedWith"); 
		nodeManager.saveRelation(affiliatedWith);
	}

	/**
	 * Hard coded data in the rest API for testing
	 */
	@RequestMapping(value = "/addnodes", method = RequestMethod.GET)
	public String addNodes(Model model) {
		
		String allData = "1892,W.A.,Setchell,Yale University,,Botany ,Instructor,,New Haven,CT,Marine Biology";
		createMBLObject(allData);
		return "home";
		/*String data [] = allData.split(",");
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
		
		*//**
		 * Below part is for relation and node serving Person <- StaysIn-> location
		 *//*
		Relation staysIn=new Relation(person, location,"StaysIn");
		DynamicProperties properties = new DynamicPropertiesContainer();
		properties=new DynamicPropertiesContainer();
		properties.setProperty("Year",year);
		staysIn.setDataset(dataset);
		staysIn.setLabel("StaysIn");
		staysIn.setType("StaysIn");
		staysIn.setProperties(properties); 
		nodeManager.saveRelation(staysIn);
		
		*//**
		 * Below part is for relation and node serving Institute <- StaysIn-> location
		 *//*
		Relation locatedAt=new Relation(institute, location,"LocatedAt");
		properties = new DynamicPropertiesContainer();
		properties=new DynamicPropertiesContainer();
		properties.setProperty("Year",year);
		locatedAt.setDataset(dataset);
		locatedAt.setLabel("LocatedAt");
		locatedAt.setType("LocatedAt");
		locatedAt.setProperties(properties); 
		nodeManager.saveRelation(locatedAt);
		
		
		*//**
		 * Below part is for relation and node serving Person <- attends-> Series
		 *//*
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
		
		
		*//**
		 * Below part is for relation and node serving Person <- Affliates-> Institute
		 *//*
		Relation affliates=new Relation(person, institute,"Affliates");
		properties = new DynamicPropertiesContainer();
		properties=new DynamicPropertiesContainer();
		properties.setProperty("role",instituteRole);
		properties.setProperty("Year",year);
		affliates.setDataset(dataset);
		affliates.setLabel("Affliates");
		affliates.setType("Affliates");
		affliates.setProperties(properties); 
		nodeManager.saveRelation(affliates);*/
		
	}
	
	
	/**
	 * Hard coded data in the rest API for testing
	 * @throws JAXBException 
	 */
	@RequestMapping(value = "/testXML", method = RequestMethod.POST)
	public String testQueryXML(Model model,@RequestBody String res) throws JAXBException {
		logger.info(" testing ");
		JAXBContext context = JAXBContext.newInstance(Query.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		unmarshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
		InputStream is = new ByteArrayInputStream(res.getBytes());
		JAXBElement<Query> response1 =  unmarshaller.unmarshal(new StreamSource(is), Query.class);
		if(response1 == null){
			logger.info("response is null");
		}
		
		
		return "success";
	}
	
	
}

