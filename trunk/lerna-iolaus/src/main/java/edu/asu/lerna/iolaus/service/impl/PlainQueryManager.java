package edu.asu.lerna.iolaus.service.impl;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import edu.asu.lerna.iolaus.configuration.neo4j.impl.Neo4jRegistry;
import edu.asu.lerna.iolaus.domain.INeo4jInstance;
import edu.asu.lerna.iolaus.domain.json.IJsonNode;
import edu.asu.lerna.iolaus.domain.json.IJsonRelation;
import edu.asu.lerna.iolaus.domain.json.impl.JsonNode;
import edu.asu.lerna.iolaus.domain.json.impl.JsonRelation;
import edu.asu.lerna.iolaus.domain.plainqueryobject.IQuery;
import edu.asu.lerna.iolaus.domain.plainqueryobject.impl.Query;
import edu.asu.lerna.iolaus.factory.IRestVelocityEngineFactory;
import edu.asu.lerna.iolaus.service.ICypherToJson;
import edu.asu.lerna.iolaus.service.IPlainQueryManager;
import edu.asu.lerna.iolaus.service.IQueryManager;
import edu.asu.lerna.iolaus.service.IRepositoryManager;

/**
 * This class has method for executing query xml which has Plain query in the body.
 * @author Karan Kothari
 * 
 */

@Service
public class PlainQueryManager implements IPlainQueryManager {

	@Autowired
	private IRestVelocityEngineFactory restVelocityEngineFactory;

	@Autowired
	private ICypherToJson cypherToJson;

	@Autowired
	private Neo4jRegistry registry;

	@Autowired
	private IRepositoryManager repositoryManager;

	@Autowired
	private IQueryManager queryManager;

	private static final Logger logger = LoggerFactory
			.getLogger(PlainQueryManager.class);

	@Override
	public String executeQuery(String xml) throws JAXBException, SAXException,
			IOException {
		
		if(xml==null)
			return null;
		
		xml=eliminateWhiteSpaces(xml);
		
		if(xml.equals(" "))
			return null;
		
		validateXml(xml);
		IQuery query = xmlToQueryObject(xml);
		String cypher = query.getCypher();
		logger.info(cypher);
		List<String> dbInstanceList = query.getDatabaseList();
		if (dbInstanceList == null || dbInstanceList.size() == 0) {
			for (INeo4jInstance instance : registry.getfileList()) {
				dbInstanceList.add(instance.getId());
			}
		}
		logger.info(" Db instance : " + dbInstanceList);
		String json = cypherToJson.plainQueryToJson(cypher);
		logger.info(" JSon : " + json);
		List<List<Object>> resultSet = repositoryManager.executeQuery(json,
				dbInstanceList);
		Map<String, List<Object>> transformedResults = transformResults(resultSet);
		String outputXml = queryManager.getRESTOutput(transformedResults);
		return outputXml;
		
	}

	private String eliminateWhiteSpaces(String cypher) {
		cypher=cypher.replaceAll("\\s+", " ");
		return cypher;
	}
	
	private Map<String, List<Object>> transformResults(
			List<List<Object>> resultSet) {
		Map<String, List<Object>> transformedResults = new LinkedHashMap<String, List<Object>>();

		if (resultSet != null) {
				if(resultSet.size()>0){
				Map<Integer, Iterator<Object>> iteratorMap = new HashMap<Integer, Iterator<Object>>();
				for (int i = 0; i < resultSet.size(); i++) {
					iteratorMap.put(i, resultSet.get(i).iterator());
				}
				while (iteratorMap.get(0).hasNext()) {
					List<Object> rowList = new ArrayList<Object>();
					String key = "";
					for (int j = 0; j < resultSet.size(); j++) {
						Object entity = iteratorMap.get(j).next();
						if (entity instanceof JsonNode) {
							IJsonNode node = (JsonNode) entity;
							key = key + node.getId();
						} else {
							IJsonRelation node = (JsonRelation) entity;
							key = key + node.getId();
						}
						rowList.add(entity);
					}
					transformedResults.put(key, rowList);
				}
			}
			return transformedResults;
		}else{
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IQuery xmlToQueryObject(String res) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(Query.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		unmarshaller
				.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
		InputStream is = new ByteArrayInputStream(res.getBytes());
		JAXBElement<Query> response1 = unmarshaller.unmarshal(new StreamSource(
				is), Query.class);
		IQuery q = response1.getValue();
		return q;
	}

	/** 
	 * {@inheritDoc}
	 */
	@Override
	public String getErrorMsg(String errorMsg, HttpServletRequest req) {
		VelocityEngine engine = null;
		Template template = null;
		StringWriter sw = new StringWriter();

		try {
			engine = restVelocityEngineFactory.getVelocityEngine(req);
			engine.init();
			template = engine.getTemplate("velocitytemplates/error.vm");
			VelocityContext context = new VelocityContext(
					restVelocityEngineFactory.getVelocityContext());
			context.put("errMsg", errorMsg);
			template.merge(context, sw);
			return sw.toString();
		} catch (ResourceNotFoundException e) {
			logger.error("Exception:", e);
		} catch (Exception e) {
			logger.error("Exception:", e);
		}
		return errorMsg;
	}

	/**
	 * This method  validates the xml against its schema.
	 * @param res is input xml.
	 * @throws SAXException
	 * @throws IOException
	 */
	private void validateXml(String res) throws SAXException, IOException {
		String classPath = URLDecoder.decode(PlainQueryManager.class
				.getProtectionDomain().getCodeSource().getLocation().getPath(),
				"UTF-8");
		URL schemaFile = new File(classPath.substring(0,
				classPath.indexOf("classes"))
				+ "classes/plainQuery.xsd").toURI().toURL();
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				classPath.substring(0, classPath.indexOf("classes"))
						+ "classes/input.xml"));
		bw.write(res);
		bw.close();
		File inputFile = new File(classPath.substring(0,
				classPath.indexOf("classes"))
				+ "classes/input.xml");
		Source xmlFile = new StreamSource(inputFile);
		SchemaFactory schemaFactory = SchemaFactory
				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schemaFactory.newSchema(schemaFile);
		Validator validator = schema.newValidator();
		validator.validate(xmlFile);
		inputFile.delete();
	}
}