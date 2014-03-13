package edu.asu.lerna.iolaus.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.lerna.iolaus.service.INeo4jInstanceManager;


/**
 * Controller for serving REST request for Neo4jInstances. 
 * @author Karan Kothari
 *
 */
@Controller
public class RESTInstanceController {
	
	@Autowired
	private INeo4jInstanceManager instanceManager;
	
	
	/**
	 * This method has mapping for /getNeo4jInstances method. 
	 * It returns list of Neo4j instance id's in form of json.
	 * @param request is empty.
	 * @param response has json.
	 * @return the list of Neo4j instances in json format.
	 */
	@ResponseBody
	@RequestMapping(value = "/getNeo4jInstances", method = RequestMethod.GET)
	public String getNeo4jInstances(HttpServletRequest request,	HttpServletResponse response){
		return instanceManager.getJsonOfInstances();
	}
	
}
