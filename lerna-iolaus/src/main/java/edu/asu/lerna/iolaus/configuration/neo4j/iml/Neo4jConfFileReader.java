package edu.asu.lerna.iolaus.configuration.neo4j.iml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.domain.INeo4jConfFile;
import edu.asu.lerna.iolaus.domain.implementation.Neo4jConfFile;

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

	public List<INeo4jConfFile> getNeo4jConfFiles() throws IOException
	{
		List<INeo4jConfFile> listOfFiles = new ArrayList<INeo4jConfFile>();
		String classPath=URLDecoder.decode(Neo4jConfFileReader.class.getProtectionDomain().getCodeSource().getLocation().getPath(),"UTF-8");
		
		File folder = new File(classPath.substring(0,classPath.indexOf("classes"))+"classes/"+env.getProperty("LOCAL_PATH_FOR_NEO4JCONFIGURATION"));
		for(final File fileEntry : folder.listFiles())
		{
			INeo4jConfFile confFile = new Neo4jConfFile();
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
					confFile.setPort(value);

			}
			listOfFiles.add(confFile);
		}
		return listOfFiles;
	}
}



