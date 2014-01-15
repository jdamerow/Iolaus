package edu.asu.lerna.iolaus.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * This class redirects requests to the root of Lerna to the Lerna home page.
 * @author : Lohith Dwaraka
 *
 */
@Controller
public class RootController {

	/**
	 * Method that answers requests to root.
	 * @return the redirect path to the Quadriga home page.
	 */
	@RequestMapping(value="/", method = RequestMethod.GET)
	public String redirectToLogin() {
		return "redirect:auth/home";
	}
}
