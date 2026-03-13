package com.hulkhiretech.payments.controller;



import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hulkhiretech.payments.pojo.CreateOrderReq;
import com.hulkhiretech.payments.service.interfaces.PaymentService;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/v1/paypal/order")
@Slf4j
public class PaymentController {

	private  final PaymentService paymentService;



	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
		log.info("PaymentController constructor called|paymentService:{}",paymentService);
	}



	@PostMapping
	public String createOrder(@RequestBody CreateOrderReq createOrderReq) {


		log.info("createOrderReq:{}",createOrderReq);

		String response = paymentService.createOrder(createOrderReq);

		return "createOrder returning"+
		" createOrderReq: " + createOrderReq + 
		"\n response:" + response;
	}

}
