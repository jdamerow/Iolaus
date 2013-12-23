package edu.asu.lerna.iolaus.service;

import java.util.List;

import edu.asu.lerna.iolaus.domain.implementation.User;

public interface IUserManager {

	/**
	 * Save given user in database.
	 * @param user User to be saved.
	 * @return true if succesful otherwise false.
	 */
	public abstract boolean saveUser(User user);

	/**
	 * Get user by username.
	 * @param userId
	 * @return
	 */
	public abstract User getUserById(String userId);

	/**
	 * Get all stored users.
	 * @return List of stored users.
	 */
	public abstract List<User> getAllUsers();

	public abstract boolean deleteUser(String username);
}
