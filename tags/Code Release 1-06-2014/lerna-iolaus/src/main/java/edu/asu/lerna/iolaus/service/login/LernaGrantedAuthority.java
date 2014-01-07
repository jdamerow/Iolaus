package edu.asu.lerna.iolaus.service.login;

import org.springframework.security.core.GrantedAuthority;

/**
 * Customized GrantedAuthority class for roles of the user
 * @author Lohith Dwaraka 
 *
 */
public class LernaGrantedAuthority implements GrantedAuthority{

	private String roleName;
	private static final long serialVersionUID = 711167440813692597L;
	
	public LernaGrantedAuthority(String name){
		this.roleName=name;
	}
	
	@Override
	public String getAuthority() {
		return this.roleName;
	}

	public void setAuthority(String roleName) {
		this.roleName = roleName;
	}
	
}
