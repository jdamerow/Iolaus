package edu.asu.lerna.iolaus.annotation;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import edu.asu.lerna.iolaus.domain.implementation.Role;

public class NotEmptyValidatorList implements ConstraintValidator<NotEmptyList, List<Role>>{

	@Override
	public void initialize(NotEmptyList arg0) {

	}

	@Override
	public boolean isValid(List<Role> arg0, ConstraintValidatorContext arg1) {
		if((arg0 != null)&&(arg0.size()!=0)){
			return true;
		}else if(arg0 == null){
			return false;
		}else{
			for(Role role : arg0){
				if(role==null){
					return false;
				}
			}
		}
		return false;
	}


}
