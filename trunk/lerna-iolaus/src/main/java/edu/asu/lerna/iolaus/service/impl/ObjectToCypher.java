package edu.asu.lerna.iolaus.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.domain.queryobject.INode;
import edu.asu.lerna.iolaus.domain.queryobject.IOperator;
import edu.asu.lerna.iolaus.domain.queryobject.IProperty;
import edu.asu.lerna.iolaus.domain.queryobject.IQuery;
import edu.asu.lerna.iolaus.domain.queryobject.IRelNode;
import edu.asu.lerna.iolaus.domain.queryobject.IRelationship;
import edu.asu.lerna.iolaus.domain.queryobject.PropertyOf;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Node;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Operator;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Property;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Relationship;
import edu.asu.lerna.iolaus.service.IObjectToCypher;
@Service
public class ObjectToCypher implements IObjectToCypher {
	
	private static final Logger logger = LoggerFactory
			.getLogger(Node.class);

	
	private int currentTarget=0;
	private int currentRelationship=0;
	
	private String start="Start ";
	private String match="Match ";
	private String where="Where ";
	private String ret="return ";
	
	String sourceOperator="";
	String targetOperator="";
	
	Map<String,String> startMap = null;
	Map<String,List<String>> whereMap=null;
	Set<String> matchSet=null;
	
	@Override
	public String objectToCypher(INode node) {
		
		nodeObject(node,PropertyOf.SOURCE);
		logger.info("Start="+start);
		logger.info("Match="+match);
		printWhere();
		return null;
	}

	private void printWhere() {
		Map<String,List<String>> whereMap=getWhereMap();
		for (Entry<String, List<String>> entry : whereMap.entrySet()) {
			
		    logger.info(entry.getKey()+"----"+entry.getValue());
		}
	}

	@Override
	public void nodeObject(INode node,PropertyOf propertyOf) {
		logger.info("Node return status : "+node.isReturn());
		List<Object> nodeDetails = node.getPropertyOrRelationshipOrAnd();
		Iterator<Object> nodeDetailsIterator = nodeDetails.iterator();
		while(nodeDetailsIterator.hasNext()){
			JAXBElement<?> element = (JAXBElement<Object>) nodeDetailsIterator.next();

			if(element.getName().toString().contains("}and")){
				setSourceOperator("and");
				IOperator opAnd = (IOperator) element.getValue();
				operatorObject(opAnd,propertyOf);
				
			}else if(element.getName().toString().contains("}or")){
				setSourceOperator("or");
				IOperator opOr = (IOperator) element.getValue();
				operatorObject(opOr,propertyOf);
			}else if(element.getName().toString().contains("}Property")){
    			IProperty prop = (IProperty) element.getValue();
    			propertyObject(prop,propertyOf);
    		}
		}
		
	}

	@Override
	public void relationshipObject(IRelationship relationship) {
		logger.info("Relationship return status : "+relationship.isReturn());
		incrementRelationship();
		incrementTarget();
		List<Object> relationshipDetails = relationship.getSourceOrTargetOrProperty();
		Iterator<Object> relationshipDetailsIterator = relationshipDetails.iterator();
		while(relationshipDetailsIterator.hasNext()){

			Object element =relationshipDetailsIterator.next();
    		if(element instanceof Property){
    			IProperty prop = (IProperty) element;
    			propertyObject(prop,PropertyOf.RELATION);
    		}else if(element instanceof JAXBElement<?>){
    			JAXBElement<?> element1 = (JAXBElement<?>) element;
    			if(element1.getName().toString().contains("}or")){
    				setTargetOperator("or");
    				IOperator opOr = (IOperator) element1.getValue();
    				operatorObject(opOr,PropertyOf.RELATION);
    			}
    			
    			String node1=(PropertyOf.SOURCE).toString();
    			String node2=(PropertyOf.TARGET).toString()+currentTarget;
    			String relation=(PropertyOf.RELATION).toString()+currentRelationship;
    			if(element1.getName().toString().contains("}target")){
    				boolean direction=true;
    				addRelationToMatch(node1, node2, relation, direction);
        			IRelNode relNode = (IRelNode) element1.getValue();
        			relNodeObject(relNode,PropertyOf.TARGET);
    			}
    			
    			if(element1.getName().toString().contains("}source")){
    				boolean direction=false;
    				addRelationToMatch(node1, node2, relation, direction);
        			IRelNode relNode = (IRelNode) element1.getValue();
        			relNodeObject(relNode,PropertyOf.TARGET);
    			}	
    		}
		}	
	}

