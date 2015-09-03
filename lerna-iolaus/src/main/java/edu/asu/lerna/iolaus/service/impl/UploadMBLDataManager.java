package edu.asu.lerna.iolaus.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.asu.lerna.iolaus.configuration.neo4j.impl.Neo4jRegistry;
import edu.asu.lerna.iolaus.domain.INeo4jInstance;
import edu.asu.lerna.iolaus.domain.mbl.nodes.Course;
import edu.asu.lerna.iolaus.domain.mbl.nodes.CourseGroup;
import edu.asu.lerna.iolaus.domain.mbl.nodes.Institution;
import edu.asu.lerna.iolaus.domain.mbl.nodes.Location;
import edu.asu.lerna.iolaus.domain.mbl.nodes.Node;
import edu.asu.lerna.iolaus.domain.mbl.nodes.Person;
import edu.asu.lerna.iolaus.domain.mbl.relations.Affiliation;
import edu.asu.lerna.iolaus.domain.mbl.relations.Attendance;
import edu.asu.lerna.iolaus.domain.mbl.relations.HasLocation;
import edu.asu.lerna.iolaus.domain.mbl.relations.Investigator;
import edu.asu.lerna.iolaus.domain.mbl.relations.IsPartOf;
import edu.asu.lerna.iolaus.domain.mbl.relations.Relation;
import edu.asu.lerna.iolaus.exception.IndexPropertyException;
import edu.asu.lerna.iolaus.exception.Neo4jInstanceIdDoesNotExist;
import edu.asu.lerna.iolaus.service.ICacheManager;
import edu.asu.lerna.iolaus.service.IUploadMBLDataManager;
import edu.asu.lerna.iolaus.service.IUploadManager;

@Service
public class UploadMBLDataManager implements IUploadMBLDataManager {
	
	private static final Logger logger = LoggerFactory
			.getLogger(UploadMBLDataManager.class);

	@Autowired
	private ICacheManager cacheManager;
	
	@Autowired
	private Neo4jRegistry registry;
	
	@Autowired
	private IUploadManager uploadManager;
	
	//Data format - Year,Last Name,Person URI,Course Name,First Name,Role,Course URI
	@Override
	public boolean uploadAttendedDataset(File file, String instanceId) throws IOException, IndexPropertyException, Neo4jInstanceIdDoesNotExist {
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		boolean success = true;
		br.readLine();
		while((line = br.readLine()) != null) {
			String[] data = line.split(IUploadMBLDataManager.regex);
			Person from = null;
			Course to = null;
			if(data.length >= 7) {
				String fromNodeId = null;
				String toNodeId = null;
				//From node
				if(data[2] != null && data[4] != null && data[1] != null) {
					from = new Person(data[2].trim().replaceAll("\"", ""), data[4].trim().replaceAll("\"", ""), data[1].trim().replaceAll("\"", ""));
					fromNodeId = createNode(from, instanceId);
				} else {
					success = false;
				}
				
				//To Node
				if(data[6] != null && data[3] != null && data[0] != null) {
					int year;
					try {
						year = Integer.parseInt(data[0].trim().replaceAll("\"", ""));
					} catch (NumberFormatException e) {
						year = 0;
					}
					to = new Course(data[6].trim(), data[3].trim(), year);
					toNodeId = createNode(to, instanceId);
				} else {
					success = false;
				}

				//Relationship
				if(fromNodeId != null && toNodeId != null && data[5] != null) {
					int year;
					try {
						year = Integer.parseInt(data[0].trim().replaceAll("\"", ""));
					} catch (NumberFormatException e) {
						year = 0;
					}
					Relation relation = new Attendance(toNodeId, year, data[5].trim().replaceAll("\"", ""));
					createRelationship(relation, fromNodeId);
				}
				
			}
		}
		br.close();
		return success;
	}

	private void createRelationship(Relation relation, String fromNodeId) throws JsonGenerationException, JsonMappingException, IOException {
		String relationshipIUri = fromNodeId + "/" + relationEntryPoint;
		uploadManager.makeRESTCall(relationshipIUri, relation.toJson(), registry.getInstanceById("1"));
	}

