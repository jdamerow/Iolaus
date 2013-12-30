package edu.asu.lerna.iolaus.service;

import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import edu.asu.lerna.iolaus.domain.queryobject.IQuery;

public interface IQueryManager {

	/**
	 * This is the only method required to be called from QueryController.
	 * It parses the input xml and produces the output xml based on the predefined schema
	 * 
	 * @param inputXML				Input xml should match the predefined input schema.
	 * @return						Output xml which contains the search results from neo4j
	 * @throws JAXBException
	 * @author Ram Kumar Kumaresan
	 */
	public abstract String executeQuery(String inputXML) throws JAXBException;

	public abstract IQuery xmlToQueryObject(String res) throws JAXBException;

	public abstract String getRESTOutput(IQuery q, boolean wantNodes, boolean wantRelations);
	
	public abstract Map<String, List<Object>> filterResults(Map<String, List<Object>> resultSet, Map<String, Boolean> isReturnTrueMap);

	public abstract Map<String, List<Object>> deleteDuplicateRows(Map<String, List<Object>> resultSet);
}
