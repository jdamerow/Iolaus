package edu.asu.lerna.iolaus.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

import javax.xml.bind.JAXBElement;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import scala.sys.Prop;

import edu.asu.lerna.iolaus.domain.json.IJsonNode;
import edu.asu.lerna.iolaus.domain.json.IJsonRelation;
import edu.asu.lerna.iolaus.domain.json.impl.JsonNode;
import edu.asu.lerna.iolaus.domain.misc.ReturnElementsOfOTC;
import edu.asu.lerna.iolaus.domain.misc.LabelTree;
import edu.asu.lerna.iolaus.domain.queryobject.INode;
import edu.asu.lerna.iolaus.domain.queryobject.IQuery;
import edu.asu.lerna.iolaus.domain.queryobject.IRelNode;
import edu.asu.lerna.iolaus.domain.queryobject.PropertyOf;
import edu.asu.lerna.iolaus.domain.queryobject.impl.RelNode;
import edu.asu.lerna.iolaus.service.ICacheManager;
import edu.asu.lerna.iolaus.service.IObjectToCypher;
import edu.asu.lerna.iolaus.service.IRepositoryManager;

/**
 * 
 * @author Karan Kothari
 *
 */
@Service
public class RepositoryManager implements IRepositoryManager{

	@Autowired
	private ICacheManager cacheManager;
	
	@Autowired
	private IObjectToCypher objectToCypher; 

	private static final Logger logger = LoggerFactory
			.getLogger(RepositoryManager.class);
	
	
	/**
	 * This method takes the IQuery object and breaks it down into multiple queries. It traverse the labels and execute the queries corresponding to that label.
	 * @param q is a instance of Query.
	 */
	@Override
	public void executeQuery(IQuery q)
	{
		LabelTree tree=breakdownQuery(q);
		traverseLabelsAndExecute(tree);
	}
	
	
	/**
	 * This method takes the IQuery object and breaks it down into multiple queries.
	 * @param q is a instance of Query.
	 * @return the tree which has object of sourceToTargetLabelMap, targetJsonMap and oldLabelToNewLabelMap.
	 */
	public LabelTree breakdownQuery(IQuery q){
		INode n = q.getNode();
		//It is the map with key=IRelNode object and value=label. It contains all the pairs which are parsed.
		Map<IRelNode,String> parsedNodeToLabelMap=new LinkedHashMap<IRelNode,String>();
		//It is the map with key=IRelNode object and value=label. It has all such pairs. 
		Map<IRelNode,String> allNodeToLabelMap=new LinkedHashMap<IRelNode,String>();
		//It is a map with key=label used as a source and value=list of list of the target labels .
		Map<String,List<List<String>>> sourceToTargetLabelMap=new LinkedHashMap<String, List<List<String>>>();
		//It is map with key=label and value=Json Query
		Map<String,String> targetJsonMap=new LinkedHashMap<String,String>();
		//It is map with key=new unique labels and value=old labels
		Map<String,String> newLabelToOldLabelMap=new LinkedHashMap<String,String>();
		
		LabelTree tree=new LabelTree();
		int targetCounter=1;
		int counter=0;
		String source="";
		List<IRelNode> keyElements=new ArrayList<IRelNode>();
		if(n!=null){
			ReturnElementsOfOTC nodeListObject = objectToCypher.objectToCypher(n);
			source=PropertyOf.SOURCE.toString();
			targetCounter=parseNodeListObject(nodeListObject,allNodeToLabelMap,parsedNodeToLabelMap,sourceToTargetLabelMap,targetJsonMap,newLabelToOldLabelMap,targetCounter,keyElements,source);
			IRelNode relNode=null;
			if(keyElements.size()!=0){
				while((relNode=keyElements.get(counter++))!=null){
					nodeListObject = objectToCypher.objectToCypher(relNode);
					source=allNodeToLabelMap.get(relNode);
					parsedNodeToLabelMap.put(relNode,source);
					targetCounter=parseNodeListObject(nodeListObject,allNodeToLabelMap,parsedNodeToLabelMap,sourceToTargetLabelMap,targetJsonMap,newLabelToOldLabelMap,targetCounter,keyElements,source);
					if(counter==allNodeToLabelMap.size()){
						break;
					}
				}
			}
		}else{
			logger.info("Node is null");
		}
		tree.setSourceToTargetLabelMap(sourceToTargetLabelMap);
		tree.setTargetJsonMap(targetJsonMap);
		tree.setOldLabelToNewLabelMap(newLabelToOldLabelMap);
		return tree;
	}
	
