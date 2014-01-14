package edu.asu.lerna.iolaus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;

/**
 * @description : This annotation checks if the specified field is empty
 * 				  Validated by {@link NotEmptyValidator}
 * 
 * @author      : Lohith Dwaraka
 *
 */
@Constraint(validatedBy = NotEmptyValidator.class)
@Target( { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotEmpty {

	/**
	 * Default message to display in case field is empty
	 * @return message of type String 
	 */
	public abstract String message() default "Username is already in use. Choose another one.";
	public abstract Class[] groups() default {};
	public abstract Class[] payload() default {};
}
