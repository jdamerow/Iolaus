package edu.asu.lerna.iolaus.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.domain.json.IJsonNode;
import edu.asu.lerna.iolaus.domain.json.IJsonRelation;
import edu.asu.lerna.iolaus.domain.json.impl.JsonNode;
import edu.asu.lerna.iolaus.domain.json.impl.JsonRelation;
import edu.asu.lerna.iolaus.repository.NodeRepository;
import edu.asu.lerna.iolaus.service.IRepositoryHandler;

@Service
public class RepositoryHandler implements IRepositoryHandler {

	@Autowired
	private NodeRepository nodeRepository;


	private void getListOfNodesAndRelations(List<String> jsonObjectList)
	{
		List<IJsonNode> nodeList = null;
		List<IJsonRelation> relationList = null;
		IJsonNode node = null;
		IJsonRelation relation = null;

		for(String jsonObject: jsonObjectList)
		{
			if(jsonObject.contains("\"start\"") && jsonObject.contains("\"end\""))
			{
				//The object is a Relation
				if(relationList == null)
					relationList = new ArrayList<IJsonRelation>();
				relation = new JsonRelation();



			}
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
		
		System.out.println("-------------------------------------------");
		for(IJsonNode jsonNode: nodeList)
		{
			System.out.println(jsonNode.getId());
			System.out.println(jsonNode.getType());
			System.out.println(jsonNode.getData().size());
		}
		System.out.println("-------------------------------------------");
	}


	@Override
	public void executeQuery(String json)
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

				//Found start of a object
				if(output.contains("{") && !output.contains("}"))
				{					
					openBracesCount ++;
					if(output.contains("["))
					{
						output = output.replace("[ ", "");					
					}

					//Add the line to the object
					oneSingleJsonObject.append(output);
					oneSingleJsonObject.append("\n");
				}
				//Found end of object
				else if(output.contains("}") && (output.contains("}, {") || !output.contains("{")))
				{
					openBracesCount --;
					if(output.contains("]"))
					{
						output = output.replace("]", "");					
					}
					else if(output.contains("}, {"))
					{
						oneSingleJsonObject.append("}");
						jsonObjectList.add(oneSingleJsonObject.toString());
						oneSingleJsonObject.append("\n");

						//Start new object
						oneSingleJsonObject = new StringBuilder();
						openBracesCount = 1;
						output = "{";						
					}

					//Add the line to the object
					oneSingleJsonObject.append(output);
					oneSingleJsonObject.append("\n");
				}
				else
				{
					//Add the line to the object
					oneSingleJsonObject.append(output);
					oneSingleJsonObject.append("\n");
				}



				//End of the json object. Add to the list and reset the string buffer
				if(openBracesCount == 0)
				{
					jsonObjectList.add(oneSingleJsonObject.toString());
					oneSingleJsonObject = new StringBuilder();
				}				

			}
			conn.disconnect();

			System.out.println("Number of json objects fetched from database: "+jsonObjectList.size());
			for(String jsonObject: jsonObjectList)
			{
				System.out.println(jsonObject);
			}

			getListOfNodesAndRelations(jsonObjectList);

		} catch (Exception e){

		}

































		//		String nodeEntryPointUri = "http://localhost:7474/db/data/node";
		//		WebResource resource = Client.create().resource( json );
		//		ClientResponse response = resource.accept( MediaType.APPLICATION_JSON )
		//		        .type( MediaType.APPLICATION_JSON )
		//		        .entity( "{}" )
		//		        .post( ClientResponse.class );
		//		 
		//		final URI location = response.getLocation();
		//		System.out.println("POST to: "+json);
		//		System.out.println("Status code: "+response.getStatus());
		//		System.out.println("Location header: "+location.toString());
		//		response.close();
		//		

		//insert a node using rest api
		//		String nodeEntryPointUri = "http://localhost:7474/db/data/node";
		//		WebResource resource = Client.create().resource( nodeEntryPointUri );
		//		ClientResponse response = resource.accept( MediaType.APPLICATION_JSON )
		//		        .type( MediaType.APPLICATION_JSON )
		//		        .entity( "{}" )
		//		        .post( ClientResponse.class );
		//		 
		//		final URI location = response.getLocation();
		//		System.out.println( String.format(
		//		        "POST to [%s], status code [%d], location header [%s]",
		//		        nodeEntryPointUri, response.getStatus(), location.toString() ) );
		//		response.close();


		//Querying nodes using spring template
		//		List<Node> nodeList = new ArrayList<Node>();
		//		EndResult<Node> nodeResult = nodeRepository.query(json,null);
		//		Iterator<Node> iterator = nodeResult.iterator();
		//
		//		while (iterator.hasNext()) {
		//			nodeList.add(iterator.next());
		//		}
		//		
		//		System.out.println("Size of nodes fetched from database: "+nodeList.size());
		//		for(Node node: nodeList)
		//		{
		//			System.out.println("_________________________________________");
		//			System.out.println(node.getType());
		//			System.out.println(node.getDataset());
		//			System.out.println(node.getLabel());
		//		}
	}
}
