package edu.asu.lerna.iolaus.repository;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.lerna.iolaus.domain.Relation;

public interface RelationRepository extends GraphRepository<Relation>{

	@Override
	@Transactional
	public <U extends Relation> U save(U arg0);
	
	

}
