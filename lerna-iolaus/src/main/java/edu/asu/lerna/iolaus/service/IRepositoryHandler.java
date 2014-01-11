package edu.asu.lerna.iolaus.service;

import java.util.List;

/**
 * This makes a Rest call to a neo4j instance inorder to execute a given json string.
 * The json output from neo4j are converted to generalized node and realtion classes.
 * 
 * @author Ram Kumar Kumaresan
 */
public interface IRepositoryHandler {

	/**
	 * Execute the input json in a neo4j instance. Will return null if json or neo4j instance is null or empty.
	 * 
	 * @param json			The json query to be executed on neo4j.
	 * @param instance		Reference to a running neo4j instance.
	 * @return				List of nodes and relations retrieved for the json input.
	 */
	public abstract List<List<Object>> executeQuery(String json, String instance);


}
