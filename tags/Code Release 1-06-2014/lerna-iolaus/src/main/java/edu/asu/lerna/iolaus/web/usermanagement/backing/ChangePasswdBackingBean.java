package edu.asu.lerna.iolaus.web.usermanagement.backing;

import edu.asu.lerna.iolaus.annotation.NotEmpty;

/**
 * Backing bean for user addition to db
 * @author Lohith Dwaraka 
 *
 */
public class ChangePasswdBackingBean {



	@NotEmpty(message = "Please provide the password.")
	private String newpassword;

	@NotEmpty(message = "Repeat password doesn't match with the above")
	private String repeatpassword;

	public String getNewpassword() {
		return newpassword;
	}

	public void setNewpassword(String newpassword) {
		this.newpassword = newpassword;
	}

	public String getRepeatpassword() {
		return repeatpassword;
	}

	public void setRepeatpassword(String repeatpassword) {
		if(!repeatpassword.equals(this.newpassword)){
			this.repeatpassword = null;
		}else{
			this.repeatpassword = repeatpassword;
		}
	}



}
