package edu.asu.lerna.iolaus.web.usermanagement;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.lerna.iolaus.domain.implementation.User;
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
	
	@RequestMapping(value = "auth/user/modifyUser", method = RequestMethod.POST)
	public String modifyUser(HttpServletRequest req, ModelMap model,	Principal principal) {

		String[] values = req.getParameterValues("selected");
		
		User user = userManager.getUserById(values[0]);
		UserBackingBean ubb =new UserBackingBean();
		
		ubb.setName(user.getName());
		ubb.setUsername(user.getUsername());
		ubb.setEmail(user.getEmail());
		List<String> roleStrList = new ArrayList<String>();
		List<LernaGrantedAuthority> roleList = user.getAuthorities();
		for(LernaGrantedAuthority l : roleList){
			roleStrList.add(l.getAuthority());
		}
		
		ubb.setRoles(roleStrList);
		model.addAttribute("availableRoles", roleManager.getRolesList());
		model.addAttribute("userBackingBean", new UserBackingBean());
		return "auth/user/modifyuser";


	}

}

