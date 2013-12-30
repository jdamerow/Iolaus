package edu.asu.lerna.iolaus.converters;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import edu.asu.lerna.iolaus.domain.implementation.Role;
import edu.asu.lerna.iolaus.service.IRoleManager;

public class StringListConverter implements Converter<List<String>, List<Role>>{

	@Autowired
	private IRoleManager manager;
	
	@Override
	public List<Role> convert(List<String> arg0) {
		
		List<Role> roleList = new ArrayList<Role>();
		for(String str : arg0){
			roleList.add(manager.getRole(str));
		}
		return roleList;
	}
}
