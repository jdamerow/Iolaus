package edu.asu.lerna.iolaus.domain;

public interface INeo4jInstance {
	String getUserName();
	void setUserName(String userName);
	boolean isActive();
	void setActive(boolean active);
	String getId();
	String getHost();
	String getDescription();
	String getPort();
	void setPort(String path);
	void setHost(String host);
	void setDescription(String description);
	void setId(String id);
	void setDbPath(String dbPath);
	String getDbPath();
	String getNodeIndex();
	void setNodeIndex(String indexName);
	String getRelationIndex();
	void setRelationIndex(String indexName);
}
