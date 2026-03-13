package com.hulkhiretech.payments.constant;

import lombok.Getter;

@Getter
public enum ErrorCodeEnum {
	GENERIC_ERROR("30000", "Unable to process you rquest. Try again later"),
    UNABLE_TO_CONNECT_PAYPAL_PROVIDER("30001", 
    		"Unable to connect to PaypalProvider, please try again later."),
	RECON_PAYMEN_FAILED("30002", "Recon payment failed. Transaction failed after 3 attempts");

    private final String code;
    private final String message;

    ErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
