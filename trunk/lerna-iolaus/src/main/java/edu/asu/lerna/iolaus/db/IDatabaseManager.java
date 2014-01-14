package edu.asu.lerna.iolaus.db;

import com.db4o.ObjectContainer;

/**
 * @description : Interface for Basic Data base functionality and set encryption in Db connectivity 
 *
 * @author : Lohith Dwaraka
 *
 */
public interface IDatabaseManager {

	/**
	 * get a DB4O client to make any DB queries
	 * @return {@link ObjectContainer} this help in connecting to the configured DB4O DB
	 */
	public abstract ObjectContainer getClient();

	/**
	 * Returns the status of encryption
	 * @return Boolean value of true or false
	 */
	public abstract boolean isEncrypt();

	/**
	 * Setter for setting the encrypt
	 * @param encrypt
	 */
	public abstract void setEncrypt(boolean encrypt);
}
