package com.hulkhiretech.payments.util.converter;

import org.modelmapper.AbstractConverter;

import com.hulkhiretech.payments.constant.PaypalTypeEnum;

public class PaymentTypeEnumConverter extends AbstractConverter<Integer, String> {
    @Override
    protected String convert(Integer source) {
        return PaypalTypeEnum.fromId(source).getName();
    }
}

