package edu.asu.lerna.iolaus.annotation;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * This class works as a validator for NotEmpty annotation
 * 
 * @author : Lohith Dwaraka
 *
 */
public class NotEmptyValidator implements ConstraintValidator<NotEmpty, String>{

	@Override
	public void initialize(NotEmpty arg0) {
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isValid(String arg0, ConstraintValidatorContext arg1) {
		if((arg0 != null)  && !(arg0.trim().equals(""))){
			return true;
		}
		return false;
	}
	
}
