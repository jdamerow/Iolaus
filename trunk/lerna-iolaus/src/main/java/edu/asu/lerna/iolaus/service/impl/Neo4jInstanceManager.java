package edu.asu.lerna.iolaus.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.asu.lerna.iolaus.configuration.neo4j.impl.Neo4jRegistry;
import edu.asu.lerna.iolaus.domain.INeo4jInstance;
import edu.asu.lerna.iolaus.service.INeo4jInstanceManager;

/**
 * This class manages the Neo4jInstances. It has methods for adding, deleting,
 * modifying Neo4j instances.
 * 
 * @author Karan Kothari
 * 
 */
@Service
public class Neo4jInstanceManager implements INeo4jInstanceManager {

	@Autowired
	private Neo4jRegistry neo4jRegistry;

	private static final Logger logger = LoggerFactory
			.getLogger(Neo4jInstanceManager.class);

	/**
	 * {@inheritDoc}
	 * 
	 * @throws UnsupportedEncodingException
	 */
	@Override
	public String addNeo4jInstance(INeo4jInstance instance)
			throws UnsupportedEncodingException {
		if (instance != null) {
			List<INeo4jInstance> fileList = neo4jRegistry.getfileList();
			int maxId = 0;
			String port = instance.getPort();
			String host = instance.getHost();
			if (port == null || host == null)
				return null;
			if (!isAlive(instance.getProtocol(), port, host)
					&& instance.isActive() == true ? true : false) {
				return "-2";
			}
			// if port and host is null then return "-1"
			if (port.equals("") || host.equals(""))
				return "-1";
			// This loop will search for the maximum id in the registry and will
			// set it to the maxId
			for (INeo4jInstance file : fileList) {
				if (file.getPort().equals(port)
						&& file.getHost().equalsIgnoreCase(host)) {
					 //if combination of port and host already exists then return "0"
					return "0";
				}

				if (Integer.parseInt(file.getId()) > maxId) {
					maxId = Integer.parseInt(file.getId());
				}
			}
			/* create a file and write configuration to the file */
			// get the class path
			String classPath = Neo4jInstanceManager.class.getProtectionDomain()
					.getCodeSource().getLocation().getPath();
			String fileName = URLDecoder.decode(
					classPath.substring(0, classPath.indexOf("classes")),
					"UTF-8")
					+ "classes/ConfigurationFiles/Neo4jConfiguration"
					+ (maxId + 1) + ".txt";
			
			//create index only if instance is active
			if(instance.isActive()){
				boolean flag;
				if(!nodeIndexExists(instance)){
				//add index name of Node to the Neo4j
					flag=createIndex(instance,true);
					if(!flag)
						return "-3";
				}
				if(!relationIndexExists(instance)){
					//add index name of Relation to the Neo4j.
					flag=createIndex(instance,false);
					if(!flag)
						return "-3";
				}
			}
			
			createFile(instance, fileName, (maxId + 1));

			instance.setId(String.valueOf(maxId + 1));
			// add new instance to registry.
			neo4jRegistry.getfileList().add(instance);

			return String.valueOf(maxId + 1);
		} else {
			return null;
		}
	}

