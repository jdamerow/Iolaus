package edu.asu.lerna.iolaus.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
	
	
	
	/**
	 * This method handles GET request for adding a Neo4j instance.
	 * @return ModelAndView of Neo4jInstance
	 */
	@RequestMapping(value = "/auth/addInstance", method = RequestMethod.GET)
	public ModelAndView addInstance() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		boolean access = false; 
		for (GrantedAuthority ga : authorities) {
			if(ga.getAuthority().equals(IRoleName.ADMIN)) //if user is ADMIN then access=true
				access=true;
		}
		if(!access){//if no access then display noaccess page otherwise return ModelAndView of Neo4jInstance
			logger.info("Access not allowes");
			return new ModelAndView("auth/noaccess");
		}else{
			return new ModelAndView("auth/addInstance", "command", new Neo4jInstance());
		}
	 }
	  
	
	
	/**
	 * This method handles POST request for adding a Neo4j Instance
	 * @param instance is object of Neo4jInstance
	 * @param model is a ModelMap
	 * @param principal is a object of Principal
	 * @return "redirect:/auth/listInstances" to tiles for creating view
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/auth/addInstance", method = RequestMethod.POST)
	public String addInstance(@ModelAttribute("SpringWeb")INeo4jInstance instance, ModelMap model,Principal principal) throws UnsupportedEncodingException {
		instance.setUserName(principal.getName());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		boolean access = false; 
		for (GrantedAuthority ga : authorities) {
			if(ga.getAuthority().equals(IRoleName.ADMIN)) //if user is ADMIN then access=true
				access=true;
		}
		if(!access){//if no access then return "auth/noaccess" to tiles
			logger.info("Access not allowes");
			return "auth/noaccess";
		}
		String instanceId=instanceManager.addNeo4jInstance(instance);
		if(instanceId.equals("0")){//if combination of port number and host is not unique in that case instanceId="0"
			model.addAttribute("instance", instance);
			model.addAttribute("noConnectivity", false);
			return "auth/addInstance";
		}
		if(instanceId.equals("-1")){//if port number of host is null then instanceId="-1"
			model.addAttribute("failure",true);
			model.addAttribute("noConnectivity", false);
			return "auth/addInstance";
		}
		if(instanceId.equals("-2")){//if connection failure then instanceId="-2"
			model.addAttribute("noConnectivity", true);
			model.addAttribute("instance", instance);
			return "auth/addInstance";
		}
		if(instanceId.equals("-3")){//if unable to create index to Neo4j
			model.addAttribute("unableToCreateIndex", true);
			model.addAttribute("noConnectivity", false);
			model.addAttribute("instance", instance);
			return "auth/addInstance";
		}
		model.addAttribute("instanceId",instanceId);
	    return "redirect:/auth/listInstances";
	}
	
	
	/**
	 * This method handles GET request for listing instances
	 * @param req is HttpServletRequest
	 * @param model is a ModelMap
	 * @param principal is object of Principal
	 * @return auth/listInstances to tiles for creating view.
	 */
	@RequestMapping(value = "auth/listInstances", method = RequestMethod.GET)
	public String listInstances(HttpServletRequest req, ModelMap model, Principal principal){
		List<INeo4jInstance> instanceList=instanceManager.getAllInstances();
		model.addAttribute("instanceList", instanceList);
		String instanceId=req.getParameter("instanceId");
		if(instanceId!=null){//instanceId!=null if request is redirected from addInstance
			INeo4jInstance instance=instanceManager.getInstance(instanceId);
			if(instance.getId()!=null){//this is true if instanceId does not exists 
				model.addAttribute("instanceId", instanceId);
			}
		}
		return "auth/listInstances";
	}
	
	
	/**
	 * This method handles the GET request for editing instance.
	 * @param instanceId is a instance id provided by the user.
	 * @param req is a HttpServletRequest
	 * @param model is a modelMap
	 * @param principal is a principal object
	 * @return the auth/editInstancen string to the tiles for creating the view.
	 */
	@RequestMapping(value = "auth/editInstance/{instanceId}", method = RequestMethod.GET)
	public String editInstance(@PathVariable("instanceId") String instanceId,HttpServletRequest req, ModelMap model,Principal principal){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		boolean access = false; 
		for (GrantedAuthority ga : authorities) {
			if(ga.getAuthority().equals(IRoleName.ADMIN))//if user is ADMIN then access=true
				access=true;
		}
		if(!access){//if no access then return "auth/noaccess" to tiles
			logger.info("Access not allowes");
			return "auth/noaccess";
		}
		INeo4jInstance instance=instanceManager.getInstance(instanceId);
		model.addAttribute("instance", instance);
		return "auth/editInstance";
	}
	
	
	/**
	 * This method handles the POST request for updating the instance.
	 * @param instance is Neo4jInstance object having values entered by user.
	 * @param model is a ModelMap
	 * @return the redirect:/auth/listInstances string to tiles for creating view. 
	 * @throws IOException 
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/auth/editInstance/updateInstance", method = RequestMethod.POST)
	public String updateInstance(@ModelAttribute("SpringWeb")INeo4jInstance instance, ModelMap model) throws UnsupportedEncodingException, IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		boolean access = false; 
		for (GrantedAuthority ga : authorities) { 
			if(ga.getAuthority().equals(IRoleName.ADMIN)) //if user is ADMIN then access=true
				access=true;
		}
		if(!access){//if no access then return "auth/noaccess" to tiles
			logger.info("Access not allowes");
			return "auth/noaccess";
		}
		int flag=instanceManager.updateNeo4jInstance(instance);//flag=1 if combination of port number and host is already in use.
		if(flag==1){
			model.addAttribute("instance", instance);
			model.addAttribute("flag", true);
			return "auth/editInstance";
		}
		if(flag==2){
			model.addAttribute("instance", instance);
			model.addAttribute("noConnectivity", true);
			return "auth/editInstance";
		}
		
	    return "redirect:/auth/listInstances";
	}
	
	
	
	/**
	 * This method handles post request for deleting a Neo4j instance
	 * @param req is a HttpServletRequest
	 * @param model is a ModelMap
	 * @param principal is a Principal Object
	 * @return the redirect:/auth/listInstances string to the tile for creating view
	 */
	@RequestMapping(value = "auth/deleteInstances", method = RequestMethod.POST)
	public String deleteInstance(HttpServletRequest req, ModelMap model,	Principal principal) {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		boolean access = false; 
		for (GrantedAuthority ga : authorities) {
			if(ga.getAuthority().equals(IRoleName.ADMIN)) //if user is ADMIN then access=true
				access=true;
		}
		if(!access){//if no access then return "auth/noaccess" to tiles
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
