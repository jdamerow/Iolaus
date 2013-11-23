package edu.asu.lerna.iolaus.configuration.neo4j.iml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Neo4jRegistry {

	@Autowired 
	Neo4jConfFileReader fileReader;
	
	ArrayList<Neo4jConfFile> fileList;
	
	@PostConstruct void init() throws IOException
	{
		fileList = fileReader.getNeo4jConfFiles();
	}
	
	public ArrayList<Neo4jConfFile> getfileList()
	{
		return this.fileList;
	}
	
	void print (ArrayList<Neo4jConfFile> filelist)
	{
		Iterator<Neo4jConfFile> it = filelist.iterator();
		while(it.hasNext())
		{
			Neo4jConfFile fileelement = it.next();
			System.out.println("File details:" +fileelement.getDescription()+ "\n" + fileelement.getTitle());
		}
	}
}