	private String createNode(Node node, String instanceId) throws JsonGenerationException, JsonMappingException, IOException, IndexPropertyException, Neo4jInstanceIdDoesNotExist {
		String nodeId;
		nodeId = cacheManager.getCachedNodeId(node, instanceId);
		if(nodeId == null) {
			String entryPoint = createUri(instanceId);
			String nodeEntryPointUri = entryPoint + nodeEntryPoint;
			String indexName = getNodeIndexName(instanceId);
			nodeId = retrieveNodeIdFromNeo4j(node, entryPoint, indexName);
			if(nodeId == null) {
				nodeId = uploadManager.makeRESTCall(nodeEntryPointUri, node.toJson(), registry.getInstanceById(instanceId));
				cacheManager.cacheNodeId(node.getUri(), instanceId, nodeId);
				if(indexName != null) {
					String indexUri = entryPoint + indexNameEntryPoint + "/" + nodeEntryPoint + "/" + indexName;
					for(String propertyJson : node.getPropertyJson(nodeId)) {
						logger.info("Adding node property to index - " + nodeId);
						if(uploadManager.makeRESTCall(indexUri, propertyJson, registry.getInstanceById(instanceId)) == null){
							throw new IndexPropertyException("Error in inserting property to Neo4j instance\n" +
									"Node index URI - " + nodeId + "Json Property - " + propertyJson);
						}
					}
				}
			} else {
				cacheManager.cacheNodeId(node.getUri(), instanceId, nodeId);
			}
		} 
		return nodeId;
	}

	private String retrieveNodeIdFromNeo4j(Node node, String entryPoint, String indexName) {
		String jsonQuery = createJsonQueryToGetNode(node.getUri(), indexName);
		WebResource resource = Client.create().resource( entryPoint + "cypher" );
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.entity(jsonQuery)
				.post(ClientResponse.class);


		String jsonResponse = response.getEntity(String.class);
		JSONObject jsonObect = new JSONObject(jsonResponse);
		String nodeId = null;
		if(jsonObect.has("data")) {
			JSONArray dataArray = jsonObect.getJSONArray("data");
			if(dataArray != null) {
				if(dataArray.length() >= 1) {
					JSONArray firstResult = dataArray.getJSONArray((0));
					JSONObject firstNodeDataObject = firstResult.getJSONObject(0);
					if(firstNodeDataObject.has("self")) {
						nodeId = firstNodeDataObject.getString("self");
						logger.info("Retrived Node from Neo4j - " + nodeId);
					}
				}
			}
		}
		response.close();
		return nodeId;
	}

