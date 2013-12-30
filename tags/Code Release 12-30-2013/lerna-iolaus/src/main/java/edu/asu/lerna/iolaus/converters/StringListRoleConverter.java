package edu.asu.lerna.iolaus.converters;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import edu.asu.lerna.iolaus.domain.implementation.Role;
import edu.asu.lerna.iolaus.service.IRoleManager;

public class StringListRoleConverter implements Converter<String, List<Role>>{

	@Autowired
	private IRoleManager manager;
	
	@Override
	public List<Role> convert(String arg0) {
		
		List<Role> roleList = new ArrayList<Role>();
		roleList.add(manager.getRole(arg0));
		return roleList;
	}
}
