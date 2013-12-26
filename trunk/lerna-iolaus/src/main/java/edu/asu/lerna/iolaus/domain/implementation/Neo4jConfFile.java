package edu.asu.lerna.iolaus.domain.implementation;

import edu.asu.lerna.iolaus.domain.INeo4jConfFile;

public class Neo4jConfFile implements INeo4jConfFile {
	
	private String id;
	private String description;
	private String host;
	private String port;
	private boolean active;
	
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
}
