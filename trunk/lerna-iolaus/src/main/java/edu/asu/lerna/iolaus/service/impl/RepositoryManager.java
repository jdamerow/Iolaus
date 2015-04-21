package edu.asu.lerna.iolaus.service.impl;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.configuration.neo4j.impl.Neo4jRegistry;
import edu.asu.lerna.iolaus.domain.INeo4jInstance;
import edu.asu.lerna.iolaus.service.ICacheManager;
import edu.asu.lerna.iolaus.service.INeo4jInstanceManager;
import edu.asu.lerna.iolaus.service.IRepositoryManager;
/**
 * This class implements {@link IRepositoryManager}. It has method for executing json query on multiple Neo4j instances.
 * @author Karan Kothari
 *
 */
@Service
public class RepositoryManager implements IRepositoryManager {

	private static final Logger logger = LoggerFactory
			.getLogger(RepositoryManager.class);

	@Autowired
	private ICacheManager cacheManager;
	
	@Autowired
	private Neo4jRegistry instanceRegistry;
	
	@Autowired
	private INeo4jInstanceManager instanceManager;

	@Autowired
	@Qualifier("cypherEndPoint")
	private String cypherEndPoint;
	
	public RepositoryManager()
	{
			//repoHanlders = new ArrayList<IRepositoryHandler>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<List<Object>> executeQuery(String json, List<String> dbInstances)
	{
		logger.debug("\n\n");
		List<List<Object>> listOfNodesAndRelations = new ArrayList<List<Object>>();;
		List<String> cypherRootURI = new ArrayList<String>();
		Iterator<String> iterator = dbInstances.iterator();
		/* key=instance id , value=Neo4jInstance*/
		Map<String,INeo4jInstance> idInstanceMap=new HashMap<String, INeo4jInstance>();
		
		/* create Map using Neo4jRegistry*/
		for(INeo4jInstance instance:instanceRegistry.getfileList()){
			idInstanceMap.put(instance.getId(), instance);
		}
		
		/*Iterate through the list of instance id's
		 *  create cypher root URI
		 *  add it to a instance URI list*/
		while(iterator.hasNext())
		{
			String dbName = iterator.next();
			if(idInstanceMap.containsKey(dbName)){
				INeo4jInstance dbFile=idInstanceMap.get(dbName);
				if(dbFile.isActive()&&
					checkConnectivity(dbFile.getProtocol(), 
							dbFile.getPort(), dbFile.getHost())){
				cypherRootURI.add(dbFile.getProtocol()+"://"+dbFile.getHost()+":"+dbFile.getPort()+"/"+dbFile.getDbPath()+"/"+cypherEndPoint);
				logger.info(dbFile.getProtocol()+"://"+dbFile.getHost()+":"+dbFile.getPort()+"/"+dbFile.getDbPath()+"/"+cypherEndPoint);
				}
			}
		}
		List<List<Object>> queryResults;
		for (String instance: cypherRootURI)
		{
			queryResults = cacheManager.executeQuery(json, instance);
			if(queryResults!=null)
				listOfNodesAndRelations.addAll(queryResults);
				logger.info(""+listOfNodesAndRelations.size());
		}
		return listOfNodesAndRelations;
	}
	
	@Override
	public List<List<Object>> executeQuery(String json, String id) {

		INeo4jInstance dbFile = instanceManager.getInstance(id);
		List<List<Object>> queryResults = null;
		if (dbFile.isActive()
				&& checkConnectivity(dbFile.getProtocol(), dbFile.getPort(),
						dbFile.getHost())) {
			
			String uri = dbFile.getProtocol() + "://" + dbFile.getHost()
					+ ":" + dbFile.getPort() + "/" + dbFile.getDbPath() + "/"
					+ cypherEndPoint;
			logger.info(dbFile.getProtocol() + "://" + dbFile.getHost() + ":"
					+ dbFile.getPort() + "/" + dbFile.getDbPath() + "/"
					+ cypherEndPoint);
			queryResults = cacheManager.executeQuery(json, uri);
		}

		return queryResults;
	}
	
	/**
	 * This method ping the Neo4j server. If it is up then returns true else returns false.  
	 * @param port is port number.
	 * @param host is address of the host machine.
	 * @return the status of server. If it is up then returns true else returns false. 
	 */
	private boolean checkConnectivity(String protocol,String port, String host) {
		boolean isAlive = true;
		String urlStr="";
		try {
			urlStr=protocol+"://"+host+":"+port+"/webadmin/";
			URL url = new URL(urlStr); 
			URLConnection connection= url.openConnection();//It will throw an exception if not able to connect to the server. 
			connection.getInputStream();
		} catch (Exception e) {
			isAlive = false;
			logger.error("Error in the connectivity : ",e);
		}
		return isAlive;
	}
}
