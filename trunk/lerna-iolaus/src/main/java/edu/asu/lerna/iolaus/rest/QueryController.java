package edu.asu.lerna.iolaus.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import edu.asu.lerna.iolaus.service.IQueryManager;

@Controller
public class QueryController {

	@Autowired
	private IQueryManager queryManager;
}
