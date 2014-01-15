package edu.asu.lerna.iolaus.web.usermanagement;



import java.security.Principal;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.lerna.iolaus.domain.implementation.User;
import edu.asu.lerna.iolaus.roles.IRoleName;
import edu.asu.lerna.iolaus.service.IUserManager;

/**
 * This controller class would list the users for view
 * 
 * @author Lohith Dwaraka 
 *
 */
@Controller
public class ListUserController {

	private static final Logger logger = LoggerFactory
			.getLogger(ListUserController.class);
	
	
	@Autowired
	private IUserManager userManager; 
	
	/**
	 * Lists the {@link User} list object for view
	 * @param model {@link ModelMap} for this request
	 * @param principal Used to fetch user details 
	 * @return send the String for apache tiles to decide on the view 
	 */
	@RequestMapping(value = "auth/user/listuser", method = RequestMethod.GET)
	public String getUserList( ModelMap model, Principal principal){
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
		List<User> users = userManager.getAllUsers();
		model.addAttribute("userList", users);
		
		return "auth/user/listuser";
	}
}
