package edu.asu.lerna.iolaus.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.domain.implementation.Role;
import edu.asu.lerna.iolaus.service.IRoleManager;

@Service
public class RoleManager implements IRoleManager{

	@Autowired
	private List<Role> roles;

	@Override
	public Role[] getRoles() {
		return roles.toArray(new Role[roles.size()]);
	}
	
	@Override
	public Role getRole(String id) {
		for (Role role : roles) {
			if (role.getId().equals(id))
				return role;
		}
		return null;
	}

	@Override
	public List<Role> getRolesList() {
		return this.roles;
	}
}
