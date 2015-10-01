package edu.asu.lerna.iolaus.service.impl;

import java.lang.annotation.Inherited;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.Base64;

import edu.asu.lerna.iolaus.configuration.neo4j.impl.Neo4jRegistry;
import edu.asu.lerna.iolaus.domain.INeo4jInstance;
import edu.asu.lerna.iolaus.domain.dataset.IDataset;
import edu.asu.lerna.iolaus.domain.dataset.INode;
import edu.asu.lerna.iolaus.domain.dataset.IProperty;
import edu.asu.lerna.iolaus.domain.dataset.IRelation;
import edu.asu.lerna.iolaus.domain.dataset.impl.Property;
import edu.asu.lerna.iolaus.exception.IndexPropertyException;
import edu.asu.lerna.iolaus.exception.InsertRelationException;
import edu.asu.lerna.iolaus.exception.UploadDatasetException;
import edu.asu.lerna.iolaus.service.IUploadManager;

/**
 * This Service uploads the nodes and Relations present in the xml to the Neo4j
 * instances specified in the Xml.
 * 
 * @author Karan Kothari
 */
@Service
public class UploadManager implements IUploadManager {

	private static final Logger logger = LoggerFactory
			.getLogger(UploadManager.class);
	@Autowired
	private Neo4jRegistry registry;

	/**
	 * {@link Inherited}
	 * 
	 * @throws UploadDatasetException
	 */
	@Override
	public boolean uploadDataset(IDataset dataset) throws JAXBException,
			UploadDatasetException {

		boolean success = true;

		/*
		 * size of list is equal to the count of Neo4j instances where data is
		 * getting replicated. map stores mapping between local and global Id of
		 * nodes for a particular instance.
		 */

		if (dataset.getDatabaseList() == null
				|| dataset.getDatabaseList().size() == 0) {
			return false;
		}

		List<INeo4jInstance> neo4jInstances = getServerRootUriList(dataset
				.getDatabaseList());

		/* Adds a default datasetType property to each node and relation */
		IProperty datasetProperty = new Property();
		datasetProperty.setName(datasetType);
		datasetProperty.setValue(dataset.getDatasetType());

		/**
		 * inserts nodes to the Neo4j instances specified in the xml. adds
		 * properties of nodes to the index.
		 */
		try {
			for (INeo4jInstance instance : neo4jInstances) {
				Map<Long, String> nodeUriList = new HashMap<Long, String>();

				String nodeIndexUri = getIndex(instance.getId(),
						nodeIndexEntryPoint);
				String relationIndexUri = getIndex(instance.getId(),
						relationIndexEntryPoint);

				for (INode node : dataset.getNodeList()) {
					node.getPropertyList().add(datasetProperty);
					/*
					 * Add type in the PropertyList in order to add type to node
					 * index
					 */
					IProperty property = new Property();
					property.setName("label");
					property.setValue(node.getLabel());
					node.getPropertyList().add(property);
					// inserts nodes in multiple instances of Neo4j
					String nodeUri = insertNode(node.getJsonNode(), instance);
					for (String json : node.getNodeAsJsonForIndexing(nodeUri)) {
						addPropertyToNodeIndex(json, nodeIndexUri, instance);
					}

					for (int i = 0; i < neo4jInstances.size(); i++) {
						nodeUriList.put(node.getId(), nodeUri);
					}
				}
				
				/**
				 * inserts relationships to the Neo4j instances specified in the xml.
				 * adds properties of relationship to the index.
				 */
				try {
					for (IRelation relation : dataset.getRelationList()) {
						relation.getPropertyList().add(datasetProperty);
						addRelations(relation, nodeUriList, instance,
								relationIndexUri, dataset.isIndexRelation());
					}
				} catch (UploadDatasetException exception) {
					success = false;
				}
			}
		} catch (UploadDatasetException exception) {
			logger.error("Error uploading dataset.", exception);
			success = false;
		}
		return success;
	}

