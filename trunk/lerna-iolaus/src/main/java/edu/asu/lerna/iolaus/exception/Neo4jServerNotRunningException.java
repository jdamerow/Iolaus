package edu.asu.lerna.iolaus.exception;

public class Neo4jServerNotRunningException extends Exception {
	
	public Neo4jServerNotRunningException(String message) {
		super(message);
	}

	private static final long serialVersionUID = 1L;

}
