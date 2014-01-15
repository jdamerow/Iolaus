package edu.asu.lerna.iolaus.service.login;

import org.springframework.security.core.GrantedAuthority;

import edu.asu.lerna.iolaus.domain.implementation.Role;
import edu.asu.lerna.iolaus.domain.implementation.User;

/**
 * Customized {@link GrantedAuthority} class for {@link Role} of the {@link User}
 * 
 * @author : Lohith Dwaraka 
 *
 */
public class LernaGrantedAuthority implements GrantedAuthority{

	private String roleName;
	private static final long serialVersionUID = 711167440813692597L;
	
	/**
	 * Sets the rolename for this object
	 * @param name
	 */
	public LernaGrantedAuthority(String name){
		this.roleName=name;
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public String getAuthority() {
		return this.roleName;
	}

	/**
	 * Setter for the {@link Role}
	 * @param roleName
	 */
	public void setAuthority(String roleName) {
		this.roleName = roleName;
	}
	
}
