package edu.asu.lerna.iolaus.rest;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.asu.lerna.iolaus.service.IDeleteDatasetManager;

/**
 * Controller for deleting the datasets. Dataset stored in multiple dataset can be deleted in a single request.
 * @author Karan Kothari
 *
 */
@Controller
public class DeleteDatasetController {
	
	private static final Logger logger = LoggerFactory
			.getLogger(DeleteDatasetController.class);
	
	@Autowired
	private IDeleteDatasetManager deleteDatasetManager;
	
	@ResponseBody
	@RequestMapping(value = "/rest/data/deleteDataset/{dataset}", method = RequestMethod.POST)
	public String deleteDataset(@PathVariable("dataset") String dataset,HttpServletRequest req, ModelMap model,Principal principal){
		
		logger.info("deleting the dataset");
		
		boolean flag=deleteDatasetManager.deleteDataset(dataset);
		
		if(flag){
			logger.info("dataset deleted");
			return "deleted";
		}
		else{
			logger.info("dataset deletion failed");
			return "failed";
		}
		
	}
	
}
