package edu.asu.lerna.iolaus.converters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.neo4j.fieldaccess.DynamicProperties;

public class DynamicPropertyToStringConverter implements Converter<DynamicProperties, String> {

	private static final Logger logger = LoggerFactory.getLogger(StringToDynamicPropertyConverter.class);
	
	@Autowired
	DynamicProperties dynamicProperties;
	

	@Override
	public String convert(DynamicProperties properties) {
		return properties.toString();
	}

}