	/**
	 * This method parse the objectToTargetLabelMap (member of nodeListObject) and breaks it further if it has any object which has relationship.
	 * @param nodeListObject is a object which has objectToTargetLabelMap and json query.
	 * @param allNodesToLabelMap is the map with key=IRelNode object and value=label. It has all such pairs. 
	 * @param parsedNodesToLabelMap is the map with key=IRelNode object and value=label. It contains all the pairs which are parsed.
	 * @param sourceToTargetLabelMap is a map with key=label used as a source and value=list of list of the target labels.
	 * @param targetJsonMap is map with key=label and value=Json Query.
	 * @param oldLabelToNewLabelMap is map with key=new unique labels and value=old labels.
	 * @param targetCounter is current value of target.
	 * @param nodeList is the list of nodes which are to be break further.
	 * @param source is the label of the source node.
	 * @return the new value of the targetCounter.(When a new target label is found, it's value gets incremented).
	 */
	public int parseNodeListObject(ReturnElementsOfOTC nodeListObject, Map<IRelNode, String> allNodesToLabelMap, Map<IRelNode, String> parsedNodesToLabelMap, Map<String, List<List<String>>> sourceToTargetLabelMap, Map<String, String> targetJsonMap, Map<String, String> oldLabelToNewLabelMap, int targetCounter, List<IRelNode> nodeList, String source){
		
		String jsonQuery = nodeListObject.getJson();
		Map<Object,String> objectToLabelMap = nodeListObject.getObjectToTargetLabelMap();
		targetJsonMap.put(source, jsonQuery);
		logger.info("***********************************\nJson Query : "+jsonQuery+"\n***********************************\n");
		List<String> subProblemList=new ArrayList<String>();
		List<List<String>> problemList=new ArrayList<List<String>>();
		String lastTarget="";
		for (Map.Entry<Object, String> entry : objectToLabelMap.entrySet()){
		    Object obj=entry.getKey();
		    if(obj instanceof RelNode){
				IRelNode relNode=(IRelNode)obj;
		    	if(!parsedNodesToLabelMap.containsKey(relNode)){
		    		if(!objectToLabelMap.get(relNode).equals(lastTarget)){
		    			subProblemList=new ArrayList<String>();
		    			problemList.add(subProblemList);
		    		}
		    		String target=PropertyOf.TARGET.toString()+targetCounter;
		    		subProblemList.add(target);
		    		oldLabelToNewLabelMap.put(target, objectToLabelMap.get(relNode));
		    		INode node=relNode.getNode();
		    		List<Object> nodeDetails = node.getPropertyOrRelationshipOrAnd();
		    		Iterator<Object> nodeDetailsIterator = nodeDetails.iterator();
		    		while(nodeDetailsIterator.hasNext()){
		    			Object o=nodeDetailsIterator.next();
		    			if(o instanceof JAXBElement){
		    				JAXBElement<?> element = (JAXBElement<Object>) o;
		    				if(element.getName().toString().contains("}and")||element.getName().toString().contains("}or")){
		    					allNodesToLabelMap.put(relNode,PropertyOf.TARGET.toString()+targetCounter);
		    					nodeList.add(relNode);
		    					break;
		    				}
		    			}	
		    		}
		    		targetCounter+=1;
		    		lastTarget=objectToLabelMap.get(relNode);
		    	}
			}
		}
		sourceToTargetLabelMap.put(source, problemList);
		return targetCounter;
	}
	
