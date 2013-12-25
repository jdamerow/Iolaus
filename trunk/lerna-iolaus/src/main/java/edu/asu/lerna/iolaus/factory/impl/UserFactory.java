package edu.asu.lerna.iolaus.factory.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.domain.implementation.Role;
import edu.asu.lerna.iolaus.domain.implementation.User;
import edu.asu.lerna.iolaus.factory.IUserFactory;
import edu.asu.lerna.iolaus.service.login.LernaGrantedAuthority;

@Service
public class UserFactory implements IUserFactory {

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
	
	@Override
	public String encrypt(String pw) {
		return BCrypt.hashpw(pw, BCrypt.gensalt());
	}

}
