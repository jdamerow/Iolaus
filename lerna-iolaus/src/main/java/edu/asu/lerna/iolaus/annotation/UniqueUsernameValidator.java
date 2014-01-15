package edu.asu.lerna.iolaus.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import edu.asu.lerna.iolaus.domain.implementation.User;
import edu.asu.lerna.iolaus.service.IUserManager;

/**
 * 	Validator for the {@link UniqueUsername} annotation
 *					This class helps the annotation to validate whether the username field is unique
 * @author : Lohith Dwaraka
 *
 */
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String>{

	@Autowired
	private IUserManager userManager;
	
	@Override
	public void initialize(UniqueUsername arg0) {
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid(String arg0, ConstraintValidatorContext arg1) {
		User user = userManager.getUserById(arg0.trim().toLowerCase());
		if (user == null)
			return true;
		return false;
	}
}
