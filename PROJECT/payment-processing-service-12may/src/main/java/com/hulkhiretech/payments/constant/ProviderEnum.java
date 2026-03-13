package com.hulkhiretech.payments.constant;

import lombok.Getter;

@Getter
public enum ProviderEnum {
	PAYPAL(1, "PAYPAL");

    private final int id;
    private final String name;

    ProviderEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ProviderEnum fromId(int id) {
        for (ProviderEnum provider : ProviderEnum.values()) {
            if (provider.getId() == id) {
                return provider;
            }
        }
        return null;
    }

    public static ProviderEnum fromName(String name) {
        for (ProviderEnum provider : ProviderEnum.values()) {
            if (provider.getName().equalsIgnoreCase(name)) {
                return provider;
            }
        }
        return null;
    }
}

