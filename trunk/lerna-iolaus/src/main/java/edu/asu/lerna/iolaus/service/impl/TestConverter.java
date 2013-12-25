package edu.asu.lerna.iolaus.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.domain.implementation.Role;

@Service
public class TestConverter {

    private ConversionService conversionService;

	@Autowired
    public TestConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }
    
    public void doIt() {
        System.out.println("Can String to Role convert : " + this.conversionService.canConvert(String.class, Role.class));
    }
}
