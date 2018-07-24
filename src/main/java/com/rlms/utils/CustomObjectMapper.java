package com.rlms.utils;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;

public class CustomObjectMapper extends ObjectMapper{

	public CustomObjectMapper(){
		SimpleModule module =  new SimpleModule("HTML XSS Serializer", new Version(1,0,0,"FINAL"));
		//module.addSerializer()
	}
}
