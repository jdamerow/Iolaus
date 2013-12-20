package edu.asu.lerna.iolaus.domain.implementation;

import edu.asu.lerna.iolaus.domain.IUser;

/**
 * @description : User class describing the properties 
 *                of a User object
 * 
 * @author      : Lohith Dwaraka
 */
public class User implements IUser {

	private String firstName;
	private String lastName;
	private String userId;
	private String password;
	private String email;
	private String role;
	
	
	/* (non-Javadoc)
	 * @see edu.asu.lerna.iolaus.domain.implementation.IUser#getFirstName()
	 */
	@Override
	public String getFirstName() {
		return firstName;
	}
	/* (non-Javadoc)
	 * @see edu.asu.lerna.iolaus.domain.implementation.IUser#setFirstName(java.lang.String)
	 */
	@Override
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/* (non-Javadoc)
	 * @see edu.asu.lerna.iolaus.domain.implementation.IUser#getLastName()
	 */
	@Override
	public String getLastName() {
		return lastName;
	}
	/* (non-Javadoc)
	 * @see edu.asu.lerna.iolaus.domain.implementation.IUser#setLastName(java.lang.String)
	 */
	@Override
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/* (non-Javadoc)
	 * @see edu.asu.lerna.iolaus.domain.implementation.IUser#getUserId()
	 */
	@Override
	public String getUserId() {
		return userId;
	}
	/* (non-Javadoc)
	 * @see edu.asu.lerna.iolaus.domain.implementation.IUser#setUserId(java.lang.String)
	 */
	@Override
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/* (non-Javadoc)
	 * @see edu.asu.lerna.iolaus.domain.implementation.IUser#getPassword()
	 */
	@Override
	public String getPassword() {
		return password;
	}
	/* (non-Javadoc)
	 * @see edu.asu.lerna.iolaus.domain.implementation.IUser#setPassword(java.lang.String)
	 */
	@Override
	public void setPassword(String password) {
		this.password = password;
	}
	/* (non-Javadoc)
	 * @see edu.asu.lerna.iolaus.domain.implementation.IUser#getEmail()
	 */
	@Override
	public String getEmail() {
		return email;
	}
	/* (non-Javadoc)
	 * @see edu.asu.lerna.iolaus.domain.implementation.IUser#setEmail(java.lang.String)
	 */
	@Override
	public void setEmail(String email) {
		this.email = email;
	}
	/* (non-Javadoc)
	 * @see edu.asu.lerna.iolaus.domain.implementation.IUser#getRole()
	 */
	@Override
	public String getRole() {
		return role;
	}
	/* (non-Javadoc)
	 * @see edu.asu.lerna.iolaus.domain.implementation.IUser#setRole(java.lang.String)
	 */
	@Override
	public void setRole(String role) {
		this.role = role;
	}
	
	
	
		
}
