package com.hulkhiretech.payments.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;

import lombok.Data;

@Data
public class HttpRequest {
	
		private HttpMethod httpMethod;
		private String url;
		private HttpHeaders httpHeaders;
		private Object requestBody;
		
	
	


}
