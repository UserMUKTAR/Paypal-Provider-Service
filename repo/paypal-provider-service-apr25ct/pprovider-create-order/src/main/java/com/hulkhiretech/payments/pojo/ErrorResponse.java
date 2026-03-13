package com.hulkhiretech.payments.pojo;

import lombok.Data;

@Data
public class ErrorResponse {

	 private String errorCode;
	    private String errorMessage;


	  

	    public ErrorResponse(String string, String errorMessage) {
	        this.errorCode = string;
	        this.errorMessage = errorMessage;
	    }

}
