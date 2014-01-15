package edu.asu.lerna.iolaus.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 *  The controller to manage the login for user 
 * 
 * @author : Lohith Dwaraka
 */

@Controller
public class LoginController {

	/**
	 * User requests a login page
	 * 
	 * @return		Redirected to the login page
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(ModelMap model) {

		return "login";

	}
 
	/**
	 *  In case the User login fails
	 * @param model
	 * @return login failed page
	 */
	@RequestMapping(value="/loginfailed", method = RequestMethod.GET)
	public String loginerror(ModelMap model) {
 
		model.addAttribute("error", "true");
		return "login";
 
	}
 
	/**
	 *  Handles the User logout page transfer
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logout(ModelMap model) {
 
		return "login";
 
	}
}
