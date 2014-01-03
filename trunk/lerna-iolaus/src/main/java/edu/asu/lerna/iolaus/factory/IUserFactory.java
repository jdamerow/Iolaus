package edu.asu.lerna.iolaus.factory;

import java.util.List;

import edu.asu.lerna.iolaus.domain.implementation.User;
import edu.asu.lerna.iolaus.domain.implementation.Role;

/**
 * User object creation factory for adding users
 * @author Dwaraka
 *
 */
public interface IUserFactory {

	/**
	 * Create User object
	 * @param username
	 * @param name
	 * @param email
	 * @param password
	 * @param roles
	 * @return
	 */
	public abstract User createUser(String username, String name, String email,
			String password, List<Role> roles);
	
	/**
	 * Create User object
	 * @param username
	 * @param name
	 * @param email
	 * @param password
	 * @param roles
	 * @return
	 */
	public abstract User createUser(String username, String name, String email,
			String password, Role[] roles);

	/**
	 * Encrypt password
	 * @param pw
	 * @return
	 */
	public abstract String encrypt(String pw);
}
