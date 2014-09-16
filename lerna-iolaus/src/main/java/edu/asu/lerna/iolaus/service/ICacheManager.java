package edu.asu.lerna.iolaus.service;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;

import edu.asu.lerna.iolaus.domain.mbl.nodes.Node;

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
	 * If uri is already present in the cache, it returns corresponding node id. Otherwise, it will add this node to Neo4j and return its id.
	 * @param node is {@link Node} of mbl dataset.
	 * @return id provided by Neo4j.
	 * @author Karan Kothari
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	public String getCachedNodeId(Node node, String instance);

	/**
	 * MD5 hash representation of the string formed based on input json and instance.
	 * 
	 * @param json			The neo4j query in json format
	 * @param instance		The address of the neo4j server from which the result was fetched.
	 * @return				Hashcode representing the json and instance.
	 */
	public abstract String getKey(String json, String instance);

	/**
	 * adds nodeId to cache and deletes all cachedResults from memcache
	 * @param uri is a property of a {@link Node}. Every node has a unique URI.
	 * @param instance is a Neo4j instance id where node is inserted.
	 * @param nodeId is unique uri assigned by Neo4j to a particular node.
	 * @author Karan Kothari
	 */
	void cacheNodeId(String uri, String instance, String nodeId);

	List<List<Object>> getCachedResults(String json, String instance);
	void delete(String key);
}
