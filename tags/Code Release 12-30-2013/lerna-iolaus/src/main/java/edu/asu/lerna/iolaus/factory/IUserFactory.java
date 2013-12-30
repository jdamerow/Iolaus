package edu.asu.lerna.iolaus.factory;

import java.util.List;

import edu.asu.lerna.iolaus.domain.implementation.User;
import edu.asu.lerna.iolaus.domain.implementation.Role;

public interface IUserFactory {

	public abstract User createUser(String username, String name, String email,
			String password, List<Role> roles);
	
	
	public abstract User createUser(String username, String name, String email,
			String password, Role[] roles);

	public abstract String encrypt(String pw);
}
