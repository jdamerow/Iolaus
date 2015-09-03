package edu.asu.lerna.iolaus.db.objectdb;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.objectdb.Utilities;

import edu.asu.lerna.iolaus.domain.implementation.IUser;
import edu.asu.lerna.iolaus.domain.implementation.User;
import edu.asu.lerna.iolaus.roles.IRoleName;
import edu.asu.lerna.iolaus.service.IUserManager;
import edu.asu.lerna.iolaus.service.login.LernaGrantedAuthority;

@Component
@PropertySource(value = "classpath:/db4o.properties")
public class ObjectDBDatabaseManager implements IUserManager {
	
	@Autowired
	private Environment env;
	private String dbpath;
	
	@PostConstruct
	public void init() {
		String dbfolder = env.getProperty("dbpath_folder");
		if (!dbfolder.endsWith(File.separator))
			dbfolder = dbfolder + File.separator;
		dbpath = dbfolder + env.getProperty("dbname");
	}
	
	@Override
	public void saveUser(IUser user) {
		EntityManager manager = Utilities.getEntityManager(dbpath);
		manager.getTransaction().begin();
		TypedQuery<PersistentUser> q = manager.createQuery("SELECT p FROM PersistentUser p WHERE p.username == '" + user.getUsername() + "'", PersistentUser.class);
		List<PersistentUser> users = q.getResultList();
		PersistentUser obj;
		if (users == null || users.isEmpty()) {
			obj = new PersistentUser();
			obj.setUsername(user.getUsername());
			manager.persist(obj);
		}
		else
			obj = users.iterator().next();
		
		List<String> roles = new ArrayList<String>();
		for (LernaGrantedAuthority authority : user.getAuthorities()) {
			roles.add(authority.getAuthority());
		}
		
		((PersistentUser) obj).setAuthorities(roles);
		((PersistentUser) obj).setEmail(user.getEmail());
		((PersistentUser) obj).setName(user.getName());
		((PersistentUser) obj).setPassword(user.getPassword());
		
		manager.getTransaction().commit();
		manager.close();
	}

	@Override
	public User getUserById(String userId) {
		EntityManager manager = Utilities.getEntityManager(dbpath);
		manager.getTransaction().begin();
		try {
			PersistentUser perUser = manager.find(PersistentUser.class, userId);
			if (perUser == null)
				return null;
			
			User user = createUser(perUser);
			return user;
		} finally {
			manager.getTransaction().commit();
			manager.close();
		}
		
	}

	private User createUser(PersistentUser persUser) {
		User user = new User();
		List<LernaGrantedAuthority> authorities = new ArrayList<LernaGrantedAuthority>();
		for (String role : persUser.getAuthorities()) {
			authorities.add(new LernaGrantedAuthority(role));
		}
		user.setAuthorities(authorities);
		user.setEmail(persUser.getEmail());
		user.setName(persUser.getName());
		user.setPassword(persUser.getPassword());
		user.setUsername(persUser.getUsername());
		return user;
	}

	@Override
	public List<User> getAllUsers() {
		EntityManager manager = Utilities.getEntityManager(dbpath);
		manager.getTransaction().begin();
		List<User> allUsers = new ArrayList<User>();
		TypedQuery<PersistentUser> query =
				    manager.createQuery("SELECT p FROM PersistentUser p", PersistentUser.class);
		List<PersistentUser> users = query.getResultList();
		
		for (PersistentUser user : users) {
			allUsers.add(createUser(user));
		}
		manager.getTransaction().commit();
		manager.close();
		return allUsers;
	}

	@Override
	public boolean deleteUser(String username) {
		EntityManager manager = Utilities.getEntityManager(dbpath);
		manager.getTransaction().begin();
		PersistentUser perUser = manager.find(PersistentUser.class, username);
		
		boolean success = true;
		if (perUser != null) {
			manager.remove(perUser);
		}
		else {
			success = false;
		}
		manager.getTransaction().commit();
		manager.close();
		return success;		
	}

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
