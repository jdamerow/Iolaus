package edu.asu.lerna.iolaus.web.usermanagement;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.lerna.iolaus.factory.IUserFactory;
import edu.asu.lerna.iolaus.service.IRoleManager;
import edu.asu.lerna.iolaus.service.IUserManager;

@Controller
public class AddUserController {

	@Autowired
	private IUserManager userManager;
	
	@Autowired 
	private IUserFactory userFactory;
	
	@Autowired
	private IRoleManager roleManager;
	
	@RequestMapping(value = "auth/adduser", method = RequestMethod.GET)
	public String getUserList( ModelMap model, Principal principal) {
	
		model.addAttribute("availableRoles", roleManager.getRoles());
		model.addAttribute("userBackingBean", new edu.asu.lerna.iolaus.web.usermanagement.backing.UserBackingBean());
		return "auth/adduser";
	}
}
