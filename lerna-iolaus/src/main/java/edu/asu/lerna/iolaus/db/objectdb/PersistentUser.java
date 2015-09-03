package edu.asu.lerna.iolaus.db.objectdb;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class PersistentUser {

	private String name;
	@Id
	private String username;
	private String email;
	private String password;
	private List<String> authorities;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<String> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(List<String> authorities) {
		this.authorities = authorities;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

}
