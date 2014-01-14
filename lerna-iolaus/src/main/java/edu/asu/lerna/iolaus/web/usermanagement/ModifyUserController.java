package edu.asu.lerna.iolaus.web.usermanagement;

import java.security.Principal;
import java.util.Collection;

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

import edu.asu.lerna.iolaus.converters.UserTranslator;
import edu.asu.lerna.iolaus.domain.implementation.User;
import edu.asu.lerna.iolaus.factory.IUserFactory;
import edu.asu.lerna.iolaus.roles.IRoleName;
import edu.asu.lerna.iolaus.service.IRoleManager;
import edu.asu.lerna.iolaus.service.IUserManager;
import edu.asu.lerna.iolaus.web.usermanagement.backing.ChangePasswdBackingBean;
import edu.asu.lerna.iolaus.web.usermanagement.backing.ModifyUserBackingBean;
/**
 * @description : This controller class would help in delete and edit {@link User} details
 * @author Lohith Dwaraka 
 *
 */
@Controller
public class ModifyUserController {

	private static final Logger logger = LoggerFactory
			.getLogger(ModifyUserController.class);

	@Autowired
	private IUserManager userManager; 

	@Autowired
	private IUserFactory userFactory; 

	@Autowired
	private IRoleManager roleManager;

	@Autowired
	private UserTranslator userTranslator;

	/**
	 * @description : Delete {@link User} from db and it can be accessed by Admin role {@link User} only
	 * @param req
	 * @param model
	 * @param principal
	 * @return
	 */
	@RequestMapping(value = "auth/user/deleteUser", method = RequestMethod.POST)
	public String deleteUser(HttpServletRequest req, ModelMap model,	Principal principal) {
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
		// gets all the User id to be deleted 
		String[] values = req.getParameterValues("selected");
		try{
			// deletes the user one by one
			for(String v : values){
				if(v.equals(principal.getName())){
					logger.info("Seleted User is loggedin, So can't delete");
				}else{
					logger.info(" selected user to be delete : "+ v);
					userManager.deleteUser(v);
				}
			}
		}catch(Exception e){
			logger.error("DB Error :",e);
		}


		return "redirect:/auth/user/listuser";
	}

	/**
	 * @description : Sets up modify user form for UI and it can be access by Admin role {@link User} only
	 * @param userName
	 * @param req
	 * @param model
	 * @param principal
	 * @return
	 */
	@RequestMapping(value = "auth/user/modifyuser/{username}", method = RequestMethod.GET)
	public String modifyUser(@PathVariable("username") String userName,HttpServletRequest req, ModelMap model,Principal principal) {
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
		// get User object using username
		User user = userManager.getUserById(userName);
		// If user not found
		if(user == null){
			model.addAttribute("message","User not found");
			return "auth/resourcenotfound";
		}
		// Prepare user backing bean
		ModifyUserBackingBean mubb = userTranslator.translateModifyUser(user);

		// Send data to jsp
		model.addAttribute("username",userName);
		model.addAttribute("availableRoles", roleManager.getRolesList());
		model.addAttribute("modifyUserBackingBean", mubb);
		return "auth/user/modifyuser";
	}

	/**
	 * @description : Modify user details based on form data and accessed by 
	 * @param userName
	 * @param userForm
	 * @param result
	 * @param model
	 * @param principal
	 * @return
	 */
	@RequestMapping(value = "auth/user/modifyuser/{username}", method = RequestMethod.POST)
	public String updateUser(@PathVariable("username") String userName,@Valid @ModelAttribute ModifyUserBackingBean userForm, BindingResult result, ModelMap model,	Principal principal) {
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
		// Validate user entry
		if (result.hasErrors()) {
			model.addAttribute("availableRoles", roleManager.getRolesList());
			return "auth/user/modifyuser";
		}
		// Validate the user name modification for uniqueness or orginial name
		if(!userName.equals(userForm.getUsername().toLowerCase())){
			User user = userManager.getUserById(userForm.getUsername().toLowerCase());
			if (!(user == null)){
				model.addAttribute("errorMsg", "Username should original or unique one");
				model.addAttribute("availableRoles", roleManager.getRolesList());
				return "auth/user/modifyuser";
			}
		}

		// If user is authorized
		// get User object using username
		User userOld = userManager.getUserById(userName);
		// If user not found
		if(userOld == null){
			model.addAttribute("message","User not found");
			return "auth/resourcenotfound";
		}

		// Create user details for modification in the db
		User user = userFactory.createUser(userForm.getUsername().toLowerCase(), userForm.getName(), userForm.getEmail(), userOld.getPassword(), userForm.getRoles());
		userManager.modifyUser(user, userName);

		return "redirect:/auth/user/listuser";
	}


	@RequestMapping(value = "auth/user/changepasswd/{username}", method = RequestMethod.GET)
	public String changePasswdGet(@PathVariable("username") String userName, ModelMap model,Principal principal) {
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
		// get User object using username
		User user = userManager.getUserById(userName);
		// If user not found
		if(user == null){
			model.addAttribute("message","User not found");
			return "auth/resourcenotfound";
		}

		// Send data to jsp
		model.addAttribute("username",userName);
		model.addAttribute("changePasswdBackingBean", new ChangePasswdBackingBean());
		return "auth/user/changepasswd";
	}


	@RequestMapping(value = "auth/user/changepasswd/{username}", method = RequestMethod.POST)
	public String updateUser(@PathVariable("username") String userName,@Valid @ModelAttribute ChangePasswdBackingBean passForm, BindingResult result, ModelMap model,	Principal principal) {
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
		// Validate user entry
		if (result.hasErrors()) {
			return "auth/user/changepasswd";
		}
		// Validate the user name modification for uniqueness or orginial name
		if(!passForm.getNewpassword().equals(passForm.getRepeatpassword())){
			model.addAttribute("errorMsg", "Repeat password are not similar");
			return "auth/user/changepasswd";
		}


		// get User object using username
		User user = userManager.getUserById(userName);
		// If user not found
		if(user == null){
			model.addAttribute("message","User not found");
			return "auth/resourcenotfound";
		}

		// Prepare user backing bean
		ModifyUserBackingBean mubb = userTranslator.translateModifyUser(user);

		User userPassChange = userFactory.createUser(mubb.getUsername(), mubb.getName(), mubb.getEmail(), passForm.getNewpassword(), mubb.getRoles());
		userManager.modifyUser(userPassChange, userName);

		// Prepare user backing bean
		mubb = userTranslator.translateModifyUser(userPassChange);

		// Send data to jsp
		model.addAttribute("username",userName);
		model.addAttribute("availableRoles", roleManager.getRolesList());
		model.addAttribute("modifyUserBackingBean", mubb);
		return "auth/user/modifyuser";
	}

}