	/**
	 * 
	 * @param results is the List of Rows sent by the executeQuery
	 * @return the map whose key is label used in the query and value is the List of IJsonNode or IJsonRelation.
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, List<Object>> processResults(List<List> results) {
		//key->label used in the query   value->List of IJsonNode or IJsonRelatoin
		Map<String, List<Object>> processedResults=new LinkedHashMap<String,List<Object>>();
		String target=PropertyOf.TARGET.toString();
		String rel=PropertyOf.RELATION.toString();
		List<Object> column;
		if(results != null)
		{
			for(List rowList: results)
			{
				int targetCount=1;
				int relationshipCount=1;
				boolean flag=true;
				for(Object obj: rowList)
				{
					
					if(obj instanceof IJsonNode)
					{
						IJsonNode jsonNode = (IJsonNode) obj;
						String current;
						if(flag){
							current=PropertyOf.SOURCE.toString();
							flag=false;
						}else{
							current=target+targetCount;
							targetCount++;
						}
						if(!processedResults.containsKey(current)){
							column=new LinkedList<Object>();
							processedResults.put(current, column);
						}
						else{
							column=processedResults.get(current);
						}
						column.add(jsonNode);
					}
					else if(obj instanceof IJsonRelation)
					{
						IJsonRelation jsonRelation = (IJsonRelation) obj;
						if(!processedResults.containsKey(rel+relationshipCount)){
							column=new LinkedList<Object>();
							processedResults.put(rel+relationshipCount, column);
						}
						else{
							column=processedResults.get(rel+relationshipCount);
						}
						column.add(jsonRelation);
						relationshipCount++;
					}
				}
			}
		}
	
		return processedResults;
	}

	
	/**
	 * It is a method which traverse the labels in post order. Then, Executes the query associated with each label.
	 * @param tree is a object of LabelTree which has three members 1. sourceToTargetLabelMap 2. targetToJsonMap 3. oldLabelToNewLabelMap
	 */
	public void traverseLabelsAndExecute(LabelTree tree) {
		
		Map<String,List<List<String>>> sourceToTargetLabelMap=tree.getSourceToTargetLabelMap();
		Map<String,String> targetLabelToJsonMap=tree.getTargetJsonMap();
		Map<String,String> oldLabelToNewLabelMap=tree.getOldLabelToNewLabelMap();
		
		Map<String,List<Integer>> currentTargetLabelCounter=new HashMap<String, List<Integer>>();
		Map<String,Map<String,List<Object>>>aggregatedResults=new HashMap<String, Map<String,List<Object>>>();
		initializeCurrentTargetLabelCounter(currentTargetLabelCounter,sourceToTargetLabelMap);
		Stack<String> stack=new Stack<String>();
		String sourceLabel=PropertyOf.SOURCE.toString();
		System.out.println(sourceToTargetLabelMap);
		int outerCount=0;
		int innerCount=0;
		boolean flag=true;
		do{
			while(flag){
				if(!sourceToTargetLabelMap.containsKey(sourceLabel)){
					System.out.println("***"+sourceLabel);
					break;
				}
				innerCount=currentTargetLabelCounter.get(sourceLabel).get(1);
				outerCount=currentTargetLabelCounter.get(sourceLabel).get(0);
				
				pushLabelsToStack(sourceToTargetLabelMap,sourceLabel,innerCount,outerCount,stack);
				changeTargetLabelCounts(sourceToTargetLabelMap,currentTargetLabelCounter,sourceLabel,innerCount,outerCount);
				
				stack.push(sourceLabel);
				sourceLabel=sourceToTargetLabelMap.get(sourceLabel).get(outerCount).get(innerCount);
			}
			sourceLabel=stack.pop();
			if(areMoreLabels(currentTargetLabelCounter,sourceLabel)){
				String parent=sourceLabel;
				sourceLabel=stack.pop();
				stack.push(parent);
				flag=true;
			}else{
				String json=targetLabelToJsonMap.get(sourceLabel);
				List<List> results=cacheManager.executeQuery(json, null);
				Map<String,List<Object>> processedResults=processResults(results);
				aggregateResults(aggregatedResults,processedResults,sourceToTargetLabelMap,oldLabelToNewLabelMap,sourceLabel);
				System.out.println(sourceLabel);
				flag=false;
			}
		}while(!stack.isEmpty());
	}
	
