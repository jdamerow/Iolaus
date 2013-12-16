package edu.asu.lerna.iolaus.configuration.neo4j.iml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	private List<Neo4jConfFile> fileList;
	
	@PostConstruct void init() throws IOException
	{
		fileList = fileReader.getNeo4jConfFiles();
		print(fileList);
	}
	
	public List<Neo4jConfFile> getfileList()
	{
		return Collections.unmodifiableList(this.fileList);
	}
	
	void print (List<Neo4jConfFile> filelist)
	{
		Iterator<Neo4jConfFile> it = filelist.iterator();
		while(it.hasNext())
		{
			Neo4jConfFile fileelement = it.next();
			System.out.println("File details:" +fileelement.getDescription()+ "\n" + fileelement.getId());
		}
	}
}
