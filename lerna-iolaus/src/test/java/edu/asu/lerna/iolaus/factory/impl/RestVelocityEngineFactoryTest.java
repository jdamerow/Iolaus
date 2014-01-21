package edu.asu.lerna.iolaus.factory.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.asu.lerna.iolaus.factory.IRestVelocityEngineFactory;

@ContextConfiguration(locations={
"file:src/test/resources/root-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class RestVelocityEngineFactoryTest {

	@Autowired
	private IRestVelocityEngineFactory restVelocityEngineFactory;
	
	private MockHttpServletRequest req;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		req=new MockHttpServletRequest();
		req.setServerName("iolaus");
		req.setServerPort(8080);
		req.setContextPath("iolaus/auth");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void getVelocityEngineTest() throws FileNotFoundException, IOException {
		VelocityEngine velocityEngine = restVelocityEngineFactory.getVelocityEngine(req);
		assertEquals((velocityEngine!=null),true);
	}
	
	@Test
	public void getVelocityContextTest() throws FileNotFoundException, IOException {
		VelocityEngine velocityEngine = restVelocityEngineFactory.getVelocityEngine(req);
		VelocityContext context = restVelocityEngineFactory.getVelocityContext();
		assertEquals((context!=null),true);
	}

}
