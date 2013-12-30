package edu.asu.lerna.iolaus.web.usermanagement;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import edu.asu.lerna.iolaus.service.IRoleManager;
import edu.asu.lerna.iolaus.service.IUserManager;
import edu.asu.lerna.iolaus.service.login.LernaGrantedAuthority;
import edu.asu.lerna.iolaus.web.usermanagement.backing.UserBackingBean;

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
		
		User user = userManager.getUserById(userName);
		UserBackingBean ubb =new UserBackingBean();
		
		ubb.setName(user.getName());
		ubb.setUsername(user.getUsername());
		ubb.setEmail(user.getEmail());
		List<Role> roleList = new ArrayList<Role>();
		List<LernaGrantedAuthority> roleLGAList = user.getAuthorities();
		for(LernaGrantedAuthority l : roleLGAList){
			roleList.add(roleManager.getRole(l.getAuthority()));
		}
		
		ubb.setRoles(roleList);
		model.addAttribute("username",userName);
		model.addAttribute("availableRoles", roleManager.getRolesList());
		model.addAttribute("userBackingBean", ubb);
		return "auth/user/modifyuser";
	}
	
	@RequestMapping(value = "auth/user/modifyuser/{username}", method = RequestMethod.POST)
	public String updateUser(@PathVariable("username") String userName,@Valid @ModelAttribute UserBackingBean userForm, BindingResult result, ModelMap model,	Principal principal) {

		
		if (result.hasErrors()) {
			User user = userManager.getUserById(userName);
			UserBackingBean ubb =new UserBackingBean();
			
			ubb.setName(user.getName());
			ubb.setUsername(user.getUsername());
			ubb.setEmail(user.getEmail());
			List<Role> roleList = new ArrayList<Role>();
			List<LernaGrantedAuthority> roleLGAList = user.getAuthorities();
			for(LernaGrantedAuthority l : roleLGAList){
				roleList.add(roleManager.getRole(l.getAuthority()));
			}
			
			ubb.setRoles(roleList);
			model.addAttribute("availableRoles", roleManager.getRolesList());
			model.addAttribute("userBackingBean", ubb);
			return "auth/user/modifyuser";
		}

		
		
		User user = userFactory.createUser(userForm.getUsername(), userForm.getName(), userForm.getEmail(), userForm.getPassword(), userForm.getRoles());
		userManager.modifyUser(user, userName);
		
		return "redirect:/auth/user/listuser";
	}

}