	@Override
	public void operatorObject(IOperator op,PropertyOf propertyOf) {
    	List<Object> objectList = op.getSourceOrTargetOrProperty();
    	Iterator<Object> operatorIterator = objectList.iterator();
    	while(operatorIterator.hasNext()){
    		Object element =operatorIterator.next();
    		if(element instanceof Property){
    			IProperty prop = (IProperty) element;
    			propertyObject(prop,propertyOf);
    		}else if(element instanceof Relationship){
    			IRelationship rel = (IRelationship) element;
    			relationshipObject(rel);
    		}else if(element instanceof JAXBElement<?>) {
    			JAXBElement<?> element1 = (JAXBElement<?>) element;
    			String node1=(PropertyOf.SOURCE).toString();
    			String node2=(PropertyOf.TARGET).toString()+currentTarget;
    			String relation=(PropertyOf.RELATION).toString()+currentRelationship;
    			if(element1.getName().toString().contains("}target")){
    				boolean direction=true;
    				addRelationToMatch(node1, node2, relation, direction);
        			IRelNode relNode = (IRelNode) element1.getValue();
        			relNodeObject(relNode,PropertyOf.TARGET);
    			}else if(element1.getName().toString().contains("}source")){
    				boolean direction=false;
    				addRelationToMatch(node1, node2, relation, direction);
        			IRelNode relNode = (IRelNode) element1.getValue();
        			relNodeObject(relNode,PropertyOf.TARGET);
    			}	
    		}
    	}		
	}

	
	@Override
	public void propertyObject(IProperty prop,PropertyOf propertyOf) {
    	
		String element="";
		Map<String,String> startMap=getStartMap();
		Map<String,List<String>> whereMap=getWhereMap();
		
		if(propertyOf.equals(PropertyOf.SOURCE)){
			element=propertyOf.toString();
		}else if(propertyOf.equals(PropertyOf.RELATION)){
			element=propertyOf.toString()+currentRelationship;
		}else{
			element=propertyOf.toString()+currentTarget;
		}
		
		boolean flag=true;
		if(prop.getValue()!=null){
			String p=prop.getName()+"="+prop.getValue();
			if(!propertyOf.equals(PropertyOf.RELATION)){
				if(!startMap.containsKey(element)){
					addToStart(element, p);
					flag=false;
				}else{
					if(startMap.get(element).equals(p)){
						flag=false;
					}
				}
				
			}
			if(flag){
				if(whereMap.containsKey(element)){
					List<String> propertyList=whereMap.get(element);
					propertyList.add(p);
				}else{
					List<String> propertyList=new ArrayList<String>();
					propertyList.add(p);
					whereMap.put(element, propertyList);
				}
			}	
		}else{
			  if(prop.getEnd()!=null){
				  String p=prop.getName()+"<="+prop.getEnd();
				  if(whereMap.containsKey(element)){
						List<String> propertyList=whereMap.get(element);
						propertyList.add(p);
				  }else{
						List<String> propertyList=new ArrayList<String>();
						propertyList.add(p);
						whereMap.put(element, propertyList);
				  }
	    	 }
			 if(prop.getStart()!=null){
				 String p=prop.getName()+">="+prop.getStart();
				 if(whereMap.containsKey(element)){
						List<String> propertyList=whereMap.get(element);
						propertyList.add(p);
				  }else{
						List<String> propertyList=new ArrayList<String>();
						propertyList.add(p);
						whereMap.put(element, propertyList);
				  }
			 }
			  
		}
		
		/*if(prop.getEnd()!=null){
    		logger.info("Property End : "+prop.getEnd() );
    	}
    	if(prop.getId()!=null){
    		logger.info("Property ID : "+prop.getId() );
    	}
    	if(prop.getName()!=null){
    		logger.info("Property Name : "+prop.getName() );
    	}
    	if(prop.getType()!=null){
    		logger.info("Property Type : "+prop.getType() );
    	}
    	if(prop.getStart()!=null){
    		logger.info("Property Start : "+prop.getStart() );
    	}
    	if(prop.getValue()!=null){
    		logger.info("Property Value : "+prop.getValue() );
    	}*/		
	}
	
	@Override
	public void relNodeObject(IRelNode relNode,PropertyOf propertyOf) {

    	INode node = relNode.getNode();
    	List <Object> nodeObjectList = node.getPropertyOrRelationshipOrAnd();
    	Iterator<Object> nodeObjectIterator= nodeObjectList.iterator();
    	int count=0;
    	while(nodeObjectIterator.hasNext()){
    		Object o = nodeObjectIterator.next();
    		if(o instanceof Property){
    			IProperty prop = (IProperty) o;
    			propertyObject(prop,propertyOf);
    			count++;
    		}
    	}	
    	String op=getTargetOperator();
    	if(!op.equals("")&&count>1){
    		Map<String,List<String>> whereMap=getWhereMap();
    		String currentTargetNode=PropertyOf.TARGET.toString()+currentTarget;
    		List<String> targetList=whereMap.get(currentTargetNode);
    		targetList.add(op);
    	}
	}

	@Override
	public void incrementTarget() {
		currentTarget+=1;
	}

	@Override
	public void incrementRelationship() {
		currentRelationship+=1;
	}

	@Override
	public void addRelationToMatch(String node1, String node2, String relation,boolean direction) {
		
		Set<String> matchSet=getMatchSet();
		if(!matchSet.contains(node2)){
			if(!match.equals("Match ")){
				match+=",";
			}
			match+=node1;
			if(direction){
				match+="-"+relation+"->";
			}else{
				match+="<-"+relation+"-";
			}
			match+=node2+" ";
			matchSet.add(node2);
		}
	}

	@Override
	public void addToStart(String label, String prop) {
		if(!start.equals("Start ")){
			start+=",";
		}
		start+=label+"=node:myIndex("+prop+")";
		Map<String,String> startMap=getStartMap();
		startMap.put(label, prop);
	}

	@Override
	public Map<String, String> getStartMap() {
		if(startMap==null){
			startMap=new LinkedHashMap<String,String>();
		}
		return startMap;
	}
	
	@Override
	public void setSourceOperator(String op) {
		sourceOperator=op;
	}

	@Override
	public String getSourceOperator() {
		return sourceOperator;
	}

	@Override
	public void setTargetOperator(String op) {
		targetOperator=op;
	}

	@Override
	public String getTargetOperator() {
		return targetOperator;
	}

	@Override
	public Map<String, List<String>> getWhereMap() {
		if(whereMap==null){
			whereMap= new LinkedHashMap<String,List<String>>();
		}
		return whereMap;
	}
	
	@Override
	public Set<String> getMatchSet() {
		if(matchSet==null){
			matchSet=new LinkedHashSet<String>();
		}
		return matchSet;
	}

}