	private String createJsonQueryToGetNode(String uri, String indexName) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		json.append("\"query\" : ");
		json.append("\"START root=node:" + indexName + "(uri={uri}) RETURN root\",");
		json.append("\"params\" : {");
		json.append("\"uri\" : " + "\"" + uri + "\"");
		json.append("} }");
		return json.toString();
	}

	private String getNodeIndexName(String instanceId) {
		String indexName = null;
		for(INeo4jInstance instance : registry.getfileList()) {
			if(instance.getId().equals(instanceId)) {
				indexName = instance.getNodeIndex();
				break;
			}
		}
		return indexName;
	}

	private String createUri(String instanceId) throws Neo4jInstanceIdDoesNotExist {
		String entryPointUri = null;
		for(INeo4jInstance instance : registry.getfileList()) {
			if(instance.getId().equals(instanceId)) {
				entryPointUri = instance.getProtocol()+"://"+instance.getHost()+":"+instance.getPort()+"/"+instance.getDbPath()+"/";
				break;
			}
		}
		if(entryPointUri == null) 
			throw new Neo4jInstanceIdDoesNotExist("Neo4j instance Id - " + instanceId + " does not exist");
		if(entryPointUri.contains("null")) 
			throw new Neo4jInstanceIdDoesNotExist("Some Properties required to create URI are null");
		return entryPointUri;
	}
	
	

	@Override
	public boolean uploadAffilationDataset(File file, String instanceId) throws IOException, IndexPropertyException, Neo4jInstanceIdDoesNotExist {
		//Institution URI,Last Name,Year,First Name,Person URI,Position,Institution
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		br.readLine();
		boolean success = true; 
		while((line = br.readLine()) != null) {
			String[] data = line.split(IUploadMBLDataManager.regex);
			Person from = null;
			Institution to = null;
			if(data.length >= 7) {
				String fromNodeId = null;
				String toNodeId = null;
				//From node
				if(data[4] != null && data[3] != null && data[1] != null) {
					from = new Person(data[4].trim().replaceAll("\"", ""), data[3].trim().replaceAll("\"", ""), data[1].trim().replaceAll("\"", ""));
					fromNodeId = createNode(from, instanceId);
				} else {
					success = false;
				}
				
				//To Node
				if(data[0] != null && data[6] != null) {
					to = new Institution(data[0].trim().replaceAll("\"", ""), data[6].trim().replaceAll("\"", ""));
					toNodeId = createNode(to, instanceId);
				} else {
					success = false;
				}

				//Relationship
				if(fromNodeId != null && toNodeId != null && data[2] != null && data[5] != null) {
					int year;
					try {
						year = Integer.parseInt(data[2].trim().replaceAll("\"", ""));
					} catch (NumberFormatException e) {
						year = 0;
					}
					Relation relation = new Affiliation(toNodeId, year, data[5].trim().replaceAll("\"", ""));
					createRelationship(relation, fromNodeId);
				}
				
			}
		}
		br.close();
		return success;
	}

	@Override
	public boolean uploadInvestigatorDataset(File file, String instanceId) throws IOException, IndexPropertyException, Neo4jInstanceIdDoesNotExist {
		//Last Name,Person URI,First Name,Role,Year,Subject
		String institute = "Marine Biological Laboratory";
		String instituteUri = "http://history.archives.mbl.edu/concepts/institution/7a9c7e3a-baf1-4855-9447-dc628cc98640";
		BufferedReader br = new BufferedReader(new FileReader(file));
		br.readLine();
		String line;
		boolean success = true; 
		while((line = br.readLine()) != null) {
			String[] data = line.split(IUploadMBLDataManager.regex);
			Institution from = null;
			Person to = null;
			if(data.length >= 6) {
				String fromNodeId = null;
				String toNodeId = null;
				//From node
				from = new Institution(instituteUri, institute);
				fromNodeId = createNode(from, instanceId);
				
				//To Node
				if(data[1] != null && data[2] != null && data[0] != null) {
					to = new Person(data[1].trim().replaceAll("\"", ""), data[2].trim().replaceAll("\"", ""), data[3].trim().replaceAll("\"", ""));
					toNodeId = createNode(to, instanceId);
				} else {
					success = false;
				}

				//Relationship
				if(fromNodeId != null && toNodeId != null && data[3] != null && data[4] != null && data[5] != null) {
					int year;
					try {
						year = Integer.parseInt(data[3].trim().replaceAll("\"", ""));
					} catch (NumberFormatException e) {
						year = 0;
					}
					Relation relation = new Investigator(toNodeId, year, data[4].trim().replaceAll("\"", ""), data[5].trim().replaceAll("\"", ""));
					createRelationship(relation, fromNodeId);
				}
				
			}
		}
		br.close();
		return success;
	}

	@Override
	public boolean uploadLocationDataset(File file, String instanceId) throws IOException, IndexPropertyException, Neo4jInstanceIdDoesNotExist {
		//Location URI,Last Name,Person URI,First Name,Location,Year
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		boolean success = true;
		br.readLine();
		while((line = br.readLine()) != null) {
			String[] data = line.split(IUploadMBLDataManager.regex);
			Person from = null;
			Location to = null;
			if(data.length >= 6) {
				String fromNodeId = null;
				String toNodeId = null;
				//From node
				if(data[2] != null && data[3] != null && data[1] != null) {
					from = new Person(data[2].trim().replaceAll("\"", ""), data[3].trim().replaceAll("\"", ""), data[1].trim().replaceAll("\"", ""));
					fromNodeId = createNode(from, instanceId);
				} else {
					success = false;
				}
				
				//To Node
				if(data[0] != null && data[4] != null) {
					to = new Location(data[0].trim().replaceAll("\"", ""), data[4].trim().replaceAll("\"", ""));
					toNodeId = createNode(to, instanceId);
				} else {
					success = false;
				}

				//Relationship
				if(fromNodeId != null && toNodeId != null && data[5] != null) {
					int year;
					try {
						year = Integer.parseInt(data[5].trim().replaceAll("\"", ""));
					} catch (NumberFormatException e) {
						year = 0;
					}
					Relation relation = new HasLocation(toNodeId, year);
					createRelationship(relation, fromNodeId);
				}
				
			}
		}
		br.close();
		return success;
	}

	@Override
	public boolean uploadCourseGroupDataset(File file, String instanceId) throws IOException, IndexPropertyException, Neo4jInstanceIdDoesNotExist {
		//Year,	Course URI, Course Group URI,	Course Group,	Course Name.
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		boolean success = true;
		br.readLine();
		while((line = br.readLine()) != null) {
			String[] data = line.split(IUploadMBLDataManager.regex);
			Course from = null;
			CourseGroup to = null;
			if(data.length >= 5) {
				String fromNodeId = null;
				String toNodeId = null;
				int year = 0;
				//From node
				if(data[1] != null && data[4] != null && data[0] != null) {
					try {
						year = Integer.parseInt(data[0].trim().replaceAll("\"", ""));
					} catch (NumberFormatException e) {
						year = 0;
					}
					from = new Course(data[1].trim().replaceAll("\"", ""), data[4].trim().replaceAll("\"", ""), year);
					fromNodeId = createNode(from, instanceId);
				} else {
					success = false;
				}
				
				//To Node
				if(data[2] != null && data[3] != null) {
					to = new CourseGroup(data[2].trim().replaceAll("\"", ""), data[3].trim().replaceAll("\"", ""));
					toNodeId = createNode(to, instanceId);
				} else {
					success = false;
				}

				//Relationship
				if(fromNodeId != null && toNodeId != null) {
					Relation relation = new IsPartOf(toNodeId, year);
					createRelationship(relation, fromNodeId);
				}
			}
		}
		br.close();
		return success;
	}
}
