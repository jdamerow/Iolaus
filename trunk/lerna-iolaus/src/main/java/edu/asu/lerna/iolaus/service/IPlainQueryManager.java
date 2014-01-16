package edu.asu.lerna.iolaus.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import edu.asu.lerna.iolaus.domain.plainqueryobject.IQuery;

public interface IPlainQueryManager {
	
	String executeQuery(String xml) throws JAXBException, SAXException, IOException;

	IQuery xmlToQueryObject(String res) throws JAXBException;

	String getErrorMsg(String errorMsg, HttpServletRequest req); 
}
