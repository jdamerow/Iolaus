package edu.asu.lerna.iolaus.domain.queryobject.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.asu.lerna.iolaus.domain.queryobject.INode;
import edu.asu.lerna.iolaus.domain.queryobject.IOperator;
import edu.asu.lerna.iolaus.domain.queryobject.IRelNode;
import edu.asu.lerna.iolaus.domain.queryobject.IRelNodeFinder;
import edu.asu.lerna.iolaus.domain.queryobject.IRelNodeFinderData;
import edu.asu.lerna.iolaus.domain.queryobject.IRelationship;

public class RelNodeFinder implements IRelNodeFinder {

	private static final Logger logger = LoggerFactory
			.getLogger(RelNodeFinder.class);
	

	@Override
	public IRelNodeFinderData getNodeRel(edu.asu.lerna.iolaus.domain.queryobject.INode node,IRelNodeFinderData rnfd){
		
		List<Object> nodeDetails = node.getPropertyOrRelationshipOrAnd();
		Iterator<Object> nodeDetailsIterator = nodeDetails.iterator();
		String path = rnfd.getPath();
		while(nodeDetailsIterator.hasNext()){
			Object o  = nodeDetailsIterator.next();
			
			if( o instanceof JAXBElement<?>){
				logger.info("o is instance of JAXBElement<?>");
			}
			JAXBElement<?> element = ((JAXBElement<Object>) o);

			if(element.getName().toString().contains("}and")){
				path = path +" -> AND";
				logger.info("We have a AND operator");
				IOperator opAnd = (IOperator) element.getValue();
				IRelNodeFinder rnf = new RelNodeFinder();
				IRelNodeFinderData rnfd1 = new RelNodeFinderData();
				rnfd1.setNodeList(rnfd.getNodeList());
				rnfd1.setPath(path);
				rnfd= rnf.parseOperatorRel(opAnd,rnfd1);
			}else if(element.getName().toString().contains("}or")){
				path = path +" -> OR";
				logger.info("We have a OR operator");
				IOperator opOr = (IOperator) element.getValue();
				IRelNodeFinder rnf = new RelNodeFinder();
				IRelNodeFinderData rnfd1 = new RelNodeFinderData();
				rnfd1.setNodeList(rnfd.getNodeList());
				rnfd1.setPath(path);
				rnfd = rnf.parseOperatorRel(opOr,rnfd1);
			}
		}

		return rnfd;
	}
	

	@Override 
	public IRelNodeFinderData  parseOperatorRel(IOperator op,IRelNodeFinderData rnfd){
		List<Object> objectList = op.getSourceOrTargetOrProperty();
		Iterator<Object> operatorIterator = objectList.iterator();
		String path = rnfd.getPath();
		while(operatorIterator.hasNext()){
			
			Object element =operatorIterator.next();
			if(element instanceof Relationship){
				path = path +" -> Relation";
				logger.info("Found Relationship object ");
				IRelationship rel = (IRelationship) element;
				IRelNodeFinder rn = new RelNodeFinder();
				IRelNodeFinderData rnfd1 = new RelNodeFinderData();
				rnfd1.setNodeList(rnfd.getNodeList());
				rnfd1.setPath(path);
				rnfd = rn.getRelationDetailsRel(rel,rnfd1);
				
			}else if(element instanceof JAXBElement<?>) {
				JAXBElement<?> element1 = (JAXBElement<?>) element;
				if(element1.getName().toString().contains("}target")){
					path = path +" -> Target";
					IRelNode relNode = (IRelNode) element1.getValue();
					logger.info("Found Target rel_node object ");
					IRelNodeFinder rnf = new RelNodeFinder();
					IRelNodeFinderData rnfd1 = new RelNodeFinderData();
					rnfd1.setNodeList(rnfd.getNodeList());
					rnfd1.setPath(path);
					rnfd = rnf.parseRelNodeRel(relNode,rnfd1);
				}

				if(element1.getName().toString().contains("}source")){
					path = path +" -> Source";
					IRelNode relNode = (IRelNode) element1.getValue();
					logger.info("Found Source rel_node object ");
					IRelNodeFinder rnf = new RelNodeFinder();
					IRelNodeFinderData rnfd1 = new RelNodeFinderData();
					rnfd1.setNodeList(rnfd.getNodeList());
					rnfd1.setPath(path);
					rnfd = rnf.parseRelNodeRel(relNode,rnfd1);
				}


			}
		}
		return rnfd;
	}
	
	
	@Override 
	public IRelNodeFinderData getRelationDetailsRel(IRelationship relationship,IRelNodeFinderData rnfd){
		List<Object> relationshipDetails = relationship.getSourceOrTargetOrProperty();
		Iterator<Object> relationshipDetailsIterator = relationshipDetails.iterator();
		String path = rnfd.getPath();
		while(relationshipDetailsIterator.hasNext()){
			
			Object element =relationshipDetailsIterator.next();
			if(element instanceof JAXBElement<?>){
    			JAXBElement<?> element1 = (JAXBElement<?>) element;
    			if(element1.getName().toString().contains("}or")){
    				path = path +" -> OR";
    				logger.info("We have a OR operator");
    				IOperator opOr = (IOperator) element1.getValue();
    				IRelNodeFinder rnf = new RelNodeFinder();
					IRelNodeFinderData rnfd1 = new RelNodeFinderData();
					rnfd1.setNodeList(rnfd.getNodeList());
					rnfd1.setPath(path);
    				rnfd = rnf.parseOperatorRel(opOr,rnfd1);
    			}
    		}
		}
		return rnfd;
	}
	
	@Override 
	public IRelNodeFinderData parseRelNodeRel(IRelNode relNode,IRelNodeFinderData rnfd){
		
    	INode node = relNode.getNode();
    	
    	List <Object> nodeObjectList = node.getPropertyOrRelationshipOrAnd();
    	Iterator<Object> nodeObjectIterator= nodeObjectList.iterator();
    	while(nodeObjectIterator.hasNext()){
    		Object o = nodeObjectIterator.next();
    		if(o instanceof Property){

    		}else if(o instanceof Relationship){
    			rnfd.setPath(rnfd.getPath()+" -> Node(Relation)");
    			logger.info("the parent object of this object should  be sent to Karan");
    			Random generator2 = new Random( 19580427 );
    			rnfd.getNodeList().put(rnfd.getPath()+generator2.nextInt(),relNode);
    		}
    		
    	}
    	return rnfd;
    }
}
