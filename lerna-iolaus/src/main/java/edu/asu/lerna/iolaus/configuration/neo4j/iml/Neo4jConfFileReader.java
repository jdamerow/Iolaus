package edu.asu.lerna.iolaus.configuration.neo4j.iml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 * @author Veena Borannagowda
 * 
 * Reads the configuration files for neo4j from the local system   
 *
 */

@PropertySource(value="/lerna.properties")
@Service
public class Neo4jConfFileReader {
	
	@Autowired	
	private Environment env;

	public ArrayList<Neo4jConfFile> getNeo4jConfFiles() throws IOException
	{
		ArrayList<Neo4jConfFile> listOfFiles = new ArrayList<Neo4jConfFile>();
		String classPath=Neo4jConfFileReader.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		File folder = new File(classPath.substring(0,classPath.indexOf("classes"))+"classes/"+env.getProperty("LOCAL_PATH_FOR_NEO4JCONFIGURATION"));
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
				if(key.equals("id"))
					confFile.setId(value);
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



