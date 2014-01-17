package edu.asu.lerna.iolaus.service;

public interface ICypherToJson {
	
	/**
	 * This method will convert cypher query to json  
	 * @param cypher is cypher query
	 * @return the json query
	 */
	String cypherToJson(String cypher);

	String plainQueryToJson(String cypher);

}
