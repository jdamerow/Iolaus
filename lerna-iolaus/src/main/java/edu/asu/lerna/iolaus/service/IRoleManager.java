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
	 * Returns {@link Role} in a form of Array
	 * @return array of all the {@link Role}
	 */
	public abstract Role getRole(String id);

	/**
	 * We can get all the {@link Role} in this project
	 * @return array of all {@link Role}
	 */
	public abstract Role[] getRoles();
	
	/**
	 * Returns {@link Role} in a form of {@link List}
	 * @return {@link List} of all the {@link Role}
	 */
	public abstract List<Role> getRolesList();

}
