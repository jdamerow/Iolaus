package edu.asu.lerna.iolaus.domain;

public interface INeo4jInstance {
	public abstract String getUserName();
	public abstract void setUserName(String userName);
	public abstract boolean isActive();
	public abstract void setActive(boolean active);
	public abstract String getId();
	public abstract String getHost();
	public abstract String getDescription();
	public abstract String getPort();
	public abstract void setPort(String path);
	public abstract void setHost(String host);
	public abstract void setDescription(String description);
	public abstract void setId(String id);
	public abstract void setDbPath(String dbPath);
	public abstract String getDbPath();
}
