package edu.asu.lerna.iolaus.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.bind.JAXBElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.domain.misc.ArgumentsInOTC;
import edu.asu.lerna.iolaus.domain.misc.ReturnParametersOfOTC;
import edu.asu.lerna.iolaus.domain.queryobject.INode;
import edu.asu.lerna.iolaus.domain.queryobject.IOperator;
import edu.asu.lerna.iolaus.domain.queryobject.IProperty;
import edu.asu.lerna.iolaus.domain.queryobject.IRelNode;
import edu.asu.lerna.iolaus.domain.queryobject.IRelationship;
import edu.asu.lerna.iolaus.domain.queryobject.PropertyOf;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Operator;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Property;
import edu.asu.lerna.iolaus.domain.queryobject.impl.RelNode;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Relationship;
import edu.asu.lerna.iolaus.service.ICypherToJson;
import edu.asu.lerna.iolaus.service.IObjectToCypher;


/**
 * This class will convert Node or RelNode object to cypher
 * @author Karan Kothari
 */

@Service
public class ObjectToCypher implements IObjectToCypher {
	
	/*private static final Logger logger = LoggerFactory
			.getLogger(ObjectToCypher.class);*/
	@Autowired
	private ICypherToJson cypherToJson;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReturnParametersOfOTC objectToCypher(INode node,String dataSet) {
		
		if(node==null)
			return null;
		//key is label and value is its property used in Start clause of Cypher query
		Map<String,String> startMap = new LinkedHashMap<String,String>();
		//key is label and value is relation(source-[r1]->target1) used in Match clause of Cypher query
		Map<String,String> matchMap = new LinkedHashMap<String,String>();
		//key is label and value is List of property used in where clause of cypher query
		Map<String,List<String>> whereMap=new LinkedHashMap<String,List<String>>();
		//key is Object (Object corresponding to source and target labels) and value is label 
		Map<Object,String> objectToLabelMap=new LinkedHashMap<Object,String>();
		//It is a map with key=Unique label and value=its return value
		Map<String,Boolean> labelToIsReturnMap=new HashMap<String,Boolean>();
		//Set Parameters in args
		ArgumentsInOTC args=new ArgumentsInOTC();
		args.setStartMap(startMap);
		args.setWhereMap(whereMap);
		args.setMatchMap(matchMap);
		args.setObjectToLabelMap(objectToLabelMap);
		args.setDataSet(dataSet);
		args.setPropertyOf(PropertyOf.SOURCE);
		args.setLabelToIsReturnMap(labelToIsReturnMap);
		ReturnParametersOfOTC returnObj=new ReturnParametersOfOTC();
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
		String json=cypherToJson.cypherToJson(query);//converts cypher query to json query
		returnObj.setJson(json);
		returnObj.setObjectToTargetLabelMap(objectToLabelMap);
		returnObj.setIsReturnMap(labelToIsReturnMap);
		return returnObj;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ReturnParametersOfOTC objectToCypher(IRelNode node, String dataSet) {
		
		//key is label and value is its property used in Start clause of Cypher query
		Map<String,String> startMap = new LinkedHashMap<String,String>();
		//key is label and value is relation(source-[r1]->target1) used in Match clause of Cypher query
		Map<String,String> matchMap = new LinkedHashMap<String,String>();
		//key is label and value is List of property used in where clause of cypher query
		Map<String,List<String>> whereMap=new LinkedHashMap<String,List<String>>();
		//key is Object (Object corresponding to source and target labels) and value is label 
		Map<Object,String> objectToTargetLabelMap=new LinkedHashMap<Object,String>();
		//It is a map with key=Unique label and value=its return value
		Map<String,Boolean> labelToIsReturnMap=new HashMap<String,Boolean>();
		ArgumentsInOTC args=new ArgumentsInOTC();
		//Set Parameters in args
		args.setStartMap(startMap);
		args.setWhereMap(whereMap);
		args.setMatchMap(matchMap);
		args.setObjectToLabelMap(objectToTargetLabelMap);
		args.setDataSet(dataSet);
		args.setPropertyOf(PropertyOf.SOURCE);
		args.setLabelToIsReturnMap(labelToIsReturnMap);
		ReturnParametersOfOTC returnObj=new ReturnParametersOfOTC();
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
		String json=cypherToJson.cypherToJson(query);//Converts cypher query to json query
		returnObj.setJson(json);
		returnObj.setObjectToTargetLabelMap(objectToTargetLabelMap);
		returnObj.setIsReturnMap(labelToIsReturnMap);
		return returnObj;
	}
	
	/**
	 * This method builds the Start clause of cypher query
	 * @param  startMap  is a Map with key-label and value-Property(e.g. type="Person")  
	 * @param  start	 Initially its value is "Start "  
	 * @return      	 builds the start clause of cypher and returns it
	 */
	private String buildStartClause(Map<String, String> startMap,String start) {
		//This loop builds the start clause of cypher query
		for(Entry<String,String> entry:startMap.entrySet()){
			if(!start.equals("Start ")){
				start+=", ";
			}
			start+=entry.getKey()+"=node:node_auto_index("+entry.getValue()+") ";
		}
		return start;
	}
	
	/**
	 * This method builds the Match clause of cypher query
	 * @param  matchMap  is a Map with key-label and value-Relationship(e.g. source-[r1]->target1)  
	 * @param  match	 Initially its value is "Match "  
	 * @return      	 It builds the match clause of cypher and returns it
	 */
	private String buildMatchClause(Map<String, String> matchMap, String match) {
		//This loop will build match clause of cypher query
		for(Entry<String,String> entry:matchMap.entrySet()){
			if(!match.equals("Match ")){
				match+=", ";
			}
			match+=entry.getValue()+" ";
		}
		return match;
	}
	
	/**
	 * This method builds the Where clause of cypher query
	 * @param  whereMap  	  is a Map with key-label and value-List of Properties associated with single label(e.g. type="Person")  
	 * @param  where	 	  Initially its value is "Where "  
	 * @param  sourceOperator is a operator between different relationships
	 * @return      	      builds the where clause of cypher and returns it
	 */
	private String buildWhereClause(Map<String,List<String>> whereMap,String where, String sourceOperator) {
		int labelCount=1;//initialized the count of label to 1
		int totalLabels=whereMap.size();//Total number of labels used in Where clause
		boolean isSourceProperty=false;//Does source label has property(if it is true then we need to add an extra parenthesis to the query) 
		for (Entry<String, List<String>> entry : whereMap.entrySet()){//For each label having a property
			String key=entry.getKey();
			if(key.equals(PropertyOf.SOURCE.toString()))//if a source label
				isSourceProperty=true;
			List<String> propertyList=entry.getValue();
			where+="(( ";
			int totalProperties=propertyList.size();
			int propertyCount=1;//count of property for a single label
			boolean newParenthesis=false;
			//This loop adds the multiple properties of a label to cypher query.
			for(String property:propertyList){
				
				if(property.equals("or")||property.equals("and")){//if next object in List is an operator
					if(propertyCount==totalProperties)//if it is a last object in the List
						where+=")) ";
					else{//otherwise the node has more property to be added to the Where clause 
						where+=") "+property+" ( ";
						newParenthesis=true;
					}
				}else{	
					  if(propertyCount>1&&!newParenthesis){//every property in a parenthesis is separated by "and" operator
						  where+="and ";
					  }
					  where+=key+"."+property+" ";
					  newParenthesis=false;
					  if(key.contains(PropertyOf.TARGET.toString())&&propertyCount==totalProperties){//if it is last property of target label
						  where+=")) ";
					  }
				}
				propertyCount++;
			}
			where+=") ";
			if(labelCount!=totalLabels){//if there are more relations in the cypher query
				where+=sourceOperator+" ";
			}
			labelCount++;
		}
		if(isSourceProperty)//If a source node in the query has a property
			where+=") ";
		return where;
	}
	
	/**
	 * This method builds a Return clause of the cypher query
	 * @param  whereMapKeySet  	  is the set of labels used in whereMap  
	 * @param  startSet	 	  	  is the set of labels used in the start clause  
	 * @param  ret     			  Initially its value is "return "
	 * @return      	          returns the return clause of cypher by taking union of whereMapKeySet and startSet
	 */
	private String buildReturnClause(Set<String> whereMapKeySet,Set<String> startSet, String ret) {
		
		List<String> allVariable=new ArrayList<String>();
		for(String returnElement:startSet){//adds all the labels that are used in the Start Clause of cypher query
				allVariable.add(returnElement);
		}
		for(String returnElement:whereMapKeySet){//add all the labels that are used in the the where clause except the ones which are already added in the return clause
			if(!allVariable.contains(returnElement)){
				allVariable.add(returnElement);
			}
		}
		//This loop builds the return clause of the cypher query
		for(String returnElement:allVariable){
			if(!ret.equals("return "))
				ret+=", ";
			ret+=returnElement;
		}
		return ret;
	}

	/**
	 * This method takes all the clauses as a input and builds complete query
	 * @param  start  	 is the start clause of cypher  
	 * @param  match	 is the match clause of cypher 
	 * @param  where     is the where  clause of cypher
	 * @param  ret   	 is return clause of cypher
	 * @return      	 returns the cypher query
	 */	
	private String buildQuery(String start,String match,String where,String ret) {
		String query=start;
		if(!match.equals("Match ")){//If no relation specified in the XML
			query=query+"     "+match;
		}
		if(!where.equals("Where ")){//If where clause has no properties
			query=query+"    "+where;
		}
		query=query+ret;
		return query;
	}
	
	/**
	 * This method parses the {@link Node} object.
	 * @param  node   is a INode object  
	 * @param  args	  is a object which has all the maps, dataset and counters
	 * @return        returns the source Operator(Operator between different relationships)
	 */	
	@SuppressWarnings("unchecked")
	private String nodeObject(INode node,ArgumentsInOTC args) {
		
		Map<Object, String> objectToLabelMap=args.getObjectToLabelMap();
		String dataSet=args.getDataSet();
		objectToLabelMap.put(node,PropertyOf.SOURCE.toString());
		args.setCurrentTarget(0);
		args.setCurrentRelationship(0);
		args.getLabelToIsReturnMap().put(PropertyOf.SOURCE.toString(), node.isReturn()==null?false:node.isReturn());
		String sourceOperator="";
		List<Object> nodeDetails = node.getPropertyOrRelationshipOrAnd();
		Iterator<Object> nodeDetailsIterator = nodeDetails.iterator();
		boolean flag=true;//flag to check if source Node has property or not
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
	 * This method parses the {@link RelNode} object.
	 * @param  relNode   is a IRelNode object  
	 * @param  args	  	 is a object which has all the maps, dataset and counters
	 * @return        	 returns the source Operator(Operator between different relationships)
	 */
	@SuppressWarnings("unchecked")
	private String nestedRelNodeObject(IRelNode relNode,ArgumentsInOTC args) {
		
		INode node=relNode.getNode();
		if(node!=null){
		Map<Object, String> objectToLabelMap=args.getObjectToLabelMap();
		String dataSet=args.getDataSet();
		objectToLabelMap.put(node,PropertyOf.SOURCE.toString());
		args.setCurrentTarget(0);
		args.setCurrentRelationship(0);
		args.getLabelToIsReturnMap().put(PropertyOf.SOURCE.toString(),node.isReturn()==null?false:node.isReturn());
		String sourceOperator="";
		List<Object> nodeDetails = node.getPropertyOrRelationshipOrAnd();
		Iterator<Object> nodeDetailsIterator = nodeDetails.iterator();
		boolean flag=true;//flag to check if source Node has property or not
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
		}else{
			return null;
		}
	}
	
	/**
	 * This method parses the {@link Relationship} object. It adds the relations in the matchMap of {@link ArgumentsInOTC}.
	 * @param  relationship   is a IRelationship object  
	 * @param  args	  		  is a object which has all the maps, dataset and counters
	 */
	private void relationshipObject(IRelationship relationship,ArgumentsInOTC args){
		
		Map<String, String> matchMap=args.getMatchMap();
		Map<Object, String> objectToLabelMap=args.getObjectToLabelMap();
		args.setCurrentTarget(increment(args.getCurrentTarget()));//increment currentTarget
		args.setCurrentRelationship(increment(args.getCurrentRelationship()));//increment currentRelationship
		//add to labelToIsReturnTrueMap a pair (key=label and value=true(if return="true") otherwise value="false"
		args.getLabelToIsReturnMap().put(PropertyOf.RELATION.toString()+args.getCurrentRelationship(), relationship.isReturn()==null?false:relationship.isReturn());
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
    				args.setTargetOperator("");//if next object is IRelNode object then there is only one IRelNode object
    				objectToLabelMap.put(element1.getValue(),PropertyOf.TARGET.toString()+args.getCurrentTarget());
	    			String node1=(PropertyOf.SOURCE).toString();
	    			String node2=(PropertyOf.TARGET).toString()+args.getCurrentTarget();
	    			String relation=(PropertyOf.RELATION).toString()+args.getCurrentRelationship();
	    			args.setPropertyOf(PropertyOf.TARGET);
	    			if(isTargetNode(element1)){
	    				boolean direction=true;//outward arrow in the relationship
	    				addRelationToMatch(node1, node2, relation, direction,matchMap);
	        			IRelNode relNode = (IRelNode) element1.getValue();
	        			relNodeObject(relNode,args); 
	    			}
	    			if(isSourceNode(element1)){
	    				boolean direction=false;//Inward arrow in the relationship
	    				addRelationToMatch(node1, node2, relation, direction,matchMap);
	        			IRelNode relNode = (IRelNode) element1.getValue();
	        			relNodeObject(relNode,args);
	    			}	
    			}
    		}
		}	
	}

	/**
	 * This method parses {@link Operator} object.
	 * @param  op   is a IOperator object  
	 * @param  args	is a object which has all the maps, dataset and counters
	 */
	private void operatorObject(IOperator op,ArgumentsInOTC args){
	
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
    		if(propertyOf.equals(PropertyOf.SOURCE)&&flag){//if PropertyOf source label and condition about data set is not added to the query 
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
    				boolean direction=true;//Outward arrow in the relationship
    				addRelationToMatch(node1, node2, relation, direction,matchMap);
        			IRelNode relNode = (IRelNode) element1.getValue();
        			relNodeObject(relNode,args);
    			}else if(isSourceNode(element1)){
    				boolean direction=false;//Inward arrow in the relationship
    				addRelationToMatch(node1, node2, relation, direction,matchMap);
        			IRelNode relNode = (IRelNode) element1.getValue();
        			relNodeObject(relNode,args);
    			}	
    		}
    	}	
	}
 
	/**
	 * This method adds property in startMap or whereMap of {@link ArgumentsInOTC}.
	 * @param  prop   is a IProperty object  
	 * @param  args	  is a object which has all the maps, dataset and counters
	 */
	private void propertyObject(IProperty prop,ArgumentsInOTC args){
		
		PropertyOf propertyOf=args.getPropertyOf();
		Map<String,String> startMap=args.getStartMap();
		Map<String,List<String>> whereMap=args.getWhereMap();
  
		String label="";
		if(propertyOf.equals(PropertyOf.SOURCE)){//if PropertyOf source Node
			label=propertyOf.toString();
		}else if(propertyOf.equals(PropertyOf.RELATION)){//if PropertyOf relationship
			label=propertyOf.toString()+args.getCurrentRelationship();
		}else{//PropertyOf target Node
			label=propertyOf.toString()+args.getCurrentTarget();
		}
		
		boolean flag=true;
		
		if(prop.getValue()!=null){
			String value=prop.getValue();
			if(!isNumeric(value)){//if not a numeric value then put property in between ""
				value="\""+prop.getValue()+"\"";
			}
			
			
			if(!propertyOf.equals(PropertyOf.RELATION)){//if not a property of relationship
				String p=prop.getName()+"="+value;
				if(!startMap.containsKey(label)){//if startMap doesn't contain label then property is added to the startMap
					addToStart(startMap,label, p);
					flag=false;
				}else{
					//if property already exists in startMap then skip that property.
					//(This case may arise if input has multiple IRelNode separated by an operator for a single relationship)
					if(startMap.get(label).equals(p)){  
						flag=false;
					}
				}
				
			}
			if(flag){
				String p="";
				if(!(isNumeric(value)||propertyOf.equals(PropertyOf.RELATION)))
					p=prop.getName()+"=~"+value;//"=~" is to treat this property as regex 
				else{
					p=prop.getName()+"="+value;//Numeric value can not be treated as regex
				}
				if(whereMap.containsKey(label)){//label already exists in whereMap
					List<String> propertyList=whereMap.get(label);
					propertyList.add(p);
				}else{//first property of label to be added in the whereMap
					List<String> propertyList=new ArrayList<String>();
					propertyList.add(p);
					whereMap.put(label, propertyList);
				}
			}	
		}else{//if value==null (range is specified instead of a single value)
			  if(prop.getEnd()!=null){
				  String p=prop.getName()+"<="+prop.getEnd();
				  if(whereMap.containsKey(label)){
						List<String> propertyList=whereMap.get(label);
						propertyList.add(p);
				  }else{
						List<String> propertyList=new ArrayList<String>();
						propertyList.add(p);
						whereMap.put(label, propertyList);
				  }
	    	 }
			 if(prop.getStart()!=null){
				 String p=prop.getName()+">="+prop.getStart();
				 if(whereMap.containsKey(label)){
						List<String> propertyList=whereMap.get(label);
						propertyList.add(p);
				  }else{
						List<String> propertyList=new ArrayList<String>();
						propertyList.add(p);
						whereMap.put(label, propertyList);
				  }
			 }
		}		
	}
	

	/**
	 * This method parses the {@link RelNode} object. 
	 * @param  relNode   is a IRelNode object  
	 * @param  args	  	 is a object which has all the maps, dataset and counters
	 */
	private void relNodeObject(IRelNode relNode,ArgumentsInOTC args){
		
		Map<String,List<String>> whereMap=args.getWhereMap();
		String targetOperator=args.getTargetOperator();
		String dataSet=args.getDataSet();
		
    	INode node = relNode.getNode();
    	//add to labelToIsReturnTrueMap a pair (key=label and value=true(if return="true") otherwise value="false"
    	args.getLabelToIsReturnMap().put(PropertyOf.TARGET.toString()+args.getCurrentTarget(), node.isReturn()==null?false:node.isReturn());
    	List <Object> nodeObjectList = node.getPropertyOrRelationshipOrAnd();
    	Iterator<Object> nodeObjectIterator= nodeObjectList.iterator();
    	int count=0;
    	while(nodeObjectIterator.hasNext()){
    		Object o = nodeObjectIterator.next();
    		if(isProperty(o)){
    			IProperty prop = (IProperty) o;
    			propertyObject(prop,args);
    			count++;
    		} else if(isJAXBelement(o)){//if true then it has a inner queries
    			JAXBElement<?> element1 = (JAXBElement<?>) o;
    			if(isOrOperator(element1)||isAndOperator(element1)){
					IOperator op = (IOperator) element1.getValue();
					List<Object> objectList = op.getSourceOrTargetOrProperty();
	    	    	Iterator<Object> operatorIterator = objectList.iterator();
	    	    	while(operatorIterator.hasNext()){
	    	    		Object element =operatorIterator.next();
	    	    		if(isProperty(element)){//Skip nested relationship objects
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
    	if(!targetOperator.equals("")&&count>1){//if at least one property in where clause and more than one IRelNode objects for a relationship
    		String currentTargetNode=PropertyOf.TARGET.toString()+args.getCurrentTarget();
    		List<String> targetList=whereMap.get(currentTargetNode);
    		targetList.add(targetOperator);
    	}
	}


	/**
	 * This method adds relation to matchMap.
	 * @param  node1     is a source Node  
	 * @param  node2     is a target Node
	 * @param  relation  is a label of relation
	 * @param  direction is direction of relation between source and target
	 * @param  matchMap  is map with key=target label and value=newly created relationshipS 
	 */
	private void addRelationToMatch(String node1, String node2, String relation,boolean direction,Map<String,String> matchMap) {
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
	 * This method adds property to the startMap
	 * @param  startMap   is map with key=label and value=property
	 * @param  label      is the label of property used in start clause
	 * @param  prop 	  is a property to be stored in startMap
	 */
	private void addToStart(Map<String,String> startMap,String label, String prop) {
		startMap.put(label, prop);
	}

	/**
	 * This method checks if {@link Object} is of type {@link Property}
	 * @param  o   is Object
	 * @return     true if o is instance of the Property else return false
	 */
	private boolean isProperty(Object o) {
		return o instanceof Property;
	}

	/**
	 * This method checks if {@link JAXBElement} is an Or operator.
	 * @param  element   is JAXBElement
	 * @return true      if element contains "}or" else return false
	 */
	private boolean isOrOperator(JAXBElement<?> element) {
		return element.getName().toString().contains("}or");
	}

	/**
	 * This method checks if {@link JAXBElement} element is an And operator. 
	 * @param  element   is JAXBElement
	 * @return           true if element contains "}and" else return false
	 */
	private boolean isAndOperator(JAXBElement<?> element) {
		return element.getName().toString().contains("}and");
	}
	
	/**
	 * This method checks if {@link Object} is of type {@link Relationship}.
	 * @param  element   is Object
	 * @return           true if o is instance of the Relationship else return false
	 */
	private boolean isRelationship(Object element) {
		return element instanceof Relationship;
	}
	
	/**
	 * This method checks if {@link JAXBElement} element is source node.
	 * @param  element1   is JAXBElement
	 * @return            true if element contains "}source" else return false
	 */
	private boolean isSourceNode(JAXBElement<?> element1) {
		return element1.getName().toString().contains("}source");
	}

	/**
	 * This method checks if {@link JAXBElement} is target node.
	 * @param  element1   is JAXBElement
	 * @return            true if element contains "}target" else return false
	 */
	private boolean isTargetNode(JAXBElement<?> element1) {
		return element1.getName().toString().contains("}target");
	}

	/**
	 * This method checks if {@link Object} is of type {@link JAXBElement}.
	 * @param  element   is Object
	 * @return           true if o is instance of the JAXBElement else return false
	 */
	private boolean isJAXBelement(Object element) {
		return element instanceof JAXBElement<?>;
	}
	
	/**
	 * This method checks if String is numeric.
	 * @param  value   is a String
	 * @return         true if it is Numeric else return false
	 */
	private boolean isNumeric(String value) {
		return value.matches("(\\d*)");
	}
	
	/**
	 * This method increments the number by 1.
	 * @param  num   integer
	 * @return       increment by 1
	 */
	private int increment(int num) {
		return num+1;
	}
}
