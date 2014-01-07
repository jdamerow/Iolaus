package edu.asu.lerna.iolaus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.domain.implementation.Role;
import edu.asu.lerna.iolaus.service.IRoleManager;

/**
 * Implemented by IRolemanager methods.
 * 
 * @author Lohith Dwaraka
 *
 */
@Service
public class RoleManager implements IRoleManager{

	@Autowired
	private List<Role> roles;

	/**
	 * Get role by its id.
	 * @param id role id
	 * @return role with appropriate id
	 */
	@Override
	public Role[] getRoles() {
		return roles.toArray(new Role[roles.size()]);
	}
	
	
	/**
	 * Get all roles.
	 * @return array of all roles
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
	 * Get All roles in a list
	 * @return list of all the roles
	 */
	@Override
	public List<Role> getRolesList() {
		return this.roles;
	}
}
