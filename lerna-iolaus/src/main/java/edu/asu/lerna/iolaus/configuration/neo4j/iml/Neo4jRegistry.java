package edu.asu.lerna.iolaus.configuration.neo4j.iml;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
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
		print(fileList);
	}
	
	public List<INeo4jConfFile> getfileList()
	{
		return this.fileList;
	}
	
	void print (List<INeo4jConfFile> filelist)
	{
		Iterator<INeo4jConfFile> it = filelist.iterator();
		while(it.hasNext())
		{
			INeo4jConfFile fileelement = it.next();
			System.out.println("File details:" +fileelement.getDescription()+ "\n" + fileelement.getId());
		}
	}
}
