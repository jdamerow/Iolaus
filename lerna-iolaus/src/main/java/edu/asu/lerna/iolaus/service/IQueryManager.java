package edu.asu.lerna.iolaus.service;

import javax.xml.bind.JAXBException;

import edu.asu.lerna.iolaus.domain.queryobject.Query;

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
	
	public abstract void parseQuery(Query q);

	public abstract Query xmlToQueryObject(String res) throws JAXBException;

	public abstract String getRESTOutput(Query q, boolean wantNodes, boolean wantRelations);


}
