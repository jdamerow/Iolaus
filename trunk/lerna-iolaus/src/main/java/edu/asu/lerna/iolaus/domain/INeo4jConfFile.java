package edu.asu.lerna.iolaus.domain;

public interface INeo4jConfFile {
	
	public abstract String getId();
	public abstract String getHost();
	public abstract String getDescription();
	public abstract String getIp();
	public abstract String getPort();
	public abstract void setPort(String path);
	public abstract void setHost(String host);
	public abstract void setIp(String ip);
	public abstract void setDescription(String description);
	public abstract void setId(String id);
	
}
