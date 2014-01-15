package edu.asu.lerna.iolaus.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import edu.asu.lerna.iolaus.domain.implementation.User;
import edu.asu.lerna.iolaus.service.IUserManager;

/**
 * Converts String to {@link User}
 * 
 * @author : Lohith Dwaraka
 *
 */
public class UserStringConverter implements Converter<String, User>{
	
	@Autowired
	private IUserManager manager;
	
	@Override
	public User convert(String arg0) {
		return manager.getUserById(arg0);
	}

}
