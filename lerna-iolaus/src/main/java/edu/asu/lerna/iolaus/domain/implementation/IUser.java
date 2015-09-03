package edu.asu.lerna.iolaus.domain.implementation;

import java.util.List;

import edu.asu.lerna.iolaus.service.login.LernaGrantedAuthority;

public interface IUser {

	public abstract String getUsername();

	public abstract void setUsername(String username);

	public abstract String getName();

	public abstract void setName(String name);

	public abstract String getEmail();

	public abstract void setEmail(String email);

	public abstract String getPassword();

	public abstract void setPassword(String password);

	public abstract List<LernaGrantedAuthority> getAuthorities();

	public abstract void setAuthorities(List<LernaGrantedAuthority> authorities);

}