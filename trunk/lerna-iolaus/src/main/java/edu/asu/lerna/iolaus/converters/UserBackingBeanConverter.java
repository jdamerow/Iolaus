package edu.asu.lerna.iolaus.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.beans.factory.annotation.Autowired;

import edu.asu.lerna.iolaus.domain.implementation.User;
import edu.asu.lerna.iolaus.service.IUserManager;
import edu.asu.lerna.iolaus.web.usermanagement.backing.UserBackingBean;

/**
 *  Converter for user backing bean from String
 * 
 * @author : Lohith Dwaraka
 *
 */
public class UserBackingBeanConverter implements Converter<String,UserBackingBean> {

	@Autowired
	private IUserManager manager;
	
	@Autowired
	private UserTranslator translator;
	
	/**
	 * Converts String to {@link UserBackingBean}
	 */
	@Override
	public UserBackingBean convert(String source) {
		User user = manager.getUserById(source);
		return translator.translateUser(user);
	}
}
