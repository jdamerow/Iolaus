package edu.asu.lerna.iolaus.service.impl;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.File;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import edu.asu.lerna.iolaus.configuration.neo4j.impl.Neo4jRegistry;
import edu.asu.lerna.iolaus.domain.INeo4jInstance;
import edu.asu.lerna.iolaus.domain.json.IJsonNode;
import edu.asu.lerna.iolaus.domain.json.IJsonRelation;
import edu.asu.lerna.iolaus.domain.json.impl.JsonNode;
import edu.asu.lerna.iolaus.domain.json.impl.JsonRelation;
import edu.asu.lerna.iolaus.domain.misc.ResultSet;
import edu.asu.lerna.iolaus.domain.queryobject.IQuery;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Query;
import edu.asu.lerna.iolaus.exception.Neo4jServerNotRunningException;
import edu.asu.lerna.iolaus.factory.IRestVelocityEngineFactory;
import edu.asu.lerna.iolaus.service.IObjecttoXMLConverter;
import edu.asu.lerna.iolaus.service.IQueryHandler;
import edu.asu.lerna.iolaus.service.IQueryManager;
import edu.asu.lerna.iolaus.service.IXMLtoCypherConverter;

@Service
public class QueryManager implements IQueryManager {

	@Autowired
	private IXMLtoCypherConverter xmlToCypherConverter;

	@Autowired
	private IObjecttoXMLConverter objectToXMLConverter;

	@Autowired
	private IRestVelocityEngineFactory restVelocityEngineFactory;
	
	@Autowired
	private IQueryHandler queryHandler;
	
	@Autowired
	private Neo4jRegistry registry;
	
	@Resource(name = "xmlStrings")
	private Properties xmlProperties;

	private static final Logger logger = LoggerFactory
			.getLogger(QueryManager.class);


	/**
	 * {@inheritDoc}
	 * @throws Neo4jServerNotRunningException 
	 */
	@Override
	public String executeQuery(String inputXML) throws JAXBException,SAXException,IOException, Neo4jServerNotRunningException
	{
		validateXml(inputXML);
		//Parse the xml and generate the query object from it
		IQuery q =  this.xmlToQueryObject(inputXML);
		if(q == null){
			return "failure";
		}
		
		for(String id : q.getDatabaseList()) {
			for(INeo4jInstance instance : registry.getfileList()) {
				if(id.equals(instance.getId()))
				if(!isNeo4jServerUp(instance)) {
					throw new Neo4jServerNotRunningException("Instance Id : " + id + ", Message : Neo4j Server is not running.");
				}
			}
		}

		//This stage should return the nodes and relations
		ResultSet rset=queryHandler.executeQuery(q);
		Map<String,List<Object>> resultSet=filterResults(rset.getResultSet(),rset.getLabelToIsReturnTrueMap());
		Map<String,List<Object>> finalResultSet=deleteDuplicateRows(resultSet);

		//Marshall to xml
		return getRESTOutput(finalResultSet);
	}
	
	private boolean isNeo4jServerUp(INeo4jInstance instance) {
		boolean isAlive = true;
		String urlStr = "";
		try {
			urlStr = instance.getProtocol() + "://" + instance.getHost() + ":" + instance.getPort() + "/webadmin/";
			URL url = new URL(urlStr);
			// It will throw an exception if not able to connect to the server.
			URLConnection connection = url.openConnection();
			connection.getInputStream();
		} catch (Exception e) {
			isAlive = false;
			logger.error("Error in the connectivity : ", e.getLocalizedMessage());
		}
		return isAlive;
	}
	

