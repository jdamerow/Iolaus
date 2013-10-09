package edu.asu.lerna.iolaus.repository;

import edu.asu.lerna.iolaus.domain.Relation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
public class RelationRepositoryImpl implements RelationRepository {
	@Autowired
	Neo4jTemplate template;
	public void saveRelation(Relation r){
		template.save(r);
	}

}
