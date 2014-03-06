package edu.asu.lerna.iolaus.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.annotation.Inherited;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.asu.lerna.iolaus.configuration.neo4j.impl.Neo4jRegistry;
import edu.asu.lerna.iolaus.domain.INeo4jInstance;
import edu.asu.lerna.iolaus.domain.dataset.IDataset;
import edu.asu.lerna.iolaus.domain.dataset.INode;
import edu.asu.lerna.iolaus.domain.dataset.IProperty;
import edu.asu.lerna.iolaus.domain.dataset.IRelation;
import edu.asu.lerna.iolaus.domain.dataset.impl.Dataset;
import edu.asu.lerna.iolaus.domain.dataset.impl.Property;
import edu.asu.lerna.iolaus.exception.IndexPropertyException;
import edu.asu.lerna.iolaus.exception.InsertNodeException;
import edu.asu.lerna.iolaus.exception.InsertRelationException;
import edu.asu.lerna.iolaus.exception.UploadDatasetException;
import edu.asu.lerna.iolaus.service.IUploadManager;

/**
 * This Service uploads the nodes and Relations present in the xml to the Neo4j instances specified in the Xml.  
 * @author Karan Kothari
 */
@Service
public class UploadManager implements IUploadManager{

	private static final Logger logger = LoggerFactory
			.getLogger(UploadManager.class);
	@Autowired
	private Neo4jRegistry registry;
	
	/**
	 * {@link Inherited}
	 * @throws UploadDatasetException 
	 */
	@Override
	public boolean uploadDataset(String datasetXml) throws JAXBException, UploadDatasetException {
		
		boolean returnFlag=true;
		IDataset dataset=null;
		
		if(datasetXml==null){
			return false;
		}
		
		try{
			dataset=xmlToObject(datasetXml);
		}catch(JAXBException exception){
			return false;
		}
		
		/*size of list is equal to the count of Neo4j instances where data is getting replicated. 
		map stores mapping between local and global Id of nodes for a particular instance.*/  
		List<Map<Long,String>> nodeUriList=new ArrayList<Map<Long,String>>();
		
		//return false if dataset is null or empty
		if(dataset.getDatabaseList()==null || dataset.getDatabaseList().size()==0)
			return false;
		
		List<String> serverRootUriList=getServerRootUriList(dataset.getDatabaseList());
		List<String> nodeIndexUriList=getIndexList(dataset.getDatabaseList(),nodeIndexEntryPoint);
		List<String> relationIndexUriList=getIndexList(dataset.getDatabaseList(),relationIndexEntryPoint);
		
		/* initialize the List equal to the nodes that you  */
		initializeList(nodeUriList,serverRootUriList.size());
		
		
		 /* Adds a default datasetType property to each node and relation*/
		IProperty datasetProperty=new Property();
		datasetProperty.setName(datasetType);
		datasetProperty.setValue(dataset.getDatasetType());
		
		/**
		 * inserts nodes to the Neo4j instances specified in the xml.
		 * adds properties of nodes to the index.  
		 */
		try {
			for (INode node : dataset.getNodeList()) {
				node.getPropertyList().add(datasetProperty);
				/*
				 * Add type in the PropertyList in order to add type to node index
				*/
				IProperty property=new Property();
		    	property.setName("label");
		    	property.setValue(node.getLabel());
		    	node.getPropertyList().add(property);
				// inserts nodes in multiple instances of Neo4j
				List<String> storedUri = insertNode(node.getJsonNode(),
						serverRootUriList);
				addPropertyToNodeIndex(node, storedUri, nodeIndexUriList);
				for (int i = 0; i < serverRootUriList.size(); i++) {
					nodeUriList.get(i).put(node.getId(), storedUri.get(i));
				}
			}
		} catch (UploadDatasetException exception) {
			returnFlag=false;
		}

		/**
		 * inserts relationships to the Neo4j instances specified in the xml.
		 * adds properties of relationship to the index.
		 */
		try {
			for (IRelation relation : dataset.getRelationList()) {
				relation.getPropertyList().add(datasetProperty);
				addRelations(relation, nodeUriList, serverRootUriList,
						relationIndexUriList,dataset.isIndexRelation());
			}
		} catch (UploadDatasetException exception) {
			returnFlag = false;
		}
		return returnFlag;
	}
	
