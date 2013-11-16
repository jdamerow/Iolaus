package edu.asu.lerna.iolaus.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.domain.queryobject.INode;
import edu.asu.lerna.iolaus.domain.queryobject.IQuery;
import edu.asu.lerna.iolaus.domain.queryobject.IRelNode;
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
		//TODO call karan's Node function 
		List<IRelNode> parsedElements=new ArrayList<IRelNode>();
		List<IRelNode> allElements=new ArrayList<IRelNode>();
		int counter=0;
		if(n!=null){
			//TODO: Call Karan's mapping code here
			List<Object> nodeListObject = objectToCypher.objectToCypher(n);
			parseNodeListObject(nodeListObject,allElements,parsedElements);
			IRelNode relNode=null;
			if(allElements.size()!=0){
				while((relNode=allElements.get(counter++))!=null){
					nodeListObject = objectToCypher.objectToCypher(relNode);
					parsedElements.add(relNode);
					parseNodeListObject(nodeListObject,allElements,parsedElements);
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
				//TODO: call karan's mapping code for RELNODE
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
	
	public void parseNodeListObject(List<Object> nodeListObject, List<IRelNode> allElements, List<IRelNode> parsedElements){
		String jsonQuery = (String) nodeListObject.get(0);
		Map<Object,String> labelToObjectMap = (LinkedHashMap<Object,String>) nodeListObject.get(1);
		logger.info("***********************************\nJson Query : "+jsonQuery+"\n***********************************\n");
		for (Map.Entry<Object, String> entry : labelToObjectMap.entrySet()){
		    Object obj=entry.getKey();
			if(obj instanceof RelNode){
				IRelNode relNode=(IRelNode)obj;
		    	if(!parsedElements.contains(relNode)){
		    		INode node=relNode.getNode();
		    		List<Object> nodeDetails = node.getPropertyOrRelationshipOrAnd();
		    		Iterator<Object> nodeDetailsIterator = nodeDetails.iterator();
		    		while(nodeDetailsIterator.hasNext()){
		    			Object o=nodeDetailsIterator.next();
		    			if(o instanceof JAXBElement){
		    				JAXBElement<?> element = (JAXBElement<Object>) o;
		    				if(element.getName().toString().contains("}and")||element.getName().toString().contains("}or")){
		    					allElements.add(relNode);
		    					break;
		    				}
		    			}	
		    		}
		    	}
				
			}
		}
	}
	
	@Override
	public void queryToCypher(IQuery q)
	{
		throw new NotImplementedException("Not yet implemented");
	}
	
}
