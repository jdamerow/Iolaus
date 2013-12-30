package edu.asu.lerna.iolaus.configuration.neo4j.iml;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.domain.INeo4jConfFile;

/**
 * @author Veena Borannagowda
 * 
 * Returns the configuration details of all the registered neo4j instances   
 *
 */

@Service
public class Neo4jRegistry {

	@Autowired 
	private Neo4jConfFileReader fileReader;
	
	private List<INeo4jConfFile> fileList;
	
	@PostConstruct void init() throws IOException
	{
		fileList = fileReader.getNeo4jConfFiles(); 
	}
	
	public List<INeo4jConfFile> getfileList()
	{
		return this.fileList;
	}
}
