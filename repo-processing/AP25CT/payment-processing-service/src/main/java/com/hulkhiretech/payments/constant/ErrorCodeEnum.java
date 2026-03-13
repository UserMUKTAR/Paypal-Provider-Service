package com.hulkhiretech.payments.constant;

import lombok.Getter;

@Getter
public enum ErrorCodeEnum {
	GENERIC_ERROR("30000","Unable to process your request, Try again later"),
    UNABLE_TO_CONNECT_PAYPAL_PROVIDER("30001", "Unable to connect to PayPalProvider. Please try again later."),
	RECON_PAYMENT_FAILED("30002", "Recon payment failed. Transaction failed after 3 attempts");
	
	

    private final String message;
	private final String code;

    ErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

 
}
