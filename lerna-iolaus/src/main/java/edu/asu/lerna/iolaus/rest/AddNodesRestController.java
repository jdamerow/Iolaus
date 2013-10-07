package edu.asu.lerna.iolaus.rest;

import java.security.Principal;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


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


//	@RequestMapping(value = "/", method = RequestMethod.GET)
//	public String validUserHandle(ModelMap model, Principal principal) {
//
//		System.out.println("Inside home controller mapping.......");
//		return "home";
//
//	}
//	
	
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/addnodes", method = RequestMethod.GET)
	public String addNodes(Model model) {
		
		
		
		
		return "home";
	}

}
