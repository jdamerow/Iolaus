package edu.asu.lerna.iolaus.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.domain.json.IJsonNode;
import edu.asu.lerna.iolaus.domain.json.IJsonRelation;
import edu.asu.lerna.iolaus.domain.json.impl.JsonNode;
import edu.asu.lerna.iolaus.domain.json.impl.JsonRelation;
import edu.asu.lerna.iolaus.json.parser.library.JSONArray;
import edu.asu.lerna.iolaus.repository.NodeRepository;
import edu.asu.lerna.iolaus.service.IRepositoryHandler;

@Service
public class RepositoryHandler implements IRepositoryHandler {

	@Autowired
	private NodeRepository nodeRepository;


	private HashMap<String, List> getListOfNodesAndRelations(List<String> jsonObjectList)
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
			JSONArray jsonArray = new JSONArray(oneSingleJsonObject.toString());
			System.out.println(jsonArray.length());

		} catch (Exception e){

		}
		return null;
	}
}