	/**
	 * This method initialize the list corresponding to keys of currentTargetLabelMap with 0. 
	 * @param currentTargetLabelMap is a map with key=label(source of a query) and value is list of the counts of target labels.
	 * @param sourceToTargetLabelMap is a map with key=label used as a source and value=list of list of the target labels.
	 */
	public void initializeCurrentTargetLabelCounter(Map<String, List<Integer>> currentTargetLabelMap,
			Map<String, List<List<String>>> sourceToTargetLabelMap) {
		
		for(Entry<String, List<List<String>>> entry:sourceToTargetLabelMap.entrySet()){
			List<Integer> currentCountList=new ArrayList<Integer>();
			currentCountList.add(0);
			currentCountList.add(0);
			currentTargetLabelMap.put(entry.getKey(), currentCountList);
		}
	}
	
	/**
	 * 
	 * @param currentTargetLabelCounter is a map with key=label(source of a query) and value=list of the counts of target labels.
	 * @param sourceLabel is the label of the source node in the query.
	 * @return true if any label for the source is yet to be processed else return false. 
	 */
	public boolean areMoreLabels(
			Map<String, List<Integer>> currentTargetLabelCounter,
			String sourceLabel) {
		return currentTargetLabelCounter.get(sourceLabel).get(0)!=-1;
	}

	/**
	 * This method increment the value of the list corresponding to keys of currentTargetLabelMap by 1.
	 * @param sourceToTargetLabelMap is a map whose key is label used as a source and value is list of the target labels.
	 * @param currentTargetLabelCounter is a map whose key is label(source of a query) and value is list of the counts of target labels.
	 * @param sourceLabel is the label of the source node in the query.
	 * @param innerCount is count of the labels corresponding to same target.
	 * @param outerCount is count of the target labels in a single query.
	 */
	public void changeTargetLabelCounts(Map<String, List<List<String>>> sourceToTargetLabelMap,Map<String, List<Integer>> currentTargetLabelCounter,
			String sourceLabel, int innerCount, int outerCount) {
		
		if(sourceToTargetLabelMap.get(sourceLabel).get(outerCount).size()-1>=innerCount+1){
			currentTargetLabelCounter.get(sourceLabel).set(1, innerCount+1);
		}
		else if(sourceToTargetLabelMap.get(sourceLabel).size()-1>outerCount+1) {
			currentTargetLabelCounter.get(sourceLabel).set(0, outerCount+1);
			currentTargetLabelCounter.get(sourceLabel).set(1, 0);
		}else{
			currentTargetLabelCounter.get(sourceLabel).set(0, -1);
			currentTargetLabelCounter.get(sourceLabel).set(1, -1);
		}
		
	}
	
	/**
	 * This method pushes the labels into the stack.
	 * @param sourceToTargetLabelMap is a map with key=label used as a source and value=list of list of the target labels. 
	 * @param sourceLabel is the label of the source node in the query
	 * @param innerCount is count of the labels corresponding to same target
	 * @param outerCount is count of the target labels in a single query
	 * @param stack is stack where labels are pushed.
	 */
	public void pushLabelsToStack(Map<String, List<List<String>>> sourceToTargetLabelMap,String sourceLabel, int innerCount, int outerCount,Stack<String> stack) {
		
		int i=0;
		for(List<String >targetLabelList:sourceToTargetLabelMap.get(sourceLabel)){
			int j=0;
			for(String labelsOfSameTarget:targetLabelList){
				if(i!=outerCount){
					if(sourceToTargetLabelMap.containsKey(labelsOfSameTarget)){
						stack.push(labelsOfSameTarget);
					}
				}else{
					if(j!=innerCount){
						if(sourceToTargetLabelMap.containsKey(labelsOfSameTarget)){
							stack.push(labelsOfSameTarget);
						}
					}
				}
				j++;
			}
			i++;
		}
	}

