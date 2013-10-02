package edu.asu.spring.iolaus.web.rest;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * Controller for Iolaus related rest api's exposed to other clients
 * 
 * @author Lohith Dwaraka
 * 
 */
@Controller
public class AddNodesRestController {

	private static final Logger logger = LoggerFactory
			.getLogger(AddNodesRestController.class);

	
	@ResponseBody
	@RequestMapping(value = "rest/uploadnetworks", method = RequestMethod.GET)
	public String getNetworkFromClients(HttpServletRequest request,
			HttpServletResponse response, @RequestBody String xml,
			@RequestHeader("Accept") String accept,Principal principal){
		
		
		return "xml";
	}
}
