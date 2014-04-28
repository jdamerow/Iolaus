package edu.asu.lerna.iolaus.configuration.neo4j.impl;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.domain.INeo4jInstance;

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
	
	private List<INeo4jInstance> fileList;
	
	@PostConstruct void init() throws IOException, ParseException
	{
		fileList = fileReader.getNeo4jConfFiles(); 
	}
	
	public List<INeo4jInstance> getfileList()
	{
		return this.fileList;
	}
	
	public String getNodeIndexName(String id){
		
		String indexName = null;
		
		for(INeo4jInstance instance : fileList){
			
			if(instance.getId().equals(id)){
				
				indexName = instance.getNodeIndex();
				break;
				
			}
			
		}
		
		return indexName;
		
	}
}
