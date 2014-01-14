package edu.asu.lerna.iolaus.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import edu.asu.lerna.iolaus.domain.implementation.Role;
import edu.asu.lerna.iolaus.service.IRoleManager;

/**
 * @description : Converts for the spring conversion of String to role
 * 
 * @author : Lohith Dwaraka
 *
 */

public class RoleStringConverter implements Converter<String, Role>{

	@Autowired
	private IRoleManager manager;
	
	/**
	 * Converts String to Role object
	 */
	@Override
	public Role convert(String arg0) {
		return manager.getRole(arg0);
	}
}
