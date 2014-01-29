package edu.asu.lerna.iolaus.service.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.asu.lerna.iolaus.domain.Label;
import edu.asu.lerna.iolaus.domain.json.IJsonNode;
import edu.asu.lerna.iolaus.domain.json.impl.JsonNode;

@ContextConfiguration(locations={"file:src/test/resources/root-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class AggregateResultTest {

	@Autowired
	private AggregateResult aggregateResult;
	

	@Test
	public void testAggregateResults() {
		
		Map<String, Map<String, List<Object>>> aggregatedResults=null;
		Map<String, List<Object>> processedResults=null;
		Map<String, Label> sourceToTargetLabelMap=null;
		Map<String, String> oldLabelToNewLabelMap=null;
		String sourceLabel=null;
		
		//check with all arguments=null
		aggregateResult.aggregateResults(aggregatedResults, processedResults, sourceToTargetLabelMap, oldLabelToNewLabelMap, sourceLabel);
		assertEquals(aggregatedResults, null);
		
		aggregatedResults=new LinkedHashMap<String, Map<String,List<Object>>>();
		
		//check with empty aggregatedResults and everything else with null
		aggregateResult.aggregateResults(aggregatedResults, processedResults, sourceToTargetLabelMap, oldLabelToNewLabelMap, sourceLabel);
		assertEquals(aggregatedResults.size(), 0);
		
		//check with empty parameters
		processedResults=new LinkedHashMap<String, List<Object>>();
		sourceToTargetLabelMap=new HashMap<String, Label>();
		oldLabelToNewLabelMap=new HashMap<String, String>();
		sourceLabel="source";
		
		assertEquals(aggregatedResults.size(), 0);
		
		Label label=new Label();
		List<List<String>> labelList=new ArrayList<List<String>>();
		List<String> targetLabelList=new ArrayList<String>();
		targetLabelList.add("target1");
		labelList.add(targetLabelList);
		targetLabelList=new ArrayList<String>();
		targetLabelList.add("target2");
		labelList.add(targetLabelList);
		label.setLabel(labelList);
		sourceToTargetLabelMap.put("source", label);
		
		label=new Label();
		labelList=new ArrayList<List<String>>();
		targetLabelList=new ArrayList<String>();
		targetLabelList.add("target3");
		labelList.add(targetLabelList);
		label.setLabel(labelList);
		sourceToTargetLabelMap.put("target1", label);
		
		oldLabelToNewLabelMap.put("source", "source");
		oldLabelToNewLabelMap.put("target1", "target1");
		oldLabelToNewLabelMap.put("target2", "target2");
		oldLabelToNewLabelMap.put("target3", "target1");
		
		sourceLabel="source";
		
		Map<String, List<Object>> results=new LinkedHashMap<String, List<Object>>();
		List<Object> column=new ArrayList<Object>();
		for(int i=0;i<10;i++){
			IJsonNode node=new JsonNode();
			node.setId(String.valueOf(i));
			column.add(node);
		}
		results.put("source", column);
		column=new ArrayList<Object>();
		for(int i=0;i<10;i++){
			IJsonNode node=new JsonNode();
			node.setId(String.valueOf(i));
			column.add(node);
		}
		results.put("target1", column);
		aggregatedResults.put("target1", results);
		
		column=new ArrayList<Object>();
		for(int i=5;i<10;i++){
			IJsonNode node=new JsonNode();
			node.setId(String.valueOf(i));
			column.add(node);
		}
		processedResults.put("source",column);
		
		aggregateResult.aggregateResults(aggregatedResults, processedResults, sourceToTargetLabelMap, oldLabelToNewLabelMap, sourceLabel);
		
		//test if size is increased if only two column in target nodes.
		assertEquals(aggregatedResults.get("source").size(),2);
		//check the size is results of intersection
		assertEquals(aggregatedResults.get("source").get("target1").size(),5);
		
	}

	@Test
	public void testReinitializeIterators() {
		Map<Integer, Iterator<Object>> iteratorMap=new HashMap<Integer, Iterator<Object>>();
		Map<String, List<Object>> resultsOfTargets=new LinkedHashMap<String,List<Object>>(); 
		int startOfTempResults=2;
		for(int i=0;i<startOfTempResults;i++){
			List<Object> list=new ArrayList<Object>();
			list.add(1);
			iteratorMap.put(i, list.iterator());
		}
		for(int i=startOfTempResults;i<5;i++){
			List<Object> list=new ArrayList<Object>();
			list.add(1);
			list.add(2);
			resultsOfTargets.put("X"+i, list);
			Iterator<Object> itr=list.iterator();
			while(itr.hasNext())
				itr.next();
			iteratorMap.put(i, itr);
		}
		
		//checking size of iterator before and after call to the method.
		int size=iteratorMap.size();
		aggregateResult.reinitializeIterators(iteratorMap, resultsOfTargets, startOfTempResults);
		assertEquals("Size of Iterator Changed", size, iteratorMap.size());
		
		//Check if iterators prior to the startOfTempResults are not getting changed
		for(int i=0;i<startOfTempResults;i++){
			if(!iteratorMap.get(i).hasNext()){
				fail("Test case failed");
			}
		}
		//Check if iterators startOfTempResults onwards are not getting changed
		for(int i=startOfTempResults;i<5;i++){
			if(!iteratorMap.get(i).hasNext()){
				fail("Test case failed");
			}
		}
		
		
		//Execute method on null resultsOfTargets
		Map<String, List<Object>> resultsOfTargetsNull=null;
		aggregateResult.reinitializeIterators(iteratorMap, resultsOfTargetsNull, startOfTempResults);
		assertEquals("resultsOfTargetsNull is not Null",resultsOfTargetsNull, null);
		
		//Execute method on null iteratorMap
		iteratorMap=null;
		aggregateResult.reinitializeIterators(iteratorMap, resultsOfTargets, startOfTempResults);
		assertEquals("Iterator Map is not Null",iteratorMap, null);
		
		//Execute method on empty iteratorMap
		iteratorMap=new HashMap<Integer, Iterator<Object>>();
		aggregateResult.reinitializeIterators(iteratorMap, resultsOfTargets, startOfTempResults);
		assertEquals("Iterator Map is not of the size of the resultsOFTargets",iteratorMap.size(),resultsOfTargets.size());
	}

}
