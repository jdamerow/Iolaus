package edu.asu.lerna.iolaus.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import edu.asu.lerna.iolaus.domain.implementation.Neo4jInstance;
import edu.asu.lerna.iolaus.service.INeo4jInstanceManager;

@Controller
public class InstanceController {
	
	@Autowired
	private INeo4jInstanceManager instanceManager;
	
	@RequestMapping(value = "/auth/instance", method = RequestMethod.GET)
	public ModelAndView instance() {
	      return new ModelAndView("auth/addInstance", "command", new Neo4jInstance());
	 }
	  
	@RequestMapping(value = "/auth/addInstance", method = RequestMethod.POST)
	public String addInstance(@ModelAttribute("SpringWeb")Neo4jInstance instance, ModelMap model) {
		instanceManager.addNeo4jInstance(instance);
	    return "redirect:/auth/home";
	}
}
