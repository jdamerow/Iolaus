package edu.asu.lerna.iolaus.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.domain.misc.ArgumentsInOTC;
import edu.asu.lerna.iolaus.domain.misc.ReturnElementsOfOTC;
import edu.asu.lerna.iolaus.domain.queryobject.INode;
import edu.asu.lerna.iolaus.domain.queryobject.IOperator;
import edu.asu.lerna.iolaus.domain.queryobject.IProperty;
import edu.asu.lerna.iolaus.domain.queryobject.IRelNode;
import edu.asu.lerna.iolaus.domain.queryobject.IRelationship;
import edu.asu.lerna.iolaus.domain.queryobject.PropertyOf;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Property;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Relationship;
import edu.asu.lerna.iolaus.service.ICypherToJson;
import edu.asu.lerna.iolaus.service.IObjectToCypher;


/**
 * @author Karan Kothari
 * 
 */

@Service
public class ObjectToCypher implements IObjectToCypher {
	
	private static final Logger logger = LoggerFactory
			.getLogger(ObjectToCypher.class);

	
	
	/**
	 * @param  node  is a INode object 
	 * @return       returnObj (It has two variables - json Query and objectToLabelMap(Key-Object,Value-Label))
	 */
	@Override
	public ReturnElementsOfOTC objectToCypher(INode node) {
		
		//key is label and value is its property used in Start clause of Cypher query
		Map<String,String> startMap = new LinkedHashMap<String,String>();
		//key is label and value is relation(source-[r1]->target1) used in Match clause of Cypher query
		Map<String,String> matchMap = new LinkedHashMap<String,String>();
		//key is label and value is List of property used in where clause of cypher query
		Map<String,List<String>> whereMap=new LinkedHashMap<String,List<String>>();
		//key is Object (Object corresponding to source and target labels) and value is label 
		Map<Object,String> objectToLabelMap=new LinkedHashMap<Object,String>();
		String dataSet="mblcourses";
		ArgumentsInOTC args=new ArgumentsInOTC();
		args.setStartMap(startMap);
		args.setWhereMap(whereMap);
		args.setMatchMap(matchMap);
		args.setObjectToLabelMap(objectToLabelMap);
		args.setDataSet(dataSet);
		args.setPropertyOf(PropertyOf.SOURCE);
		ReturnElementsOfOTC returnObj=new ReturnElementsOfOTC();
		String sourceOperator=nodeObject(node,args);
		String start=s;
		String match=m;
		String where=w;
		String ret=r;
		start=buildStartClause(startMap,start);
		match=buildMatchClause(matchMap,match);
		where=buildWhereClause(whereMap,where,sourceOperator);
		ret=buildReturnClause(whereMap.keySet(),startMap.keySet(),ret);
		String query=buildQuery(start,match,where,ret);
		ICypherToJson cypherToJson=new CypherToJson();
		String json=cypherToJson.cypherToJson(query);
		returnObj.setJson(json);
		returnObj.setObjectToTargetLabelMap(objectToLabelMap);
		return returnObj;
	}

	/**
	 * @param  node  is a IRelNode object 
	 * @return       returnObj (It has two variables - json Query and objectToLabelMap(Key-Object,Value-Label))
	 */
	@Override
	public ReturnElementsOfOTC objectToCypher(IRelNode node) {
		
		//key is label and value is its property used in Start clause of Cypher query
		Map<String,String> startMap = new LinkedHashMap<String,String>();
		//key is label and value is relation(source-[r1]->target1) used in Match clause of Cypher query
		Map<String,String> matchMap = new LinkedHashMap<String,String>();
		//key is label and value is List of property used in where clause of cypher query
		Map<String,List<String>> whereMap=new LinkedHashMap<String,List<String>>();
		//key is Object (Object corresponding to source and target labels) and value is label 
		Map<Object,String> objectToTargetLabelMap=new LinkedHashMap<Object,String>();
		String dataSet="mblcourses";
		ArgumentsInOTC args=new ArgumentsInOTC();
		args.setStartMap(startMap);
		args.setWhereMap(whereMap);
		args.setMatchMap(matchMap);
		args.setObjectToLabelMap(objectToTargetLabelMap);
		args.setDataSet(dataSet);
		args.setPropertyOf(PropertyOf.SOURCE);
		ReturnElementsOfOTC returnList=new ReturnElementsOfOTC();
		String sourceOperator=nestedRelNodeObject(node,args);
		String start=s;
		String match=m;
		String where=w;
		String ret=r;
		start=buildStartClause(startMap,start);
		match=buildMatchClause(matchMap,match);
		where=buildWhereClause(whereMap,where,sourceOperator);
		ret=buildReturnClause(whereMap.keySet(),startMap.keySet(),ret);
		String query=buildQuery(start,match,where,ret);
		ICypherToJson cypherToJson=new CypherToJson();
		String json=cypherToJson.cypherToJson(query);
		returnList.setJson(json);
		returnList.setObjectToTargetLabelMap(objectToTargetLabelMap);
		return returnList;
	}
	
