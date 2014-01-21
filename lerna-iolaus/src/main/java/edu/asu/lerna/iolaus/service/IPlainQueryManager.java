package edu.asu.lerna.iolaus.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;

import edu.asu.lerna.iolaus.domain.plainqueryobject.IQuery;

public interface IPlainQueryManager {
	
	/**
	 * This method takes queryXml and returns outputXml. 
	 * @param xml is a input queryXml.
	 * @return the outputXml.
	 * @throws JAXBException
	 * @throws SAXException
	 * @throws IOException
	 */
	String executeQuery(String xml) throws JAXBException, SAXException, IOException;

	/**
	 * This method takes String as an input and converts it into Xml. 
	 * @param res is String.
	 * @return the xml.
	 * @throws JAXBException
	 */
	IQuery xmlToQueryObject(String res) throws JAXBException;

	/**
	 * This method takes error message and generates error xml. 
	 * @param errorMsg
	 * @param req
	 * @return
	 */
	String getErrorMsg(String errorMsg, HttpServletRequest req); 
}
