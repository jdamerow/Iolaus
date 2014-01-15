package edu.asu.lerna.iolaus.factory;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCrypt;

import edu.asu.lerna.iolaus.domain.implementation.User;
import edu.asu.lerna.iolaus.domain.implementation.Role;

/**
 * User object creation factory for adding users
 * @author : Lohith Dwaraka
 *
 */
public interface IUserFactory {

	/**
	 * Create User object and it encrypts the password info using {@link BCrypt}
	 * This is usally called if List of {@link Role} is used
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
	 * Create User object and it encryupts the password info using {@link BCrypt}. 
	 * This is usually called if array of {@link Role} is used
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
	 * 	Encrypt password based on {@link BCrypt}
	 * @param pw takes password as input
	 * @return Encrypted password to store in DB
	 */
	public abstract String encrypt(String pw);
}
