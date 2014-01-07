package edu.asu.lerna.iolaus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;

@Constraint(validatedBy = UniqueUsernameValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUsername {

	public abstract String message() default "Username is already in use. Choose another one.";
	public abstract Class[] groups() default {};
	public abstract Class[] payload() default {};
}
