package edu.asu.lerna.iolaus.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.domain.Label;
import edu.asu.lerna.iolaus.domain.misc.LabelTree;
import edu.asu.lerna.iolaus.domain.misc.ReturnParametersOfOTC;
import edu.asu.lerna.iolaus.domain.queryobject.INode;
import edu.asu.lerna.iolaus.domain.queryobject.IQuery;
import edu.asu.lerna.iolaus.domain.queryobject.IRelNode;
import edu.asu.lerna.iolaus.domain.queryobject.PropertyOf;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Query;
import edu.asu.lerna.iolaus.domain.queryobject.impl.RelNode;
import edu.asu.lerna.iolaus.service.IFragmentQuery;
import edu.asu.lerna.iolaus.service.IObjectToCypher;


/**
 * This class takes a {@link Query} object as an input and breaks it down and creates a tree of cypher queries   
 * @author Karan Kothari
 *
 */
@Service
public class FragmentQuery implements IFragmentQuery {

	@Autowired
	private IObjectToCypher objectToCypher; 
	
	private static final Logger logger = LoggerFactory
			.getLogger(FragmentQuery.class);
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public LabelTree breakdownQuery(IQuery q) {

		INode n = q.getNode();
		//It is the map with key=IRelNode object and value=label. It contains all the pairs which are parsed.
		Map<IRelNode,String> parsedNodeToLabelMap=new LinkedHashMap<IRelNode,String>();
		//It is the map with key=IRelNode object and value=label. It has all such pairs. 
		Map<IRelNode,String> allNodeToLabelMap=new LinkedHashMap<IRelNode,String>();
		//It is a map with key=label used as a source and value=Label .
		Map<String,Label> sourceToTargetLabelMap=new LinkedHashMap<String, Label>();
		//It is map with key=label and value=Json Query
		Map<String,String> targetJsonMap=new LinkedHashMap<String,String>();
		//It is map with key=new unique labels and value=old labels
		Map<String,String> newLabelToOldLabelMap=new LinkedHashMap<String,String>();
		//It is a map with key=Unique label and value=its return value
		Map<String,Boolean> isReturnTrueMap=new HashMap<String,Boolean>();
		
		LabelTree tree=new LabelTree();
		int targetCounter=1;
		int uniqueTargetCounter=1;
		int relationshipCounter=1;
		int counter=0;
		String source="";
		List<IRelNode> keyElements=new ArrayList<IRelNode>();
		if(n!=null){
			ReturnParametersOfOTC nodeListObject = objectToCypher.objectToCypher(n,q.getDataset().getId());
			Map<String,Boolean> isReturnTempMap=nodeListObject.getIsReturnMap();
			source=PropertyOf.SOURCE.toString();
			isReturnTrueMap.put(source, n.isReturn()==null?false:n.isReturn());
			//This loop will be executed for INode object(i.e. for outermost query)
			//This loop will add all the target and relationship labels used in the cypher to isReturnTrueMap
			for(Entry<String,Boolean> entry:isReturnTempMap.entrySet()){
				String label=entry.getKey();
				if(label.contains(PropertyOf.TARGET.toString())){
					isReturnTrueMap.put(PropertyOf.TARGET.toString()+uniqueTargetCounter, entry.getValue());
					uniqueTargetCounter++;
				}else{
					isReturnTrueMap.put(PropertyOf.RELATION.toString()+relationshipCounter, entry.getValue());
					relationshipCounter++;
				}
			}
			
			targetCounter=parseNodeListObject(nodeListObject,allNodeToLabelMap,parsedNodeToLabelMap,sourceToTargetLabelMap,targetJsonMap,newLabelToOldLabelMap,targetCounter,keyElements,source);
			IRelNode relNode=null;
			if(keyElements.size()!=0){//if there are more IRelNode objects having IRelation Object
				while((relNode=keyElements.get(counter++))!=null){
					nodeListObject = objectToCypher.objectToCypher(relNode,q.getDataset().getId());
					isReturnTempMap=nodeListObject.getIsReturnMap();
					source=allNodeToLabelMap.get(relNode);
					parsedNodeToLabelMap.put(relNode,source);
					
					//This loop will add all the target and relationship labels used in the cypher to isReturnTrueMap
					for(Entry<String,Boolean> entry:isReturnTempMap.entrySet()){
						String label=entry.getKey();
						if(label.contains(PropertyOf.TARGET.toString())){
							isReturnTrueMap.put(PropertyOf.TARGET.toString()+uniqueTargetCounter, entry.getValue());
							uniqueTargetCounter++;
						}else{
							isReturnTrueMap.put(PropertyOf.RELATION.toString()+relationshipCounter, entry.getValue());
							relationshipCounter++;
						}
					}
					
					targetCounter=parseNodeListObject(nodeListObject,allNodeToLabelMap,parsedNodeToLabelMap,sourceToTargetLabelMap,targetJsonMap,newLabelToOldLabelMap,targetCounter,keyElements,source);
					if(counter==allNodeToLabelMap.size()){//if all the IRelNode objects are converted into cyphers
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
		tree.setLabelToIsReturnTrueMap(isReturnTrueMap);
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
	@SuppressWarnings("unchecked")
	public int parseNodeListObject(ReturnParametersOfOTC nodeListObject, Map<IRelNode, String> allNodesToLabelMap, Map<IRelNode, String> parsedNodesToLabelMap, Map<String, Label> sourceToTargetLabelMap, Map<String, String> targetJsonMap, Map<String, String> oldLabelToNewLabelMap, int targetCounter, List<IRelNode> nodeList, String source){
		
		String jsonQuery = nodeListObject.getJson();
		Map<Object,String> objectToLabelMap = nodeListObject.getObjectToTargetLabelMap();
		targetJsonMap.put(source, jsonQuery);
		
		List<String> targetLabelList=new ArrayList<String>();
		List<List<String>> labelList=new ArrayList<List<String>>();
		String lastTarget="";
		//This loop will parse all the Objects and the IRelNode objects which has Relationship will be added to allNodesToLabelMap
		for (Map.Entry<Object, String> entry : objectToLabelMap.entrySet()){
		    Object obj=entry.getKey();
		    if(obj instanceof RelNode){
				IRelNode relNode=(IRelNode)obj;
				
		    	if(!parsedNodesToLabelMap.containsKey(relNode)){//if IRelNode is not already parsed
		    		if(!objectToLabelMap.get(relNode).equals(lastTarget)){//if IRelNode Object corresponds to same target label in cypher
		    			targetLabelList=new ArrayList<String>();
		    			labelList.add(targetLabelList);
		    		}
		    		String target=PropertyOf.TARGET.toString()+targetCounter;
		    		targetLabelList.add(target);
		    		oldLabelToNewLabelMap.put(target, objectToLabelMap.get(relNode));
		    		INode node=relNode.getNode();
		    		List<Object> nodeDetails = node.getPropertyOrRelationshipOrAnd();
		    		Iterator<Object> nodeDetailsIterator = nodeDetails.iterator();
		    		while(nodeDetailsIterator.hasNext()){
		    			Object o=nodeDetailsIterator.next();
		    			if(o instanceof JAXBElement){
		    				JAXBElement<?> element = (JAXBElement<Object>) o;
		    				//if IRelNode object has operator(i.e. it has IRelationship object)
		    				if(element.getName().toString().contains("}and")||element.getName().toString().contains("}or")){
		    					allNodesToLabelMap.put(relNode,PropertyOf.TARGET.toString()+targetCounter);
		    					nodeList.add(relNode);
		    					break;//break after parsing one level
		    				}
		    			}	
		    		}
		    		targetCounter+=1;
		    		lastTarget=objectToLabelMap.get(relNode);
		    	}
			}
		}
		Label label=new Label();
		label.setLabel(labelList);
		sourceToTargetLabelMap.put(source, label);
		return targetCounter;
	}

}