	/**
	 * This method retrieves the index names from Neo4j instances corresponding to databaseList.  
	 * @param databaseList is list of id of database instances.
	 * @param entrypoint specifies the node index or relation index.
	 * 			if entry point is "node" then index name for node will be retrieved. 
	 * 			if entry point is "relationship" then index name for relationship will be retrieved. 
	 * @return the list of uri for nodes or relationships. 
	 * 			e.g. [http://localhost:7474/db/data/index/node/favorites,http://localhost:7476/db/data/index/node/new_index].
	 */
	private List<String> getIndexList(List<String> databaseList, String entrypoint) {
		if(databaseList==null || databaseList.size()==0)
			return null;
		else{
			List<String> indexNameUriList=new ArrayList<String>();
			for(INeo4jInstance instance:registry.getfileList()){
				if(instance.isActive()&&
						checkConnectivity(instance.getProtocol(), 
								instance.getPort(), instance.getHost())){
					//http://localhost:7474/db/data/index/node/favorites
					if(databaseList.contains(instance.getId())){
						String index;
						String indexName;
						if(entrypoint.equals(nodeIndexEntryPoint)){
							index=nodeIndexEntryPoint;
							indexName=instance.getNodeIndex();
						}else{
							index=relationIndexEntryPoint;
							indexName=instance.getRelationIndex();
						}
						//creates uri for adding properties to the index.
						indexNameUriList.add(instance.getProtocol()+"://"+instance.getHost()+":"+
								instance.getPort()+"/"+instance.getDbPath()+"/"+
								indexNameEntryPoint+"/"+index+"/"+indexName);
					}
				}
			}
			return indexNameUriList;
		}
	}

	/**
	 * This method adds properties of Node to the node index name.
	 * @param node is {@link INode} consisting List of properties.
	 * @param nodeUriList is the list of uri. each uri corresponds to the URI of node for particular instance. 
	 * @param nodeIndexUriList is list of index name URI of instances specified in the xml.
	 * @throws IndexPropertyException when REST call fails.
	 */
	private void addPropertyToNodeIndex(INode node,	List<String> nodeUriList, List<String> nodeIndexUriList) throws IndexPropertyException {
		
		/*adds properties of nodes to the list of node index uri's*/
		for(IProperty property : node.getPropertyList()){
			for(String nodeUri : nodeUriList){
				addPropertyToNodeIndex(property.getJsonProperty(nodeUri),nodeIndexUriList);
			}
		}
	}
	
	/**
	 * This method adds a single property of {@link INode} to the node index name.
	 * @param jsonProperty is a Property of node in Json form. e.g. 
		 * 		{
		    	  "value" : "some value",
		    	  "uri" : "http://localhost:7474/db/data/node/83",
		    	  "key" : "some-key"
	    		} 
	 * @param nodeIndexUriList is list of index name URI of instances specified in the xml. 
	 * @throws IndexPropertyException when REST call fails.
	 */
	private void addPropertyToNodeIndex(String jsonProperty,
			List<String> nodeIndexUriList) throws IndexPropertyException {
		/*adds single property to the list of node index uri's*/
		for(String nodeIndexUri:nodeIndexUriList){
			if(makeRESTCall(nodeIndexUri, jsonProperty)==null){
				throw new IndexPropertyException("Error in inserting relations into Neo4j instance\n" +
						"Relation index URI - "+nodeIndexUri +"Json Property - "+jsonProperty);
			}
		}
	}

	/**
	 * This method parse the nodeURI and extracts node id from it.
	 * @param nodeUri is URI of node.
	 * @return node id.
	 */
	private long getNodeId(String nodeUri) {
		if(nodeUri!=null)
			return Long.parseLong(nodeUri.substring(nodeUri.lastIndexOf("/")+1));
		else
			return 0;
	}

