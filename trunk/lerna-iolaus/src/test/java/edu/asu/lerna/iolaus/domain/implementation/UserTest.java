package edu.asu.lerna.iolaus.domain.implementation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.asu.lerna.iolaus.service.login.LernaGrantedAuthority;

public class UserTest {

	private User user;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		user = new User();
		List<LernaGrantedAuthority> authorityList = new ArrayList<LernaGrantedAuthority>();
		user.setAuthorities(authorityList);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetUsername() {
		user.setUsername(null);
		assertEquals(user.getUsername(),null);
		
		user.setUsername("lernauser");
		assertEquals(user.getUsername(),"lernauser");
	}

	@Test
	public void testSetUsername() {
		user.setUsername(null);
		assertEquals(user.getUsername(),null);
		
		user.setUsername("lernauser");
		assertEquals(user.getUsername(),"lernauser");
	}

	@Test
	public void testGetName() {
		user.setName(null);
		assertEquals(user.getName(),null);
		
		user.setName("lerna");
		assertEquals(user.getName(),"lerna");
	}

	@Test
	public void testSetName() {
		user.setName(null);
		assertEquals(user.getName(),null);
		
		user.setName("lerna");
		assertEquals(user.getName(),"lerna");
	}

	@Test
	public void testGetEmail() {
		user.setEmail(null);
		assertEquals(user.getEmail(),null);
		
		user.setEmail("lerna@iolaus.com");
		assertEquals(user.getEmail(), "lerna@iolaus.com");
	}

	@Test
	public void testSetEmail() {
		user.setEmail(null);
		assertEquals(user.getEmail(),null);
		
		user.setEmail("lerna@iolaus.com");
		assertEquals(user.getEmail(), "lerna@iolaus.com");
	}

	@Test
	public void testGetPassword() {
		user.setPassword(null);
		assertEquals(user.getPassword(),null);
		
		user.setPassword("admin");
		assertEquals(user.getPassword(), "admin");
	}

	@Test
	public void testAddAuthority() {
		
		LernaGrantedAuthority auth = new LernaGrantedAuthority("");
		user.addAuthority(auth);
		assertEquals(user.getAuthorities().get(0).getAuthority(),"");
		

		LernaGrantedAuthority auth1 = new LernaGrantedAuthority("test");
		user.addAuthority(auth1);
		assertEquals(user.getAuthorities().get(1).getAuthority(),"test");
	}

	@Test
	public void testSetPassword() {
		user.setPassword(null);
		assertEquals(user.getPassword(),null);
		
		user.setPassword("admin");
		assertEquals(user.getPassword(), "admin");
	}

	@Test
	public void testGetAuthorities() {
		List<LernaGrantedAuthority> authorityList = new ArrayList<LernaGrantedAuthority>();
		user.setAuthorities(authorityList);
		assertEquals(user.getAuthorities().size(),0);
	}

	@Test
	public void testSetAuthorities() {
		List<LernaGrantedAuthority> authorityList = new ArrayList<LernaGrantedAuthority>();
		user.setAuthorities(authorityList);
		assertEquals(user.getAuthorities().size(),0);
	}

	@Test
	public void testIsAccountNonExpired() {
		assertEquals(user.isAccountNonExpired(),true);
	}

	@Test
	public void testIsAccountNonLocked() {
		assertEquals(user.isAccountNonLocked(),true);
	}

	@Test
	public void testIsCredentialsNonExpired() {
		assertEquals(user.isCredentialsNonExpired(),true);
	}

	@Test
	public void testIsEnabled() {
		assertEquals(user.isEnabled(),true);;
	}

}
