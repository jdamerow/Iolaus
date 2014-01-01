package edu.asu.lerna.iolaus.web.usermanagement;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.lerna.iolaus.domain.implementation.Role;
import edu.asu.lerna.iolaus.domain.implementation.User;
import edu.asu.lerna.iolaus.factory.IUserFactory;
import edu.asu.lerna.iolaus.roles.IRoleName;
import edu.asu.lerna.iolaus.service.IRoleManager;
import edu.asu.lerna.iolaus.service.IUserManager;
import edu.asu.lerna.iolaus.service.login.LernaGrantedAuthority;
import edu.asu.lerna.iolaus.web.usermanagement.backing.ModifyUserBackingBean;

@Controller
public class ModifyUserController {

	private static final Logger logger = LoggerFactory
			.getLogger(ModifyUserController.class);

	@Autowired
	private IUserManager userManager; 
	
	@Autowired
	private IUserFactory userFactory; 
	
	@Autowired
	IRoleManager roleManager;

	@RequestMapping(value = "auth/user/deleteUser", method = RequestMethod.POST)
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
		String[] values = req.getParameterValues("selected");
		try{
			for(String v : values){
				logger.info(" selected : "+ v);
				userManager.deleteUser(v);
			}
		}catch(Exception e){
			logger.error("DB Error :",e);
		}


		return "redirect:/auth/user/listuser";
	}
	
	@RequestMapping(value = "auth/user/modifyuser/{username}", method = RequestMethod.GET)
	public String modifyUser(@PathVariable("username") String userName,HttpServletRequest req, ModelMap model,Principal principal) {
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
		User user = userManager.getUserById(userName);
		if(user == null){
			model.addAttribute("message","User not found");
			return "auth/resourcenotfound";
		}
		ModifyUserBackingBean mubb =new ModifyUserBackingBean();
		
		mubb.setName(user.getName());
		mubb.setUsername(user.getUsername());
		mubb.setEmail(user.getEmail());
		List<Role> roleList = new ArrayList<Role>();
		List<LernaGrantedAuthority> roleLGAList = user.getAuthorities();
		for(LernaGrantedAuthority l : roleLGAList){
			roleList.add(roleManager.getRole(l.getAuthority()));
		}
		
		mubb.setRoles(roleList);
		model.addAttribute("username",userName);
		model.addAttribute("availableRoles", roleManager.getRolesList());
		model.addAttribute("modifyUserBackingBean", mubb);
		return "auth/user/modifyuser";
	}
	
	@RequestMapping(value = "auth/user/modifyuser/{username}", method = RequestMethod.POST)
	public String updateUser(@PathVariable("username") String userName,@Valid @ModelAttribute ModifyUserBackingBean userForm, BindingResult result, ModelMap model,	Principal principal) {
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
		
		if (result.hasErrors()) {
			model.addAttribute("availableRoles", roleManager.getRolesList());
			return "auth/user/modifyuser";
		}
		if(!userName.equals(userForm.getUsername())){
			User user = userManager.getUserById(userForm.getUsername());
			if (!(user == null)){
				model.addAttribute("errorMsg", "Username should original or unique one");
				model.addAttribute("availableRoles", roleManager.getRolesList());
				return "auth/user/modifyuser";
			}
		}
		
		
		
		User user = userFactory.createUser(userForm.getUsername(), userForm.getName(), userForm.getEmail(), userForm.getPassword(), userForm.getRoles());
		userManager.modifyUser(user, userName);
		
		return "redirect:/auth/user/listuser";
	}

}