	/**
	 * This method aggregates the results of a source label once we have results for all the target labels.
	 * @param aggregatedResults is a map which stores the results for source labels in a query. Key is Label and 
	 * 		  value is Map with key=label used in query and value=IJsonNode or IJsonRelation.
	 * @param processedResults is a map which stores result for a sourceLabel. key=label used in query and value=IJsonNode or IJsonRelation.
	 * @param sourceToTargetLabelMap is a map whose key is label used as a source and value is list of the target labels.
	 * @param oldLabelToNewLabelMap is a map with key=new unique labels and value=labels used in query. 
	 * @param sourceLabel is a label corresponding to the source label of the processedResults.
	 */
	public void aggregateResults(
			Map<String, Map<String, List<Object>>> aggregatedResults,
			Map<String, List<Object>> processedResults,
			Map<String, List<List<String>>> sourceToTargetLabelMap, Map<String, String> oldLabelToNewLabelMap, String sourceLabel) {
		
			Map<Integer,Map<String,List<Object>>> resultsOfTargets=new LinkedHashMap<Integer,Map<String, List<Object>>>();
			int outerForCounter=0;
			for(List<String> targetsOfSameSource:sourceToTargetLabelMap.get(sourceLabel)){
				for(String sameTargets:targetsOfSameSource){
					if(sourceToTargetLabelMap.containsKey(sameTargets)){
						unionOfResults(aggregatedResults,resultsOfTargets,outerForCounter,sameTargets);
					}	
				}
				outerForCounter++;
			}
			Map<String, List<Object>> sourceLabelResults=new LinkedHashMap<String, List<Object>>();
			intersectionOfResultsWithSourceQuery(sourceLabelResults,processedResults,resultsOfTargets,oldLabelToNewLabelMap,sourceToTargetLabelMap,sourceLabel);	
	}
	

	/**
	 * This methods takes the union of the results of the labels corresponding to same target label.
	 * @param aggregateResults is a map which stores results for each label which has a relation. 
	 * 		  key=unique label, value=Map(key=labels used in the query, value=List of IJsonNode or IJsonRelation.
	 * @param tempResults is map with key= count of the target labels used in the query and value is the map with 
	 * 		  key=unique label, value=Map(key=labels used in the query, value=List of IJsonNode or IJsonRelation.
	 * @param currentTarget is the counter of the current target label.
	 * @param targetLabel results of this label will take part the union.
	 */
	public void unionOfResults(
			Map<String, Map<String, List<Object>>> aggregateResults,
			Map<Integer, Map<String, List<Object>>> tempResults,
			int currentTarget, String targetLabel) {
		
		Map<String, List<Object>> childResult;
		if(!tempResults.containsKey(currentTarget)){
			tempResults.put(currentTarget, aggregateResults.get(targetLabel));
		}else{
			childResult=tempResults.get(currentTarget);
			Map<String,List<Object>> siblingResult=aggregateResults.get(targetLabel);
			Iterator<Entry<String, List<Object>>> itr = siblingResult.entrySet().iterator();
			Map<Integer,Iterator<Object>> iterator=new HashMap<Integer,Iterator<Object>>();
			String[] labels=new String[siblingResult.size()];
			int i=0;
			while(itr.hasNext()){
				 Map.Entry<String,List<Object>> pairs = (Map.Entry<String,List<Object>>)itr.next();
				 List<Object> column=(List<Object>) pairs.getValue();
				 labels[i]=pairs.getKey();
				 iterator.put(i++, column.iterator());
			}
			List<Object> sourceColumn;
			while(iterator.get(0).hasNext()){
				for(i=0;i<siblingResult.size();i++){
					sourceColumn=childResult.get(labels[i]);
					Object value = iterator.get(i).next();
					sourceColumn.add(value);
				 }
			}
		}	
	}
	
