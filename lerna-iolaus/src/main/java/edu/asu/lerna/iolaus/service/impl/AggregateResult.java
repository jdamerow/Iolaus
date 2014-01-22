
package edu.asu.lerna.iolaus.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.domain.Label;
import edu.asu.lerna.iolaus.domain.json.impl.JsonNode;
import edu.asu.lerna.iolaus.domain.queryobject.PropertyOf;
import edu.asu.lerna.iolaus.service.IAggregateResult;


/**
 * This class will aggregate the results of outer query with inner query.
 * @author Karan Kothari
 *
 */

@Service
public class AggregateResult implements IAggregateResult {
	
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void aggregateResults(
			Map<String, Map<String, List<Object>>> aggregatedResults,
			Map<String, List<Object>> processedResults,
			Map<String, Label> sourceToTargetLabelMap,
			Map<String, String> oldLabelToNewLabelMap, String sourceLabel) {

		
		Map<Integer,Map<String,List<Object>>> resultsOfTargets=new LinkedHashMap<Integer,Map<String, List<Object>>>();
		int outerForCounter=0;
		//This loop will take union of the results of the inner query. This process will be taken place for each target label used in the query having inner query.
		for(List<String> targetsOfSameSource:sourceToTargetLabelMap.get(sourceLabel).getLabel()){
			for(String sameTargets:targetsOfSameSource){
				if(sourceToTargetLabelMap.containsKey(sameTargets)){
					unionOfResults(aggregatedResults,resultsOfTargets,outerForCounter,sameTargets);
				}	
			}
			outerForCounter++;
		}
		if(resultsOfTargets.size()==0){//if 0 rows are returned
			aggregatedResults.put(sourceLabel, processedResults);
		}else{
			//This will take intersection of results of inner queries with the results of outer query  
			Map<String, List<Object>> sourceLabelResults=intersectionOfResultsWithSourceQuery(processedResults,resultsOfTargets,oldLabelToNewLabelMap,sourceToTargetLabelMap,sourceLabel);	
			
			aggregatedResults.put(sourceLabel, sourceLabelResults);
		}

	}

	/**
	 * This methods takes the union of the results of the labels corresponding to same target label.
	 * @param aggregateResults is a map which stores results for each label which has a relation. 
	 * 		  key=unique label, value=Map(key=labels used in the query, value=List of IJsonNode or IJsonRelation.
	 * @param tempResults is map with key= count of the target labels used in the query and value is the map with 
	 * 		  key=unique label, value=Map(key=labels used in the query, value=List of IJsonNode or IJsonRelation.
	 * @param currentTarget is the counter of the current target label.
	 * @param targetLabel results of this label will take part the union.
	 */
	public void unionOfResults(
			Map<String, Map<String, List<Object>>> aggregateResults,
			Map<Integer, Map<String, List<Object>>> tempResults,
			int currentTarget, String targetLabel) {
		
		Map<String, List<Object>> sameTargetLabelResult;
		if(!tempResults.containsKey(currentTarget)){//This will be true for first inner query (first target label belonging to same target in query)
			tempResults.put(currentTarget, aggregateResults.get(targetLabel));
		}else{
			sameTargetLabelResult=tempResults.get(currentTarget);
			Map<String,List<Object>> innerQueryResult=aggregateResults.get(targetLabel);
			Iterator<Entry<String, List<Object>>> itr = innerQueryResult.entrySet().iterator();
			Map<Integer,Iterator<Object>> iterator=new HashMap<Integer,Iterator<Object>>();
			String[] labels=new String[innerQueryResult.size()];
			int i=0;
			//This loop will create iterator Map with key=label and value=iterator. It will use label of previous query (i.e. query belonging to same target)
			while(itr.hasNext()){
				 Map.Entry<String,List<Object>> pairs = (Map.Entry<String,List<Object>>)itr.next();
				 List<Object> column=(List<Object>) pairs.getValue();
				 labels[i]=pairs.getKey();
				 iterator.put(i++, column.iterator());
			}
			List<Object> sourceColumn;
			//This loop will add results to the previous results(union)
			while(iterator.get(0).hasNext()){
				for(i=0;i<innerQueryResult.size();i++){
					sourceColumn=sameTargetLabelResult.get(labels[i]);
					Object value = iterator.get(i).next();
					sourceColumn.add(value);
				 }
			}
		}	
	}
	
