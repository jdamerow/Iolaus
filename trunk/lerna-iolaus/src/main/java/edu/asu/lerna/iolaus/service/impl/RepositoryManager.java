package edu.asu.lerna.iolaus.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.domain.queryobject.INode;
import edu.asu.lerna.iolaus.domain.queryobject.IQuery;
import edu.asu.lerna.iolaus.domain.queryobject.IRelNode;
import edu.asu.lerna.iolaus.domain.queryobject.IRelNodeFinder;
import edu.asu.lerna.iolaus.domain.queryobject.IRelNodeFinderData;
import edu.asu.lerna.iolaus.domain.queryobject.impl.RelNodeFinder;
import edu.asu.lerna.iolaus.domain.queryobject.impl.RelNodeFinderData;
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
		cacheManager.executeQuery("");
	}
	
	@Override
	public void breakdownQuery(IQuery q)
	{
		IRelNodeFinder rnf = new RelNodeFinder();
		INode n = q.getNode();
		//TODO call karan's Node function 
		if(n!=null){
			//TODO: Call Karan's mapping code here
			List<Object> nodeListObject = objectToCypher.objectToCypher(n);
			parseNodeListObject(nodeListObject);
			LinkedHashMap<String, IRelNode> nodeList = new LinkedHashMap<String, IRelNode>();
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
			}
			
			
		}else{
			logger.info("Node is null");
		}
		//throw new NotImplementedException("Not yet implemented");
	}
	
	public void parseNodeListObject(List<Object> nodeListObject){
		logger.info("Printing the node to label from Karan");
		String jsonQuery = (String) nodeListObject.get(0);
		Map<String, Object> labelToObjectMap = (LinkedHashMap<String, Object>) nodeListObject.get(1);
		logger.info("Json Query : "+jsonQuery);
		for (Map.Entry<String, Object> entry : labelToObjectMap.entrySet())
		{
		    System.out.println(entry.getKey() + "             " + entry.getValue().getClass());
		}
	}
	
	@Override
	public void queryToCypher(IQuery q)
	{
		throw new NotImplementedException("Not yet implemented");
	}
	
}
