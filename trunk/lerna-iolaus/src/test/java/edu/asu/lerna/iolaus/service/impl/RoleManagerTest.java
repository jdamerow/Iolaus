package edu.asu.lerna.iolaus.service.impl;

import static org.junit.Assert.assertEquals;

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
import edu.asu.lerna.iolaus.service.IRoleManager;

@ContextConfiguration(locations={
"file:src/test/resources/root-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class RoleManagerTest {

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
	public void testGetRoles() {
		Role [] roles =roleManager.getRoles();
		
		Role roleUser = roles[0];
		Role roleAdmin = roles[1];
		
		assertEquals(roleUser.getId() ,"ROLE_USER" );
		assertEquals(roleUser.getDescription() ,"A regular user." );
		assertEquals(roleUser.getName() ,"Lerna User" );
		
		assertEquals(roleAdmin.getId() ,"ROLE_ADMIN" );
		assertEquals(roleAdmin.getDescription() ,"Lerna Administrator to add neo4j instance and users." );
		assertEquals(roleAdmin.getName() ,"Lerna Admin" );
		
			
	}

	@Test
	public void testGetRole() {
		Role roleAdmin = roleManager.getRole("ROLE_ADMIN");
		
		assertEquals(roleAdmin.getId() ,"ROLE_ADMIN" );
		assertEquals(roleAdmin.getDescription() ,"Lerna Administrator to add neo4j instance and users." );
		assertEquals(roleAdmin.getName() ,"Lerna Admin" );
	}

	@Test
	public void testGetRolesList() {
		List<Role> roleList =roleManager.getRolesList();
		
		Role roleUser = roleList.get(0);
		Role roleAdmin = roleList.get(1);
		
		assertEquals(roleUser.getId() ,"ROLE_USER" );
		assertEquals(roleUser.getDescription() ,"A regular user." );
		assertEquals(roleUser.getName() ,"Lerna User" );
		
		assertEquals(roleAdmin.getId() ,"ROLE_ADMIN" );
		assertEquals(roleAdmin.getDescription() ,"Lerna Administrator to add neo4j instance and users." );
		assertEquals(roleAdmin.getName() ,"Lerna Admin" );
	}

}
