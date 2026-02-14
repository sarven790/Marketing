package com.example.stockservice.common.base.controller;

import com.example.stockservice.common.base.response.BaseResponse;
import com.example.stockservice.common.utils.MessageUtil;
import org.springframework.context.MessageSource;

public abstract class BaseController {

    protected <T> BaseResponse<T> responseSuccess(T data) {
        return new BaseResponse<>(true,data);
    }

    protected BaseResponse<Void> responseSuccess(String message) {
        return new BaseResponse<>(true,message);
    }

    protected BaseResponse<Void> responseFail(String messageCode, MessageSource messageSource, String errorCode) {
        var errorDescription = MessageUtil.getErrorMessageForCreate(messageSource,messageCode);
        return new BaseResponse<>(false,errorCode,errorDescription);
    }

}
