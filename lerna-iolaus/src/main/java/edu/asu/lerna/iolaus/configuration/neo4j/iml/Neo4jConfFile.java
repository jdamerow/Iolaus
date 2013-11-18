package edu.asu.lerna.iolaus.configuration.neo4j.iml;

public class Neo4jConfFile {

	private String title;
	private String description;
	private String host;
	private String path;
	
	public void setTitle(String title)
	{
		this.title = title;
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
	public String getTitle()
	{
		return title;
	}
}
