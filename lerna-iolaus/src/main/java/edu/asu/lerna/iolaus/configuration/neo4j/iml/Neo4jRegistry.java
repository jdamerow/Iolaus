package edu.asu.lerna.iolaus.configuration.neo4j.iml;

import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

public class Neo4jRegistry {

	@Autowired Neo4jConfFileReader fileReader;
	ArrayList<Neo4jConfFile> fileList;
	
	@PostConstruct void init() throws IOException
	{
		fileList = fileReader.getNeo4jConfFiles();
	}
	
}