	/**
	 * This method adds {@link IRelation} in the specified NEo4j instances. 
	 * It also adds properties associated with them to the Relation Index Name.
	 * @param relation is a {@link IRelation}. 
	 * @param nodeURIList is a list of Node URI for finding mapping between local id's and id's assigned by Neo4j.
	 * @param serverRootUriList is a list of server root URI for adding Relations to Neo4j.
	 * @param relationIndexUriList is a list of Relation index names for adding properties of Relations.
	 * @throws InsertRelationException when REST call for adding relation fails.
	 * @throws IndexPropertyException when REST call for adding property to relation index name fails.
	 */
	private void addRelations(IRelation relation,List<Map<Long, String>> nodeURIList, 
			List<String> serverRootUriList, List<String> relationIndexUriList, boolean isIndexRelation) throws InsertRelationException,IndexPropertyException {

		for(int i=0;i<serverRootUriList.size();i++){
			long startNode=relation.getStartNode();//local id specified in the xml
			long endNode=relation.getEndNode();
			long startNodeId=getNodeId(getNodeUri(nodeURIList,startNode,i));//unique id assigned by the database
			String endNodeUri=getNodeUri(nodeURIList,endNode,i);
			String location=addRelation(startNodeId,relation.getJsonRelation(endNodeUri),serverRootUriList.get(i));
			if(location==null){
				throw new InsertRelationException("Error in inserting relations into Neo4j instance\n" +
						"URI - "+serverRootUriList.get(i));
			}
			if(isIndexRelation){
				addPropertyToRelationIndex(relation,location,relationIndexUriList.get(i));
			}
		}
		
	}
	
	/**
	 * This method adds relation to the specified Neo4j instance.
	 * @param startNodeId is id of the start node of the relation
	 * @param json is a json of relation. e.g.
	 * 		 	{
	 *		  	  "to"   : "http://localhost:7474/db/data/node/10",
	 *			  "type" : "LOVES",
	 *			  "data" : {
	 *		    	"foo": "bar"
	 *			   }	
	 *			 }
	 * @param serverRootUri is a URI for identifying Neo4j instance
	 * @return the URI of the relationship.
	 */
	private String addRelation(long startNodeId, String json, String serverRootUri) {
		
		final String relationEntryPointUri = serverRootUri + nodeEntryPoint + "/" + startNodeId + "/" +relationEntryPoint;
		// http://localhost:7474/db/data/node/1/relationships
		return makeRESTCall(relationEntryPointUri, json);
	}
	
	/**
	 * This method adds all the properties of the relation to the relation index URI.
	 * @param relation is a {@link IRelation}.
	 * @param location is the URI of the relation.
	 * @param relationIndexUri is the URI of relation index name.
	 * @throws IndexPropertyException when REST call for adding property to Relation Index URI fails.
	 */
	private void addPropertyToRelationIndex(IRelation relation,	String location, String relationIndexUri) throws IndexPropertyException {
		for(IProperty property : relation.getPropertyList()){
				addPropertyToRelationIndex(property.getJsonProperty(location),relationIndexUri);
		}
	}
	
	
	/**
	 * This method adds a single property to the Relation Index URI.
	 * @param jsonProperty is json form of the property.
	 * @param relationIndexUri is the URI of relation index name. 
	 * @throws IndexPropertyException when REST call for adding property to Relation Index URI fails. 
	 */
	private void addPropertyToRelationIndex(String jsonProperty,
			String relationIndexUri) throws IndexPropertyException {
			
		if(makeRESTCall(relationIndexUri, jsonProperty)==null){
				throw new IndexPropertyException("Error in inserting relations into Neo4j instance\n" +
						"Relation index URI - "+relationIndexUri +"Json Property - "+jsonProperty);
		}
	}
	
	/**
	 * This method gets Node URI from the List.
	 * @param nodeUriList is List of mappings between local id's and URI.
	 * @param nodeId is local id.
	 * @param index is index of the Map within the List
	 * @return the URI which refers the global id.
	 */
	private String getNodeUri(List<Map<Long,String>> nodeUriList, long nodeId, int index) {
		if(nodeUriList.size()>index){
			if(nodeUriList.get(index).containsKey(nodeId)){
				return nodeUriList.get(index).get(nodeId);
			}
		}
		return null;
	}