	/**
	 * This method takes the intersection of the results of all the targets having relations with the processed results. 
	 * @param processedResults is a map which has results of a sourceLabel. Its key=label used in query and value=IJsonNode or IJsonRelation.
	 * @param resultsOfTargets is a map which is union of the results of all the target labels.
	 * @param oldLabelToNewLabelMap is a map with key=new unique labels and value=labels used in query. 
	 * @param sourceToTargetLabelMap is a map whose key is label used as a source and value is list of the target labels.
	 * @param sourceLabel is a label corresponding to the source label of the processedResults
	 * @return the result after intersection with source node
	 */
	public Map<String, List<Object>> intersectionOfResultsWithSourceQuery(Map<String, List<Object>> processedResults,Map<Integer, Map<String, List<Object>>> resultsOfTargets,
			Map<String, String> oldLabelToNewLabelMap, Map<String, Label> sourceToTargetLabelMap, String sourceLabel) {
		
		
		Map<Integer,Map<String, List<Object>>>intermediateResults=new LinkedHashMap<Integer,Map<String, List<Object>>>();
		intermediateResults.put(0, processedResults);
		int loopCounter;
		for(loopCounter=0;loopCounter<resultsOfTargets.size();loopCounter++){
			
			Map<String,List<Object>> currentIterationResults=new LinkedHashMap<String,List<Object>>();
			Iterator<Entry<String, List<Object>>> intermediateResultsIterator = intermediateResults.get(loopCounter).entrySet().iterator();
			Iterator<Entry<String, List<Object>>> tempResultsIterator = resultsOfTargets.get(loopCounter).entrySet().iterator();
			Map<Integer,Iterator<Object>> iterator=new HashMap<Integer,Iterator<Object>>();
			int targetCount=1;
			int relationshipCount=1;
			String[] labels=new String[intermediateResults.get(loopCounter).size()+resultsOfTargets.get(loopCounter).size()];	
			int i=0;
			boolean flag=true;
			String targetNode=sourceToTargetLabelMap.get(sourceLabel).getLabel().get(loopCounter).get(0);
			String oldTargetMapping=oldLabelToNewLabelMap.get(targetNode);
			int targetId=0;
			//This loop creates labels for the results of source query
			while(intermediateResultsIterator.hasNext()){
				 Map.Entry<String,List<Object>> pairs = (Map.Entry<String,List<Object>>)intermediateResultsIterator.next();
				 List<Object> column=(List<Object>) pairs.getValue();
				 String label="";
				 if(column.get(0) instanceof JsonNode){
					 if(flag){
						 flag=false;
						 label=PropertyOf.SOURCE.toString();
					 }else{
						 label=PropertyOf.TARGET.toString()+targetCount;
						 if(label.equals(oldTargetMapping)){
							 targetId=i;
						 }
						 targetCount++;
					 }
				 }else{
					 label=PropertyOf.RELATION.toString()+relationshipCount;
					 relationshipCount++;
				 }
				 labels[i]=label;
				 currentIterationResults.put(label, new LinkedList<Object>());
				 iterator.put(i++, column.iterator());
			}
			
			int startOfTempResults=i;
			flag=true;
			//This loop creates labels for the results of Target query
			while(tempResultsIterator.hasNext()){
				 Map.Entry<String,List<Object>> pairs = (Map.Entry<String,List<Object>>)tempResultsIterator.next();
				 List<Object> column=(List<Object>) pairs.getValue();
				 String label="";
				 if(column.get(0) instanceof JsonNode){
					 if(flag){
						 flag=false;
						 label=PropertyOf.SOURCE.toString();
					 }else{
						 label=PropertyOf.TARGET.toString()+targetCount;
						 currentIterationResults.put(label, new LinkedList<Object>());
						 targetCount++;
					 }
				 }else{
					 label=PropertyOf.RELATION.toString()+relationshipCount;
					 currentIterationResults.put(label, new LinkedList<Object>());
					 relationshipCount++;
				 }
				 labels[i]=label;
				 iterator.put(i++, column.iterator());
			}
			List<Object> row;
			while(iterator.get(targetId).hasNext()){
				row=new LinkedList<Object>();
				createRow(row,iterator,startOfTempResults);
				JsonNode node=(JsonNode)row.get(targetId);
				String id=node.getId();
				Map<Integer,List<Object>> matchedRows = new LinkedHashMap<Integer, List<Object>>();
				getMatchedRows(matchedRows,iterator,id,startOfTempResults,labels);
				reinitializeIterators(iterator,resultsOfTargets.get(loopCounter),startOfTempResults);
				cartesianProduct(row,matchedRows,startOfTempResults,currentIterationResults,labels);	
			}
			intermediateResults.put(loopCounter+1, currentIterationResults);
		}
		return intermediateResults.get(loopCounter);
	}
	