	/**
	 * This method retrieves the index names from Neo4j instances corresponding
	 * to databaseList.
	 * 
	 * @param databaseList
	 *            is list of id of database instances.
	 * @param entrypoint
	 *            specifies the node index or relation index. if entry point is
	 *            "node" then index name for node will be retrieved. if entry
	 *            point is "relationship" then index name for relationship will
	 *            be retrieved.
	 * @return the list of uri for nodes or relationships. e.g.
	 *         [http://localhost
	 *         :7474/db/data/index/node/favorites,http://localhost
	 *         :7476/db/data/index/node/new_index].
	 */
	private String getIndex(String instanceId, String entrypoint) {

		INeo4jInstance instance = registry.getInstanceById(instanceId);

		if (instance.isActive()
				&& checkConnectivity(instance.getProtocol(),
						instance.getPort(), instance.getHost())) {
			// http://localhost:7474/db/data/index/node/favorites
			String index;
			String indexName;
			if (entrypoint.equals(nodeIndexEntryPoint)) {
				index = nodeIndexEntryPoint;
				indexName = instance.getNodeIndex();
			} else {
				index = relationIndexEntryPoint;
				indexName = instance.getRelationIndex();
			}
			// creates uri for adding properties to the index.
			return instance.getProtocol() + "://" + instance.getHost() + ":"
					+ instance.getPort() + "/" + instance.getDbPath() + "/"
					+ indexNameEntryPoint + "/" + index + "/" + indexName;

		}
		return null;
	}

	/**
	 * This method adds a single property of {@link INode} to the node index
	 * name.
	 * 
	 * @param jsonProperty
	 *            is a Property of node in Json form. e.g. { "value" :
	 *            "some value", "uri" : "http://localhost:7474/db/data/node/83",
	 *            "key" : "some-key" }
	 * @param nodeIndexUriList
	 *            is list of index name URI of instances specified in the xml.
	 * @throws IndexPropertyException
	 *             when REST call fails.
	 */
	private void addPropertyToNodeIndex(String jsonProperty,
			String nodeIndexUri, INeo4jInstance instance)
			throws IndexPropertyException {
		/* adds single property to the list of node index uri's */
		if (makeRESTCall(nodeIndexUri, jsonProperty, instance) == null) {
			throw new IndexPropertyException(
					"Error in inserting relations into Neo4j instance\n"
							+ "Relation index URI - " + nodeIndexUri
							+ "Json Property - " + jsonProperty);
		}

	}

	/**
	 * This method adds {@link IRelation} in the specified NEo4j instances. It
	 * also adds properties associated with them to the Relation Index Name.
	 * 
	 * @param relation
	 *            is a {@link IRelation}.
	 * @param nodeURIList
	 *            is a list of Node URI for finding mapping between local id's
	 *            and id's assigned by Neo4j.
	 * @param neo4jInstances
	 *            is a list of server root URI for adding Relations to Neo4j.
	 * @param relationIndexUriList
	 *            is a list of Relation index names for adding properties of
	 *            Relations.
	 * @throws InsertRelationException
	 *             when REST call for adding relation fails.
	 * @throws IndexPropertyException
	 *             when REST call for adding property to relation index name
	 *             fails.
	 */
	private void addRelations(IRelation relation,
			Map<Long, String> nodeURIList, INeo4jInstance neo4jInstance,
			String relationIndexUri, boolean isIndexRelation)
			throws InsertRelationException, IndexPropertyException {

		long startNode = relation.getStartNode();// local id specified in
		long endNode = relation.getEndNode();
		String startNodeUri = nodeURIList.get(startNode);
		String endNodeUri = nodeURIList.get(endNode);

		String location = addRelation(startNodeUri,
				relation.getJsonRelation(endNodeUri), neo4jInstance);
		if (location == null) {
			throw new InsertRelationException(
					"Error in inserting relations into Neo4j instance\n"
							+ "URI - " + neo4jInstance);
		}
		if (isIndexRelation) {
			addPropertyToRelationIndex(relation, location, relationIndexUri,
					neo4jInstance);
		}
	}

	/**
	 * This method adds relation to the specified Neo4j instance.
	 * 
	 * @param startNodeId
	 *            is id of the start node of the relation
	 * @param json
	 *            is a json of relation. e.g. { "to" :
	 *            "http://localhost:7474/db/data/node/10", "type" : "LOVES",
	 *            "data" : { "foo": "bar" } }
	 * @param serverRootUri
	 *            is a URI for identifying Neo4j instance
	 * @return the URI of the relationship.
	 */
	private String addRelation(String startNodeUri, String json,
			INeo4jInstance instance) {

		final String relationEntryPointUri = startNodeUri + "/"
				+ relationEntryPoint;
		// http://localhost:7474/db/data/node/1/relationships
		return makeRESTCall(relationEntryPointUri, json, instance);
	}

