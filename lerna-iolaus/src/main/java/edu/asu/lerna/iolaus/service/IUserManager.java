package edu.asu.lerna.iolaus.service;

import java.util.List;

import edu.asu.lerna.iolaus.domain.implementation.IUser;
import edu.asu.lerna.iolaus.domain.implementation.User;

/**
 *  Interface will all the user level activities like 
 * List, save, modify, delete {@link User} 
 * @author : 	Lohith Dwaraka
 *
 */
public interface IUserManager {

	/**
	 * Saves a given {@link User} in database.
	 * @param {@link User} to be saved.
	 * @return true if successful otherwise false.
	 */
	public abstract void saveUser(IUser user);

	/**
	 * Get {@link User} by username.
	 *  Can be used for authentication, and for fetching {@link User} info.
	 * 
	 * @param userId
	 * @return
	 */
	public abstract User getUserById(String userId);

	/**
	 * Get all stored {@link User} from the database.
	 * @return List of stored {@link User}.
	 */
	public abstract List<User> getAllUsers();

	/**
	 * Deletes the {@link User} from the Database.
	 * @param username to be deleted
	 * @return {@link Boolean} value for the status of this operation
	 */
	public abstract boolean deleteUser(String username);

	/**
	 * Check if the {@link User} has any admin access, 
	 * used for accessing resources which is restricted to the Admin
	 * @param user
	 * @return {@link Boolean} value for the status of this operation
	 */
	public abstract boolean hasAdminAccess(User user);
}
