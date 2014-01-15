package edu.asu.lerna.iolaus.factory;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 *  Create a {@link VelocityEngine} Object based on the classpath
 * 					for resources like velocity templates.	  
 * 
 * @author : Lohith Dwaraka
 *
 */
public interface IRestVelocityEngineFactory {

	/**
	 * Prepares the {@link VelocityEngine} based on classpath
	 * @param req used to form a URL for xsd access
	 * @return {@link VelocityEngine} 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public abstract VelocityEngine getVelocityEngine(HttpServletRequest req) throws FileNotFoundException, IOException;
	
	/**
	 *  This is a getter for {@link VelocityContext}
	 *  
	 * @return {@link VelocityContext}
	 */
	public abstract VelocityContext getVelocityContext();
}
