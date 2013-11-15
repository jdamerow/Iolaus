package edu.asu.lerna.iolaus.domain.queryobject.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

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
			String path1=path;
			if( o instanceof JAXBElement<?>){
				logger.info("o is instance of JAXBElement<?>");
				JAXBElement<?> element = ((JAXBElement<Object>) o);

				if(element.getName().toString().contains("}and")){
					path1 = path1 +" -> AND";
					logger.info("We have a AND operator");
					IOperator opAnd = (IOperator) element.getValue();
					IRelNodeFinder rnf = new RelNodeFinder();
					IRelNodeFinderData rnfd1 = new RelNodeFinderData();
					rnfd1.setNodeList(rnfd.getNodeList());
					rnfd1.setPath(path1);
					rnfd= rnf.parseOperatorRel(opAnd,rnfd1);
				}else if(element.getName().toString().contains("}or")){
					path1 = path1 +" -> OR";
					logger.info("We have a OR operator");
					IOperator opOr = (IOperator) element.getValue();
					IRelNodeFinder rnf = new RelNodeFinder();
					IRelNodeFinderData rnfd1 = new RelNodeFinderData();
					rnfd1.setNodeList(rnfd.getNodeList());
					rnfd1.setPath(path1);
					rnfd = rnf.parseOperatorRel(opOr,rnfd1);
				}
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
			String path1=path;
			Object element =operatorIterator.next();
			if(element instanceof Relationship){
				path1 = path1 +" -> Relation";
				logger.info("Found Relationship object ");
				IRelationship rel = (IRelationship) element;
				IRelNodeFinder rn = new RelNodeFinder();
				IRelNodeFinderData rnfd1 = new RelNodeFinderData();
				rnfd1.setNodeList(rnfd.getNodeList());
				rnfd1.setPath(path1);
				rnfd = rn.getRelationDetailsRel(rel,rnfd1);

			}else if(element instanceof JAXBElement<?>) {
				JAXBElement<?> element1 = (JAXBElement<?>) element;
				if(element1.getName().toString().contains("}target")){
					path1 = path1 +" -> Target";
					IRelNode relNode = (IRelNode) element1.getValue();
					logger.info("Found Target rel_node object ");
					IRelNodeFinder rnf = new RelNodeFinder();
					IRelNodeFinderData rnfd1 = new RelNodeFinderData();
					rnfd1.setNodeList(rnfd.getNodeList());
					rnfd1.setPath(path1);
					rnfd = rnf.parseRelNodeRel(relNode,rnfd1);
				}

				if(element1.getName().toString().contains("}source")){
					path1 = path1 +" -> Source";
					IRelNode relNode = (IRelNode) element1.getValue();
					logger.info("Found Source rel_node object ");
					IRelNodeFinder rnf = new RelNodeFinder();
					IRelNodeFinderData rnfd1 = new RelNodeFinderData();
					rnfd1.setNodeList(rnfd.getNodeList());
					rnfd1.setPath(path1);
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
			String path1=path;
			Object element =relationshipDetailsIterator.next();
			if(element instanceof JAXBElement<?>){
				JAXBElement<?> element1 = (JAXBElement<?>) element;
				if(element1.getName().toString().contains("}or")){
					path1 = path1 +" -> OR";
					logger.info("We have a OR operator");
					IOperator opOr = (IOperator) element1.getValue();
					IRelNodeFinder rnf = new RelNodeFinder();
					IRelNodeFinderData rnfd1 = new RelNodeFinderData();
					rnfd1.setNodeList(rnfd.getNodeList());
					rnfd1.setPath(path1);
					rnfd = rnf.parseOperatorRel(opOr,rnfd1);
				}else if (element1.getName().toString().contains("}and")){
					path1 = path1 +" -> AND";
					logger.info("We have a AND operator");
					IOperator opAnd = (IOperator) element1.getValue();
					IRelNodeFinder rnf = new RelNodeFinder();
					IRelNodeFinderData rnfd1 = new RelNodeFinderData();
					rnfd1.setNodeList(rnfd.getNodeList());
					rnfd1.setPath(path1);
					rnfd = rnf.parseOperatorRel(opAnd,rnfd1);
				}
			}
		}
		return rnfd;
	}

	@Override 
	public IRelNodeFinderData parseRelNodeRel(IRelNode relNode,IRelNodeFinderData rnfd){

		String path = rnfd.getPath();
		
		if(checkForRelationship(relNode)){
			rnfd.setPath(rnfd.getPath()+" -> Node(Relation)");
			logger.info("the parent object of this object should  be sent to Karan");
			UUID random = UUID.randomUUID();
			rnfd.getNodeList().put(rnfd.getPath()+random,relNode);
		}
		INode node = relNode.getNode();
		List <Object> nodeObjectList = node.getPropertyOrRelationshipOrAnd();
		Iterator<Object> nodeObjectIterator= nodeObjectList.iterator();
		while(nodeObjectIterator.hasNext()){
			String path1=path;
			Object o = nodeObjectIterator.next();
			if(o instanceof Property){

			}else if(o instanceof Relationship){
				
			}else if(o instanceof JAXBElement<?>){
				JAXBElement<?> element1 = (JAXBElement<?>) o;
				if(element1.getName().toString().contains("}or")){
					path1 = path1 +" -> OR";
					logger.info("We have a OR operator");
					IOperator opOr = (IOperator) element1.getValue();
					IRelNodeFinder rnf = new RelNodeFinder();
					IRelNodeFinderData rnfd1 = new RelNodeFinderData();
					rnfd1.setNodeList(rnfd.getNodeList());
					rnfd1.setPath(path1);
					rnfd = rnf.parseOperatorRel(opOr,rnfd1);
				}else if (element1.getName().toString().contains("}and")){
					path1 = path1 +" -> AND";
					logger.info("We have a AND operator");
					IOperator opAnd = (IOperator) element1.getValue();
					IRelNodeFinder rnf = new RelNodeFinder();
					IRelNodeFinderData rnfd1 = new RelNodeFinderData();
					rnfd1.setNodeList(rnfd.getNodeList());
					rnfd1.setPath(path1);
					rnfd = rnf.parseOperatorRel(opAnd,rnfd1);
				}
			}else{
				logger.info(o.getClass()+"-----------");
			}

		}
		return rnfd;
	}
	
	public boolean checkForRelationship(IRelNode relNode){
		INode node = relNode.getNode();
		List <Object> nodeObjectList = node.getPropertyOrRelationshipOrAnd();
		Iterator<Object> nodeObjectIterator= nodeObjectList.iterator();
		while(nodeObjectIterator.hasNext()){
			Object o = nodeObjectIterator.next();
			if(o instanceof Property){

			}else if(o instanceof Relationship){
				return true;
			}else if(o instanceof JAXBElement<?>){
				JAXBElement<?> element1 = (JAXBElement<?>) o;
				if(element1.getName().toString().contains("}or")){
					logger.info("We have a OR operator");
					IOperator opOr = (IOperator) element1.getValue();
					return parseOperatorFromIRelNode(opOr);
				}else if (element1.getName().toString().contains("}and")){
					logger.info("We have a AND operator");
					IOperator opAnd = (IOperator) element1.getValue();
					return parseOperatorFromIRelNode(opAnd);
				}
			}

		}
		return false;
	}
	
	
	public boolean  parseOperatorFromIRelNode(IOperator op){
		List<Object> objectList = op.getSourceOrTargetOrProperty();
		Iterator<Object> operatorIterator = objectList.iterator();
		while(operatorIterator.hasNext()){
			Object element =operatorIterator.next();
			if(element instanceof Relationship){
				return true;
			}
		}
		return false;
	}
}
