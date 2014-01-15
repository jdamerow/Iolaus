package edu.asu.lerna.iolaus.factory.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.factory.IRestVelocityEngineFactory;

/**
 * Prepares the {@link VelocityEngine} and {@link VelocityContext} 
 * 					for any velocity based template utilization
 * @author : 	Lohith Dwaraka
 *
 */
@Service
public class RestVelocityEngineFactory implements IRestVelocityEngineFactory{

	private VelocityContext context;
	
	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public VelocityEngine getVelocityEngine(HttpServletRequest req)
			throws FileNotFoundException, IOException {
		VelocityEngine engine = new VelocityEngine();
		engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		engine.setProperty("classpath.resource.loader.class",
				ClasspathResourceLoader.class.getName());
		
		engine.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
				"org.apache.velocity.runtime.log.Log4JLogChute"
				);
		engine.setProperty("runtime.log.logsystem.log4j.logger","velocity");
		context = new VelocityContext();
		context.put("url", "http://"+req.getServerName()+":"+req.getServerPort()+req.getContextPath());
		Properties props = new Properties();
		props.put("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
		props.put("runtime.log.logsystem.log4j.category", "runtime.log.logsystem.log4j.category");
		props.put("runtime.log.logsystem.log4j.logger", "velocity");
		
		return engine;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public VelocityContext getVelocityContext() {
		// TODO Auto-generated method stub
		return context;
	}

}
