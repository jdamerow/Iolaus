package edu.asu.lerna.iolaus.domain;

public interface IUser {

	public abstract String getFirstName();

	public abstract void setFirstName(String firstName);

	public abstract String getLastName();

	public abstract void setLastName(String lastName);

	public abstract String getUserId();

	public abstract void setUserId(String userId);

	public abstract String getPassword();

	public abstract void setPassword(String password);

	public abstract String getEmail();

	public abstract void setEmail(String email);

	public abstract String getRole();

	public abstract void setRole(String role);

}