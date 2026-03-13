package com.hulkhiretech.payments.utils;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

/**
 * Uses Jackson to convert Java objects to JSON and vice versa.
 */
@Component
@RequiredArgsConstructor
public class JsonUtils {
	
	private final ObjectMapper objectMapper;
	
	/**
	 * Converts a Java object to a JSON string.
	 * 
	 * @param obj the object to convert
	 * @return the JSON string representation of the object
	 */
	public String toJson(Object obj) {
		String requestBodyAsJson = null;
		
		try {
			requestBodyAsJson = objectMapper.writerWithDefaultPrettyPrinter(
					).writeValueAsString(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        return requestBodyAsJson;
	}

	/**
	 * Converts a JSON string to a Java object of the specified class.
	 * 
	 * @param json  the JSON string to convert
	 * @param clazz the class of the object to convert to
	 * @return the converted object
	 */
	public <T> T fromJson(String json, Class<T> clazz) {		
		T response = null;
		
		try {
			response = objectMapper.readValue(json, clazz);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return response;
	}
	

}
