package edu.asu.lerna.iolaus.configuration.neo4j.iml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.springframework.stereotype.Service;

@Service
public class Neo4jConfFileReader {

	public ArrayList<Neo4jConfFile> getNeo4jConfFiles() throws IOException
	{
		ArrayList<Neo4jConfFile> listOfFiles = new ArrayList<>();
		File folder = new File("G:\\ASU\\DIGING\\Neo4j_Configuration");

		for(final File fileEntry : folder.listFiles())
		{
			Neo4jConfFile confFile = new Neo4jConfFile();
			BufferedReader br = new BufferedReader(new FileReader(fileEntry));
			try{
				String line = br.readLine();
				while(line != null)
				{
					int loc = line.indexOf('=');
					String key = line.substring(0, loc);
					String value = line.substring(loc+1);
					if(key.equals("title"))
						confFile.setTitle(value);
					if(key.equals("host"))
						confFile.setHost(value);
					if(key.equals("description"))
						confFile.setDescription(value);
					if(key.equals("path"))
						confFile.setPath(value);
										
				}
			}
			finally{
				br.close();
			}
			listOfFiles.add(confFile);
		}
		return listOfFiles;
	}
}



