package edu.asu.lerna.iolaus.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.domain.queryobject.INode;
import edu.asu.lerna.iolaus.domain.queryobject.IProperty;

@Service(value="default")
public class XMLToCypherConverterForMultipleDataset extends
		XMLToCypherConverter {
	
	@Override
	public String createStableQuery(INode node, String dataset, String nodeIndex) {
		List<QueryFragment> queryFragments = new ArrayList<QueryFragment>(); 
		
		while(node != null) {
			QueryFragment queryFragment = createQueryFragment(node);
			queryFragments.add(queryFragment);
			node = queryFragment.getNextNode();
		}
		
		String startClause = "";
		String matchClause = createMatch(queryFragments); 
		String whereClause = createWhere(queryFragments, dataset);
		String returnClause = createReturn(queryFragments);
		//System.out.println(startClause + " " + matchClause + " " + whereClause + " " + returnClause);
		
		String query = buildQuery(startClause, matchClause, whereClause, returnClause);
		
		String jsonQuery = cypherToJson.cypherToJson(query);
		
		System.out.println(jsonQuery);
		
		return jsonQuery;
	}
	
	@Override
	protected String createWhere(List<QueryFragment> queryFragments,
			String dataset) {
		
		String whereClause = w;
		int nodeIndex = 1;
		int relIndex = 1;
		boolean moreThanOnePropertyInWhereClause = false;
		
		for(QueryFragment fragment : queryFragments) {
			String nodeLabel = "n" + nodeIndex;
			String relLabel = "r" + relIndex;
			
			List<IProperty> nodeProperties = fragment.getNodeProperties();
			
			if(nodeProperties != null) {
				//start from 1 since 1st property is already included in Start clause
				for(int i = 0; i < nodeProperties.size(); i++) {
					IProperty property = nodeProperties.get(i);
					if(moreThanOnePropertyInWhereClause)
						whereClause += " and ";
					String expression = null;
					if(isNumeric(property.getValue())) {
						expression= property.getName() + "=" + property.getValue();
					} else {
						expression = property.getName() + "=~\"" + property.getValue() + "\"";
					}
					whereClause += nodeLabel + "." + expression;
					moreThanOnePropertyInWhereClause = true;
				}
			}
			
			List<IProperty> relationshipProperties = fragment.getRelationshipProperties();
			if(relationshipProperties != null) {
				for(int i = 0; i < relationshipProperties.size(); i++) {
					IProperty property = relationshipProperties.get(i);
					
					if(property.getStart() != null) {
						whereClause += " and ";
						String expression = property.getName() + ">=" + property.getStart();
						whereClause += relLabel + "." + expression;
					}
					
					if(property.getEnd() != null) {
						whereClause += " and ";
						String expression = property.getName() + "<=" + property.getEnd();
						whereClause += relLabel + "." + expression;
					}
					
					if(property.getValue() != null) {
						whereClause += " and ";
						String expression;
						if(isNumeric(property.getValue())) {
							expression= property.getName() + "=" + property.getValue();
						} else {
							expression = property.getName() + "=~\"" + property.getValue() + "\"";
						}
						whereClause += relLabel + "." + expression;
					}
				}
			}
			
			nodeIndex++;
			relIndex++;
		}
		
		
		return whereClause;
	}
	
	

}
