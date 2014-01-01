package edu.asu.lerna.iolaus.web;

import java.security.Principal;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import edu.asu.lerna.iolaus.domain.INeo4jInstance;
import edu.asu.lerna.iolaus.domain.implementation.Neo4jInstance;
import edu.asu.lerna.iolaus.roles.IRoleName;
import edu.asu.lerna.iolaus.service.INeo4jInstanceManager;

/**
 * This is controller for Instance Management
 * @author Karan Kothari
 *
 */

@Controller
public class InstanceController {
	
	private static final Logger logger = LoggerFactory
			.getLogger(InstanceController.class);

	
	@Autowired
	private INeo4jInstanceManager instanceManager;
	
	@RequestMapping(value = "/auth/addInstance", method = RequestMethod.GET)
	public ModelAndView addInstance() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		boolean access = false; 
		for (GrantedAuthority ga : authorities) {
			if(ga.getAuthority().equals(IRoleName.ADMIN))
				access=true;
		}
		if(access ==false){
			logger.info("Access not allowes");
			return new ModelAndView("auth/noaccess");
		}
	      return new ModelAndView("auth/addInstance", "command", new Neo4jInstance());
	 }
	  
	@RequestMapping(value = "/auth/addInstance", method = RequestMethod.POST)
	public String addInstance(@ModelAttribute("SpringWeb")Neo4jInstance instance, ModelMap model,Principal principal) {
		instance.setUserName(principal.getName());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		boolean access = false; 
		for (GrantedAuthority ga : authorities) {
			if(ga.getAuthority().equals(IRoleName.ADMIN))
				access=true;
		}
		if(access ==false){
			logger.info("Access not allowes");
			return "auth/noaccess";
		}
		String instanceId=instanceManager.addNeo4jInstance(instance);
		if(instanceId.equals("0")){
			model.addAttribute("instance", instance);
			return "auth/addInstance";
		}
		if(instanceId.equals("-1")){
			model.addAttribute("failure",true);
			return "auth/addInstance";
		}
		model.addAttribute("instanceId",instanceId);
	    return "redirect:/auth/listInstances";
	}
	
	@RequestMapping(value = "auth/listInstances", method = RequestMethod.GET)
	public String listInstances(HttpServletRequest req, ModelMap model, Principal principal){
		List<INeo4jInstance> instanceList=instanceManager.getAllInstances();
		model.addAttribute("instanceList", instanceList);
		String instanceId=req.getParameter("instanceId");
		if(instanceId!=null){
			INeo4jInstance instance=instanceManager.getInstance(instanceId);
			if(instance.getId()!=null){
				model.addAttribute("instanceId", instanceId);
			}
		}
		return "auth/listInstances";
	}
	
	@RequestMapping(value = "auth/editInstance/{instanceId}", method = RequestMethod.GET)
	public String editInstance(@PathVariable("instanceId") String instanceId,HttpServletRequest req, ModelMap model,Principal principal){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		boolean access = false; 
		for (GrantedAuthority ga : authorities) {
			if(ga.getAuthority().equals(IRoleName.ADMIN))
				access=true;
		}
		if(access ==false){
			logger.info("Access not allowes");
			return "auth/noaccess";
		}
		INeo4jInstance instance=instanceManager.getInstance(instanceId);
		model.addAttribute("instance", instance);
		return "auth/editInstance";
	}
	
	@RequestMapping(value = "/auth/editInstance/updateInstance", method = RequestMethod.POST)
	public String updateInstance(@ModelAttribute("SpringWeb")Neo4jInstance instance, ModelMap model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		boolean access = false; 
		for (GrantedAuthority ga : authorities) {
			if(ga.getAuthority().equals(IRoleName.ADMIN))
				access=true;
		}
		if(access ==false){
			logger.info("Access not allowes");
			return "auth/noaccess";
		}
		boolean flag=instanceManager.updateNeo4jInstance(instance);
		if(!flag){
			model.addAttribute("instance", instance);
			model.addAttribute("flag", true);
			return "auth/editInstance";
		}
	    return "redirect:/auth/listInstances";
	}
	
	@RequestMapping(value = "auth/deleteInstances", method = RequestMethod.POST)
	public String deleteUser(HttpServletRequest req, ModelMap model,	Principal principal) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		boolean access = false; 
		for (GrantedAuthority ga : authorities) {
			if(ga.getAuthority().equals(IRoleName.ADMIN))
				access=true;
		}
		if(access ==false){
			logger.info("Access not allowes");
			return "auth/noaccess";
		}
		String[] idList = req.getParameterValues("selected");
		try{
			for(String instanceId : idList){
				logger.info(" selected : "+ instanceId);
				instanceManager.deleteNeo4jInstance(instanceId);
			}
		}catch(Exception e){
			logger.error("System Error while deleting the file :",e);
		}


		return "redirect:/auth/listInstances";
	}
}
