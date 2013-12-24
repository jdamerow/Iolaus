package edu.asu.lerna.iolaus.web.usermanagement;



import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ListUserController {

	
	@RequestMapping(value = "auth/listuser", method = RequestMethod.GET)
	public String getUserList( ModelMap model, Principal principal)
					 {
	

		return "auth/listuser";
	}
}
