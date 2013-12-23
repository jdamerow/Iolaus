package edu.asu.lerna.iolaus.domain.implementation;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import edu.asu.lerna.iolaus.service.login.LernaGrantedAuthority;


/**
 * @description : User class describing the properties 
 *                of a User object
 * 
 * @author      : Lohith Dwaraka
 */
public class User implements UserDetails {

	private String name;
	private String email;
	private String password;
	private List<LernaGrantedAuthority> authorities;
	
	private static final long serialVersionUID = 2581926783303008251L;
	
	private String username;
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
	public List<LernaGrantedAuthority> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(List<LernaGrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	@Override
	public boolean isEnabled() {
		return true;
	}
		
}
