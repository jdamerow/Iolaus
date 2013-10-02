package edu.asu.spring.iolaus.domain;

import java.util.Set;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.annotation.RelatedToVia;

@NodeEntity
public class Node {
	
  @GraphId Long id;

  @Indexed(type = "", indexName = "search")
  String title;

  Person director;

  @RelatedTo(type="ACTS_IN", direction = INCOMING)
  Set<Person> actors;

  @RelatedToVia(type = "RATED")
  Iterable<Rating> ratings;

  @Query("start movie=node({self}) match 
          movie-->genre<--similar return similar")
  Iterable<Movie> similarMovies;
}