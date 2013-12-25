package edu.asu.lerna.iolaus.web.usermanagement.backing;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import edu.asu.lerna.iolaus.domain.implementation.Role;

public class UserBackingBean {

	@NotEmpty(message = "Please provide a username.")
	private String username;
	
	@NotEmpty(message = "Please provide name of user.")
	private String name;
	
	@NotEmpty(message = "Please enter a password.")
	private String password;
	
	@NotEmpty(message = "Please provide an email address.")
	private String email;
	
	@NotEmpty(message = "At least one role needs to be selected.")
	private List<String> roles;
//	private Role[] roles;
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
//	public Role[] getRoles() {
//		return roles;
//	}
//	public void setRoles(Role[] roles) {
//		this.roles = roles;
//	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
