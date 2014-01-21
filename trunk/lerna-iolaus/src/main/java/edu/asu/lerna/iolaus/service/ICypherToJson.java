package edu.asu.lerna.iolaus.service;

/**
 * This class converts a cypher query into a json query.
 * @author Karan Kothari
 *
 */
public interface ICypherToJson {
	
	/**
	 * This method will convert cypher query to json. This method will add wild characters while string comparison. 
	 * e.g. name="karan" will be converted into name="(?i).*karan.*"   
	 * @param cypher is cypher query
	 * @return the json query
	 */
	String cypherToJson(String cypher);

	/**
	 * This method will convert cypher query to json. This method will not add wild characters while string comparison.
	 * @param cypher is a input cypher query.
	 * @return the json query.
	 */
	String plainQueryToJson(String cypher);

}