	/**
	 * {@inheritDoc}
	 */
	private void validateXml(String res) throws SAXException, IOException {
		String classPath=URLDecoder.decode(QueryManager.class.getProtectionDomain().getCodeSource().getLocation().getPath(),"UTF-8");
		URL schemaFile = new File(classPath.substring(0,classPath.indexOf("classes"))+"classes/query-schema.xsd").toURI().toURL();
		BufferedWriter bw=new BufferedWriter(new FileWriter(classPath.substring(0,classPath.indexOf("classes"))+"classes/input.xml"));
		bw.write(res);
		bw.close();
		File inputFile=new File(classPath.substring(0,classPath.indexOf("classes"))+"classes/input.xml");
		Source xmlFile = new StreamSource(inputFile);
		SchemaFactory schemaFactory = SchemaFactory
		    .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schemaFactory.newSchema(schemaFile);
		Validator validator = schema.newValidator();
		validator.validate(xmlFile);
		inputFile.delete();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, List<Object>> filterResults(Map<String, List<Object>> resultSet, Map<String, Boolean> isReturnTrueMap) {

		Map<String,List<Object>> filteredResults=new LinkedHashMap<String, List<Object>>();
		//This loop will add only columns(labels) for which return="true"
		for(Entry<String,List<Object>> entry:resultSet.entrySet()){
			String label=entry.getKey();
			if(isReturnTrueMap.get(label)){
				filteredResults.put(label, entry.getValue());
			}
		}
		return filteredResults;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, List<Object>> deleteDuplicateRows(Map<String, List<Object>> resultSet) {

		Map<Integer,Iterator<Object>> iteratorMap=new HashMap<Integer, Iterator<Object>>();
		int i=0;//Count of labels for which return="true"
		for(Entry<String,List<Object>> entry:resultSet.entrySet()){
			iteratorMap.put(i++, entry.getValue().iterator());
		}
		Map<String,List<Object>> finalResultSet=new LinkedHashMap<String, List<Object>>();
		if(i!=0){
			while(iteratorMap.get(0).hasNext()){
				String key="";
				List<Object> row=new ArrayList<Object>();;
				for(int j=0;j<i;j++){
					Object obj=iteratorMap.get(j).next();
					row.add(obj);
					if(obj instanceof IJsonNode){
						IJsonNode node=(IJsonNode)obj;
						key=key+node.getId();
					}else{
						IJsonRelation rel=(IJsonRelation) obj;
						key=key+rel.getId();
					}
				}
				if(!finalResultSet.containsKey(key)){
					finalResultSet.put(key, row);
				}
			}
		}
		return finalResultSet;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IQuery xmlToQueryObject(String res) throws JAXBException{
		JAXBContext context = JAXBContext.newInstance(Query.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		unmarshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
		InputStream is = new ByteArrayInputStream(res.getBytes());
		JAXBElement<Query> response1 =  unmarshaller.unmarshal(new StreamSource(is), Query.class);
		IQuery q = response1.getValue();
		return q;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getRESTOutput(Map<String,List<Object>> resultSet)
	{
		JAXBContext jaxbContextNode = null;
		Marshaller jaxbMarshallerNode = null;
		JAXBContext jaxbContextRelation = null;
		Marshaller jaxbMarshallerRelation = null;

		StringWriter outputWriter = new StringWriter();
		outputWriter.write(xmlProperties.getProperty("xml.header"));
		outputWriter.write(System.lineSeparator());
		outputWriter.write(xmlProperties.getProperty("xml.resultset.start"));		
		outputWriter.write(System.lineSeparator());
		try {
			//Set up the jaxb writers for node and relation
			jaxbContextNode = JAXBContext.newInstance(JsonNode.class);
			jaxbMarshallerNode = jaxbContextNode.createMarshaller();
			jaxbMarshallerNode.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshallerNode.setProperty(Marshaller.JAXB_FRAGMENT, true);

			jaxbContextRelation = JAXBContext.newInstance(JsonRelation.class);
			jaxbMarshallerRelation = jaxbContextRelation.createMarshaller();
			jaxbMarshallerRelation.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			jaxbMarshallerRelation.setProperty(Marshaller.JAXB_FRAGMENT, true);
			
			//Create the xml elements from the class
			if(resultSet !=null)
				for(Entry<String, List<Object>> entry: resultSet.entrySet())
				{
					outputWriter.write(xmlProperties.getProperty("xml.record.start"));
					outputWriter.write(System.lineSeparator());
					for(Object listobject : entry.getValue())
					{
						if(listobject instanceof IJsonNode)
						{
							try {
								jaxbMarshallerNode.marshal(listobject, outputWriter);		
								outputWriter.write(System.lineSeparator());
							} catch (JAXBException e) {
								e.printStackTrace();
							}
						}
						else if(listobject instanceof IJsonRelation)
						{
							try {
								jaxbMarshallerRelation.marshal(listobject, outputWriter);
								outputWriter.write(System.lineSeparator());
							} catch (JAXBException e) {
								e.printStackTrace();
							}
						}
					}

					outputWriter.write(xmlProperties.getProperty("xml.record.end"));
					outputWriter.write(System.lineSeparator());
				}
		} catch (JAXBException e) {
			logger.debug("Exception in marshalling ",e);
			//Reset the writer
			outputWriter = new StringWriter();
			outputWriter.write(xmlProperties.getProperty("xml.header"));
			outputWriter.write(System.lineSeparator());
			outputWriter.write(xmlProperties.getProperty("xml.resultset.start"));
			outputWriter.write(System.lineSeparator());
		}


		outputWriter.write(xmlProperties.getProperty("xml.resultset.end"));
		outputWriter.write(System.lineSeparator());
		
		return outputWriter.toString();
	}
}
