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
		log.info("PPGetOrderHelper.prepareHttpRequest() called txn: {}", txn);
		HttpHeaders httpHeader = new HttpHeaders();
		httpHeader.setContentType(MediaType.APPLICATION_JSON);
		
		String url = ppGetOrderUrl;
		url = url.replace(Constant.ORDER_ID, txn.getProviderReference());
		
		HttpRequest httpRequest = new HttpRequest();
		httpRequest.setUrl(url);
		httpRequest.setHttpMethod(HttpMethod.GET);
		httpRequest.setHeaders(httpHeader);
		httpRequest.setRequestBody(Constant.EMPTY_STRING);
		httpRequest.setDestinationServiceName(Constant.PAYPAL_PROVIDER_SERVICE);
		log.info("httpRequest:" + httpRequest);
		return httpRequest;
	}
	
	public PPOrder processGetOrderResponse(ResponseEntity<String> getOrderResponse) {
		String responseBody = getOrderResponse.getBody();
		log.info("responseBody:" + responseBody);

		if (getOrderResponse.getStatusCode() == HttpStatus.OK) {// Success
			PPOrder resObj = jsonUtils.fromJson(responseBody, PPOrder.class);
			log.info("resObj:" + resObj);

			if (resObj != null 
					&& resObj.getOrderId() != null && !resObj.getOrderId().isEmpty() 
					&& resObj.getPaypalStatus() != null && 
					!resObj.getPaypalStatus().isEmpty()) {
				// SUCCESS scenario

				log.info("SUCCESS 200 with valid id and status");

				log.info("resObj:{}", resObj);

				return resObj;

			} 
			log.error("SUCCESS 200 but invalid id or status");
		}

		// Failed response
		//if we get 4xx or 5xx from paypal, 
		//then we need to return the error response as it is
		if (getOrderResponse.getStatusCode().is4xxClientError()
				|| getOrderResponse.getStatusCode().is5xxServerError()) {
			log.error("Paypal error response: {}", responseBody);
			
			PPErrorResponse errorRes = jsonUtils.fromJson(
					responseBody, PPErrorResponse.class);
			log.info("errorRes:{}", errorRes);
			
			throw new ProcessingServiceException(
					errorRes.getErrorCode(),
					errorRes.getErrorMessage(), 
					HttpStatus.valueOf(getOrderResponse.getStatusCode().value()));
		} 

		// Anything other than 4xx or 5xx, generic exception handling.
		log.error("Got unexpected response from Paypal processing. "
				+ "Returnign GENERIC ERROR: {}", getOrderResponse);

		throw new ProcessingServiceException(
				ErrorCodeEnum.GENERIC_ERROR.getCode(),
				ErrorCodeEnum.GENERIC_ERROR.getMessage(), 
				HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
}
