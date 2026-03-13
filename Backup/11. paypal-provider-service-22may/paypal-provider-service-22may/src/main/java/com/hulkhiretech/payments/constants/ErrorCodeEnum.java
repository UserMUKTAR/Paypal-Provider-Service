package com.hulkhiretech.payments.constants;

import lombok.Getter;

@Getter
public enum ErrorCodeEnum {
	GENERIC_ERROR("40000", "Unable to process you rquest. Try again later"),
	TEMP1_ERROR("40001", "TEMP1 error."),
    TEMP2_ERROR("40002", "TEMP2 error."),
    TEMP3_ERROR("40003", "TEMP3 error."),
    NAME_NULL("40004", "Name is empty. Please check & try again."),
    UNABLE_TO_CONNECT_PAYPAL("40005", "Unable to connect to Paypal, please try again later."),
    PAYPAL_ERROR("40006", "<PREPARE DYNAMIC MESSAGE from Paypal error response>");

    private final String code;
    private final String message;

    ErrorCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
