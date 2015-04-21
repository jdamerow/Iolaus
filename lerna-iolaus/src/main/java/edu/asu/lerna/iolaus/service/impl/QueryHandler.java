package edu.asu.lerna.iolaus.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.configuration.neo4j.impl.Neo4jRegistry;
import edu.asu.lerna.iolaus.domain.INeo4jInstance;
import edu.asu.lerna.iolaus.domain.Label;
import edu.asu.lerna.iolaus.domain.json.IJsonNode;
import edu.asu.lerna.iolaus.domain.json.IJsonRelation;
import edu.asu.lerna.iolaus.domain.json.impl.Nodes;
import edu.asu.lerna.iolaus.domain.json.impl.ReferenceNode;
import edu.asu.lerna.iolaus.domain.json.impl.Resultset;
import edu.asu.lerna.iolaus.domain.json.impl.Row;
import edu.asu.lerna.iolaus.domain.json.impl.Rows;
import edu.asu.lerna.iolaus.domain.json.impl.StepNodes;
import edu.asu.lerna.iolaus.domain.misc.LabelTree;
import edu.asu.lerna.iolaus.domain.misc.ResultSet;
import edu.asu.lerna.iolaus.domain.queryobject.IQuery;
import edu.asu.lerna.iolaus.domain.queryobject.PropertyOf;
import edu.asu.lerna.iolaus.service.IAggregateResult;
import edu.asu.lerna.iolaus.service.IFragmentQuery;
import edu.asu.lerna.iolaus.service.IQueryHandler;
import edu.asu.lerna.iolaus.service.IRepositoryManager;

/**
 * 
 * @author Karan Kothari
 *
 */
@Service
public class QueryHandler implements IQueryHandler{

	@Autowired
	private IRepositoryManager repositoryManager;

	
	@Autowired
	private IFragmentQuery fragmentQuery;
	
	@Autowired
	private IAggregateResult aggregateResults;
	
	@Autowired
	private Neo4jRegistry registry;
	
	@Qualifier("specific")
	@Autowired
	private XMLToCypherConverter xmlToCypherConverter;
	
	@Qualifier("default")
	@Autowired
	private XMLToCypherConverterForMultipleDataset xmlToCypherConverterForMultipleDataset;

	private static final Logger logger = LoggerFactory
			.getLogger(QueryHandler.class);
	
	
	/**
	 * This method takes the IQuery object and breaks it down into multiple queries. It traverse the labels and execute the queries corresponding to that label.
	 * @param q is a instance of Query.
	 */
	@Override
	public ResultSet executeQuery(IQuery q)
	{
		if(q==null)
			return null;
		LabelTree tree=fragmentQuery.breakdownQuery(q);
		List<String> dbInstances=new ArrayList<String>();
		List<String> dbList =  q.getDatabaseList();
		if(dbList==null)
			return null;
		Iterator<String> dbIterator = dbList.iterator();
		while(dbIterator.hasNext()){
			dbInstances.add(dbIterator.next());//add a dbInstance to the List;
		}
		
		if(dbInstances.size()==0){
			for(INeo4jInstance instance:registry.getfileList()){
				dbInstances.add(instance.getId());
			}
		}
		
		Map<String,List<Object>> resultSet;
		//if cypher query doesn't have target node the "if" statement will get executed otherwise "else" statement 
		if(tree.getSourceToTargetLabelMap().get(PropertyOf.SOURCE.toString()).getLabel().size()!=0){
			resultSet=traverseLabelsAndExecute(tree,dbInstances);
			
		}
		else{ 
			List<List<Object>> results=repositoryManager.executeQuery(tree.getTargetJsonMap().get(PropertyOf.SOURCE.toString()),dbInstances);
			resultSet=processResults(results);
		}
		ResultSet rset=new ResultSet();
		rset.setResultSet(resultSet);
		rset.setLabelToIsReturnTrueMap(tree.getLabelToIsReturnTrueMap());
		return rset;
	}
	
	@Override
	public Resultset executeStableQuery(IQuery q) {
		if(q==null)
			return null;
		
		List<List<Object>> finalResults = new ArrayList<List<Object>>();
		
		for(String id : q.getDatabaseList()) {
			String cypherJson = null;
			if(!q.getDataset().getId().equals("default")) {
				cypherJson = xmlToCypherConverter.createStableQuery(q.getNode(), q.getDataset().getId(), registry.getNodeIndexName(id));
			} else {
				cypherJson = xmlToCypherConverterForMultipleDataset.createStableQuery(q.getNode(), q.getDataset().getId(), registry.getNodeIndexName(id));
			}
			List<List<Object>> results = repositoryManager.executeQuery(cypherJson, id);
			if(results != null) {
				finalResults.addAll(results);
			}
		}
		
		return processMappingResults(finalResults);
	}
	
