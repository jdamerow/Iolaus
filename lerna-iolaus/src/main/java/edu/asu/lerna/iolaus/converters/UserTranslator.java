package edu.asu.lerna.iolaus.converters;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.domain.implementation.Role;
import edu.asu.lerna.iolaus.domain.implementation.User;
import edu.asu.lerna.iolaus.service.IRoleManager;
import edu.asu.lerna.iolaus.web.usermanagement.backing.ModifyUserBackingBean;
import edu.asu.lerna.iolaus.web.usermanagement.backing.UserBackingBean;

/**
 * Converts {@link User} to {@link UserBackingBean} and {@link ModifyUserBackingBean}
 *  
 * @author : Lohith Dwaraka
 *
 */

@Service
public class UserTranslator {

	@Autowired
	private IRoleManager roleManager;
	
	/**
	 * Translate  {@link User} to {@link UserBackingBean}
	 * All the variables in the {@link User} would be used to build a {@link UserBackingBean} object 
	 * @param user {@link User} object
	 * @return
	 */
	public UserBackingBean translateUser(User user) {
		UserBackingBean bean = new UserBackingBean();
		bean.setEmail(user.getEmail());
		bean.setName(user.getName());
		bean.setPassword(user.getPassword());
		bean.setUsername(user.getUsername());
		bean.setRoles(new ArrayList<Role>());
		
		for (GrantedAuthority auth : user.getAuthorities()) {
			Role role = roleManager.getRole(auth.getAuthority());
			if (role != null)
				bean.getRoles().add(role);
		}
		
		return bean;
	}
	
	/**
	 * Translate  {@link User} to {@link ModifyUserBackingBean}
	 * All the variables in the {@link User} would be used to build a {@link ModifyUserBackingBean} object 
	 * @param user {@link User} object
	 * @return
	 */
	public ModifyUserBackingBean translateModifyUser(User user) {
		ModifyUserBackingBean bean = new ModifyUserBackingBean();
		bean.setEmail(user.getEmail());
		bean.setName(user.getName());
		bean.setUsername(user.getUsername());
		bean.setRoles(new ArrayList<Role>());
		
		for (GrantedAuthority auth : user.getAuthorities()) {
			Role role = roleManager.getRole(auth.getAuthority());
			if (role != null)
				bean.getRoles().add(role);
		}
		
		return bean;
	}
}
