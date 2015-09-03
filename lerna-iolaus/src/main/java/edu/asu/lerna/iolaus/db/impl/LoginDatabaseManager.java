//package edu.asu.lerna.iolaus.db.impl;
//
//import org.springframework.stereotype.Service;
//
//import com.db4o.ObjectSet;
//
//import edu.asu.lerna.iolaus.db.ILoginManager;
//import edu.asu.lerna.iolaus.domain.implementation.IUser;
//import edu.asu.lerna.iolaus.domain.implementation.User;
//
///**
// * 	This service works on implementing {@link ILoginManager}.
// * 					It is used for authenticating the user who tries to login to the system
// * @author : Lohith Dwaraka
// *
// */
//@Service
//public class LoginDatabaseManager extends DBManager implements ILoginManager {
//
//	/**
//	 * 
//	 * {@inheritDoc}
//	 */
//	@Override
//	public User getUserById(String username) {
//		IUser example = new User();
//		example.setUsername(username);
//		ObjectSet<User> foundUsers = database.queryByExample(example);
//		if (foundUsers.size() == 0)
//			return null;
//					
//		return foundUsers.get(0);
//	}
//
//}
