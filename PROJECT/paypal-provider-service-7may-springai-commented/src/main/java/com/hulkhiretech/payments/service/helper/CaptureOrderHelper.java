package com.hulkhiretech.payments.service.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.hulkhiretech.payments.constants.Constants;
import com.hulkhiretech.payments.http.HttpRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class CaptureOrderHelper {

	@Value("${paypal.captureOrderUrl}")
	private String captureOrderUrl;

	public HttpRequest prepareHttpRequest(String orderId, String accessToken) {
		HttpHeaders headerObj = new HttpHeaders();
		headerObj.setBearerAuth(accessToken);
		headerObj.setContentType(MediaType.APPLICATION_JSON);

		String url = captureOrderUrl;
		url = url.replace("{orderId}", orderId);
		
		// Create HttpRequest object		
		HttpRequest httpRequest = new HttpRequest();
		httpRequest.setHttpMethod(HttpMethod.POST);

		httpRequest.setUrl(url);
		httpRequest.setHeaders(headerObj);
		httpRequest.setRequestBody(Constants.EMPTY_STRING);
		log.info("httpRequest:" + httpRequest);
		return httpRequest;
	}

}
