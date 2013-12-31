package edu.asu.lerna.iolaus.service.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.MediaType;

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

@Service
public class RepositoryHandler implements IRepositoryHandler {


	@Override
	public List<List<Object>> executeQuery(String jsonTraverserPayload, String neo4jInstance)
	{
		URI traverserUri = null;
 
		try {
			traverserUri = new URI( neo4jInstance );
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		WebResource resource = Client.create().resource( traverserUri );
		ClientResponse response = resource.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.entity(jsonTraverserPayload)
				.post(ClientResponse.class);

				
		String oneSingleJsonString = response.getEntity(String.class);
		response.close();
		return getListOfNodesAndRelations(oneSingleJsonString);
	}

	private List<List<Object>> getListOfNodesAndRelations(String oneSingleJsonString)
	{
		List<List<Object>> resultList = new ArrayList<List<Object>>();
		IJsonNode node = null;
		IJsonRelation relation = null;
		
		JSONObject jsonResponseObject = new JSONObject(oneSingleJsonString);
		JSONArray dataArray = jsonResponseObject.optJSONArray("data");
		
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
		
		return resultList;
	}
	
}
