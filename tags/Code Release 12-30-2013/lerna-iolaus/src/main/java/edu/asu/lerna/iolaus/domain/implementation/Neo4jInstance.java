package edu.asu.lerna.iolaus.domain.implementation;

import java.util.Date;

import edu.asu.lerna.iolaus.domain.INeo4jInstance;

public class Neo4jInstance extends Neo4jConfFile implements INeo4jInstance{
	private Date date;
	private User user;
	
	@Override
	public User getUser() {
		return user;
	}
	@Override
	public void setUser(User user) {
		this.user = user;
	}
	@Override
	public Date getDate() {
		return date;
	}
	@Override
	public void setDate(Date date) {
		this.date = date;
	}
}
