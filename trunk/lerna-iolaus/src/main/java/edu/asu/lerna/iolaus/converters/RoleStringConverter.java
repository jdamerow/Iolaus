package edu.asu.lerna.iolaus.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import edu.asu.lerna.iolaus.domain.implementation.Role;
import edu.asu.lerna.iolaus.service.IRoleManager;

@Component
public class RoleStringConverter implements Converter<String, Role>{

	@Autowired
	private IRoleManager manager;
	
	@Override
	public Role convert(String arg0) {
		System.out.println("manager.getRole(arg0) : "+manager.getRole(arg0));
		return manager.getRole(arg0);
	}
}
