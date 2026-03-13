package com.hulkhiretech.payments.service.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.hulkhiretech.payments.constant.Constant;
import com.hulkhiretech.payments.constant.ErrorCodeEnum;
import com.hulkhiretech.payments.dto.TransactionDTO;
import com.hulkhiretech.payments.exception.ProcessingServiceException;
import com.hulkhiretech.payments.http.HttpRequest;
import com.hulkhiretech.payments.paypalprovider.PPErrorResponse;
import com.hulkhiretech.payments.paypalprovider.PPOrder;
import com.hulkhiretech.payments.util.JsonUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PPGetOrderHelper {
	

	

	private final JsonUtils jsonUtils;
	
	@Value("${paypalprovider.getOrderUrl}")
	private String ppGetOrderUrl;
	
	public HttpRequest prepareHttpRequest(TransactionDTO txn) {
		HttpHeaders httpHeader = new HttpHeaders();
		httpHeader.setContentType(MediaType.APPLICATION_JSON);
		
		
		String url = ppGetOrderUrl;
		url = url.replace(Constant.ORDER_ID, txn.getProviderReference());
		
		
		
		HttpRequest httpRequest = new HttpRequest();
		httpRequest.setUrl(url);
		httpRequest.setHttpMethod(HttpMethod.GET);
		httpRequest.setHttpHeaders(httpHeader);
		httpRequest.setRequestBody(Constant.EMPTY_STRING);
		httpRequest.setDestinationServiceName(Constant.PAYPAL_PROVIDER_SERVICE);
		return httpRequest;
		
	}
	
	public PPOrder processGetOrderResponse(ResponseEntity<String> getOrderResponse) {
		String responseBody = getOrderResponse.getBody();
		log.info("ResponseBody:" + responseBody);
		
		
		if (getOrderResponse.getStatusCode() == HttpStatus.OK) {// success
			
			PPOrder resObj1 = jsonUtils.fromJson(responseBody,PPOrder.class);
			log.info("resObj {}",resObj1);
			
			if(resObj1 != null 
					&& resObj1.getOrderId() != null  && !resObj1.getOrderId().isEmpty()
					&& resObj1.getPaypalStatus() != null && !resObj1.getPaypalStatus().isEmpty()) {
				
				//SUCCESS scenario
				
				log.info("SUCCESS 200 with valid id and status ");
				
				
				log.info("orderRes:{}", resObj1);
				
				
				return resObj1;
				
			}
				log.info("SUCCESS 200 but invalid id and status ");
			
					
		}
		
		// FAILED RESPONSE
		//if we get 4xx or 5xx from paypal, then we need to return the error response as it is
		if (getOrderResponse.getStatusCode().is4xxClientError()
				|| getOrderResponse.getStatusCode().is5xxServerError()) {
			log.error("Paypal error response: {}", responseBody);

			PPErrorResponse errorRes = jsonUtils.fromJson(responseBody,PPErrorResponse.class);
			log.error("errorRes: {}",errorRes);
			
			throw new ProcessingServiceException(
					errorRes.getErrorCode(),
					errorRes.getErrorMessage(),
					HttpStatus.valueOf(getOrderResponse.getStatusCode().value()));
		} 
		// Anything other than 4xx or 5xx, generic exception handling.
		log.error("Got unexpected response from Paypal processing. "
				+ "Returning GENERIC ERROR: {}", getOrderResponse);

		
		throw new ProcessingServiceException(
				ErrorCodeEnum.GENERIC_ERROR.getCode(),
				ErrorCodeEnum.GENERIC_ERROR.getMessage(),
				HttpStatus.INTERNAL_SERVER_ERROR);
		
		
	}
	

}
