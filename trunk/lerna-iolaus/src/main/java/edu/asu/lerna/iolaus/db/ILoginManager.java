package edu.asu.lerna.iolaus.db;

import org.springframework.security.core.userdetails.UserDetailsService;

import edu.asu.lerna.iolaus.domain.implementation.User;

/**
 * Interface for login based DB4O functionality
 * 
 * @author : Lohith Dwaraka
 *
 */
public interface ILoginManager {

	/**
	 * This works closely with {@link UserDetailsService} Spring service
	 * @param username to fetch the matching User object from the DB4O object
	 * @return match {@link User} object
	 */
	public abstract User getUserById(String username);
}
