package edu.asu.lerna.iolaus.service.impl;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.asu.lerna.iolaus.configuration.neo4j.impl.Neo4jRegistry;
import edu.asu.lerna.iolaus.domain.INeo4jInstance;
import edu.asu.lerna.iolaus.service.IDeleteDatasetManager;
import edu.asu.lerna.iolaus.web.InstanceController;

/**
 * This service deletes the dataset from all the Neo4j instances
 * @author Karan Kothari
 */
@Service
public class DeleteDatasetManager implements IDeleteDatasetManager {

	@Autowired
	private Neo4jRegistry registry;
	
	private static final Logger logger = LoggerFactory
			.getLogger(InstanceController.class);
	
	@Autowired
	@Qualifier("cypherEndPoint")
	private String cypherEndPoint;
	
	@Override
	public boolean deleteDataset(String dataset) {
		
		/*get server root uri's*/
		List<String> serverRootUriList=getCypherUriList();
		List<String> indexNameList=getIndexNameList();
		
		/*create jsonjson for deleting dataset and create a delete rest call*/
		if(serverRootUriList.size()==indexNameList.size()){
			for(int i=0;i<serverRootUriList.size();i++){
				String json=createJson(dataset,indexNameList.get(i));
				boolean flag=deleteRestCall(serverRootUriList.get(i),json);
				if(!flag)
					return false;
			}
		}

		return true;
	}
	
	/**
	 * This method creates json for deleting all Nodes and Relations. 
	 * @param dataset is a dataset property of node or relation.
	 * @param indexName is a name of Node Index.
	 * @return the json form for deleting all the Nodes and relations with dataset specified by the user.
	 */
	private String createJson(String dataset, String indexName) {

		StringBuilder query=new StringBuilder();
		query.append("\t\"query\":\"");
		query.append("start n=node:"+indexName+"(dataset={param1}) ");
		query.append("Match n-[r]-() ");
		query.append("delete n,r \",");
		StringBuilder params=new StringBuilder();
		params.append("\t\"params\":{\n");
		params.append("\t\t\"param1\":"+"\""+dataset+"\"\n");
		params.append("\t}");
		String json="{\n"+query.toString()+"\n"+params.toString()+"\n}";
		return json;
	}

	/**
	 * This method retrieves the all index names from the Neo4j instances
	 * @return the list for index names of the nodes.
	 */
	private List<String> getIndexNameList() {
		List<String> indexNameList = new ArrayList<String>();
		for (INeo4jInstance instance : registry.getfileList()) {
			if (instance.isActive()&&
					checkConnectivity(instance.getProtocol(), 
							instance.getPort(), instance.getHost())) {
				indexNameList.add(instance.getNodeIndex());
			}
		}
		return indexNameList;
	}

	/**
	 * This connects with the Neo4j rest api and passes json with the query. 
	 * @param entryPointUri is a URI where Iolaus will be hitting Neo4j REST api. 
	 * @param json is json for deleting dataset property.
	 * @return true if deletion is successful.
	 */
	private boolean deleteRestCall(String entryPointUri, String json) {
		WebResource resource = Client.create().resource( entryPointUri );
		ClientResponse response = resource.accept( MediaType.APPLICATION_JSON ).
				type( MediaType.APPLICATION_JSON ).
				entity( json ).
				post( ClientResponse.class );
		logger.info(json);
		logger.info(String.format("POST to [%s], status code [%d]",
				entryPointUri, response.getStatus()));
		if(response.getStatus()!=200){
			return false;
		}else{
			return true;
		}
	}

	/**
	 * This method generates the server root URI using the databaseList.
	 * @param databaseList is list of id's of the instances specified in the xml.
	 * @return the list of server root URI corresponding to the list of database id's.
	 */
	private List<String> getCypherUriList() {
		List<String> serverRootUriList = new ArrayList<String>();
		for (INeo4jInstance instance : registry.getfileList()) {
			if (instance.isActive()&&
					checkConnectivity(instance.getProtocol(), 
							instance.getPort(), instance.getHost())) {
				serverRootUriList.add(instance.getProtocol() + "://"
						+ instance.getHost() + ":" + instance.getPort() + "/"
						+ instance.getDbPath() + "/" + cypherEndPoint);
			}
		}
		return serverRootUriList;
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
