package edu.asu.lerna.iolaus.domain.implementation;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RoleTest {

	private Role role;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		this.role = new Role();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetId() {
		role.setId(null);
		assertEquals(role.getId(), null);
		
		role.setId("ROLE_ADMIN");
		assertEquals(role.getId(), "ROLE_ADMIN");
	}

	@Test
	public void testSetId() {
		role.setId(null);
		assertEquals(role.getId(), null);
		
		role.setId("ROLE_ADMIN");
		assertEquals(role.getId(), "ROLE_ADMIN");
	}

	@Test
	public void testGetName() {
		role.setName(null);
		assertEquals(role.getName(), null);
		
		role.setName("Lerna User");
		assertEquals(role.getName(), "Lerna User");
	}

	@Test
	public void testSetName() {
		role.setName(null);
		assertEquals(role.getName(), null);
		
		role.setName("Lerna User");
		assertEquals(role.getName(), "Lerna User");
	}

	@Test
	public void testGetDescription() {
		role.setDescription(null);
		assertEquals(role.getDescription(), null);
		
		role.setDescription("A regular user.");
		assertEquals(role.getDescription(), "A regular user.");
	}

	@Test
	public void testSetDescription() {
		role.setDescription(null);
		assertEquals(role.getDescription(), null);
		
		role.setDescription("A regular user.");
		assertEquals(role.getDescription(), "A regular user.");
	}

	@Test
	public void testToString() {
		role.setId(null);
		role.setDescription(null);
		role.setName(null);
		assertEquals(role.toString(), "Role [id=null, name=null, description=null]");
		
		role.setId("ROLE_USER");
		role.setDescription("A regular user.");
		role.setName("Lerna User");
		assertEquals(role.toString(), "Role [id=ROLE_USER, name=Lerna User, description=A regular user.]");
	}

}
