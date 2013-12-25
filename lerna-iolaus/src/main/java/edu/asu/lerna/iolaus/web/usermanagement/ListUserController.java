package edu.asu.lerna.iolaus.web.usermanagement;



import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.lerna.iolaus.domain.implementation.User;
import edu.asu.lerna.iolaus.service.IUserManager;

@Controller
public class ListUserController {

	private static final Logger logger = LoggerFactory
			.getLogger(ListUserController.class);
	


	
	@Autowired
	private IUserManager userManager; 
	
	@RequestMapping(value = "auth/user/listuser", method = RequestMethod.GET)
	public String getUserList( ModelMap model, Principal principal){

		List<User> users = userManager.getAllUsers();
//		for(User user : users){
//			logger.info("" + user.getUsername());
//			
//		}
		model.addAttribute("userList", users);
		
		return "auth/user/listuser";
	}
}
