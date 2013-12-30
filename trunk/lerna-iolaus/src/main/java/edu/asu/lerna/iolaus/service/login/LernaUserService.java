package edu.asu.lerna.iolaus.service.login;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.db.ILoginManager;


@Service(value = "lernaUserService")
public class LernaUserService implements UserDetailsService{
	
	private static final Logger logger = LoggerFactory
			.getLogger(LernaUserService.class);
	
	@Autowired
	private ILoginManager userManager;

	@Override
	public synchronized UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		UserDetails matchingUser = userManager.getUserById(username);
		logger.info("------------------------ came here ");
		if (matchingUser == null) {
			throw new UsernameNotFoundException("Wrong username or password");
		}

		return matchingUser;
	}
	

}
