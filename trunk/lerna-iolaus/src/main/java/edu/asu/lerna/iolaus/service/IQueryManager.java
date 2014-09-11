package edu.asu.lerna.iolaus.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import edu.asu.lerna.iolaus.domain.queryobject.IQuery;
import edu.asu.lerna.iolaus.exception.Neo4jServerNotRunningException;

public interface IQueryManager {

	/**
	 * This is the only method required to be called from QueryController.
	 * It parses the input xml and produces the output xml based on the predefined schema
	 * 
	 * @param inputXML				Input xml should match the predefined input schema.
	 * @return						Output xml which contains the search results from neo4j
	 * @throws JAXBException
	 * @throws IOException
	 * 
	 * @author Ram Kumar Kumaresan
	 * @throws SAXException 
	 * @throws Neo4jServerNotRunningException 
	 */
	public abstract String executeQuery(String inputXML) throws JAXBException, IOException, SAXException, Neo4jServerNotRunningException;

	/**
	 * Use Unmarshaller to unmarshal the XMl into Query object
	 * @param res
	 * @author Lohith Dwaraka
	 * @throws IOException 
	 */
	public abstract IQuery xmlToQueryObject(String res) throws JAXBException, IOException;

	/**
	 * Generate the xml output for the given set of nodes and relations.
	 * 
	 * @param resultSet	Map of nodes and relations, with each key representing a row/combination of nodes and relations
	 * @return	XML representing the input data. A null input will only produce xml headers
	 * 
	 * @author Ram Kumar Kumaresan
	 */
	public abstract String getRESTOutput(Map<String,List<Object>> resultSet);
	
	/**
	 * This method filters the columns of the results based on whether labels are to be displayed to user or not
	 * @param resultSet is a results having number of columns=number of labels used in the cypher query
	 * @param isReturnTrueMap is a map with key=label and value=whether that column is to be displayed to user or not
	 * @return a map with key=label(only for which return="true") and value=List of IJonNode and IJsonRelation
	 * 
	 * @author Karan Kothari
	 */
	public abstract Map<String, List<Object>> filterResults(Map<String, List<Object>> resultSet, Map<String, Boolean> isReturnTrueMap);

	/**
	 * This method deletes the duplicate rows based on the number of labels for which return="true"
	 * @param resultSet is a map with key=label and value=List of IJsonNode or IJsomRelation 
	 * @return a map with key = id of all the IJson objects for which return="true" and value=List of IJsonNode or IJsomRelation 
	 * 
	 * @author Karan Kothari
	 */
	public abstract Map<String, List<Object>> deleteDuplicateRows(Map<String, List<Object>> resultSet);

}
