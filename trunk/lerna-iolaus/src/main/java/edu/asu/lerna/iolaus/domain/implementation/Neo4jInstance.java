package edu.asu.lerna.iolaus.domain.implementation;

import edu.asu.lerna.iolaus.domain.INeo4jInstance;

public class Neo4jInstance implements INeo4jInstance{
	
	private String id;
	private String description;
	private String host;
	private String port;
	private boolean active;
	private String userName;
	private String dbPath;
	
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
}
