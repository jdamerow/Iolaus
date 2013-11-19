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
		//TODO: Break the query into smaller parts.
		breakdownQuery(q);
		//TODO: Create cypher for each sub-query
		
		//TODO: Execute cypher by calling the cache manager
		//cacheManager.executeQuery("");
	}
	
	@Override
	public void breakdownQuery(IQuery q)
	{
		INode n = q.getNode();
		Map<IRelNode,String> parsedElements=new LinkedHashMap<IRelNode,String>();
		Map<IRelNode,String> allElements=new LinkedHashMap<IRelNode,String>();
		Map<String,List<List<String>>> nestedProblemMap=new HashMap<String, List<List<String>>>();
		Map<String,String> targetJsonMap=new HashMap<String,String>();
		int targetCounter=1;
		int counter=0;
		String source="";
		List<IRelNode> keyElements=new ArrayList<IRelNode>();
		if(n!=null){
			List<Object> nodeListObject = objectToCypher.objectToCypher(n);
			source=PropertyOf.SOURCE.toString();
			targetCounter=parseNodeListObject(nodeListObject,allElements,parsedElements,nestedProblemMap,targetJsonMap,targetCounter,keyElements,source);
			IRelNode relNode=null;
			if(keyElements.size()!=0){
				while((relNode=keyElements.get(counter++))!=null){
					nodeListObject = objectToCypher.objectToCypher(relNode);
					source=allElements.get(relNode);
					parsedElements.put(relNode,source);
					targetCounter=parseNodeListObject(nodeListObject,allElements,parsedElements,nestedProblemMap,targetJsonMap,targetCounter,keyElements,source);
					if(counter==allElements.size()){
						break;
					}
				}
			}
			/*LinkedHashMap<String, IRelNode> nodeList = new LinkedHashMap<String, IRelNode>();
			IRelNodeFinderData rnfd = new RelNodeFinderData();
			rnfd.setNodeList(nodeList);
			rnfd.setPath("");
			
			rnfd = rnf.getNodeRel(n,rnfd);
			nodeList = rnfd.getNodeList();
			logger.info("Node List "+rnfd.getNodeList().size());
			for (final String key : nodeList.keySet()) {
				IRelNode rn= nodeList.get(key);
				//
				List<Object> nodeListObject1 = objectToCypher.objectToCypher(rn);
				parseNodeListObject(nodeListObject1);
				if(rn!=null){
					logger.info("IRelNode is not empty");
				}
				logger.info("key : "+key);
			}*/
			
			
		}else{
			logger.info("Node is null");
		}
		//throw new NotImplementedException("Not yet implemented");
	}
	
	public int parseNodeListObject(List<Object> nodeListObject, Map<IRelNode, String> allElements, Map<IRelNode, String> parsedElements, Map<String, List<List<String>>> nestedProblemMap, Map<String, String> targetJsonMap, int targetCounter, List<IRelNode> keyElements, String source){
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
		    		subProblemList.add(PropertyOf.TARGET.toString()+targetCounter);
		    		System.out.println(PropertyOf.TARGET.toString()+"->"+targetCounter+objectToLabelMap.get(relNode));
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
