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

	@Autowired
	IProperty newProperty;
	final private String dataSet="mblCourses";
	
	/*String sourceOperator="";
	String targetOperator="";*/
	
	@Override
	public String objectToCypher(INode node) {
		
		Map<String,String> startMap = new LinkedHashMap<String,String>();
		Map<String,String> matchMap = new LinkedHashMap<String,String>();
		Map<String,List<String>> whereMap=new LinkedHashMap<String,List<String>>();
		String sourceOperator=nodeObject(node,PropertyOf.SOURCE,startMap,whereMap,matchMap);
		String start="Start ";
		String match="Match ";
		String where="Where ";
		String ret="return ";
		start=buildStartClause(startMap,start);
		match=buildMatchClause(matchMap,match);
		where=buildWhereClause(whereMap,where,sourceOperator);
		ret=buildReturnClause(whereMap.keySet(),ret);
		String query=buildQuery(start,match,where,ret);
		logger.info(query);
		return query;
	}

	private String buildMatchClause(Map<String, String> matchMap, String match) {
		
		for(Entry<String,String> entry:matchMap.entrySet()){
			if(!match.equals("Match ")){
				match+=", ";
			}
			match+=entry.getValue()+" ";
		}
		return match;
	}

	private String buildStartClause(Map<String, String> startMap,String start) {
		
		for(Entry<String,String> entry:startMap.entrySet()){
			if(!start.equals("Start ")){
				start+=", ";
			}
			start+=entry.getKey()+"=node:myIndex("+entry.getValue()+") ";
		}
		return start;
	}

	private String buildQuery(String start,String match,String where,String ret) {
		String query=start;
		if(!match.equals("Match ")){
			query=query+"     "+match;
		}
		if(!where.equals("Where ")){
			query=query+"    "+where;
		}
		query=query+ret;
		return query;
	}

	private String buildReturnClause(Set<String> keySet,String ret) {
		for(String returnElement:keySet){
			if(!ret.equals("return "))
				ret+=", ";
			ret+=returnElement;
		}
		return ret;
	}

	private String buildWhereClause(Map<String,List<String>> whereMap,String where, String sourceOperator) {
		int elementCount=1;
		int totalElements=whereMap.size();
		boolean isSourceProperty=false;
		for (Entry<String, List<String>> entry : whereMap.entrySet()) {
			String key=entry.getKey();
			if(key.equals(PropertyOf.SOURCE.toString()))
				isSourceProperty=true;
			List<String> propertyList=entry.getValue();
			where+="(( ";
			int totalProperties=propertyList.size();
			int propertyCount=1;
			boolean newParenthesis=false;
			for(String property:propertyList){
				
				if(property.equals("or")||property.equals("and")){
					if(propertyCount==totalProperties)
						where+=")) ";
					else{
						where+=") "+property+" ( ";
						newParenthesis=true;
					}
				}else{	
					  if(propertyCount>1&&!newParenthesis){
						  where+="and ";
					  }
					  where+=key+"."+property+" ";
					  newParenthesis=false;
					  if(key.contains("target")&&propertyCount==totalProperties){
						  where+=")) ";
					  }
				}
				propertyCount++;
			}
			where+=") ";
			if(elementCount!=totalElements){
				where+=sourceOperator+" ";
			}
			elementCount++;
		}
		if(isSourceProperty)
			where+=") ";
		return where;
	}

	/*private void printWhere() {
		Map<String,List<String>> whereMap=getWhereMap();
		for (Entry<String, List<String>> entry : whereMap.entrySet()) {
			
		    logger.info(entry.getKey()+"----"+entry.getValue());
		}
	}*/

	@Override
	public String nodeObject(INode node,PropertyOf propertyOf,Map<String,String> startMap,Map<String,List<String>> whereMap, Map<String, String> matchMap) {
	
		
		int currentTarget=1;
		int currentRelationship=1;
		String sourceOperator="";
		logger.info("Node return status : "+node.isReturn());
		List<Object> nodeDetails = node.getPropertyOrRelationshipOrAnd();
		Iterator<Object> nodeDetailsIterator = nodeDetails.iterator();
		boolean flag=true;
		while(nodeDetailsIterator.hasNext()){
			Object o=nodeDetailsIterator.next();
			if(o instanceof Property ){
				IProperty prop = (IProperty) o;
    			propertyObject(prop,propertyOf,startMap,whereMap,currentTarget,currentRelationship);
			}
			if(propertyOf.equals(PropertyOf.SOURCE)&&flag){
    			newProperty.setName("dataset");
    			newProperty.setValue(dataSet);
    			propertyObject(newProperty,propertyOf,startMap,whereMap,currentTarget,currentRelationship);
    			flag=false;
			} else{
				JAXBElement<?> element = (JAXBElement<Object>) nodeDetailsIterator.next();
				if(element.getName().toString().contains("}and")){
					sourceOperator="and";
					IOperator opAnd = (IOperator) element.getValue();
					operatorObject(opAnd,propertyOf,startMap,whereMap,matchMap,currentTarget,currentRelationship);
					
				}else if(element.getName().toString().contains("}or")){
					sourceOperator="or";
					IOperator opOr = (IOperator) element.getValue();
					List<Integer> list=operatorObject(opOr,propertyOf,startMap,whereMap,matchMap,currentTarget,currentRelationship);
					currentTarget=list.get(0);
					currentRelationship=list.get(1);
				}
			}
		}
		return sourceOperator;
	}

	@Override
	public List<Integer> relationshipObject(IRelationship relationship,Map<String,String> startMap,Map<String,List<String>> whereMap,Map<String, String> matchMap, int currentTarget,int currentRelationship) {
		
		logger.info("Relationship return status : "+relationship.isReturn());
		String targetOperator="";
		currentTarget=increment(currentTarget);
		currentRelationship=increment(currentRelationship);
		List<Object> relationshipDetails = relationship.getSourceOrTargetOrProperty();
		Iterator<Object> relationshipDetailsIterator = relationshipDetails.iterator();
		while(relationshipDetailsIterator.hasNext()){

			Object element =relationshipDetailsIterator.next();
    		if(element instanceof Property){
    			IProperty prop = (IProperty) element;
    			propertyObject(prop,PropertyOf.RELATION,startMap,whereMap,currentTarget,currentRelationship);
    		}else if(element instanceof JAXBElement<?>){
    			JAXBElement<?> element1 = (JAXBElement<?>) element;
    			if(element1.getName().toString().contains("}or")){
    				targetOperator="or";
    				IOperator opOr = (IOperator) element1.getValue();
    				List<Integer> list=operatorObject(opOr,PropertyOf.RELATION,startMap,whereMap,matchMap,currentTarget,currentRelationship);
    				currentTarget=list.get(0);
    				currentRelationship=list.get(1);
    			}
    			
    			String node1=(PropertyOf.SOURCE).toString();
    			String node2=(PropertyOf.TARGET).toString()+currentTarget;
    			String relation=(PropertyOf.RELATION).toString()+currentRelationship;
    			if(element1.getName().toString().contains("}target")){
    				boolean direction=true;
    				addRelationToMatch(node1, node2, relation, direction,matchMap);
        			IRelNode relNode = (IRelNode) element1.getValue();
        			relNodeObject(relNode,PropertyOf.TARGET,startMap,whereMap,currentTarget,currentRelationship,targetOperator); 
    			}
    			
    			if(element1.getName().toString().contains("}source")){
    				boolean direction=false;
    				addRelationToMatch(node1, node2, relation, direction,matchMap);
        			IRelNode relNode = (IRelNode) element1.getValue();
        			relNodeObject(relNode,PropertyOf.TARGET,startMap,whereMap,currentTarget,currentRelationship,targetOperator);
    			}	
    		}
		}	
		List<Integer> _returnList=new ArrayList<Integer>();
    	_returnList.add(currentTarget);
    	_returnList.add(currentRelationship);
    	return _returnList;
	}

	private int increment(int num) {
		return num++;
	}

	@Override 
	public List<Integer> operatorObject(IOperator op,PropertyOf propertyOf,Map<String,String> startMap,Map<String,List<String>> whereMap,Map<String, String> matchMap, int currentTarget,int currentRelationship) {
    	List<Object> objectList = op.getSourceOrTargetOrProperty();
    	Iterator<Object> operatorIterator = objectList.iterator();
    	boolean flag=true;
    	while(operatorIterator.hasNext()){
    		Object element =operatorIterator.next();
    		if(element instanceof Property){
    			IProperty prop = (IProperty) element;
    			propertyObject(prop,propertyOf,startMap,whereMap,currentTarget,currentRelationship);
    		}
    		if(propertyOf.equals(PropertyOf.SOURCE)&&flag){
    			newProperty.setName("dataset");
    			newProperty.setValue(dataSet);
    			propertyObject(newProperty,propertyOf,startMap,whereMap,currentTarget,currentRelationship);
    			flag=false;
			} 
    		if(element instanceof Relationship){
    			IRelationship rel = (IRelationship) element;
    			relationshipObject(rel,startMap,whereMap,matchMap,currentTarget,currentRelationship);
    		}else if(element instanceof JAXBElement<?>) {
    			JAXBElement<?> element1 = (JAXBElement<?>) element;
    			String node1=(PropertyOf.SOURCE).toString();
    			String node2=(PropertyOf.TARGET).toString()+currentTarget;
    			String relation=(PropertyOf.RELATION).toString()+currentRelationship;
    			if(element1.getName().toString().contains("}target")){
    				boolean direction=true;
    				addRelationToMatch(node1, node2, relation, direction,matchMap);
        			IRelNode relNode = (IRelNode) element1.getValue();
        			relNodeObject(relNode,PropertyOf.TARGET,startMap,whereMap,currentTarget,currentRelationship,"");
    			}else if(element1.getName().toString().contains("}source")){
    				boolean direction=false;
    				addRelationToMatch(node1, node2, relation, direction,matchMap);
        			IRelNode relNode = (IRelNode) element1.getValue();
        			relNodeObject(relNode,PropertyOf.TARGET,startMap,whereMap,currentTarget,currentRelationship,"");
    			}	
    		}
    	}	
    	List<Integer> _returnList=new ArrayList<Integer>();
    	_returnList.add(currentTarget);
    	_returnList.add(currentRelationship);
    	return _returnList;
	}
 
	
	@Override
	public void propertyObject(IProperty prop,PropertyOf propertyOf,Map<String,String> startMap,Map<String,List<String>> whereMap,int currentTarget,int currentRelationship) {
    	
		String element="";
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
					addToStart(startMap,element, p);
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
	public void relNodeObject(IRelNode relNode,PropertyOf propertyOf,Map<String,String> startMap,Map<String,List<String>> whereMap,int currentTarget,int currentRelationship, String targetOperator) {

    	INode node = relNode.getNode();
    	List <Object> nodeObjectList = node.getPropertyOrRelationshipOrAnd();
    	Iterator<Object> nodeObjectIterator= nodeObjectList.iterator();
    	int count=0;
    	while(nodeObjectIterator.hasNext()){
    		Object o = nodeObjectIterator.next();
    		logger.info(""+o.getClass());
    		if(o instanceof Property){
    			IProperty prop = (IProperty) o;
    			propertyObject(prop,propertyOf,startMap,whereMap,currentTarget,currentRelationship);
    			count++;
    		} else {
    			JAXBElement<?> element1 = (JAXBElement<?>) o;
    			if(element1.getName().toString().contains("}or")||element1.getName().toString().contains("}and")){
					IOperator op = (IOperator) element1.getValue();
					List<Object> objectList = op.getSourceOrTargetOrProperty();
	    	    	Iterator<Object> operatorIterator = objectList.iterator();
	    	    	while(operatorIterator.hasNext()){
	    	    		Object element =operatorIterator.next();
	    	    		if(element instanceof Property){
	    	    			IProperty prop = (IProperty) element;
	    	    			propertyObject(prop,propertyOf,startMap,whereMap,currentTarget,currentRelationship);
	    	    			count++;
	    	    		}
	    	    	}
    			}
    		}
    	}	
		newProperty.setName("dataset");
		newProperty.setValue(dataSet);
		propertyObject(newProperty,propertyOf,startMap,whereMap,currentTarget,currentRelationship);
    	if(!targetOperator.equals("")&&count>1){
    		String currentTargetNode=PropertyOf.TARGET.toString()+currentTarget;
    		List<String> targetList=whereMap.get(currentTargetNode);
    		targetList.add(targetOperator);
    	}
	}



	@Override
	public void addRelationToMatch(String node1, String node2, String relation,boolean direction,Map<String,String> matchMap) {
		
		String match;
		if(!matchMap.containsKey(node2)){
			
			match=node1;
			if(direction){
				match+="-"+relation+"->";
			}else{
				match+="<-"+relation+"-";
			}
			match+=node2;
			matchMap.put(node2,match);
		}
	}

	@Override
	public void addToStart(Map<String,String> startMap,String label, String prop) {
		startMap.put(label, prop);
	}

	
	
	
	/*@Override
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
*/

	
	

	/*@Override
	public String objectToCypher(IRelNode node) {
		resetClassVariables();
		nodeObject(node,PropertyOf.SOURCE);
//		printWhere();
		buildWhereClause();
		buildReturnClause();
		String query=buildQuery();
		logger.info(query);
		return query;
	}
	
	@Override
	public void nestedRelNodeObject(IRelNode node,PropertyOf propertyOf) {
		
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
	}*/

}
