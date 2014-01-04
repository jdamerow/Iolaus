package edu.asu.lerna.iolaus.service;

import java.util.List;

/**
 * CacheManager connects to the various memcached servers to store the results.
 * It also makes the decision to connect to a repository to execute a query and to store 
 * the result in the cache.
 * 
 * @author Ram Kumar Kumaresan
 */
public interface ICacheManager {
	
	/**
	 * Fetch a list of nodes and relations either from the memcached or directly from
	 * the given neo4j instance.
	 * 
	 * @param json			The neo4j query in json format
	 * @param instance		The address of the neo4j server from which the result was fetched.
	 * @return				List of nodes and relations fetched from neo4j instance for the given json
	 */
	public abstract List<List<Object>> executeQuery(String json,String instance);

	/**
	 * MD5 hash representation of the string formed based on input json and instance.
	 * 
	 * @param json			The neo4j query in json format
	 * @param instance		The address of the neo4j server from which the result was fetched.
	 * @return				Hashcode representing the json and instance.
	 */
	public abstract String getKey(String json, String instance);

}
