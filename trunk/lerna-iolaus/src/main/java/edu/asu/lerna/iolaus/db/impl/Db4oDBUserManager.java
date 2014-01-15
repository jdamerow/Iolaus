package edu.asu.lerna.iolaus.db.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.db4o.ObjectSet;

import edu.asu.lerna.iolaus.domain.implementation.User;
import edu.asu.lerna.iolaus.roles.IRoleName;
import edu.asu.lerna.iolaus.service.IUserManager;
import edu.asu.lerna.iolaus.service.login.LernaGrantedAuthority;

/**
 * Service uses {@link DBManager} to implement {@link IUserManager}.
 * 					This class works on implementing all user level activities.
 * 
 * @author : Lohith Dwaraka
 *
 */
@Service
public class Db4oDBUserManager extends DBManager implements IUserManager {


	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean saveUser(User user) {
		return updateObject(user);
	}
	

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public User getUserById(String username) {
		User example = new User();
		example.setUsername(username);
		ObjectSet<User> foundUsers = database.queryByExample(example);
		if (foundUsers.size() == 0)
			return null;
		return foundUsers.get(0);
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public List<User> getAllUsers() {
		ObjectSet<User> users = database.query(User.class);
		List<User> allUsers = new ArrayList<User>();
		allUsers.addAll(users);
		return allUsers;
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean deleteUser(String username) {
		User userToBeDeleted = getUserById(username);
		database.delete(userToBeDeleted);
		return true;
	}
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean modifyUser(User user,String username) {
		User userToBeDeleted = getUserById(username);
		database.delete(userToBeDeleted);
		updateObject(user);
		return true;
	}
	
	

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasAdminAccess(User user) {
		List<LernaGrantedAuthority> authorities = user.getAuthorities();
		for(LernaGrantedAuthority lgh : authorities){
			if(lgh.getAuthority().equals( IRoleName.ADMIN)){
				return true;
			}
		}
		return false;
	}
}
