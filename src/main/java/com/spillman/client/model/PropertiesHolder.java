package com.spillman.client.model;

import java.util.HashMap;
import java.util.Map;

public class PropertiesHolder {

	private static final Map<String, Object> properties = new HashMap<>();

	public static void setProperties(Map<String, Object> properties) {
		PropertiesHolder.properties.clear();
		PropertiesHolder.properties.putAll(properties);
	}

	public static String getPropertyValue(String name) {
		return (String) properties.get(name);
	}

	public static Map<String, Object> getNestedProperty(String name) {
		return (Map<String, Object>) properties.get(name);
	}

	public static Map<String, Object> getProperties() {
		return new HashMap<>(properties);
	}
}