	/*private Map<String, List<Element>> processMappingResults(
			List<List<Object>> results) {
		//key->label used in the query   value->List of IJsonNode or IJsonRelatoin
		Map<String, List<Element>> processedResults=new LinkedHashMap<String,List<Element>>();
		String target=PropertyOf.TARGET.toString();
		String rel=PropertyOf.RELATION.toString();
		Map<String, StepNodes> stepNodeMap = new LinkedHashMap<String, StepNodes>();
		if(results != null)
		{
			for(List<Object> rowList: results)
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
						if(flag){//first object will always be "source" for each row
							current=PropertyOf.SOURCE.toString();
							flag=false;//flag=false after creating key for first label in each row
						}else{
							current=target+targetCount;
							targetCount++;
						}
						
						StepNodes stepNodes = null;
						String mappingKey = jsonNode.getData().get("iolaus-mapping-key");
						if(!stepNodeMap.containsKey(mappingKey)){
							stepNodes = new StepNodes();
							stepNodeMap.put(mappingKey, stepNodes);
						} else{
							stepNodes = stepNodeMap.get(current);
						}
						
						stepNodes.addNode(jsonNode);
						
						if(!processedResults.containsKey(current)) {
							processedResults.put(current, new LinkedList<Element>());
						}
						
						processedResults.get(current).add(stepNodes);
						
					}
					else if(obj instanceof IJsonRelation) {
						IJsonRelation jsonRelation = (IJsonRelation) obj;
						if(!processedResults.containsKey(rel+relationshipCount)){
							processedResults.put(rel+relationshipCount, new LinkedList<Element>());
						}
										
						processedResults.get(rel+relationshipCount).add((JsonRelation)jsonRelation);
						relationshipCount++;
					}
				}
			}
		}
	
		return processedResults;
	}
*/
	
	
	private Resultset processMappingResults(List<List<Object>> finalResults) {
		
		Resultset resultset = null;
		Nodes nodes = null;
		Rows rows = null;
		
		if(finalResults != null) {
			resultset = new Resultset();
			nodes = new Nodes();
			rows = new Rows();
			resultset.setNodes(nodes);
			resultset.setRows(rows);
			Map<String, String> idMap = new HashMap<String, String>(); 
			Map<String, StepNodes> stepNodeMap = new LinkedHashMap<String, StepNodes>();
			int idCounter = 0;
			for(List<Object> line : finalResults) {
				Row row = new Row();
				for(Object obj : line) {
					if(obj instanceof IJsonNode) {
						IJsonNode jsonNode = (IJsonNode) obj;
						StepNodes stepNodes = null;
						String mappingKey = jsonNode.getData().get("iolausMappingId");
						if(!stepNodeMap.containsKey(mappingKey)){
							stepNodes = new StepNodes();
							stepNodeMap.put(mappingKey, stepNodes);
							stepNodes.setId(String.valueOf(idCounter++));
							nodes.addStepNode(stepNodes);
						} else{
							stepNodes = stepNodeMap.get(mappingKey);
						}
						
						stepNodes.addNode(jsonNode);
						idMap.put(jsonNode.getId(), stepNodes.getId());
						ReferenceNode referenceNode = new ReferenceNode();
						referenceNode.setId(stepNodes.getId());
						row.addReferenceNode(referenceNode);
					}
				}
				for(Object obj : line) {
					if(obj instanceof IJsonRelation) {
						IJsonRelation jsonRelation = (IJsonRelation) obj;
						if(idMap.containsKey(jsonRelation.getStartNode())) {
							jsonRelation.setStartNode(idMap.get(jsonRelation.getStartNode()));
						} else {
							System.out.println("Id not found");
						}
						
						if(idMap.containsKey(jsonRelation.getEndNode())) {
							jsonRelation.setEndNode(idMap.get(jsonRelation.getEndNode()));
						} else {
							System.out.println("Id not found");
						}
						
						row.addRelation(jsonRelation);
					}
				}
				rows.addRow(row);
			}
		}
		return resultset;
	}

