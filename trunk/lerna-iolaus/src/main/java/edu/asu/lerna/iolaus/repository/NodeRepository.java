package edu.asu.lerna.iolaus.repository;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.lerna.iolaus.domain.Node;
import edu.asu.lerna.iolaus.domain.Relation;

public interface NodeRepository extends GraphRepository<Relation>{

	//Node findById(long id);
	
	@Override
	@Transactional
	public <U extends Relation> U save(U arg0);
	//public <U extends Node> U save(U arg0);
}
