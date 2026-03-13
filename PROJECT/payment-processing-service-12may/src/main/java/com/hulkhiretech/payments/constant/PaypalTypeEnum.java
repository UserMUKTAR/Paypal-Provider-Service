package com.hulkhiretech.payments.constant;

import lombok.Getter;
@Getter
public enum PaypalTypeEnum {
	
	
		SALE(1, "SALE");

	    private final int id;
	    private final String name;

	    PaypalTypeEnum(int id, String name) {
	        this.id = id;
	        this.name = name;
	    }

	    public static PaypalTypeEnum fromId(int id) {
	        for (PaypalTypeEnum type : PaypalTypeEnum.values()) {
	            if (type.getId() == id) {
	                return type;
	            }
	        }
	        return null;
	    }

	    public static PaypalTypeEnum fromName(String name) {
	        for (PaypalTypeEnum type : PaypalTypeEnum.values()) {
	            if (type.getName().equalsIgnoreCase(name)) {
	                return type;
	            }
	        }
	        return null;
	    }
	
}
