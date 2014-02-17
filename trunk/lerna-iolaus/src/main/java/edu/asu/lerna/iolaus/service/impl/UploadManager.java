package edu.asu.lerna.iolaus.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
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
import edu.asu.lerna.iolaus.service.IUploadManager;
import edu.asu.lerna.iolaus.web.InstanceController;

@Service
public class UploadManager implements IUploadManager{

	private static final Logger logger = LoggerFactory
			.getLogger(InstanceController.class);
	@Autowired
	private Neo4jRegistry registry;
	
	@Override
	public boolean uploadDataset(String datasetXml) throws JAXBException {
		
		IDataset dataset=xmlToObject(datasetXml);
		List<Map<Long,String>> nodeUriList=new ArrayList<Map<Long,String>>();
		
		//return false if dataset is null or empty
		if(dataset.getDatabaseList()==null || dataset.getDatabaseList().size()==0)
			return false;
		
		List<String> serverRootUriList=getServerRootUriList(dataset.getDatabaseList());
		List<String> nodeIndexUriList=getIndexList(dataset.getDatabaseList(),nodeIndexEntryPoint);
		List<String> relationIndexUriList=getIndexList(dataset.getDatabaseList(),relationIndexEntryPoint);
		
		initializeList(nodeUriList,serverRootUriList.size());
		
		/**
		 * Add a default datasetType property to each node and relation
		 */
		IProperty datasetProperty=new Property();
		datasetProperty.setName(datasetType);
		datasetProperty.setValue(dataset.getDatasetType());
		
		/**
		 * inserts all nodes present in the dataset
		 */
		for(INode node:dataset.getNodeList()){
			node.getPropertyList().add(datasetProperty);
			//inserts nodes in multiple instances of Neo4j
			List<String> storedUri=insertNode(node.getJsonNode(),serverRootUriList);
			addPropertyToNodeIndex(node,storedUri,nodeIndexUriList);
			for(int i=0;i<serverRootUriList.size();i++){
				nodeUriList.get(i).put(node.getId(), storedUri.get(i));
			}
		}

		/**
		 * inserts all relationships in the dataset
		 */
		for(IRelation relation:dataset.getRelationList()){
			relation.getPropertyList().add(datasetProperty);
			addRelations(relation,nodeUriList,serverRootUriList,relationIndexUriList);
		}
		return true;
	}
	
	private List<String> getIndexList(List<String> databaseList, String entrypoint) {
		if(databaseList==null || databaseList.size()==0)
			return null;
		else{
			List<String> indexNameUriList=new ArrayList<String>();
			for(INeo4jInstance instance:registry.getfileList()){
				if(instance.isActive()){
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
						indexNameUriList.add("http://"+instance.getHost()+":"+
								instance.getPort()+"/"+instance.getDbPath()+"/"+
								indexNameEntryPoint+"/"+index+"/"+indexName);
					}
				}
			}
			return indexNameUriList;
		}
	}

	private void addPropertyToNodeIndex(INode node,	List<String> nodeUriList, List<String> indexNameList) {
		for(IProperty property : node.getPropertyList()){
			for(String nodeUri : nodeUriList){
				addPropertyToNodeIndex(property.getJsonProperty(nodeUri),indexNameList);
			}
		}
	}

	private void addPropertyToNodeIndex(String jsonProperty,
			List<String> indexNameUriList) {
		
		for(String indexNameUri:indexNameUriList){
			executeJson(indexNameUri, jsonProperty);
		}
	}

	private long getNodeId(String nodeUri) {
		if(nodeUri!=null)
			return Long.parseLong(nodeUri.substring(nodeUri.lastIndexOf("/")+1));
		else
			return 0;
	}

	private void addRelations(IRelation relation,List<Map<Long, String>> nodeUriList, List<String> serverRootUriList, List<String> relationIndexUriList) {
		
		for(int i=0;i<serverRootUriList.size();i++){
			long startNode=relation.getStartNode();//local id specified in the xml
			long endNode=relation.getEndNode();
			long startNodeId=getNodeId(getNodeUri(nodeUriList,startNode,i));//unique id assigned by the database
			String endNodeUri=getNodeUri(nodeUriList,endNode,i);
			String location=addRelation(startNodeId,relation.getJsonRelation(endNodeUri),serverRootUriList.get(i));
			addPropertyToRelationIndex(relation,location,relationIndexUriList.get(i));
		}
		
	}
	
	private String addRelation(long startNodeId, String json, String serverRootUri) {
		
		final String relationEntryPointUri = serverRootUri + nodeEntryPoint + "/" + startNodeId + "/" +relationEntryPoint;
		// http://localhost:7474/db/data/node/1/relationships
		return executeJson(relationEntryPointUri, json);
	}
	
	private void addPropertyToRelationIndex(IRelation relation,	String location, String relationIndexUri) {
		for(IProperty property : relation.getPropertyList()){
				addPropertyToRelationIndex(property.getJsonProperty(location),relationIndexUri);
		}
	}
	
	private void addPropertyToRelationIndex(String jsonProperty,
			String relationIndexUri) {
			
		executeJson(relationIndexUri, jsonProperty);
	}
	
	private String getNodeUri(List<Map<Long,String>> nodeUriList, long nodeId, int i) {
		if(nodeUriList.size()>i){
			if(nodeUriList.get(i).containsKey(nodeId)){
				return nodeUriList.get(i).get(nodeId);
			}
		}
		return null;
	}

	private List<String> getServerRootUriList(List<String> databaseList) {
		if(databaseList==null || databaseList.size()==0)
			return null;
		else{
			List<String> serverRootUriList=new ArrayList<String>();
			for(INeo4jInstance instance:registry.getfileList()){
				if(instance.isActive()){
					if(databaseList.contains(instance.getId())){
						serverRootUriList.add("http://"+instance.getHost()+":"+instance.getPort()+"/"+instance.getDbPath()+"/");
					}
				}
			}
			return serverRootUriList;
		}
	}

	private void initializeList(List<Map<Long, String>> nodeUriList, int size) {
		for(int i=0;i<size;i++)
			nodeUriList.add(new HashMap<Long,String>());
	}

	private List<String> insertNode(String json,List<String> serverRootUriList){
		if(json==null || serverRootUriList==null || serverRootUriList.size()==0)
			return null;
		List<String> nodeUriList=new ArrayList<String>();
		for(String serverUri:serverRootUriList){
			nodeUriList.add(insertNode(json,serverUri));
		}
		return nodeUriList;
	}
	
	private String insertNode(String json,String serverRootUri){
		final String nodeEntryPointUri = serverRootUri + nodeEntryPoint;
		// http://localhost:7474/db/data/node
		return executeJson(nodeEntryPointUri, json);
	}	
	
	private String executeJson(String entryPointUri,String json){
		WebResource resource = Client.create().resource( entryPointUri );
		ClientResponse response = resource.accept( MediaType.APPLICATION_JSON ).
				type( MediaType.APPLICATION_JSON ).
				entity( json ).
				post( ClientResponse.class );
		final URI location = response.getLocation();
		logger.info(String.format("POST to [%s], status code [%d], location header [%s]",
				entryPointUri, response.getStatus(), location.toString()));
		response.close();
		return location.toString();
	}
	
	private IDataset xmlToObject(String res) throws JAXBException{
		JAXBContext context = JAXBContext.newInstance(Dataset.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		unmarshaller.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
		InputStream is = new ByteArrayInputStream(res.getBytes());
		JAXBElement<Dataset> response =  unmarshaller.unmarshal(new StreamSource(is), Dataset.class);
		IDataset dataset = response.getValue();
		return dataset;
	}
	
}
