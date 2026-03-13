package com.hulkhiretech.payments.http;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class HttpServiceEngine {

	private RestClient restClient;
	
	public HttpServiceEngine(RestClient.Builder restClientBuilder) {
		restClient = restClientBuilder.build();
		log.info("restClient created|restClient:" + restClient);
	}
	
	public ResponseEntity<String> makeHttpCall(HttpRequest httpRequest) {
		
		
        
		ResponseEntity<String> responseEntity = restClient.method(httpRequest.getHttpMethod())
			.uri(httpRequest.getUrl())
			.headers( header-> header.addAll(httpRequest.getHttpHeaders()))// Lamda expression, functional coding
			.body(httpRequest.getRequestBody())
			.retrieve()
			.toEntity(String.class);
		
		
		log.info("responseEntity:" + responseEntity);
		
		return responseEntity;
	}
	
	
}