	/**
	 * @param  startMap  is a Map with key-label and value-Property(e.g. type="Person")  
	 * @param  start	 Initially its value is "Start "  
	 * @return      	 builds the start clause of cypher and returns it
	 */
	private String buildStartClause(Map<String, String> startMap,String start) {
		
		for(Entry<String,String> entry:startMap.entrySet()){
			if(!start.equals("Start ")){
				start+=", ";
			}
			start+=entry.getKey()+"=node:new_index("+entry.getValue()+") ";
		}
		return start;
	}
	
	/**
	 * @param  matchMap  is a Map with key-label and value-Relationship(e.g. source-[r1]->target1)  
	 * @param  match	 Initially its value is "Match "  
	 * @return      	 It builds the match clause of cypher and returns it
	 */
	private String buildMatchClause(Map<String, String> matchMap, String match) {
		
		for(Entry<String,String> entry:matchMap.entrySet()){
			if(!match.equals("Match ")){
				match+=", ";
			}
			match+=entry.getValue()+" ";
		}
		return match;
	}
	
	/**
	 * @param  whereMap  	  is a Map with key-label and value-List of Properties associated with single label(e.g. type="Person")  
	 * @param  where	 	  Initially its value is "Where "  
	 * @param  sourceOperator is a operator between different relationships
	 * @return      	      builds the where clause of cypher and returns it
	 */
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
					  if(key.contains(PropertyOf.TARGET.toString())&&propertyCount==totalProperties){
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
	
	/**
	 * @param  whereMapKeySet  	  is the set of labels used in whereMap  
	 * @param  startSet	 	  	  is the set of labels used in the start clause  
	 * @param  ret     			  Initially its value is "return "
	 * @return      	          returns the return clause of cypher by taking union of whereMapKeySet and startSet
	 */
	private String buildReturnClause(Set<String> whereMapKeySet,Set<String> startSet, String ret) {
		
		List<String> allVariable=new ArrayList<String>();
		for(String returnElement:startSet){
				allVariable.add(returnElement);
		}
		for(String returnElement:whereMapKeySet){
			if(!allVariable.contains(returnElement)){
				allVariable.add(returnElement);
			}
		}
		for(String returnElement:allVariable){
			if(!ret.equals("return "))
				ret+=", ";
			ret+=returnElement;
		}
		return ret;
	}

	/**
	 * @param  start  	 is the start clause of cypher  
	 * @param  match	 is the match clause of cypher 
	 * @param  where     is the where  clause of cypher
	 * @param  ret   	 is return clause of cypher
	 * @return      	 returns the cypher query
	 */	
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
	
	/**
	 * @param  node   is a INode object  
	 * @param  args	  is a object which has all the maps, dataset and counters
	 * @return        returns the source Operator(Operator between different relationships)
	 */	
	public String nodeObject(INode node,ArgumentsInOTC args) {
		
		Map<Object, String> objectToLabelMap=args.getObjectToLabelMap();
		String dataSet=args.getDataSet();
		objectToLabelMap.put(node,PropertyOf.SOURCE.toString());
		args.setCurrentTarget(0);
		args.setCurrentRelationship(0);
		String sourceOperator="";
		List<Object> nodeDetails = node.getPropertyOrRelationshipOrAnd();
		Iterator<Object> nodeDetailsIterator = nodeDetails.iterator();
		boolean flag=true;
		while(nodeDetailsIterator.hasNext()){
			Object o=nodeDetailsIterator.next();
			if(isProperty(o)){
				IProperty prop = (IProperty) o;
    			propertyObject(prop,args);
    			flag=true;
			} else{
				flag=false;
				JAXBElement<?> element = (JAXBElement<Object>) o;
				args.setTargetOperator("");
				if(isAndOperator(element)){
					sourceOperator="and";
					IOperator opAnd = (IOperator) element.getValue();
					operatorObject(opAnd,args);
					
				}else if(isOrOperator(element)){
					sourceOperator="or";
					IOperator opOr = (IOperator) element.getValue();
					operatorObject(opOr,args);
				}
			}
		}
		if(flag==true){
			IProperty newProperty=new Property();
			newProperty.setName("dataset");
			newProperty.setValue(dataSet);
			propertyObject(newProperty,args);
		}
		return sourceOperator;
	}

	/**
	 * @param  relNode   is a IRelNode object  
	 * @param  args	  	 is a object which has all the maps, dataset and counters
	 * @return        	 returns the source Operator(Operator between different relationships)
	 */
	public String nestedRelNodeObject(IRelNode relNode,ArgumentsInOTC args) {
		
		INode node=relNode.getNode();
		Map<Object, String> objectToLabelMap=args.getObjectToLabelMap();
		String dataSet=args.getDataSet();
		objectToLabelMap.put(node,PropertyOf.SOURCE.toString());
		args.setCurrentTarget(0);
		args.setCurrentRelationship(0);
		String sourceOperator="";
		List<Object> nodeDetails = node.getPropertyOrRelationshipOrAnd();
		Iterator<Object> nodeDetailsIterator = nodeDetails.iterator();
		boolean flag=true;
		while(nodeDetailsIterator.hasNext()){
			Object o=nodeDetailsIterator.next();
			if(isProperty(o)){
				IProperty prop = (IProperty) o;
    			propertyObject(prop,args);
			} else if(isJAXBelement(o)){
				flag=false;
				JAXBElement<?> element = (JAXBElement<Object>) o;
				if(isAndOperator(element)){
					sourceOperator="and";
					IOperator opAnd = (IOperator) element.getValue();
					operatorObject(opAnd,args);
					
				}else if(isOrOperator(element)){
					sourceOperator="or";
					IOperator opOr = (IOperator) element.getValue();
					operatorObject(opOr,args);
				}
			}
		}
		if(flag==true){
			IProperty newProperty=new Property();
			newProperty.setName("dataset");
			newProperty.setValue(dataSet);
			propertyObject(newProperty,args);
		}
		return sourceOperator;
	}
	
	/**
	 * @param  relationship   is a IRelationship object  
	 * @param  args	  		  is a object which has all the maps, dataset and counters
	 */
	public void relationshipObject(IRelationship relationship,ArgumentsInOTC args){
		
		Map<String, String> matchMap=args.getMatchMap();
		Map<Object, String> objectToLabelMap=args.getObjectToLabelMap();
		
		args.setCurrentTarget(increment(args.getCurrentTarget()));
		args.setCurrentRelationship(increment(args.getCurrentRelationship()));
		objectToLabelMap.put(relationship,PropertyOf.RELATION.toString()+args.getCurrentRelationship());
		List<Object> relationshipDetails = relationship.getSourceOrTargetOrProperty();
		Iterator<Object> relationshipDetailsIterator = relationshipDetails.iterator();
		while(relationshipDetailsIterator.hasNext()){

			Object element =relationshipDetailsIterator.next();
    		if(isProperty(element)){
    			IProperty prop = (IProperty) element;
    			args.setPropertyOf(PropertyOf.RELATION);
    			propertyObject(prop,args);
    		}else if(isJAXBelement(element)){
    			JAXBElement<?> element1 = (JAXBElement<?>) element;
    			if(isOrOperator(element1)){
    				args.setTargetOperator("or");
    				IOperator opOr = (IOperator) element1.getValue();
    				args.setPropertyOf(PropertyOf.RELATION);
    				operatorObject(opOr,args);
    				
    			}else{
    				objectToLabelMap.put(element1.getValue(),PropertyOf.TARGET.toString()+args.getCurrentTarget());
	    			String node1=(PropertyOf.SOURCE).toString();
	    			String node2=(PropertyOf.TARGET).toString()+args.getCurrentTarget();
	    			String relation=(PropertyOf.RELATION).toString()+args.getCurrentRelationship();
	    			args.setPropertyOf(PropertyOf.TARGET);
	    			if(isTargetNode(element1)){
	    				boolean direction=true;
	    				addRelationToMatch(node1, node2, relation, direction,matchMap);
	        			IRelNode relNode = (IRelNode) element1.getValue();
	        			relNodeObject(relNode,args); 
	    			}
	    			if(isSourceNode(element1)){
	    				boolean direction=false;
	    				addRelationToMatch(node1, node2, relation, direction,matchMap);
	        			IRelNode relNode = (IRelNode) element1.getValue();
	        			relNodeObject(relNode,args);
	    			}	
    			}
    		}
		}	
	}

	/**
	 * @param  op   is a IOperator object  
	 * @param  args	is a object which has all the maps, dataset and counters
	 */
	public void operatorObject(IOperator op,ArgumentsInOTC args){
	
		PropertyOf propertyOf=args.getPropertyOf();
		Map<String, String> matchMap=args.getMatchMap();
		Map<Object, String> objectToLabelMap=args.getObjectToLabelMap();
		String dataSet=args.getDataSet();
    	
		List<Object> objectList = op.getSourceOrTargetOrProperty();
    	Iterator<Object> operatorIterator = objectList.iterator();
    	boolean flag=true;
    	while(operatorIterator.hasNext()){
    		Object element =operatorIterator.next();
    		if(isProperty(element)){
    			IProperty prop = (IProperty) element;
    			propertyObject(prop,args);
    		}
    		if(propertyOf.equals(PropertyOf.SOURCE)&&flag){
    			IProperty newProperty=new Property();
    			newProperty.setName("dataset");
    			newProperty.setValue(dataSet);
    			propertyObject(newProperty,args);
    			flag=false;
			} 
    		if(isRelationship(element)){
    			IRelationship rel = (IRelationship) element;
    			relationshipObject(rel,args);
    		}else if(isJAXBelement(element)) {
    			
    			JAXBElement<?> element1 = (JAXBElement<?>) element;
    			objectToLabelMap.put(element1.getValue(),PropertyOf.TARGET.toString()+args.getCurrentTarget());
    			String node1=(PropertyOf.SOURCE).toString();
    			String node2=(PropertyOf.TARGET).toString()+args.getCurrentTarget();
    			String relation=(PropertyOf.RELATION).toString()+args.getCurrentRelationship();
    			args.setPropertyOf(PropertyOf.TARGET);
    			if(isTargetNode(element1)){
    				boolean direction=true;
    				addRelationToMatch(node1, node2, relation, direction,matchMap);
        			IRelNode relNode = (IRelNode) element1.getValue();
        			relNodeObject(relNode,args);
    			}else if(isSourceNode(element1)){
    				boolean direction=false;
    				addRelationToMatch(node1, node2, relation, direction,matchMap);
        			IRelNode relNode = (IRelNode) element1.getValue();
        			relNodeObject(relNode,args);
    			}	
    		}
    	}	
	}
 
	/**
	 * @param  prop   is a IProperty object  
	 * @param  args	  is a object which has all the maps, dataset and counters
	 */
	public void propertyObject(IProperty prop,ArgumentsInOTC args){
		
		PropertyOf propertyOf=args.getPropertyOf();
		Map<String,String> startMap=args.getStartMap();
		Map<String,List<String>> whereMap=args.getWhereMap();
  
		String element="";
		if(propertyOf.equals(PropertyOf.SOURCE)){
			element=propertyOf.toString();
		}else if(propertyOf.equals(PropertyOf.RELATION)){
			element=propertyOf.toString()+args.getCurrentRelationship();
		}else{
			element=propertyOf.toString()+args.getCurrentTarget();
		}
		
		boolean flag=true;
		
		if(prop.getValue()!=null){
			String value=prop.getValue();
			if(!isNumeric(value)){
				value="\""+prop.getValue()+"\"";
			}
			
			
			if(!propertyOf.equals(PropertyOf.RELATION)){
				String p=prop.getName()+"="+value;
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
				String p=prop.getName()+"=~"+value;
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
	}
	

	/**
	 * @param  relNode   is a IRelNode object  
	 * @param  args	  	 is a object which has all the maps, dataset and counters
	 */
	public void relNodeObject(IRelNode relNode,ArgumentsInOTC args){
		
		Map<String,List<String>> whereMap=args.getWhereMap();
		String targetOperator=args.getTargetOperator();
		String dataSet=args.getDataSet();
		
    	INode node = relNode.getNode();
    	List <Object> nodeObjectList = node.getPropertyOrRelationshipOrAnd();
    	Iterator<Object> nodeObjectIterator= nodeObjectList.iterator();
    	int count=0;
    	while(nodeObjectIterator.hasNext()){
    		Object o = nodeObjectIterator.next();
    		if(isProperty(o)){
    			IProperty prop = (IProperty) o;
    			propertyObject(prop,args);
    			count++;
    		} else if(isJAXBelement(o)){
    			JAXBElement<?> element1 = (JAXBElement<?>) o;
    			if(isOrOperator(element1)||isAndOperator(element1)){
					IOperator op = (IOperator) element1.getValue();
					List<Object> objectList = op.getSourceOrTargetOrProperty();
	    	    	Iterator<Object> operatorIterator = objectList.iterator();
	    	    	while(operatorIterator.hasNext()){
	    	    		Object element =operatorIterator.next();
	    	    		if(isProperty(element)){
	    	    			IProperty prop = (IProperty) element;
	    	    			propertyObject(prop,args);
	    	    			count++;
	    	    		}
	    	    	}
    			}
    		}
    	}	
    	IProperty newProperty=new Property();
		newProperty.setName("dataset");
		newProperty.setValue(dataSet);
		propertyObject(newProperty,args);
    	if(!targetOperator.equals("")&&count>1){
    		String currentTargetNode=PropertyOf.TARGET.toString()+args.getCurrentTarget();
    		List<String> targetList=whereMap.get(currentTargetNode);
    		targetList.add(targetOperator);
    	}
	}


	/**
	 * @param  node1     is a source Node  
	 * @param  node2     is a target Node
	 * @param  relation  is a label of relation
	 * @param  direction is direction of relation between source and target
	 * @param  matchMap  is map with key=target label and value=newly created relationshipS 
	 */
	public void addRelationToMatch(String node1, String node2, String relation,boolean direction,Map<String,String> matchMap) {
		String match;
		if(!matchMap.containsKey(node2)){
			
			match=node1;
			if(direction){
				match+="-["+relation+"]->";
			}else{
				match+="<-["+relation+"]-";
			}
			match+=node2;
			matchMap.put(node2,match);
		}
	}

	/**
	 * @param  startMap   is map with key=label and value=property
	 * @param  label      is the label of property used in start clause
	 * @param  prop 	  is a property to be stored in startMap
	 */
	public void addToStart(Map<String,String> startMap,String label, String prop) {
		startMap.put(label, prop);
	}

	/**
	 * @param  o   is Object
	 * @return     true if o is instance of the Property else return false
	 */
	public boolean isProperty(Object o) {
		return o instanceof Property;
	}

	/**
	 * @param  element   is JAXBElement
	 * @return true      if element contains "}or" else return false
	 */
	public boolean isOrOperator(JAXBElement<?> element) {
		return element.getName().toString().contains("}or");
	}

	/**
	 * @param  element   is JAXBElement
	 * @return           true if element contains "}and" else return false
	 */
	public boolean isAndOperator(JAXBElement<?> element) {
		return element.getName().toString().contains("}and");
	}
	
	/**
	 * @param  element   is Object
	 * @return           true if o is instance of the Relationship else return false
	 */
	public boolean isRelationship(Object element) {
		return element instanceof Relationship;
	}
	
	/**
	 * @param  element1   is JAXBElement
	 * @return            true if element contains "}source" else return false
	 */
	public boolean isSourceNode(JAXBElement<?> element1) {
		return element1.getName().toString().contains("}source");
	}

	/**
	 * @param  element1   is JAXBElement
	 * @return            true if element contains "}target" else return false
	 */
	public boolean isTargetNode(JAXBElement<?> element1) {
		return element1.getName().toString().contains("}target");
	}

	/**
	 * @param  element   is Object
	 * @return           true if o is instance of the JAXBElement else return false
	 */
	public boolean isJAXBelement(Object element) {
		return element instanceof JAXBElement<?>;
	}
	
	/**
	 * @param  value   is a String
	 * @return         true if it is Numeric else return false
	 */
	public boolean isNumeric(String value) {
		return value.matches("(\\d*)");
	}
	
	/**
	 * @param  num   integer
	 * @return       increment by 1
	 */
	public int increment(int num) {
		return num+1;
	}
}
