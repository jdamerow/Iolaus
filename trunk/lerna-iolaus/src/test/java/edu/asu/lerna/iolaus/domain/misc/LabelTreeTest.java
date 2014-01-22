package edu.asu.lerna.iolaus.domain.misc;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.asu.lerna.iolaus.domain.Label;

public class LabelTreeTest {
	
	private LabelTree labelTree;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		labelTree=new LabelTree();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetLabelToIsReturnTrueMap() {
		labelTree.setLabelToIsReturnTrueMap(null);
		assertEquals(labelTree.getLabelToIsReturnTrueMap(), null);
		
		Map<String,Boolean> labelToIsReturnTrueMap=new HashMap<String,Boolean>();
		
		labelTree.setLabelToIsReturnTrueMap(labelToIsReturnTrueMap);
		assertEquals(labelTree.getLabelToIsReturnTrueMap(), labelToIsReturnTrueMap);
	}

	@Test
	public void testSetLabelToIsReturnTrueMap() {
		labelTree.setLabelToIsReturnTrueMap(null);
		assertEquals(labelTree.getLabelToIsReturnTrueMap(), null);

		Map<String,Boolean> labelToIsReturnTrueMap=new HashMap<String,Boolean>();
		
		labelTree.setLabelToIsReturnTrueMap(labelToIsReturnTrueMap);
		assertEquals(labelTree.getLabelToIsReturnTrueMap(), labelToIsReturnTrueMap);
	}

	@Test
	public void testGetSourceToTargetLabelMap() {
		labelTree.setSourceToTargetLabelMap(null);
		assertEquals(labelTree.getSourceToTargetLabelMap(), null);
		
		Map<String,Label> sourceToTargetLabelMap=new HashMap<String,Label>();
		
		labelTree.setSourceToTargetLabelMap(sourceToTargetLabelMap);
		assertEquals(labelTree.getSourceToTargetLabelMap(), sourceToTargetLabelMap);
	}

	@Test
	public void testSetSourceToTargetLabelMap() {
		labelTree.setSourceToTargetLabelMap(null);
		assertEquals(labelTree.getSourceToTargetLabelMap(), null);
		
		Map<String,Label> sourceToTargetLabelMap=new HashMap<String,Label>();
		
		labelTree.setSourceToTargetLabelMap(sourceToTargetLabelMap);
		assertEquals(labelTree.getSourceToTargetLabelMap(), sourceToTargetLabelMap);
	}

	@Test
	public void testGetTargetJsonMap() {
		labelTree.setTargetJsonMap(null);
		assertEquals(labelTree.getTargetJsonMap(), null);
		
		Map<String,String> targetJsonMap=new HashMap<String,String>();
		
		labelTree.setTargetJsonMap(targetJsonMap);
		assertEquals(labelTree.getTargetJsonMap(), targetJsonMap);
	}

	@Test
	public void testSetTargetJsonMap() {
		labelTree.setTargetJsonMap(null);
		assertEquals(labelTree.getTargetJsonMap(), null);
		
		Map<String,String> targetJsonMap=new HashMap<String,String>();
		
		labelTree.setTargetJsonMap(targetJsonMap);
		assertEquals(labelTree.getTargetJsonMap(), targetJsonMap);
	}

	@Test
	public void testGetOldLabelToNewLabelMap() {
		labelTree.setOldLabelToNewLabelMap(null);
		assertEquals(labelTree.getOldLabelToNewLabelMap(), null);
		
		Map<String,String> oldLabelToNewLabelMap=new HashMap<String,String>();
		
		labelTree.setOldLabelToNewLabelMap(oldLabelToNewLabelMap);
		assertEquals(labelTree.getOldLabelToNewLabelMap(), oldLabelToNewLabelMap);
	}

	@Test
	public void testSetOldLabelToNewLabelMap() {
		labelTree.setOldLabelToNewLabelMap(null);
		assertEquals(labelTree.getOldLabelToNewLabelMap(), null);
		
		Map<String,String> oldLabelToNewLabelMap=new HashMap<String,String>();
		
		labelTree.setOldLabelToNewLabelMap(oldLabelToNewLabelMap);
		assertEquals(labelTree.getOldLabelToNewLabelMap(), oldLabelToNewLabelMap);
	}

}
