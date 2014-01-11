package edu.asu.lerna.iolaus.service.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import edu.asu.lerna.iolaus.domain.json.IJsonNode;
import edu.asu.lerna.iolaus.domain.json.IJsonRelation;
import edu.asu.lerna.iolaus.domain.json.impl.JsonNode;
import edu.asu.lerna.iolaus.domain.json.impl.JsonRelation;
import edu.asu.lerna.iolaus.json.parser.library.JSONArray;
import edu.asu.lerna.iolaus.json.parser.library.JSONObject;
import edu.asu.lerna.iolaus.service.IRepositoryHandler;

/**
 * This makes a Rest call to a neo4j instance inorder to execute a given json string.
 * The json output from neo4j are converted to generalized node and realtion classes.
 * 
 * @author Ram Kumar Kumaresan
 */
@Service
public class RepositoryHandler implements IRepositoryHandler {

	private static final Logger logger = LoggerFactory
			.getLogger(RepositoryHandler.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<List<Object>> executeQuery(String jsonTraverserPayload, String neo4jInstance)
	{
		if(jsonTraverserPayload == null || jsonTraverserPayload.equals("") || neo4jInstance == null || neo4jInstance.equals(""))
			return null;
			
		
		URI traverserUri = null;

		try {
			traverserUri = new URI( neo4jInstance );
		} catch (URISyntaxException e) {
			logger.debug("The input json: "+jsonTraverserPayload);
			logger.debug("Neo4j instance value: "+neo4jInstance);
			logger.debug("Exception occurrent in the REST connection to the neo4j instance", e);
			return null;
		}
		WebResource resource = Client.create().resource( traverserUri );
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.entity(jsonTraverserPayload)
				.post(ClientResponse.class);


		String oneSingleJsonString = response.getEntity(String.class);
		response.close();
		System.out.println(oneSingleJsonString);
		return getListOfNodesAndRelations(oneSingleJsonString);
	}

	/**
	 * Produce a generalized list of nodes and relations by parsing xml string using json libraries.
	 * 
	 * @param oneSingleJsonString		The xml string(response) from neo4j instance.
	 * @return							List of generalized node and relation class objects.
	 */
	private List<List<Object>> getListOfNodesAndRelations(String oneSingleJsonString)
	{
		List<List<Object>> resultList = null;
		IJsonNode node = null;
		IJsonRelation relation = null;

		JSONObject jsonResponseObject = new JSONObject(oneSingleJsonString);
		JSONArray dataArray = jsonResponseObject.optJSONArray("data");

		if(dataArray != null)
		{
			resultList = new ArrayList<List<Object>>();
			for(int i=0;i<dataArray.length();i++)
			{
				List<Object> rowList = new ArrayList<Object>();
				resultList.add(rowList);

				JSONArray rowObjects = dataArray.getJSONArray((i));
				for(int j=0;j<rowObjects.length();j++)
				{
					JSONObject jsonObject = rowObjects.getJSONObject(j);
					if(jsonObject.has("start") && jsonObject.has("end"))
					{
						//Found a relation object
						relation = new JsonRelation();

						Iterator<String> keysIterator = jsonObject.keys();
						while(keysIterator.hasNext())
						{
							String key = keysIterator.next();
							if(key.equals("data"))
							{
								JSONObject objectData = jsonObject.getJSONObject(key);
								Iterator<String> dataIterator = objectData.keys();
								while(dataIterator.hasNext())
								{
									String dataKey = dataIterator.next(); 							
									relation.addData(dataKey, objectData.optString(dataKey));							
								}
							}
							else if(key.equals("start"))
							{
								relation.setStartNode(jsonObject.getString(key));
							}
							else if(key.equals("end"))
							{
								relation.setEndNode(jsonObject.getString(key));
							}
							else if(key.equals("type"))
							{
								relation.setType(jsonObject.getString(key));
							}
							else if(key.equals("self"))
							{
								relation.setId(jsonObject.getString(key));
							}
						} //End of parsing relation object
						rowList.add(relation);
					}
					else
					{
						//Found a node object
						node = new JsonNode();

						Iterator<String> keysIterator = jsonObject.keys();
						while(keysIterator.hasNext())
						{
							String key = keysIterator.next();
							if(key.equals("data"))
							{
								JSONObject objectData = jsonObject.getJSONObject(key);
								Iterator<String> dataIterator = objectData.keys();
								while(dataIterator.hasNext())
								{
									String dataKey = dataIterator.next();
									if(dataKey.equals("type"))
										node.setType(objectData.optString(dataKey));
									else
										node.addData(dataKey, objectData.optString(dataKey));							
								}
							}
							else if(key.equals("self"))
							{
								node.setId(jsonObject.getString(key));
							}
						} //End of parsing node object
						rowList.add(node);
					} //End of else for node and relation parsing
				} //End of for: row data
			} //End of for: complete json
		} // End of if: null check

		return resultList;
	}

}
