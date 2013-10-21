package edu.asu.lerna.iolaus.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import net.ricecode.similarity.JaroWinklerStrategy;
import net.ricecode.similarity.SimilarityStrategy;
import net.ricecode.similarity.StringSimilarityService;
import net.ricecode.similarity.StringSimilarityServiceImpl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.asu.lerna.iolaus.rest.AddNodesRestController;

public class namesResolution {

	/**
	 * @param args
	 */

	private static final Logger logger = LoggerFactory
			.getLogger(AddNodesRestController.class);

	public void parseFile(String filename) throws IOException
	{
		File file = new File(filename);
		BufferedReader b = null;
		String line;


		int distance;
		Boolean firstEntry = true;
		Boolean found = false;

		HashMap<String, ArrayList<String>> nameMap = new HashMap<>();

		try
		{
			b = new BufferedReader (new FileReader(file));
		}catch (IOException e) {
			logger.error("IO Error, File not uploaded",e);
		}

		while((line=b.readLine())!=null)
		{
			String data [] = line.split(",");
			String firstName=data[0];
			String lastName=data[1];

			StringBuilder name = new StringBuilder(firstName);
			name.append(' ');
			name.append(lastName);

			found = false;
			if(firstEntry == true)
			{
				ArrayList<String> nameList = new ArrayList<>();
				nameList.add(name.toString());
				nameMap.put(name.toString(), nameList);
				firstEntry = false;
			}

			Iterator<String> iterator = nameMap.keySet().iterator();	
			while(iterator.hasNext() && !firstEntry)
			{
				String stringInMap = iterator.next();
				SimilarityStrategy strategy = new JaroWinklerStrategy();
				StringSimilarityService service = new StringSimilarityServiceImpl(strategy);
				double score = service.score(stringInMap, name.toString());
				if(score >= 0.9 && score <1.0)
				{
					nameMap.get(stringInMap).add(name.toString());
					found = true;
					break;
				}

			}
			if(found == false)
			{
				ArrayList<String> nameList = new ArrayList<>();
				nameList.add(name.toString());
				nameMap.put(name.toString(), nameList);
			}
		}

		printHashMap(nameMap);

	}

	//	public void singleLine(String line)
	//	{
	//		String data [] = line.split(",");
	//		String firstName=data[0];
	//		String lastName=data[1];
	//		StringBuilder name = new StringBuilder(firstName);
	//		name.append(' ');
	//		name.append(lastName);
	//		int distance;
	//		Boolean firstEntry = true;
	//		
	//		ArrayList<String> nameList = new ArrayList<>();
	//		HashMap<String, ArrayList<String>> nameMap = new HashMap<>();
	//		Iterator<String> iterator = nameMap.keySet().iterator();
	//		
	//	    if(firstEntry == true)
	//	    {
	//	    	nameList.add(name.toString());
	//			nameMap.put(name.toString(), nameList);
	//			firstEntry = false;
	//	    }
	//		
	//		while(iterator.hasNext() && !firstEntry)
	//		{
	//			String stringInMap = iterator.next();
	//			distance = StringUtils.getLevenshteinDistance(stringInMap, name.toString());
	//			if(distance <= 2)
	//			{
	//				nameMap.get(stringInMap).add(name.toString());
	//			}
	//			else 
	//			{
	//				nameList.add(name.toString());
	//				nameMap.put(name.toString(), nameList);
	//			}
	//		}
	//	
	//		
	//		
	//	}

	public void printHashMap(HashMap<String, ArrayList<String>> hashMap)
	{
		for (String key : hashMap.keySet())
		{
			ArrayList<String> nameList = hashMap.get(key);
			if(nameList.size() > 1)
			{
				System.out.println("Similar names to: "+ key);
				Iterator<String> it = nameList.iterator();
				while(it.hasNext())
				{
					System.out.println(it.next());
				}
			}

		}
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

			
		namesResolution object = new namesResolution();
		object.parseFile("names.csv");

	}

}
