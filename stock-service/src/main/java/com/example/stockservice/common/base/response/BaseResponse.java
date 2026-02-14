package com.example.stockservice.common.base.response;

import lombok.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BaseResponse<T> {

    private boolean isSuccess;
    private T data;
    private String executionDate;
    private String message;
    private String errorCode;
    private String errorDescription;

    private BaseResponse() {}

    public BaseResponse(boolean isSuccess, T data) {
        this.isSuccess = isSuccess;
        this.data = data;
        this.executionDate = ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public BaseResponse(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
        this.executionDate = ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public BaseResponse(boolean isSuccess, String errorCode, String errorDescription) {
        this.isSuccess = isSuccess;
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
        this.executionDate = ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

}
