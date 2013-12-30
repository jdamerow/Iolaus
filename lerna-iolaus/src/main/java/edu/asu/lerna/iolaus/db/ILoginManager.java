package edu.asu.lerna.iolaus.db;

import edu.asu.lerna.iolaus.domain.implementation.User;

public interface ILoginManager {

	public abstract User getUserById(String username);
}
