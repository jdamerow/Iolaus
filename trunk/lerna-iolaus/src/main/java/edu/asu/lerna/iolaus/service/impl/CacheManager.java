package edu.asu.lerna.iolaus.service.impl;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.GetFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.configuration.neo4j.impl.Neo4jRegistry;
import edu.asu.lerna.iolaus.domain.mbl.nodes.Node;
import edu.asu.lerna.iolaus.service.ICacheManager;
import edu.asu.lerna.iolaus.service.IMemcachedKeySet;
import edu.asu.lerna.iolaus.service.IRepositoryHandler;
import edu.asu.lerna.iolaus.service.IUploadManager;


/**
 * CacheManager connects to the various memcached servers to store the results.
 * It also makes the decision to connect to a repository to execute a query and to store 
 * the result in the cache.
 * 
 * @author Ram Kumar Kumaresan, Karan Kothari
 */
@Service
public class CacheManager implements ICacheManager {

	@Autowired 
	private IRepositoryHandler repositoryHandler;
	
	@Autowired
	private IUploadManager uploadManager;
	
	@Autowired
	private Neo4jRegistry registry;

	private static final Logger logger = LoggerFactory
			.getLogger(CacheManager.class);

	@Autowired
	private MemcachedClient memcachedClient;
	
	@Autowired
	private IMemcachedKeySet memcachedKeySet;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getKey(String json, String instance){
		String key = instance + json;
		Md5PasswordEncoder keyEncoder = new Md5PasswordEncoder();
		return keyEncoder.encodePassword(key, null);
	}

	/**
	 * Release the memcached connection before destroying the class object
	 */
	@PreDestroy
	public void shutDown(){
		if(memcachedClient != null)
		{
			try
			{
				memcachedClient.shutdown();
			}
			catch(Exception e)
			{
				logger.debug("Error in shutting down the CacheManager resources", e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<List<Object>> executeQuery(String json,String instance) {
		List<List<Object>> resultSet=null;

		//json="{\"query\":\"start n=node:node_auto_index(type={param1}) return n\",\"params\":{\"param1\":\"Person\"}}";
		if(json !=null && instance != null && !json.equals("") && !instance.equals(""))		
		{
			//Check if the result is available in the cache
			try {
				resultSet=getCachedResults(json,instance);			
			} catch (Exception e) {
				logger.debug("Error in fetching the cache :",e);
			}

			//Query the neo4j instance and save the result to a cache
			if(resultSet==null){
				resultSet=repositoryHandler.executeQuery(json, instance);
				// Save the result set to the cache
				try {
					cacheResults(json,instance,resultSet);
				} catch (Exception e) {
					logger.debug("Error in saving to the cache :",e);
				}
			}
		}

		return resultSet;
	}
	

	@Override
	public void cacheNodeId(String uri, String instance, String nodeId) {
		try
		{
			memcachedClient.set(getKey(uri, instance),86400,nodeId);
			Set<String> keySet = memcachedKeySet.getKeySet();
			for(String key : keySet) {
				memcachedClient.delete(key);
			}
			memcachedKeySet.removeAll();
		}
		catch(Exception e)
		{
			logger.debug("Error in storing the cache", e);
		}
	}

	@Override
	public String getCachedNodeId(Node node, String instance) {
		String nodeId = null;
		GetFuture<Object> returnedFutureObject = null;
		try
		{
			// Try to get a value, for up to 5 seconds, and cancel if it doesn't return
			returnedFutureObject = memcachedClient.asyncGet(getKey(node.getUri(), instance));
			nodeId = (String) returnedFutureObject.get(5, TimeUnit.SECONDS); 
		}
		catch(Exception e) 
		{
			logger.debug("Error in retrieving the cache", e);
			if(returnedFutureObject != null)
				returnedFutureObject.cancel(false);
			return null;
		}
		
		if(nodeId != null) {
			logger.info("Node Retrieved from the Memcached");
		}
		return nodeId;
	}

	/**
	 * This method will cache the result into one of the memcached servers.
	 * The json and instance input form the key for the cache.
	 * 
	 * @param json			The neo4j query in json format
	 * @param instance		The address of the neo4j server from which the result was fetched.
	 * @param resultSet		The result for the corresponding input json.
	 * 
	 */
	private void cacheResults(String json, String instance,List<List<Object>> resultSet) {
		try
		{
			String key = getKey(json, instance);
			memcachedClient.set(key , 60 * 60 * 24 * 30,resultSet);
			memcachedKeySet.addKey(key);
		}
		catch(Exception e)
		{
			logger.debug("Error in storing the cache", e);
		}
	}


	/**
	 * Get the result set from memcached server for the given input.
	 * 
	 * @param json			The neo4j query in json format
	 * @param instance		The address of the neo4j server from which the result was fetched.
	 * @return				The result for the corresponding input json which was cached.
	 */
	public List<List<Object>> getCachedResults(String json, String instance) {
		Object returnedObject = null;
		GetFuture<Object> returnedFutureObject = null;
		try
		{
			// Try to get a value, for up to 5 seconds, and cancel if it doesn't return
			returnedFutureObject = memcachedClient.asyncGet(getKey(json, instance));
			returnedObject = returnedFutureObject.get(5, TimeUnit.SECONDS); 
		}
		catch(Exception e) 
		{
			logger.debug("Error in retrieving the cache", e);
			if(returnedFutureObject != null)
				returnedFutureObject.cancel(false);
			return null;
		}
		return (List<List<Object>>) returnedObject;
	}

	@Override
	public void delete(String key) {
		if(key != null) {
			memcachedClient.delete(key);
			memcachedKeySet.remove(key);
		}
	}

}
