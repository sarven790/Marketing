package com.example.notificationservice.common.exception.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UnhandledErrorType implements BaseErrorType{
    UNHANDLED_ERROR_TYPE("app.product_create.duplicate_brand_model");

    private final String code;

    @Override
    public String getCode() {
        return code;
    }
}
