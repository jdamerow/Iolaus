package edu.asu.lerna.iolaus.factory.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.domain.implementation.Role;
import edu.asu.lerna.iolaus.domain.implementation.User;
import edu.asu.lerna.iolaus.factory.IUserFactory;
import edu.asu.lerna.iolaus.service.login.LernaGrantedAuthority;
/**
 * Factory to create the user 
 * @author Lohith Dwaraka
 *
 */
@Service
public class UserFactory implements IUserFactory {

	/**
	 * Create User object
	 * @param username
	 * @param name
	 * @param email
	 * @param password
	 * @param roles
	 * @return
	 */
	@Override
	public User createUser(String username, String name, String email,
			String password, List<Role> roles) {
		User user = new User();
		user.setUsername(username);
		user.setName(name);
		user.setEmail(email);
		user.setPassword(encrypt(password));
		
		user.setAuthorities(new ArrayList<LernaGrantedAuthority>());
		for (Role role : roles) {
			user.addAuthority(new LernaGrantedAuthority(role.getId()));
		}
		
		return user;
	}

	/**
	 * Create User object
	 * @param username
	 * @param name
	 * @param email
	 * @param password
	 * @param roles
	 * @return
	 */
	@Override
	public User createUser(String username, String name, String email,
			String password, Role[] roles) {
		User user = new User();
		user.setUsername(username);
		user.setName(name);
		user.setEmail(email);
		user.setPassword(encrypt(password));
		
		user.setAuthorities(new ArrayList<LernaGrantedAuthority>());
		for (Role role : roles) {
			user.addAuthority(new LernaGrantedAuthority(role.getId()));
		}
		
		return user;
	}
	
	/**
	 *  Encrypt the password
	 */
	@Override
	public String encrypt(String pw) {
		return BCrypt.hashpw(pw, BCrypt.gensalt());
	}

}
