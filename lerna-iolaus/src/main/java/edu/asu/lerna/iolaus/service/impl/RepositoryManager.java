package edu.asu.lerna.iolaus.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBElement;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.domain.queryobject.INode;
import edu.asu.lerna.iolaus.domain.queryobject.IQuery;
import edu.asu.lerna.iolaus.domain.queryobject.IRelNode;
import edu.asu.lerna.iolaus.domain.queryobject.PropertyOf;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Node;
import edu.asu.lerna.iolaus.domain.queryobject.impl.RelNode;
import edu.asu.lerna.iolaus.service.ICacheManager;
import edu.asu.lerna.iolaus.service.IObjectToCypher;
import edu.asu.lerna.iolaus.service.IRepositoryManager;

@Service
public class RepositoryManager implements IRepositoryManager{

	@Autowired
	private ICacheManager cacheManager;
	
	@Autowired
	private IObjectToCypher objectToCypher; 

	private static final Logger logger = LoggerFactory
			.getLogger(RepositoryManager.class);
	
	@Override
	public void executeQuery(IQuery q)
	{
		List<Object> treeStructure=breakdownQuery(q);
		cacheManager.executeQuery(null);
		//TODO: Execute cypher by calling the cache manager
		//cacheManager.executeQuery("");
	}
	/**
	 * @author Karan
	 * It takes IQuery as a input parameter and breaks down a single queries in multiple queries.
	 * targetJsonMap is a mapping of target label with its json query.
	 * oldLabelToNewLableMap is a mapping of the labels used in the query with unique labels created by parseNodeListObject.
	 * nestedProblemMap is mapping of target label with the targets of their nested jsons.
	 * It returns tree structure which is required for the aggregating results.
	 **/
	@Override
	public List<Object> breakdownQuery(IQuery q){
		INode n = q.getNode();
		Map<IRelNode,String> parsedElements=new LinkedHashMap<IRelNode,String>();
		Map<IRelNode,String> allElements=new LinkedHashMap<IRelNode,String>();
		Map<String,List<List<String>>> nestedProblemMap=new LinkedHashMap<String, List<List<String>>>();
		Map<String,String> targetJsonMap=new LinkedHashMap<String,String>();
		Map<String,String> oldLabelToNewLabelMap=new LinkedHashMap<String,String>();
		List<Object> treeStructure=new ArrayList<Object>();
		int targetCounter=1;
		int counter=0;
		String source="";
		List<IRelNode> keyElements=new ArrayList<IRelNode>();
		if(n!=null){
			List<Object> nodeListObject = objectToCypher.objectToCypher(n);
			source=PropertyOf.SOURCE.toString();
			targetCounter=parseNodeListObject(nodeListObject,allElements,parsedElements,nestedProblemMap,targetJsonMap,oldLabelToNewLabelMap,targetCounter,keyElements,source);
			IRelNode relNode=null;
			if(keyElements.size()!=0){
				while((relNode=keyElements.get(counter++))!=null){
					nodeListObject = objectToCypher.objectToCypher(relNode);
					source=allElements.get(relNode);
					parsedElements.put(relNode,source);
					targetCounter=parseNodeListObject(nodeListObject,allElements,parsedElements,nestedProblemMap,targetJsonMap,oldLabelToNewLabelMap,targetCounter,keyElements,source);
					if(counter==allElements.size()){
						break;
					}
				}
			}
		}else{
			logger.info("Node is null");
		}
		treeStructure.add(nestedProblemMap);
		treeStructure.add(targetJsonMap);
		treeStructure.add(oldLabelToNewLabelMap);
		return treeStructure;
	}
	
	
	/**
	 * @author Karan
	 * This method parse the results of objectToCypher method. It returns the list of two elements. First is json and second is objectToLabelMap.
	 * It just considers the entry with key of type IRelNode and ignores everything else
	 **/
	public int parseNodeListObject(List<Object> nodeListObject, Map<IRelNode, String> allElements, Map<IRelNode, String> parsedElements, Map<String, List<List<String>>> nestedProblemMap, Map<String, String> targetJsonMap, Map<String, String> oldLabelToNewLabelMap, int targetCounter, List<IRelNode> keyElements, String source){
		String jsonQuery = (String) nodeListObject.get(0);
		Map<Object,String> objectToLabelMap = (LinkedHashMap<Object,String>) nodeListObject.get(1);
		targetJsonMap.put(source, jsonQuery);
		logger.info("***********************************\nJson Query : "+jsonQuery+"\n***********************************\n");
		List<String> subProblemList=new ArrayList<String>();
		List<List<String>> problemList=new ArrayList<List<String>>();
		String lastTarget="";
		for (Map.Entry<Object, String> entry : objectToLabelMap.entrySet()){
		    Object obj=entry.getKey();
		    if(obj instanceof RelNode){
				IRelNode relNode=(IRelNode)obj;
		    	if(!parsedElements.containsKey(relNode)){
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
		    					allElements.put(relNode,PropertyOf.TARGET.toString()+targetCounter);
		    					keyElements.add(relNode);
		    					break;
		    				}
		    			}	
		    		}
		    		targetCounter+=1;
		    		lastTarget=objectToLabelMap.get(relNode);
		    	}
			}
		}
		nestedProblemMap.put(source, problemList);
		return targetCounter;
	}
	
	@Override
	public void queryToCypher(IQuery q)
	{
		throw new NotImplementedException("Not yet implemented");
	}
	
}
