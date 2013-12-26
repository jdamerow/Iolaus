package edu.asu.lerna.iolaus.web.usermanagement;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import EDU.purdue.cs.bloat.tree.ArrayLengthExpr;

import edu.asu.lerna.iolaus.domain.implementation.Role;
import edu.asu.lerna.iolaus.domain.implementation.User;
import edu.asu.lerna.iolaus.factory.IUserFactory;
import edu.asu.lerna.iolaus.service.IRoleManager;
import edu.asu.lerna.iolaus.service.IUserManager;
import edu.asu.lerna.iolaus.service.impl.Neo4jInstanceManager;
import edu.asu.lerna.iolaus.web.usermanagement.backing.UserBackingBean;

@Controller
public class AddUserController {

	@Autowired
	private IUserManager userManager;
	
	@Autowired 
	private IUserFactory userFactory;
	
	@Autowired
	private IRoleManager roleManager;
	
	private static final Logger logger = LoggerFactory
			.getLogger(AddUserController.class);
	
	@RequestMapping(value = "auth/user/adduser", method = RequestMethod.GET)
	public String getToAddUserPage( ModelMap model, Principal principal) {
		model.addAttribute("availableRoles", roleManager.getRolesList());
		model.addAttribute("userBackingBean", new UserBackingBean());
		return "auth/user/adduser";
	}
	
	@RequestMapping(value = "auth/user/adduser", method = RequestMethod.POST)
	public String addNewUser(@Valid @ModelAttribute UserBackingBean userForm, BindingResult result, ModelMap map) {
		if (result.hasErrors()) {
			map.addAttribute("availableRoles", roleManager.getRoles());
			
			return "auth/user/adduser";
		}
		List<String> roleList = userForm.getRoles();
		
		List<Role> roleList1  = new ArrayList<Role>();
		for(String role : roleList){
			Role rol1 = roleManager.getRole(role);
			roleList1.add(rol1);
		}
		
		
		User user = userFactory.createUser(userForm.getUsername(), userForm.getName(), userForm.getEmail(), userForm.getPassword(), roleList1);
		userManager.saveUser(user);
		
		return "redirect:/auth/user/listuser";
	}
}
