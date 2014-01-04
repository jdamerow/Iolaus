package edu.asu.lerna.iolaus.service;

import java.util.List;
import java.util.Map;

import edu.asu.lerna.iolaus.domain.Label;

public interface IAggregateResult {
	/**
	 * This method aggregates the results of a source label once we have results for all the target labels.
	 * @param aggregatedResults is a map which stores the results for source labels in a query. Key is Label and 
	 * 		  value is Map with key=label used in query and value=IJsonNode or IJsonRelation.
	 * @param processedResults is a map which stores result for a sourceLabel. key=label used in query and value=IJsonNode or IJsonRelation.
	 * @param sourceToTargetLabelMap is a map whose key is label used as a source and value is list of the target labels.
	 * @param oldLabelToNewLabelMap is a map with key=new unique labels and value=labels used in query. 
	 * @param sourceLabel is a label corresponding to the source label of the processedResults.
	 */
	public void aggregateResults(
			Map<String, Map<String, List<Object>>> aggregatedResults,
			Map<String, List<Object>> processedResults,
			Map<String, Label> sourceToTargetLabelMap, Map<String, String> oldLabelToNewLabelMap, String sourceLabel); 
}
