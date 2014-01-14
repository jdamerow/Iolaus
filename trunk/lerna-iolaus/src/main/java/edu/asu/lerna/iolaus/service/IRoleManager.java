package edu.asu.lerna.iolaus.service;

import java.util.List;

import edu.asu.lerna.iolaus.domain.implementation.Role;


/**
 * @description : Interface to be implemented by role manager classes.
 * 
 * @author Lohith Dwaraka
 *
 */
public interface IRoleManager {

	/**
	 * @description : Returns {@link Role} object for role name as input. 
	 * @param id role id
	 * @return {@link Role} for appropriate id
	 */
	public abstract Role getRole(String id);

	/**
	 * @description : We can get all the {@link Role} in this project
	 * @return array of all {@link Role}
	 */
	public abstract Role[] getRoles();
	
	/**
	 * @description : Returns {@link Role} in a form of {@link List}
	 * @return {@link List} of all the {@link Role}
	 */
	public abstract List<Role> getRolesList();

}
