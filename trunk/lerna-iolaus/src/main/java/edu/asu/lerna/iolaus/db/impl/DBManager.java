package edu.asu.lerna.iolaus.db.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import edu.asu.lerna.iolaus.db.IDatabaseManager;
import edu.asu.lerna.iolaus.domain.implementation.Role;
import edu.asu.lerna.iolaus.domain.implementation.User;
import edu.asu.lerna.iolaus.factory.IUserFactory;
import edu.asu.lerna.iolaus.roles.IRoleName;
import edu.asu.lerna.iolaus.service.IRoleManager;
/**
 * @description : 	This class initializes the DB connection for DB4O. 
 * 					It also works on adding a "lernauser" user,
 *  				if there is no user in the database during startup 
 *  
 * @author : Lohith Dwaraka
 *
 */
public  abstract class DBManager {

	@Autowired 
	IDatabaseManager dbManager;

	@Autowired
	IUserFactory userFactory;

	@Autowired
	IRoleManager roleManager;

	ObjectContainer database;

	private static final Logger logger = LoggerFactory
			.getLogger(DBManager.class);

	public DBManager() {
		super();
	}

	/**
	 * Initializing the connection to DB4O, 
	 * the connection doesn't go down untill the application goes down.
	 * Also this post construct creates a "lernauser" user, 
	 * if there is no user in the DB
	 */
	@PostConstruct
	public synchronized void init() {
		database = dbManager.getClient();
		// Gets all the users from the DB
		ObjectSet<User> users = database.query(User.class);
		List<User> allUsers = new ArrayList<User>();
		allUsers.addAll(users);
		logger.debug("allUsers.size() :" + allUsers.size());
		// If Users count is equal to 0, then add the default user
		if(allUsers.size()==0){
			List<Role> roles = new ArrayList<Role>();
			roles.add(roleManager.getRole(IRoleName.ADMIN));
			roles.add(roleManager.getRole(IRoleName.USER));
			User user=userFactory.createUser("lernauser", "lerna", "lerna@gmail.com", "admin", roles);
			updateObject(user);
			logger.info("Created a user since there were no users in the system");
		}
	}

	/**
	 * Stores the object and commits
	 * 
	 * @param object any object stored in the database
	 * @return success or failure
	 */
	public synchronized boolean updateObject(Object object) {
		database.store(object);
		database.commit();
		return true;
	}
}
