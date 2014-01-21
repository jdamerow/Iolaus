package edu.asu.lerna.iolaus.factory.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.asu.lerna.iolaus.domain.implementation.Role;
import edu.asu.lerna.iolaus.domain.implementation.User;
import edu.asu.lerna.iolaus.factory.IUserFactory;
import edu.asu.lerna.iolaus.service.IRoleManager;

@ContextConfiguration(locations={
"file:src/test/resources/root-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class UserFactoryTest {

	@Autowired
	private IUserFactory userFactory;
	
	@Autowired
	private IRoleManager roleManager;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreateUserStringStringStringStringListOfRole() {
		Role role = roleManager.getRole("ROLE_USER");
		List<Role> roleList = new ArrayList<Role>();
		roleList.add(role);
		User user = userFactory.createUser("lernauser", "lerna", "lerna@iolaus.com", "test", roleList);
		assertEquals(user.getEmail(),"lerna@iolaus.com");
		assertEquals(user.getUsername(),"lernauser");
		assertEquals(user.getName(), "lerna");
		assertEquals(user.getAuthorities().get(0).getAuthority(), "ROLE_USER");
		
	}

	@Test
	public void testCreateUserStringStringStringStringRoleArray() {
		Role role = roleManager.getRole("ROLE_USER");
		Role roleArr [] = new Role[1];
		roleArr[0] = role;
		User user = userFactory.createUser("lernauser", "lerna", "lerna@iolaus.com", "test", roleArr);
		assertEquals(user.getEmail(),"lerna@iolaus.com");
		assertEquals(user.getUsername(),"lernauser");
		assertEquals(user.getName(), "lerna");
		assertEquals(user.getAuthorities().get(0).getAuthority(), "ROLE_USER");
	}

	@Test
	public void testEncrypt() {
		String encodedStr = userFactory.encrypt("something");
		assertEquals(encodedStr.length(),60);
	}

}