	/**
	 * This method takes the intersection of the results of all the targets having relations with the processed results. 
	 * @param sourceQueryResults is a map which has result of the intersection
	 * @param processedResults is a map which has results of a sourceLabel. Its key=label used in query and value=IJsonNode or IJsonRelation.
	 * @param resultsOfTargets is a map which is union of the results of all the target labels.
	 * @param oldLabelToNewLabelMap is a map with key=new unique labels and value=labels used in query. 
	 * @param sourceToTargetLabelMap is a map whose key is label used as a source and value is list of the target labels.
	 * @param sourceLabel is a label corresponding to the source label of the processedResults
	 */
	public void intersectionOfResultsWithSourceQuery(
			Map<String, List<Object>> sourceQueryResults,Map<String, List<Object>> processedResults,Map<Integer, Map<String, List<Object>>> resultsOfTargets,
			Map<String, String> oldLabelToNewLabelMap, Map<String, List<List<String>>> sourceToTargetLabelMap, String sourceLabel) {
		
		
		Map<Integer,Map<String, List<Object>>>intermediateResults=new LinkedHashMap<Integer,Map<String, List<Object>>>();
		intermediateResults.put(0, processedResults);
		
		for(int loopCounter=0;loopCounter<resultsOfTargets.size();loopCounter++){
			
			Map<String,List<Object>> currentIterationResults=new LinkedHashMap<String,List<Object>>();
			intermediateResults.put(loopCounter+1, currentIterationResults);
			Iterator<Entry<String, List<Object>>> intermediateResultsIterator = intermediateResults.get(loopCounter).entrySet().iterator();
			Iterator<Entry<String, List<Object>>> tempResultsIterator = resultsOfTargets.get(loopCounter).entrySet().iterator();
			Map<Integer,Iterator<Object>> iterator=new HashMap<Integer,Iterator<Object>>();
			int targetCount=1;
			int relationshipCount=1;
			String[] labels=new String[intermediateResults.get(loopCounter).size()];	
			int i=0;
			boolean flag=true;
			String targetNode=sourceToTargetLabelMap.get(sourceLabel).get(loopCounter).get(0);
			String oldTargetMapping=oldLabelToNewLabelMap.get(targetNode);
			int targetId=0;
			//This loop creates labels for the results of source query
			while(intermediateResultsIterator.hasNext()){
				 Map.Entry<String,List<Object>> pairs = (Map.Entry<String,List<Object>>)intermediateResultsIterator.next();
				 List<Object> column=(List<Object>) pairs.getValue();
				 String label="";
				 if(column.get(0) instanceof JsonNode){
					 if(flag){
						 flag=false;
						 label=PropertyOf.SOURCE.toString();
					 }else{
						 label=PropertyOf.TARGET.toString()+targetCount;
						 if(label.equals(oldTargetMapping)){
							 targetId=i;
						 }
						 targetCount++;
					 }
				 }else{
					 label=PropertyOf.RELATION.toString()+relationshipCount;
					 relationshipCount++;
				 }
				 labels[i]=label;
				 currentIterationResults.put(label, new LinkedList<Object>());
				 iterator.put(i++, column.iterator());
			}
			
			int startOfTempResults=i;
	
			//This loop creates labels for the results of Target query
			while(tempResultsIterator.hasNext()){
				 Map.Entry<String,List<Object>> pairs = (Map.Entry<String,List<Object>>)tempResultsIterator.next();
				 List<Object> column=(List<Object>) pairs.getValue();
				 String label="";
				 if(column.get(0) instanceof JsonNode){
					 if(flag){
						 flag=false;
						 label=PropertyOf.SOURCE.toString();
					 }else{
						 label=PropertyOf.TARGET.toString()+targetCount;
						 currentIterationResults.put(label, new LinkedList<Object>());
						 targetCount++;
					 }
				 }else{
					 label=PropertyOf.RELATION.toString()+relationshipCount;
					 currentIterationResults.put(label, new LinkedList<Object>());
					 relationshipCount++;
				 }
				 labels[i]=label;
				 iterator.put(i++, column.iterator());
			}
			
			List<Object> row;
			while(iterator.get(targetId).hasNext()){
				row=new LinkedList<Object>();
				createRow(row,iterator,startOfTempResults);
				JsonNode node=(JsonNode)row.get(targetId);
				String id=node.getId();
				Map<Integer,List<Object>> matchedRows = new LinkedHashMap<Integer, List<Object>>();
				getMatchedRows(matchedRows,iterator,id,startOfTempResults,labels);
				reinitializeIterators(iterator,resultsOfTargets.get(loopCounter),startOfTempResults);
				cartesianProduct(row,matchedRows,startOfTempResults,currentIterationResults,labels);	
			}
		}
		
	}
	
