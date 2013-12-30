package edu.asu.lerna.iolaus.db.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.db4o.ObjectSet;

import edu.asu.lerna.iolaus.domain.implementation.User;
import edu.asu.lerna.iolaus.service.IUserManager;

@Service
public class Db4oDBUserManager extends DBManager implements IUserManager {

	
	/* (non-Javadoc)
	 * @see edu.asu.momo.db.impl.IUserManager#saveUser(edu.asu.momo.core.User)
	 */
	@Override
	public boolean saveUser(User user) {
		return updateObject(user);
	}
	
	/* (non-Javadoc)
	 * @see edu.asu.momo.db.impl.IUserManager#getUserById(java.lang.String)
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
	
	@Override
	public List<User> getAllUsers() {
		ObjectSet<User> users = database.query(User.class);
		List<User> allUsers = new ArrayList<User>();
		allUsers.addAll(users);
		return allUsers;
	}
	
	@Override
	public boolean deleteUser(String username) {
		User userToBeDeleted = getUserById(username);
		database.delete(userToBeDeleted);
		return true;
	}
	
	@Override
	public boolean modifyUser(User user,String username) {
		User userToBeDeleted = getUserById(username);
		database.delete(userToBeDeleted);
		updateObject(user);
		return true;
	}
}
