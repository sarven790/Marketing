package com.example.notificationservice.common.base.response.builer;


import com.example.notificationservice.common.base.response.BaseResponse;

public class BaseResponseBuilder {

    public static <T> BaseResponse<T> build(boolean isSuccess, T data) {
        return new BaseResponse<T>(isSuccess,data);
    }

    public static <T>BaseResponse<T> build(boolean isSuccess, String errorCode,
                                                          String errorDescription) {
        return new BaseResponse<>(isSuccess,errorCode,errorDescription);
    }

}
