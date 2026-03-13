package com.hulkhiretech.payments.service.impl;



import org.springframework.stereotype.Service;

import com.hulkhiretech.payments.pojo.CreateOrderReq;
import com.hulkhiretech.payments.service.TokenService;
import com.hulkhiretech.payments.service.interfaces.PaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentsServiceImpl implements PaymentService {

	
	private final TokenService tokenService;
	
	@Override
	public String createOrder(CreateOrderReq req) {
		
		log.info("crateOrder called|req:{}", req);
		
		// TODO write the business logic to create order
		
		String accessToken = tokenService.getAccessToken();
		
		return "returning from service impl | accessToken :" +accessToken;
	}

	
}
