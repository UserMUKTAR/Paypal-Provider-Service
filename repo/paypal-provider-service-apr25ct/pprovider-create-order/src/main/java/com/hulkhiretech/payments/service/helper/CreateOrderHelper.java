package com.hulkhiretech.payments.service.helper;



import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.hulkhiretech.payments.constants.Constants;
import com.hulkhiretech.payments.http.HttpRequest;
import com.hulkhiretech.payments.paypal.req.Amount;
import com.hulkhiretech.payments.paypal.req.ExperienceContext;
import com.hulkhiretech.payments.paypal.req.PayPal;
import com.hulkhiretech.payments.paypal.req.PaymentRequest;
import com.hulkhiretech.payments.paypal.req.PaymentSource;
import com.hulkhiretech.payments.paypal.req.PurchaseUnit;
import com.hulkhiretech.payments.pojo.CreateOrderReq;
import com.hulkhiretech.payments.utils.JsonUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class CreateOrderHelper {

	private final JsonUtils jsonUtils;

	@Value("${paypal.createOrderUrl}")
	private String createOrderUrl;

	public HttpRequest prepareHttpRequestForCreateOrder(CreateOrderReq req, String accessToken) {

		HttpHeaders headerObj = new HttpHeaders();
		headerObj.setBearerAuth(accessToken);
		headerObj.setContentType(MediaType.APPLICATION_JSON);
		headerObj.add(Constants.PAY_PAL_REQUEST_ID,req.getTxnRef());

		// Create Amount
		Amount amount = new Amount();
		amount.setCurrencyCode(req.getCurrency());
		amount.setValue(req.getAmount());

		// Create PurchaseUnit
		PurchaseUnit purchaseUnit = new PurchaseUnit();
		purchaseUnit.setAmount(amount);

		// Create ExperienceContext
		ExperienceContext experienceContext = new ExperienceContext();
		experienceContext.setPaymentMethodPreference(Constants.PMP_IMMEDIATE_PAYMENT_REQUIRED);
		experienceContext.setLandingPage(Constants.LANDING_PAGE_LOGIN);
		experienceContext.setShippingPreference(Constants.SP_NO_SHIPPING);
		experienceContext.setUserAction(Constants.UA_PAY_NOW);
		experienceContext.setReturnUrl(req.getReturnUrl());
		experienceContext.setCancelUrl(req.getCancelUrl());

		// Create PayPal
		PayPal paypal = new PayPal();
		paypal.setExperienceContext(experienceContext);

		// Create PaymentSource
		PaymentSource paymentSource = new PaymentSource();
		paymentSource.setPaypal(paypal);

		// Create PayPalOrderRequest
		PaymentRequest paymentRequest = new PaymentRequest();
		paymentRequest.setIntent(Constants.INTENT_CAPTURE);
		paymentRequest.setPurchaseUnits(Collections.singletonList(purchaseUnit));
		paymentRequest.setPaymentSource(paymentSource);

		String requestBodyAsJson = jsonUtils.toJson(paymentRequest);

		log.info("requestBodyAsJson: "+requestBodyAsJson);

		if(requestBodyAsJson == null) {
			log.error("requestBodyAsJson is null");
			throw new RuntimeException("requestBosyAsJson is null");
		}


		HttpRequest httpRequest = new HttpRequest();
		httpRequest.setHttpMethod(HttpMethod.POST);

		httpRequest.setUrl(createOrderUrl);
		httpRequest.setHeaders(headerObj);
		httpRequest.setRequestBody(requestBodyAsJson);
		log.info("hhtpRequest:{}",httpRequest);
		return httpRequest;
	}

}