	/**
	 * This method generates the server root URI using the databaseList.
	 * @param databaseList is list of id's of the instances specified in the xml.
	 * @return the list of server root URI corresponding to the list of database id's.
	 */
	private List<String> getServerRootUriList(List<String> databaseList) {
		if(databaseList==null || databaseList.size()==0)
			return null;
		else{
			List<String> serverRootUriList=new ArrayList<String>();
			for(INeo4jInstance instance:registry.getfileList()){
				if(instance.isActive()&&
						checkConnectivity(instance.getProtocol(), 
								instance.getPort(), instance.getHost())){
					if(databaseList.contains(instance.getId())){
						serverRootUriList.add(instance.getProtocol()+"://"+instance.getHost()+":"+instance.getPort()+"/"+instance.getDbPath()+"/");
					}
				}
			}
			return serverRootUriList;
		}
	}

	/**
	 * This method creates empty objects of Map<Long,String> and adds it to the List
	 * @param nodeUriList is a node URI list
	 * @param size is equal to the number of instances specified in the xml.
	 */
	private void initializeList(List<Map<Long, String>> nodeUriList, int size) {
		for(int i=0;i<size;i++)
			nodeUriList.add(new HashMap<Long,String>());
	}
	
	
	/**
	 * This method inserts json to the list of server root URI.
	 * @param json is a json form of the node.
	 * 			e.g. {
  	 *					"foo" : "bar"
	 *				 } 
	 * @param serverRootUriList is the list of server root URI.
	 * @return the list of URI assigned by the Neo4j instances.
	 * @throws InsertNodeException when REST call for adding nodes to the Neo4j fails.
	 */
	private List<String> insertNode(String json,List<String> serverRootUriList) throws InsertNodeException{
		if(json==null || serverRootUriList==null || serverRootUriList.size()==0)
			return null;
		List<String> nodeUriList=new ArrayList<String>();
		for(String serverUri:serverRootUriList){
			String nodeUri=insertNode(json,serverUri);
			if(nodeUri==null)
				throw new InsertNodeException("Problem in inserting node - " + json+"\n to server uri - "+serverUri);
			else 
				nodeUriList.add(nodeUri);
		}
		return nodeUriList;
	}
	
	
	/**
	 * This method adds a json node to single Neo4j instance.
	 * @param json is json form of the node.
	 * @param serverRootUri is the server root URI of the Neo4j instance.
	 * @return the location (URI assigned by the Neo4j).
	 */
	private String insertNode(String json,String serverRootUri){
		final String nodeEntryPointUri = serverRootUri + nodeEntryPoint;
		// http://localhost:7474/db/data/node
		return makeRESTCall(nodeEntryPointUri, json);
	}	
	
	/**
	 * This method makes a POST REST call to the REST api of Neo4j.
	 * @param entryPointUri is the target URI. 
	 * @param json is the entity passed with the POST request.
	 * @return
	 */
	private String makeRESTCall(String entryPointUri,String json){
		WebResource resource = Client.create().resource( entryPointUri );
		ClientResponse response = resource.accept( MediaType.APPLICATION_JSON ).
				type( MediaType.APPLICATION_JSON ).
				entity( json ).
				post( ClientResponse.class );
		final URI location = response.getLocation();
		if(location==null){
			return null;
		}
		logger.info(String.format("POST to [%s], status code [%d], location header [%s]",
				entryPointUri, response.getStatus(), location.toString()));
		response.close();
		return location.toString();
	}
	
	/**
	 * Use Unmarshaller to unmarshal the XMl into Query object.
	 * @param xml is input xml
	 * @return the {@link IDataset} object after unmarshalling the xml.
	 * @throws JAXBException when unmarshalling fails.
	 */
	private IDataset xmlToObject(String xml) throws JAXBException{
		JAXBContext context = JAXBContext.newInstance(Dataset.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		unmarshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
		InputStream is = new ByteArrayInputStream(xml.getBytes());
		JAXBElement<Dataset> response =  unmarshaller.unmarshal(new StreamSource(is), Dataset.class);
		IDataset dataset = response.getValue();
		return dataset;
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
