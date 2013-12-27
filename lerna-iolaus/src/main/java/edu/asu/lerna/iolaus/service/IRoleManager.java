package edu.asu.lerna.iolaus.service;

import java.util.List;

import edu.asu.lerna.iolaus.domain.implementation.Role;


/**
 * Interface to be implemented by role manager classes.
 * 
 * @author Lohith Dwaraka
 *
 */
public interface IRoleManager {

	/**
	 * Get role by its id.
	 * @param id role id
	 * @return role with appropriate id
	 */
	public abstract Role getRole(String id);

	/**
	 * Get all roles.
	 * @return array of all roles
	 */
	public abstract Role[] getRoles();
	
	/**
	 * Get All roles in a list
	 * @return list of all the roles
	 */
	public abstract List<Role> getRolesList();

}