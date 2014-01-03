package edu.asu.lerna.iolaus.web.usermanagement;

import java.security.Principal;
import java.util.Collection;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.lerna.iolaus.domain.implementation.User;
import edu.asu.lerna.iolaus.factory.IUserFactory;
import edu.asu.lerna.iolaus.roles.IRoleName;
import edu.asu.lerna.iolaus.service.IRoleManager;
import edu.asu.lerna.iolaus.service.IUserManager;
import edu.asu.lerna.iolaus.web.usermanagement.backing.UserBackingBean;

/**
 *	This controller class would help in adding user to the db
 * @author Lohith Dwaraka 
 *
 */
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
	
	/**
	 * Helps create add user form by getting the object ready for form jsp tags
	 * @param model
	 * @param principal
	 * @return
	 */
	@RequestMapping(value = "auth/user/adduser", method = RequestMethod.GET)
	public String getToAddUserPage( ModelMap model, Principal principal) {
		// Checking authentication issues based on role
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		boolean access = false; 
		for (GrantedAuthority ga : authorities) {
			if(ga.getAuthority().equals(IRoleName.ADMIN))
				access=true;
		}
		
		// If user is not authorized
		if(access ==false){
			logger.info("Access not allowes");
			return "auth/noaccess";
		}
		// If user is authorized
		model.addAttribute("availableRoles", roleManager.getRolesList());
		model.addAttribute("userBackingBean", new UserBackingBean());
		return "auth/user/adduser";
	}
	
	/**
	 * Adds users by receiving the user details as object form
	 * @param userForm
	 * @param result
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "auth/user/adduser", method = RequestMethod.POST)
	public String addNewUser(@Valid @ModelAttribute UserBackingBean userForm, BindingResult result, ModelMap map) {

		// Checking authentication issues based on role
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		boolean access = false; 
		for (GrantedAuthority ga : authorities) {
			if(ga.getAuthority().equals(IRoleName.ADMIN))
				access=true;
		}
		// If user is not authorized
		if(access ==false){
			logger.info("Access not allowes");
			return "auth/noaccess";
		}
		// If user data has validation issues
		if (result.hasErrors()) {
			map.addAttribute("availableRoles", roleManager.getRoles());
			return "auth/user/adduser";
		}		
		
		// Create the user object with the form data
		User user = userFactory.createUser(userForm.getUsername(), userForm.getName(), userForm.getEmail(), userForm.getPassword(), userForm.getRoles());
		// Saves into db4o
		userManager.saveUser(user);
		
		return "redirect:/auth/user/listuser";
	}
}
