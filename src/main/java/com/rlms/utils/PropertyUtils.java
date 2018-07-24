package com.rlms.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class PropertyUtils extends PropertyPlaceholderConfigurer{

	private static Map<String, String> propertiesMap = new HashMap<String, String>();
	
	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties properties) {
		super.processProperties(beanFactory, properties);
		for(Map.Entry<Object, Object> key : properties.entrySet()){
			String keyStr = key.getKey().toString();
			propertiesMap.put(keyStr, this.setNullIfEmpty(properties.getProperty(keyStr)));
		}
	}
	
	private String setNullIfEmpty(Object value){
		String val = null;
		if(null != value){
			val = value.toString();
		}
		return val;
		
	}
	
	public static String getPrpertyFromContext(String key){
		return propertiesMap.get(key);
	}
}
