package edu.asu.lerna.iolaus.db.impl;

import org.springframework.stereotype.Service;

import com.db4o.ObjectSet;

import edu.asu.lerna.iolaus.db.ILoginManager;
import edu.asu.lerna.iolaus.domain.implementation.User;

@Service
public class LoginDatabaseManager extends DBManager implements ILoginManager {

	@Override
	public User getUserById(String username) {
		User example = new User();
		example.setUsername(username);
		ObjectSet<User> foundUsers = database.queryByExample(example);
		if (foundUsers.size() == 0)
			return null;
					
		return foundUsers.get(0);
	}

}
