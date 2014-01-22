package edu.asu.lerna.iolaus.domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LabelTest {
	
	private Label label;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		label=new Label();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetLabel() {
		label.setLabel(null);
		assertEquals(label.getLabel(), null);
		
		List<List<String>> labels=new ArrayList<List<String>>();
		
		label.setLabel(labels);
		assertEquals(label.getLabel(), labels);
	}

	@Test
	public void testSetLabel() {
		
		label.setLabel(null);
		assertEquals(label.getLabel(), null);
		
		List<List<String>> labels=new ArrayList<List<String>>();
		
		label.setLabel(labels);
		assertEquals(label.getLabel(), labels);
	}

}
