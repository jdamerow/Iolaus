package edu.asu.lerna.iolaus.service;

import java.util.List;

/**
 * This interface has method for executing Json query on the multiple Neo4j instances.
 * @author Karan Kothari
 *
 */
public interface IRepositoryManager {

	/**
	 * This method takes json query and list of databases as input and executes it.
	 * @param json is a query in Json format..
	 * @param dbInstances is list of databases.
	 * @return the resultset which is in List<List<Object> format.
	 */
	public abstract List<List<Object>> executeQuery(String json, List<String> dbInstances);


}
