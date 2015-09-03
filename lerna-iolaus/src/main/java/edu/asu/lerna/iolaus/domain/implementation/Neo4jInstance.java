package edu.asu.lerna.iolaus.domain.implementation;

import edu.asu.lerna.iolaus.domain.INeo4jInstance;

public class Neo4jInstance implements INeo4jInstance{
	
	private String id;
	private String description;
	private String host;
	private String port;
	private boolean active;
	private String userName;
	private String nodeIndex;
	private String relationIndex;
	private String dbPath;
	private String protocol;
	private String password;
	
	@Override
	public String getDbPath() {
		return dbPath;
	}

	@Override
	public void setDbPath(String dbPath) {
		this.dbPath = dbPath;
	}

	@Override
	public boolean isActive() {
		return active;
	}
	
	@Override
	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getHost() {
		return host;
	}

	@Override
	public void setId(String id) {
		this.id=id;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(String description) {
		this.description=description;
	}

	@Override
	public String getPort() {
		return port;
	}

	@Override
	public void setPort(String port) {
		this.port=port;
	}

	@Override
	public void setHost(String host) {
		this.host=host;
	}
	@Override
	public String getUserName() {
		return userName;
	}
	@Override
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Override
	public String getNodeIndex() {
		return nodeIndex;
	}
	@Override
	public void setNodeIndex(String indexName) {
		this.nodeIndex = indexName;
	}
	@Override
	public String getRelationIndex() {
		return relationIndex;
	}
	@Override
	public void setRelationIndex(String relationIndex) {
		this.relationIndex = relationIndex;
	}
	
	@Override
	public String getProtocol() {
		return protocol;
	}

	@Override
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String getRootPath() {
		StringBuffer sb = new StringBuffer();
		sb.append(getProtocol());
		sb.append("://");
		sb.append(getHost());
		sb.append(":");
		sb.append(getPort());
		sb.append("/");
		sb.append(getDbPath());
		sb.append("/");
		return sb.toString();
	}
}
