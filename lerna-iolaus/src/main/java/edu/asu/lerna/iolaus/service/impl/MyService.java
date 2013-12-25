package edu.asu.lerna.iolaus.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import edu.asu.lerna.iolaus.domain.implementation.Role;

@Service
public class MyService {

    private ConversionService conversionService;

	@Autowired
    public MyService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }
    
    public void doIt() {
        System.out.println("Can String to Role convert : " + this.conversionService.canConvert(String.class, Role.class));
    }
}
