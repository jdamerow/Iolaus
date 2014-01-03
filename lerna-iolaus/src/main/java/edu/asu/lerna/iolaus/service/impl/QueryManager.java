package edu.asu.lerna.iolaus.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
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

import edu.asu.lerna.iolaus.domain.json.IJsonNode;
import edu.asu.lerna.iolaus.domain.json.IJsonRelation;
import edu.asu.lerna.iolaus.domain.json.impl.JsonNode;
import edu.asu.lerna.iolaus.domain.json.impl.JsonRelation;
import edu.asu.lerna.iolaus.domain.misc.ResultSet;
import edu.asu.lerna.iolaus.domain.queryobject.IQuery;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Query;
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
	private IQueryHandler queryHandler;
	
	@Resource(name = "xmlStrings")
	private Properties xmlProperties;

	private static final Logger logger = LoggerFactory
			.getLogger(QueryManager.class);


	@Override
	public String executeQuery(String inputXML) throws JAXBException
	{
		//Parse the xml and generate the query object from it
		IQuery q =  this.xmlToQueryObject(inputXML);
		if(q == null){
			return "failure";
		}

		//Parse the query generated from the xml and get node, relation objects
		//xmlToCypherConverter.parseQuery(q);	

		//This stage should return the nodes and relations
		ResultSet rset=queryHandler.executeQuery(q);
		Map<String,List<Object>> resultSet=filterResults(rset.getResultSet(),rset.getLabelToIsReturnTrueMap());
		Map<String,List<Object>> finalResultSet=deleteDuplicateRows(resultSet);

		//Marshall to xml
		return getRESTOutput(finalResultSet, inputXML);
	}

	/**
	 * This method filters the columns of the results based on whether labels are to be displayed to user or not
	 * @param resultSet is a results having number of columns=number of labels used in the cypher query
	 * @param isReturnTrueMap is a map with key=label and value=whether that column is to be displayed to user or not
	 * @return a map with key=label(only for which return="true") and value=List of IJonNode and IJsonRelation
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
	 * This method deletes the duplicate rows based on the number of labels for which return="true"
	 * @param resultSet is a map with key=label and value=List of IJsonNode or IJsomRelation 
	 * @return a map with key = id of all the IJson objects for which return="true" and value=List of IJsonNode or IJsomRelation 
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
	 * Use Unmarshaller to unmarshal the XMl into Query object
	 * @param res
	 * @author Lohith Dwaraka
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



	@Override
	public String getRESTOutput(Map<String,List<Object>> resultSet, String inputXML)
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
