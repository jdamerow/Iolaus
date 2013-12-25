package edu.asu.lerna.iolaus.web.usermanagement;



import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.support.ConversionServiceFactory;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.lerna.iolaus.service.IUserManager;
import edu.asu.lerna.iolaus.service.impl.CypherToJson;
import edu.asu.lerna.iolaus.service.impl.MyService;
import edu.asu.lerna.iolaus.domain.implementation.Role;
import edu.asu.lerna.iolaus.domain.implementation.User;

@Controller
public class ListUserController {

	private static final Logger logger = LoggerFactory
			.getLogger(ListUserController.class);
	

	@Autowired
	private MyService myService;
	
	@Autowired
	private IUserManager userManager; 
	
	@RequestMapping(value = "auth/user/listuser", method = RequestMethod.GET)
	public String getUserList( ModelMap model, Principal principal){
		
		myService.doIt();
 
//		try{
//			String s = "ROLE_USER";
//			Role role = conversionService.convert(s, Role.class) ;
//			logger.info("Role name : "+role.getName());
//		}catch(Exception e ){
//			e.printStackTrace();
//		}
		List<User> users = userManager.getAllUsers();
		for(User user : users){
			logger.info("" + user.getUsername());
		}

		return "auth/user/listuser";
	}
}
