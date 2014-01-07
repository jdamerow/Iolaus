package edu.asu.lerna.iolaus.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PreDestroy;

import net.spy.memcached.MemcachedClient;
import net.spy.memcached.internal.GetFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.service.ICacheManager;
import edu.asu.lerna.iolaus.service.IRepositoryHandler;


/**
 * CacheManager connects to the various memcached servers to store the results.
 * It also makes the decision to connect to a repository to execute a query and to store 
 * the result in the cache.
 * 
 * @author Ram Kumar Kumaresan
 */
@Service
public class CacheManager implements ICacheManager {

	@Autowired 
	private IRepositoryHandler repositoryHandler;

	private static final Logger logger = LoggerFactory
			.getLogger(CacheManager.class);

	@Autowired
	private MemcachedClient memcachedClient;

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

		return resultSet;
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
			memcachedClient.set(getKey(json, instance),86400,resultSet);
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
	private List<List<Object>> getCachedResults(String json, String instance) {
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

}
