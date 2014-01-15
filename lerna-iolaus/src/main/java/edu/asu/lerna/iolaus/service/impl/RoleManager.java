package edu.asu.lerna.iolaus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.domain.implementation.Role;
import edu.asu.lerna.iolaus.service.IRoleManager;

/**
 * Implemented by IRolemanager methods. 
 * Class works on getting All {@link Role} in the DB, {@link Role} by role id
 * 
 * @author : Lohith Dwaraka
 *
 */
@Service
public class RoleManager implements IRoleManager{

	@Autowired
	private List<Role> roles;

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Role[] getRoles() {
		return roles.toArray(new Role[roles.size()]);
	}
	
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public Role getRole(String id) {
		for (Role role : roles) {
			if (role.getId().equals(id))
				return role;
		}
		return null;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public List<Role> getRolesList() {
		return this.roles;
	}
}
