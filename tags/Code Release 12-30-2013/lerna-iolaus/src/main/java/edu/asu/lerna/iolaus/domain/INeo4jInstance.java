package edu.asu.lerna.iolaus.domain;

import java.util.Date;

import edu.asu.lerna.iolaus.domain.implementation.User;

public interface INeo4jInstance extends INeo4jConfFile {
	public abstract User getUser();
	public abstract Date getDate();
	public abstract void setUser(User user);
	public abstract void setDate(Date date);
}
