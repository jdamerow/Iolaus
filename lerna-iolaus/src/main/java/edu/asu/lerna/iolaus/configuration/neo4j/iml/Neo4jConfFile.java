package edu.asu.lerna.iolaus.configuration.neo4j.iml;

import org.springframework.stereotype.Service;

/**
 * @author Veena Borannagowda
 * 
 * Attributes of the  configuration files of Neo4j   
 *
 */

@Service
public class Neo4jConfFile {

	private String id;
	private String description;
	private String host;
	private String path;
	
	public void setId(String title)
	{
		this.id = title;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
	
	public void setHost(String host)
	{
		this.host = host;
	}
	
	public void setPath(String path)
	{
		this.path = path;
	}
	
	public String getDescription()
	{
		return description;
	}
	public String getHost()
	{
		return host;
	}
	public String getPath()
	{
		return path;
	}
	public String getId()
	{
		return id;
	}
}