	/**
	 * This method create a single instance of the result by iterating from 0 to  startOfTempResults.
	 * @param row is the single instance of the result.
	 * @param iterator is the Iterator which iterates from 0 to startOfTempResults.  
	 * @param lastColumn is the upper bound for the iterator to iterate.
	 */
	public void createRow(List<Object> row,
			Map<Integer, Iterator<Object>> iterator, int lastColumn) {
		
		for(int i=0;i<lastColumn;i++){
			row.add(iterator.get(i).next());
		}
	}
	
	/**
	 * This method stores all the instances into matchedRows which matches with the id.
	 * @param matchedRows is the map of the rows matched with the id.
	 * @param iterator is the Iterator which iterates from startOfTempResults to the size of the labels  
	 * @param id is the node id of the IJsonNode
	 * @param startOfTempResults is the lower bound for the iterator to iterate through.
	 * @param labels is array of string(labels) which are unique labels
	 */
	public void getMatchedRows(Map<Integer, List<Object>> matchedRows,
			Map<Integer, Iterator<Object>> iterator, String id,
			int startOfTempResults, String[] labels) {
		int rowCount=0;
		while(iterator.get(startOfTempResults).hasNext()){//while you have more results to compare with the results of inner query
			JsonNode node=(JsonNode) iterator.get(startOfTempResults).next();
			List<Object> newRow=null;
			boolean flag=false;
			if(node.getId().equals(id)){//if match is found
				newRow=new LinkedList<Object>();
				matchedRows.put(rowCount++, newRow);
				flag=true;
			}	
			for(int j=startOfTempResults+1;j<labels.length;j++){//if any match is found, this loop will create a row
				Object obj=iterator.get(j).next();
				if(flag){
					newRow.add(obj);
				}
			}
		}
	}
	
	/**
	 * This method takes the Cartesian product of the row with the matchedRows. 
	 * @param row is a single instance of the result.
	 * @param matchedRows is the map of the rows with key=counter(integer) and value=row.
	 * @param lastColumn is the upper bound for the iteration.
	 * @param currentResults is the result of inersection of current target with the processedResults. (Output of this method).
	 * @param labels is array of string(labels) which are unique labels.
	 */
	public void cartesianProduct(List<Object> row,
			Map<Integer, List<Object>> matchedRows, int lastColumn,
			Map<String, List<Object>> currentResults, String[] labels) {
		if(matchedRows.size()!=0){
			//System.out.println("Here");
			for(int x=0;x<matchedRows.size();x++){
				int j=0;
				for(;j<lastColumn;j++){
					currentResults.get(labels[j]).add(row.get(j));
				}
				j=lastColumn+1;
				for(Object obj:matchedRows.get(x)){
					currentResults.get(labels[j++]).add(obj);	
				}
			}
		}	
	}
	
	/**
	 * This method reinitialize the iterators
	 * @param iterator is the Iterator which iterates from startOfTempResults to the size of the labels.
	 * @param resultsOfTargets is a map which has the results of current target label. key=label used in query and value=IJsonNode or IJsonRelation 
	 * @param startOfTempResults is the lower bound for the iterator to iterate through.
	 */
	public void reinitializeIterators(
			Map<Integer, Iterator<Object>> iterator,
			Map<String, List<Object>> resultsOfTargets, int startOfTempResults) {
		for(Entry<String, List<Object>> entry:resultsOfTargets.entrySet()){
			iterator.put(startOfTempResults++, entry.getValue().iterator());
		}
	}

}
