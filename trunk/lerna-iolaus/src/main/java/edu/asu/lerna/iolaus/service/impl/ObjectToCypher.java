package edu.asu.lerna.iolaus.service.impl;

import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import edu.asu.lerna.iolaus.domain.queryobject.INode;
import edu.asu.lerna.iolaus.domain.queryobject.IOperator;
import edu.asu.lerna.iolaus.domain.queryobject.IProperty;
import edu.asu.lerna.iolaus.domain.queryobject.IQuery;
import edu.asu.lerna.iolaus.domain.queryobject.IRelNode;
import edu.asu.lerna.iolaus.domain.queryobject.IRelationship;
import edu.asu.lerna.iolaus.domain.queryobject.PropertyOf;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Node;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Property;
import edu.asu.lerna.iolaus.domain.queryobject.impl.Relationship;
import edu.asu.lerna.iolaus.service.IObjectToCypher;

public class ObjectToCypher implements IObjectToCypher {
	
	private static final Logger logger = LoggerFactory
			.getLogger(Node.class);

	@Autowired 
	private IObjectToCypher otc;
	
	private int currentSource=1;
	private int currentTarget=0;
	private int currentRelationship=0;
	
	private String start="Start ";
	private String match="match ";
	private String where="where ";
	private String ret="return ";
	
	@Override
	public String objectToCypher(IQuery query) {
		
		INode node=query.getNode();
		IRelationship relationship=query.getRelationship();
		
		if(node==null){
			otc.nodeObject(node,PropertyOf.SOURCE);
		}else if(relationship==null){
			otc.relationshipObject(relationship);
		}else{
			return null;
		}
		
		return null;
	}

	@Override
	public void nodeObject(INode node,PropertyOf propertyOf) {
		logger.info("Node return status : "+node.isReturn());
		List<Object> nodeDetails = node.getPropertyOrRelationshipOrAnd();
		Iterator<Object> nodeDetailsIterator = nodeDetails.iterator();
		while(nodeDetailsIterator.hasNext()){
			JAXBElement<?> element = (JAXBElement<Object>) nodeDetailsIterator.next();

			if(element.getName().toString().contains("}and")){
				IOperator opAnd = (IOperator) element.getValue();
				otc.operatorObject(opAnd,propertyOf);
				
			}else if(element.getName().toString().contains("}or")){
				
				IOperator opOr = (IOperator) element.getValue();
				otc.operatorObject(opOr,propertyOf);
			}else if(element.getName().toString().contains("}Property")){
    			IProperty prop = (IProperty) element.getValue();
    			otc.propertyObject(prop,propertyOf);
    		}
		}
		
	}

	@Override
	public void relationshipObject(IRelationship relationship) {
		logger.info("Relationship return status : "+relationship.isReturn());
		otc.incrementRelationship();
		List<Object> relationshipDetails = relationship.getSourceOrTargetOrProperty();
		Iterator<Object> relationshipDetailsIterator = relationshipDetails.iterator();
		while(relationshipDetailsIterator.hasNext()){

			Object element =relationshipDetailsIterator.next();
    		if(element instanceof Property){
    			IProperty prop = (IProperty) element;
    			otc.propertyObject(prop,PropertyOf.RELATION);
    		}else if(element instanceof JAXBElement<?>){
    			JAXBElement<?> element1 = (JAXBElement<?>) element;
    			if(element1.getName().toString().contains("}or")){
    				IOperator opOr = (IOperator) element1.getValue();
    				otc.operatorObject(opOr,PropertyOf.RELATION);
    			}
    			String node1=(PropertyOf.SOURCE).toString()+currentSource;
    			String node2=(PropertyOf.TARGET).toString()+currentTarget;
    			String relation=(PropertyOf.RELATION).toString()+currentRelationship;
    			if(element1.getName().toString().contains("}target")){
    				boolean direction=true;
    				otc.addRelationToMatch(node1, node2, relation, direction);
        			IRelNode relNode = (IRelNode) element1.getValue();
        			otc.relNodeObject(relNode,PropertyOf.TARGET);
    			}
    			
    			if(element1.getName().toString().contains("}source")){
    				boolean direction=false;
    				otc.addRelationToMatch(node1, node2, relation, direction);
        			IRelNode relNode = (IRelNode) element1.getValue();
        			otc.relNodeObject(relNode,PropertyOf.SOURCE);
    			}	
    		}
		}	
	}

	@Override
	public void operatorObject(IOperator op,PropertyOf propertyOf) {
    	List<Object> objectList = op.getSourceOrTargetOrProperty();
    	Iterator<Object> operatorIterator = objectList.iterator();
    	if(propertyOf.equals(PropertyOf.RELATION)){
    		otc.incrementTarget();
    	}
    	while(operatorIterator.hasNext()){
    		Object element =operatorIterator.next();
    		if(element instanceof Property){
    			IProperty prop = (IProperty) element;
    			otc.propertyObject(prop,propertyOf);
    		}else if(element instanceof Relationship){
    			IRelationship rel = (IRelationship) element;
    			otc.relationshipObject(rel);
    		}else if(element instanceof JAXBElement<?>) {
    			JAXBElement<?> element1 = (JAXBElement<?>) element;
    			String node1=(PropertyOf.SOURCE).toString()+currentSource;
    			String node2=(PropertyOf.TARGET).toString()+currentTarget;
    			String relation=(PropertyOf.RELATION).toString()+currentRelationship;
    			if(element1.getName().toString().contains("}target")){
    				boolean direction=true;
    				otc.addRelationToMatch(node1, node2, relation, direction);
        			IRelNode relNode = (IRelNode) element1.getValue();
        			otc.relNodeObject(relNode,PropertyOf.TARGET);
    			}else if(element1.getName().toString().contains("}source")){
    				boolean direction=false;
    				otc.addRelationToMatch(node1, node2, relation, direction);
        			IRelNode relNode = (IRelNode) element1.getValue();
        			otc.relNodeObject(relNode,PropertyOf.SOURCE);
    			}	
    		}
    	}		
	}

	
	@Override
	public void propertyObject(IProperty prop,PropertyOf propertyOf) {
    	if(prop.getEnd()!=null){
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
    	}		
	}

	@Override
	public void relNodeObject(IRelNode relNode,PropertyOf propertyOf) {

    	INode node = relNode.getNode();
    	List <Object> nodeObjectList = node.getPropertyOrRelationshipOrAnd();
    	Iterator<Object> nodeObjectIterator= nodeObjectList.iterator();
    	while(nodeObjectIterator.hasNext()){
    		Object o = nodeObjectIterator.next();
    		if(o instanceof Property){
    			IProperty prop = (IProperty) o;
    			otc.propertyObject(prop,propertyOf);
    		}
    		
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
	public void incrementSource() {
		currentSource+=1;
	}

	@Override
	public void addRelationToMatch(String node1, String node2, String relation,boolean direction) {
		if(!match.equals("match ")){
			match+=",";
		}
		match+=node1;
		if(direction){
			match+="-"+relation+"->";
		}else{
			match+="<-"+relation+"-";
		}
		match+=node2+" ";
	}

}
