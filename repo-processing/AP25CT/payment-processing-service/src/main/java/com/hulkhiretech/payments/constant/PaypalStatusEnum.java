package com.hulkhiretech.payments.constant;

import lombok.Getter;

@Getter
public enum PaypalStatusEnum {

	PAYER_ACTION_REQUIRED("PAYER_ACTION_REQUIRED"),

	APPROVED("APPROVED"),

	COMPLETED("COMPLETED");

	private String name;
	
	PaypalStatusEnum(String name) {
		this.name = name;
	}
	
	
	public static PaypalStatusEnum fromString(String name) {
        for (PaypalStatusEnum type : PaypalStatusEnum.values()) {
            if (type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }
	
}
