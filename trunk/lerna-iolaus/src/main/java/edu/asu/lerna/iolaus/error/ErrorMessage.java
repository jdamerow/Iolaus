package edu.asu.lerna.iolaus.error;

import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.factory.IRestVelocityEngineFactory;

/**
 * This service generates error xmls using Velocity Engine.
 * @author Karan Kothari
 *
 */
@Service
public class ErrorMessage {

	@Autowired
	private IRestVelocityEngineFactory restVelocityEngineFactory;
	
	private static final Logger logger = LoggerFactory
			.getLogger(ErrorMessage.class);
	
	

	/**
	 * This method puts the error message in Xml body.
	 * @param errorMsg is a error message
	 * @param req is object of {@link HttpServletRequest}. 
	 * @return the xml form of the error.
	 */
	public String getErrorMsg(String errorMsg, HttpServletRequest req) {
		VelocityEngine engine = null;
		Template template = null;
		StringWriter sw = new StringWriter();

		try {
			engine = restVelocityEngineFactory.getVelocityEngine(req);
			engine.init();
			template = engine.getTemplate("velocitytemplates/error.vm");
			VelocityContext context = new VelocityContext(
					restVelocityEngineFactory.getVelocityContext());
			context.put("errMsg", errorMsg);
			template.merge(context, sw);
			return sw.toString();
		} catch (ResourceNotFoundException e) {
			logger.error("Exception:", e);
		} catch (Exception e) {
			logger.error("Exception:", e);
		}
		return errorMsg;
	}
	
}