	/**
	 * This method create a single instance of the result by iterating from 0 to  startOfTempResults.
	 * @param row is the single instance of the result.
	 * @param iterator is the Iterator which iterates from 0 to startOfTempResults.  
	 * @param lastColumn is the upper bound for the iterator to iterate.
	 */
	public void createRow(List<Object> row,
			Map<Integer, Iterator<Object>> iterator, int lastColumn) {
		for(int i=0;i<lastColumn;i++){
			row.add(iterator.get(i).next());
		}
	}
	
	/**
	 * This method stores all the instances into matchedRows which matches with the id.
	 * @param matchedRows is the map of the rows matched with the id.
	 * @param iterator is the Iterator which iterates from startOfTempResults to the size of the labels  
	 * @param id is the node id of the IJsonNode
	 * @param startOfTempResults is the lower bound for the iterator to iterate through.
	 * @param labels is array of string(labels) which are unique labels
	 */
	public void getMatchedRows(Map<Integer, List<Object>> matchedRows,
			Map<Integer, Iterator<Object>> iterator, String id,
			int startOfTempResults, String[] labels) {
		int rowCount=0;
		while(iterator.get(startOfTempResults).hasNext()){
			JsonNode node=(JsonNode) iterator.get(startOfTempResults).next();
			List<Object> newRow=null;
			boolean flag=false;
			if(node.getId().equals(id)){
				newRow=new LinkedList<Object>();
				matchedRows.put(rowCount++, newRow);
				flag=true;
			}	
			for(int j=startOfTempResults+1;j<labels.length;j++){
				Object obj=iterator.get(j).next();
				if(flag){
					newRow.add(obj);
				}
			}
		}
	}
	
	/**
	 * This method takes the Cartesian product of the row with the matchedRows. 
	 * @param row is a single instance of the result.
	 * @param matchedRows is the map of the rows with key=counter(integer) and value=row.
	 * @param lastColumn is the upper bound for the iteration.
	 * @param currentResults is the result of inersection of current target with the processedResults. (Output of this method).
	 * @param labels is array of string(labels) which are unique labels.
	 */
	public void cartesianProduct(List<Object> row,
			Map<Integer, List<Object>> matchedRows, int lastColumn,
			Map<String, List<Object>> currentResults, String[] labels) {
		if(matchedRows.size()!=0){
			for(int x=0;x<matchedRows.size();x++){
				int j=0;
				for(;j<lastColumn;j++){
					currentResults.get(labels[j]).add(row.get(j));
				}
				j=lastColumn+1;
				for(Object obj:matchedRows.get(x)){
					currentResults.get(labels[j++]).add(obj);	
				}
			}
		}	
	}
	
	/**
	 * This method reinitialize the iterators
	 * @param iterator is the Iterator which iterates from startOfTempResults to the size of the labels.
	 * @param resultsOfTargets is a map which has the results of current target label. key=label used in query and value=IJsonNode or IJsonRelation 
	 * @param startOfTempResults is the lower bound for the iterator to iterate through.
	 */
	public void reinitializeIterators(
			Map<Integer, Iterator<Object>> iterator,
			Map<String, List<Object>> resultsOfTargets, int startOfTempResults) {
		for(Entry<String, List<Object>> entry:resultsOfTargets.entrySet()){
			iterator.put(startOfTempResults++, entry.getValue().iterator());
		}
	}
		
}