	private boolean createIndex(INeo4jInstance instance,boolean isNode) {
		//http://localhost:7474/db/data/index/node/
		String entryPointUri = instance.getProtocol() + "://"
				+ instance.getHost() + ":" + instance.getPort() + "/"
				+ instance.getDbPath() + "/" + "index";
		String json=new String();
		if(isNode){
			entryPointUri+="/node";
			json="{\"name\" : " + "\"" + instance.getNodeIndex() + "\"}";
		}else{
			entryPointUri+="/relationship";
			json="{\"name\" : " + "\"" + instance.getRelationIndex() + "\"}";
		}
		logger.info(entryPointUri);
		logger.info(json);
		WebResource resource = Client.create().resource(entryPointUri);
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).entity(json)
				.post(ClientResponse.class);
		//staus=201-->index created
		if(response.getStatus()==201)
			return true;
		else
			return false;
	}

	private void createFile(INeo4jInstance instance, String fileName, int id) {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(fileName));
			bw.append("id:" + id + "\n");
			String modifiedDescription = instance.getDescription()
					.replaceAll("\n", " ").replace("\r", "");
			bw.append("port:" + instance.getPort() + "\n");
			bw.append("host:" + instance.getHost() + "\n");
			bw.append("protocol:" + instance.getProtocol() + "\n");
			bw.append("nodeIndex:" + instance.getNodeIndex() + "\n");
			bw.append("relationIndex:" + instance.getRelationIndex() + "\n");
			bw.append("dbPath:" + instance.getDbPath() + "\n");
			bw.append("description:" + modifiedDescription + "\n");
			bw.append("active:"
					+ String.valueOf(instance.isActive() == true ? true : false)
					+ "\n");
			bw.append("userName:" + instance.getUserName() + "\n");
			bw.close();
		} catch (IOException e) {
			if (bw == null) {
				logger.info("Error occured while creating the file");
			} else {
				try {
					bw.close();
					logger.info("Error occured while writting to the file");
				} catch (IOException exp) {
					System.out.println("Error occured while closing the file");
				}
			}
		}

	}

	/**
	 * This method ping the Neo4j server. If it is up then returns true else
	 * returns false.
	 * 
	 * @param port
	 *            is port number.
	 * @param host
	 *            is address of the host machine.
	 * @param protocol
	 * 				  is protocol on which server is running.  e.g. Http, Https
	 * @return the status of server. If it is up then returns true else returns
	 *         false.
	 */
	private boolean isAlive(String protocol, String port, String host) {
		boolean isAlive = true;
		String urlStr = "";
		try {
			urlStr = protocol + "://" + host + ":" + port + "/webadmin/";
			URL url = new URL(urlStr);
			// It will throw an exception if not able to connect to the server.
			URLConnection connection = url.openConnection();
			connection.getInputStream();
		} catch (Exception e) {
			isAlive = false;
			logger.error("Error in the connectivity");
		}
		return isAlive;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteNeo4jInstance(String instanceId)
			throws UnsupportedEncodingException {
		if (instanceId != null) {
			List<INeo4jInstance> configFileList = neo4jRegistry.getfileList();
			for (INeo4jInstance configFile : configFileList) {
				if (configFile.getId().equals(instanceId)) {
					configFileList.remove(configFile);
					String classPath = Neo4jInstanceManager.class
							.getProtectionDomain().getCodeSource()
							.getLocation().getPath();
					String filePath = URLDecoder.decode(classPath.substring(0,
							classPath.indexOf("classes")), "UTF-8")
							+ "classes/ConfigurationFiles/Neo4jConfiguration"
							+ instanceId + ".txt";
					File file = new File(filePath);
					file.delete();
					break;
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int updateNeo4jInstance(INeo4jInstance instance)
			throws UnsupportedEncodingException, IOException {
		if (instance != null) {
			List<INeo4jInstance> configFileList = neo4jRegistry.getfileList();
			String port = instance.getPort();
			String host = instance.getHost();
			if (!isAlive(instance.getProtocol(), port, host)
					&& instance.isActive() == true ? true : false) {
				return 2;
			}
			
			
			boolean flag = false;
			for (INeo4jInstance configFile : configFileList) {
				if (instance.getId().equals(configFile.getId())) {
					String modifiedDescription = instance.getDescription()
							.replaceAll("\n", " ").replace("\r", "");
					configFile.setDescription(modifiedDescription);
					configFile.setHost(instance.getHost());
					configFile.setPort(instance.getPort());
					configFile.setActive(instance.isActive() == true ? true
							: false);
					updateFile(configFile);
					flag = true;
				} else if (configFile.getPort().equals(port)
						&& configFile.getHost().equalsIgnoreCase(host)) {
					// combination of port and host already exists
					return 1;
				}
			}
			if (flag) {
				// valid instance id
				if(instance.isActive()){
					
					if(!nodeIndexExists(instance)){
						//add index name of Node to the Neo4j
						createIndex(instance,true);
					}
					
					if(!relationIndexExists(instance)){
						//add index name of Relation to the Neo4j.
						createIndex(instance,false);
					}

				}
				return 0;
			} else {
				// instance id doesn't exists
				return -2;
			}
		} else {
			// instance id is null
			return -1;
		}
	}

	private boolean nodeIndexExists(INeo4jInstance instance) {
		
		String nodeIndexEntryPointUri= instance.getProtocol() + "://"
				+ instance.getHost() + ":" + instance.getPort() + "/"
				+ instance.getDbPath() + "/" + "index/node";
		WebResource resource = Client.create().resource(nodeIndexEntryPointUri);
		ClientResponse response = resource.get(ClientResponse.class);
	    String listOfIndexes = response.getEntity(String.class);
		return listOfIndexes.contains("\"" + instance.getNodeIndex() + "\" : {");
		
	}
	
	private boolean relationIndexExists(INeo4jInstance instance) {
		
		String relationshipIndexEntryPointUri= instance.getProtocol() + "://"
				+ instance.getHost() + ":" + instance.getPort() + "/"
				+ instance.getDbPath() + "/" + "index/relationship";
		WebResource resource = Client.create().resource(relationshipIndexEntryPointUri);
		ClientResponse response = resource.get(ClientResponse.class);
	    String listOfIndexes = response.getEntity(String.class);
		return listOfIndexes.contains("\"" + instance.getRelationIndex() + "\" : {");
		
	}
	

	private void updateFile(INeo4jInstance instance)
			throws UnsupportedEncodingException, IOException {
		if (instance != null) {
			// gets the class path
			String classPath = Neo4jInstanceManager.class.getProtectionDomain()
					.getCodeSource().getLocation().getPath();
			BufferedWriter bw = null;
			bw = new BufferedWriter(new FileWriter(URLDecoder.decode(
					classPath.substring(0, classPath.indexOf("classes")),
					"UTF-8")
					+ "classes/ConfigurationFiles/Neo4jConfiguration"
					+ instance.getId() + ".txt"));
			String modifiedDescription = instance.getDescription()
					.replaceAll("\n", " ").replace("\r", "");
			
			bw.append("id:" + instance.getId() + "\n");
			bw.append("port:" + instance.getPort() + "\n");
			bw.append("host:" + instance.getHost() + "\n");
			bw.append("nodeIndex:" + instance.getNodeIndex() + "\n");
			bw.append("relationIndex:" + instance.getRelationIndex() + "\n");
			bw.append("protocol:" + instance.getProtocol() + "\n");
			bw.append("dbPath:" + instance.getDbPath() + "\n");
			bw.append("description:" + modifiedDescription + "\n");
			bw.append("active:"
					+ String.valueOf(instance.isActive() == true ? true : false)
					+ "\n");
			bw.append("userName:" + instance.getUserName());
			bw.close();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public INeo4jInstance getInstance(String id) {
		INeo4jInstance instance = null;
		for (INeo4jInstance confFile : neo4jRegistry.getfileList()) {
			if (confFile.getId().equals(id)) {
				instance = confFile;
				break;
			}
		}
		return instance;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<INeo4jInstance> getAllInstances() {
		return neo4jRegistry.getfileList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getJsonOfInstances() {
		
		StringBuilder json = new StringBuilder();
		List<INeo4jInstance> instanceList = getActiveInstances();
		json.append("{\"instanceList\" : [ ");
		
		if(instanceList != null){
			
			boolean isFirst = true;
			for (INeo4jInstance instance : instanceList) {
				if (isFirst) {
					
					json.append("\"" + instance.getId() + "\"");
					isFirst = false;
					
				} else {
					json.append(", \"" + instance.getId() + "\"");
				}
			}
			
		}
		
		json.append(" ] }");
		return json.toString();
	}

	@Override
	public List<INeo4jInstance> getActiveInstances() {

		List<INeo4jInstance> instanceList = null;
		
		for(INeo4jInstance instance : neo4jRegistry.getfileList()){
			
			if(isAlive(instance.getProtocol(), instance.getPort(), instance.getHost())){
				
				if(instanceList == null){
					
					instanceList = new ArrayList<INeo4jInstance>();
					
				}
				
				instanceList.add(instance);
			}
			
		}
		
		return instanceList;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getInstanceId(String port, String host) {
		List<INeo4jInstance> fileList = neo4jRegistry.getfileList();
		for (INeo4jInstance file : fileList) {
			if (file.getPort().equals(port)
					&& file.getHost().equalsIgnoreCase(host)) {
				return file.getId();
			}
		}
		return null;
	}

	@Override
	public boolean isNeo4jInstanceRunning(String instanceId) {
		List<INeo4jInstance> activeInstances = getActiveInstances();
			if(activeInstances != null && activeInstances.size() != 0) {
			for(INeo4jInstance instance : activeInstances) {
				if(instance.getId().equals(instanceId)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
}
