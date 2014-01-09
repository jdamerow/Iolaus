package edu.asu.lerna.iolaus.factory;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public interface IRestVelocityEngineFactory {

	public abstract VelocityEngine getVelocityEngine(HttpServletRequest req) throws FileNotFoundException, IOException;
	public abstract VelocityContext getVelocityContext();
}
