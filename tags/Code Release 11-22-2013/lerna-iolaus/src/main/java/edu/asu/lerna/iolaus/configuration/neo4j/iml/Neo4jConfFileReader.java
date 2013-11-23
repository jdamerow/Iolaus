package edu.asu.lerna.iolaus.configuration.neo4j.iml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

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
			Properties properties = new Properties();
			try {
				properties.load(new FileInputStream(fileEntry));
			} catch (IOException e) {

			}
			for(String key : properties.stringPropertyNames()) {
				String value = properties.getProperty(key);
				if(key.equals("title"))
					confFile.setTitle(value);
				if(key.equals("host"))
					confFile.setHost(value);
				if(key.equals("description"))
					confFile.setDescription(value);
				if(key.equals("path"))
					confFile.setPath(value);

			}

		listOfFiles.add(confFile);
	}
	return listOfFiles;
}
}



