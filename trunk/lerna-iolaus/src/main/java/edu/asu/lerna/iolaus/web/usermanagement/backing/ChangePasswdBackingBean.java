package edu.asu.lerna.iolaus.web.usermanagement.backing;

import edu.asu.lerna.iolaus.annotation.NotEmpty;
import edu.asu.lerna.iolaus.domain.implementation.User;

/**
 * @description : Backing bean for {@link User} change password, 
 * it also handles any errors through annotation based validation
 * @author : Lohith Dwaraka 
 *
 */
public class ChangePasswdBackingBean {



	@NotEmpty(message = "Please provide the password.")
	private String newpassword;

	@NotEmpty(message = "Repeat password doesn't match with the above")
	private String repeatpassword;

	/**
	 * @description : Getter method for new password entered
	 * @return newpassword
	 */
	public String getNewpassword() {
		return newpassword;
	}

	/**
	 * @description : Setter method for new password
	 * @param newpassword
	 */
	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
	}

	/**
	 * @description : Getter method for repeatpassword
	 * @return repeatpassword
	 */
	public String getRepeatpassword() {
		return repeatpassword;
	}

	/**
	 * @description : Setter method for repeatpassword
	 * @param repeatpassword
	 */
	public void setRepeatpassword(String repeatpassword) {
		if(!repeatpassword.equals(this.newpassword)){
			this.repeatpassword = null;
		}else{
			this.repeatpassword = repeatpassword;
		}
	}



}