	/**
	 * This method adds all the properties of the relation to the relation index
	 * URI.
	 * 
	 * @param relation
	 *            is a {@link IRelation}.
	 * @param location
	 *            is the URI of the relation.
	 * @param relationIndexUri
	 *            is the URI of relation index name.
	 * @throws IndexPropertyException
	 *             when REST call for adding property to Relation Index URI
	 *             fails.
	 */
	private void addPropertyToRelationIndex(IRelation relation,
			String location, String relationIndexUri, INeo4jInstance instance)
			throws IndexPropertyException {
		for (IProperty property : relation.getPropertyList()) {
			addPropertyToRelationIndex(property.getJsonProperty(location),
					relationIndexUri, instance);
		}
	}

	/**
	 * This method adds a single property to the Relation Index URI.
	 * 
	 * @param jsonProperty
	 *            is json form of the property.
	 * @param relationIndexUri
	 *            is the URI of relation index name.
	 * @throws IndexPropertyException
	 *             when REST call for adding property to Relation Index URI
	 *             fails.
	 */
	private void addPropertyToRelationIndex(String jsonProperty,
			String relationIndexUri, INeo4jInstance instance)
			throws IndexPropertyException {

		if (makeRESTCall(relationIndexUri, jsonProperty, instance) == null) {
			throw new IndexPropertyException(
					"Error in inserting relations into Neo4j instance\n"
							+ "Relation index URI - " + relationIndexUri
							+ "Json Property - " + jsonProperty);
		}
	}

	/**
	 * This method generates the server root URI using the databaseList.
	 * 
	 * @param databaseList
	 *            is list of id's of the instances specified in the xml.
	 * @return the list of server root URI corresponding to the list of database
	 *         id's.
	 */
	private List<INeo4jInstance> getServerRootUriList(List<String> databaseList) {
		if (databaseList == null || databaseList.size() == 0) {
			return null;
		} else {
			List<INeo4jInstance> instances = new ArrayList<INeo4jInstance>();
			for (INeo4jInstance instance : registry.getfileList()) {
				if (instance.isActive()
						&& checkConnectivity(instance.getProtocol(),
								instance.getPort(), instance.getHost())) {
					if (databaseList.contains(instance.getId())) {
						instances.add(instance);
					}
				}
			}
			return instances;
		}
	}


	/**
	 * This method adds a json node to single Neo4j instance.
	 * 
	 * @param json
	 *            is json form of the node.
	 * @param serverRootUri
	 *            is the server root URI of the Neo4j instance.
	 * @return the location (URI assigned by the Neo4j).
	 */
	private String insertNode(String json, INeo4jInstance instance) {
		final String nodeEntryPointUri = instance.getRootPath()
				+ nodeEntryPoint;
		// http://localhost:7474/db/data/node
		return makeRESTCall(nodeEntryPointUri, json, instance);
	}

	/**
	 * This method makes a POST REST call to the REST api of Neo4j.
	 * 
	 * @param entryPointUri
	 *            is the target URI.
	 * @param json
	 *            is the entity passed with the POST request.
	 * @return
	 */
	@Override
	public String makeRESTCall(String entryPointUri, String json,
			INeo4jInstance neo4jInstance) {
		WebResource resource = Client.create().resource(entryPointUri);
		String auth = new String(Base64.encode(neo4jInstance.getUserName()
				+ ":" + neo4jInstance.getPassword()));

		ClientResponse response = resource
				.header("Authorization", "Basic " + auth)
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).entity(json)
				.post(ClientResponse.class);
		final URI location = response.getLocation();
		if (location == null) {
			return null;
		}
		logger.info(String.format(
				"POST to [%s], status code [%d], location header [%s]",
				entryPointUri, response.getStatus(), location.toString()));
		response.close();
		return location.toString();
	}

	/**
	 * This method ping the Neo4j server. If it is up then returns true else
	 * returns false.
	 * 
	 * @param port
	 *            is port number.
	 * @param host
	 *            is address of the host machine.
	 * @return the status of server. If it is up then returns true else returns
	 *         false.
	 */
	private boolean checkConnectivity(String protocol, String port, String host) {
		boolean isAlive = true;
		String urlStr = "";
		try {
			urlStr = protocol + "://" + host + ":" + port + "/webadmin/";
			URL url = new URL(urlStr);
			URLConnection connection = url.openConnection();// It will throw an
															// exception if not
															// able to connect
															// to the server.
			connection.getInputStream();
		} catch (Exception e) {
			isAlive = false;
			logger.error("Error in the connectivity : ", e);
		}
		return isAlive;
	}

}
