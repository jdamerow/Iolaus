package edu.asu.lerna.iolaus.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthenticationController {
	
	/**
	 * REST API to tell the client that server is up
	 * Herckules is expected to access this API to verify connectivity of the newly added Iolaus details into Herckules
	 * @param request 		would contain nothing
	 * @param response		would be a string, telling I am LIVE
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/rest/authenticate", method = RequestMethod.GET)
	public void authenticate(HttpServletRequest request,	HttpServletResponse response){
		//authenticating the username and password sent by the Herckules using Rest spring security
		response.setStatus(200);
		
	}
	
}
