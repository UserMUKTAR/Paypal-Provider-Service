package com.hulkhiretech.payments.paypalprovider;

import lombok.Data;

@Data
public class PPErrorResponse {

	 private String errorCode;
	    private String errorMessage;


	  

	    public PPErrorResponse(String string, String errorMessage) {
	        this.errorCode = string;
	        this.errorMessage = errorMessage;
	    }

}