	/**
	 * This method process the results and convert rows into column
	 * @param results is the List of Rows sent by the executeQuery
	 * @return the map whose key is label used in the query and value is the List of IJsonNode or IJsonRelation.
	 */
	private Map<String, List<Object>> processResults(List<List<Object>> results) {
		//key->label used in the query   value->List of IJsonNode or IJsonRelatoin
		Map<String, List<Object>> processedResults=new LinkedHashMap<String,List<Object>>();
		String target=PropertyOf.TARGET.toString();
		String rel=PropertyOf.RELATION.toString();
		List<Object> column;
		if(results != null)
		{
			for(List<Object> rowList: results)
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
						if(flag){//first object will always be "source" for each row
							current=PropertyOf.SOURCE.toString();
							flag=false;//flag=false after creating key for first label in each row
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
	 * This method traverse the labels the in post order. Then, Executes the query associated with each label.
	 * @param tree is a object of LabelTree which has three members 1. sourceToTargetLabelMap 2. targetToJsonMap 3. oldLabelToNewLabelMap
	 * @param dbInstances is the list of id's of Neo4j instances where queries are to be executed
	 * @return the final result of the query
	 */
	private Map<String, List<Object>> traverseLabelsAndExecute(LabelTree tree, List<String> dbInstances) {
		
		Map<String,Label> sourceToTargetLabelMap=tree.getSourceToTargetLabelMap();
		Map<String,String> targetLabelToJsonMap=tree.getTargetJsonMap();
		Map<String,String> oldLabelToNewLabelMap=tree.getOldLabelToNewLabelMap();
		
		Map<String,List<Integer>> currentTargetLabelCounter=new HashMap<String, List<Integer>>();
		Map<String,Map<String,List<Object>>>aggregatedResults=new HashMap<String, Map<String,List<Object>>>();
		initializeCurrentTargetLabelCounter(currentTargetLabelCounter,sourceToTargetLabelMap);
		Stack<String> stack=new Stack<String>();
		String sourceLabel=PropertyOf.SOURCE.toString();

		int outerCount=0;
		int innerCount=0;
		boolean flag=true;
		do{
			while(flag){
				if(!sourceToTargetLabelMap.containsKey(sourceLabel)){//if last label in nested queries
					logger.debug("***"+sourceLabel);
					break;
				}
				innerCount=currentTargetLabelCounter.get(sourceLabel).get(1);//counter for the labels of same target node
				outerCount=currentTargetLabelCounter.get(sourceLabel).get(0);//counter of the label of the targets of the relation
				
				pushLabelsToStack(sourceToTargetLabelMap,sourceLabel,innerCount,outerCount,stack);
				changeTargetLabelCounts(sourceToTargetLabelMap,currentTargetLabelCounter,sourceLabel,innerCount,outerCount);
				
				stack.push(sourceLabel);
				sourceLabel=sourceToTargetLabelMap.get(sourceLabel).getLabel().get(outerCount).get(innerCount);
			}
			sourceLabel=stack.pop();
			if(!stack.isEmpty()&&areMoreLabels(currentTargetLabelCounter,sourceLabel)){//if some target labels of a query are yet to be added to the stack
				String parent=sourceLabel;
				sourceLabel=stack.pop();
				stack.push(parent);
				flag=true;
			}else{
				String json=targetLabelToJsonMap.get(sourceLabel);
				List<List<Object>> results=repositoryManager.executeQuery(json, dbInstances);
				Map<String,List<Object>> processedResults=processResults(results);
				if(processedResults != null && processedResults.size() != 0)
					aggregateResults.aggregateResults(aggregatedResults,processedResults,sourceToTargetLabelMap,oldLabelToNewLabelMap,sourceLabel);
				flag=false;
			}
		}while(!stack.isEmpty());
		return aggregatedResults.get(PropertyOf.SOURCE.toString());
	}
	


	/**
	 * This method initialize the list corresponding to keys of currentTargetLabelMap with 0. 
	 * @param currentTargetLabelMap is a map with key=label(source of a query) and value is list of the counts of target labels.
	 * @param sourceToTargetLabelMap is a map with key=label used as a source and value=list of list of the target labels.
	 */
	private void initializeCurrentTargetLabelCounter(Map<String, List<Integer>> currentTargetLabelMap,
			Map<String, Label> sourceToTargetLabelMap) {
		//This loop will initialize all the labels having other target labels to 0 
		for(Entry<String, Label> entry:sourceToTargetLabelMap.entrySet()){
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
	private boolean areMoreLabels(
			Map<String, List<Integer>> currentTargetLabelCounter,
			String sourceLabel) {
		return currentTargetLabelCounter.get(sourceLabel).get(0)!=-1;
	}

	/**
	 * This method increment the value of the list corresponding to keys of currentTargetLabelMap by 1.
	 * @param sourceToTargetLabelMap is a map whose key is label used as a source and value is list of the target labels.
	 * @param currentTargetLabelCounter is a map whose key is label(source of a query) and value target label.
	 * @param sourceLabel is the label of the source node in the query.
	 * @param innerCount is count of the labels corresponding to same target.
	 * @param outerCount is count of the target labels in a single query.
	 */
	private void changeTargetLabelCounts(Map<String, Label> sourceToTargetLabelMap,Map<String, List<Integer>> currentTargetLabelCounter,
			String sourceLabel, int innerCount, int outerCount) {
		
		if(sourceToTargetLabelMap.get(sourceLabel).getLabel().get(outerCount).size()-1>=innerCount+1){//if all the labels corresponding to same target are added to the stack 
			currentTargetLabelCounter.get(sourceLabel).set(1, innerCount+1);
		}
		else if(sourceToTargetLabelMap.get(sourceLabel).getLabel().size()-1>=outerCount+1) {//if all the target labels of a query are added to the stack
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
	private void pushLabelsToStack(Map<String, Label> sourceToTargetLabelMap,String sourceLabel, int innerCount, int outerCount,Stack<String> stack) {
		
		int i=0;
		for(List<String> targetLabelList: sourceToTargetLabelMap.get(sourceLabel).getLabel()){
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

}
