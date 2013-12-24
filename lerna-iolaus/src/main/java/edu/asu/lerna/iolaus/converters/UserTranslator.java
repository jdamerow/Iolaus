package edu.asu.lerna.iolaus.converters;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.domain.implementation.Role;
import edu.asu.lerna.iolaus.domain.implementation.User;
import edu.asu.lerna.iolaus.service.IRoleManager;
import edu.asu.lerna.iolaus.web.usermanagement.backing.UserBackingBean;

@Service
public class UserTranslator {

	@Autowired
	private IRoleManager roleManager;
	
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
}
