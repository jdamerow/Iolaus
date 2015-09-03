package edu.asu.lerna.iolaus.domain.implementation;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import edu.asu.lerna.iolaus.service.login.LernaGrantedAuthority;


/**
 * User class describing the properties 
 *                of a User object
 * 
 * @author      : Lohith Dwaraka
 */
public class User implements UserDetails, IUser {

	private String name;
	private String email;
	private String password;
	private List<LernaGrantedAuthority> authorities;
	
	private static final long serialVersionUID = 2581926783303008251L;
	
	private String username;
	/* (non-Javadoc)
	 * @see edu.asu.lerna.iolaus.domain.implementation.IUser#getUsername()
	 */
	@Override
	public String getUsername() {
		return username;
	}
	/* (non-Javadoc)
	 * @see edu.asu.lerna.iolaus.domain.implementation.IUser#setUsername(java.lang.String)
	 */
	@Override
	public void setUsername(String username) {
		this.username = username;
	}
	/* (non-Javadoc)
	 * @see edu.asu.lerna.iolaus.domain.implementation.IUser#getName()
	 */
	@Override
	public String getName() {
		return name;
	}
	/* (non-Javadoc)
	 * @see edu.asu.lerna.iolaus.domain.implementation.IUser#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}
	/* (non-Javadoc)
	 * @see edu.asu.lerna.iolaus.domain.implementation.IUser#getEmail()
	 */
	@Override
	public String getEmail() {
		return email;
	}
	/* (non-Javadoc)
	 * @see edu.asu.lerna.iolaus.domain.implementation.IUser#setEmail(java.lang.String)
	 */
	@Override
	public void setEmail(String email) {
		this.email = email;
	}
	/* (non-Javadoc)
	 * @see edu.asu.lerna.iolaus.domain.implementation.IUser#getPassword()
	 */
	@Override
	public String getPassword() {
		return password;
	}
	/* (non-Javadoc)
	 * @see edu.asu.lerna.iolaus.domain.implementation.IUser#addAuthority(edu.asu.lerna.iolaus.service.login.LernaGrantedAuthority)
	 */
	public void addAuthority(LernaGrantedAuthority auth) {
		this.authorities.add(auth);
	}
	/* (non-Javadoc)
	 * @see edu.asu.lerna.iolaus.domain.implementation.IUser#setPassword(java.lang.String)
	 */
	@Override
	public void setPassword(String password) {
		this.password = password;
	}
	/* (non-Javadoc)
	 * @see edu.asu.lerna.iolaus.domain.implementation.IUser#getAuthorities()
	 */
	@Override
	public List<LernaGrantedAuthority> getAuthorities() {
		return authorities;
	}
	/* (non-Javadoc)
	 * @see edu.asu.lerna.iolaus.domain.implementation.IUser#setAuthorities(java.util.List)
	 */
	@Override
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
