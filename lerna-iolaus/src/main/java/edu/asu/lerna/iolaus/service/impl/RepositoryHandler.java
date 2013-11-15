package edu.asu.lerna.iolaus.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.domain.json.IJsonNode;
import edu.asu.lerna.iolaus.domain.json.IJsonRelation;
import edu.asu.lerna.iolaus.domain.json.impl.JsonNode;
import edu.asu.lerna.iolaus.domain.json.impl.JsonRelation;
import edu.asu.lerna.iolaus.json.parser.library.JSONArray;
import edu.asu.lerna.iolaus.json.parser.library.JSONObject;
import edu.asu.lerna.iolaus.repository.NodeRepository;
import edu.asu.lerna.iolaus.service.IRepositoryHandler;

@Service
public class RepositoryHandler implements IRepositoryHandler {

	@Autowired
	private NodeRepository nodeRepository;



	private HashMap<String, List> getListOfNodesAndRelations_outdated(List<String> jsonObjectList)
	{
		List<IJsonNode> nodeList = null;
		List<IJsonRelation> relationList = null;
		IJsonNode node = null;
		IJsonRelation relation = null;
		HashMap<String, List> listOfNodesAndRelations = new HashMap<String, List>();



		for(String jsonObject: jsonObjectList)
		{
			if(jsonObject.contains("\"start\"") && jsonObject.contains("\"end\""))
			{
				//The object is a Relation
				if(relationList == null)
					relationList = new ArrayList<IJsonRelation>();
				relation = new JsonRelation();

				String[] entries = jsonObject.split("\n");
				for(int iRowCount=0;iRowCount<entries.length;iRowCount++)
				{
					String entry = entries[iRowCount];
					if(entry.contains("\"self\""))
					{
						String[] self = entry.split(" : ");
						if(self.length > 1)
						{							
							relation.setId(self[1].replace("\"","").replace(",","").trim());
						}
					}
					else if(entry.contains("\"start\""))
					{
						String[] values = entry.split(" : ");
						if(values.length > 1)
							relation.setStartNode(values[1].replace("\"", "").replace(",","").trim());
					}
					else if(entry.contains("\"end\""))
					{
						String[] values = entry.split(" : ");
						if(values.length > 1)
							relation.setEndNode(values[1].replace("\"", "").replace(",","").trim());
					}
					else if(entry.contains("\"type\""))
					{
						String[] values = entry.split(" : ");
						if(values.length > 1)
							relation.setType(values[1].replace("\"", "").replace(",","").trim());
					}
					else if(entry.contains("\"data\""))
					{
						while(true)
						{
							iRowCount++;
							entry = entries[iRowCount];		
							if(entry.contains("}"))
							{
								break;
							}
							else
							{
								String[] values = entry.split(" : ");
								if(values.length > 1)
									relation.addData(values[0].replace("\"","").trim(), values[1].replace("\"","").replace(",","").trim());
							}
						}
					}
				}
				relationList.add(relation);
			} // End of if - relation parsing
			else
			{
				//The object is a Node
				if(nodeList == null)
					nodeList = new ArrayList<IJsonNode>();
				node = new JsonNode();

				String[] entries = jsonObject.split("\n");
				for(int iRowCount=0;iRowCount<entries.length;iRowCount++)
				{
					String entry = entries[iRowCount];

					if(entry.contains("\"self\""))
					{
						String[] self = entry.split(" : ");
						if(self.length > 1)
						{							
							node.setId(self[1].replace("\"","").replace(",","").trim());
						}
					}
					else if(entry.contains("\"data\""))
					{
						while(true)
						{
							iRowCount++;
							entry = entries[iRowCount];						
							if(entry.contains("}"))
							{
								break;
							}
							else if(entry.contains("\"type\""))
							{
								String[] values = entry.split(" : ");
								if(values.length > 1)
									node.setType(values[1].replace("\"", "").replace(",","").trim());
							}
							else
							{
								String[] values = entry.split(" : ");
								if(values.length > 1)
									node.addData(values[0].replace("\"","").trim(), values[1].replace("\"","").replace(",","").trim());
							}
						}
					}
				} 

				nodeList.add(node);

			} //End of else - node parsing

		} //End of for - parsed all json objects

		listOfNodesAndRelations.put("nodesList", nodeList);
		listOfNodesAndRelations.put("relationList",relationList);
		return listOfNodesAndRelations;
	}


	@Override
	public HashMap<String, List> executeQuery(String json)
	{

		URL url;
		try {
			url = new URL(json);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			List<String> jsonObjectList = new ArrayList<String>();
			int openBracesCount = 0;
			StringBuilder oneSingleJsonObject = new StringBuilder();


			while ((output = br.readLine()) != null) {
				oneSingleJsonObject.append(output);
				oneSingleJsonObject.append("\n");
			}

			if(!oneSingleJsonObject.toString().contains("["))
			{
				oneSingleJsonObject = new StringBuilder("["+oneSingleJsonObject+"]");
				System.out.println(oneSingleJsonObject.toString());
			}


			JSONArray jsonArray = new JSONArray(oneSingleJsonObject.toString());
			return getListOfNodesAndRelations(jsonArray);


		} catch (Exception e){
			e.printStackTrace();

		}
		return null;
	}

	private HashMap<String, List> getListOfNodesAndRelations(JSONArray jsonArray)
	{
		List<IJsonNode> nodeList = null;
		List<IJsonRelation> relationList = null;
		IJsonNode node = null;
		IJsonRelation relation = null;
		HashMap<String, List> listOfNodesAndRelations = new HashMap<String, List>();


		System.out.println("Total number of json objects fetched: "+jsonArray.length());
		for(int i=0;i<jsonArray.length();i++)
		{
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			if(jsonObject.has("start") && jsonObject.has("end"))
			{
				//Found a relation object
				if(relationList == null)
					relationList = new ArrayList<IJsonRelation>();
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

				}
				relationList.add(relation);
			}
			else
			{
				//Found a node object
				if(nodeList == null)
					nodeList = new ArrayList<IJsonNode>();
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
				}
				nodeList.add(node);
			}
		}

		//		System.out.println("Number of relations: "+relationList.size());
		listOfNodesAndRelations.put("nodesList", nodeList);
		listOfNodesAndRelations.put("relationList",relationList);
		return listOfNodesAndRelations;

	}
}
