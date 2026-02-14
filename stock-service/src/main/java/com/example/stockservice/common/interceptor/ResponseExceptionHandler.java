package com.example.stockservice.common.interceptor;

import com.example.stockservice.common.base.controller.BaseController;
import com.example.stockservice.common.base.response.BaseResponse;
import com.example.stockservice.common.exception.BusinessException;
import com.example.stockservice.common.exception.enums.UnhandledErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ResponseExceptionHandler extends BaseController {

    private final MessageSource messageSource;

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<Void> handleValidationExceptionHandler(MethodArgumentNotValidException e) {
        Optional<FieldError> fieldError = Optional.ofNullable(e.getFieldError());
        String errorCode = fieldError.map(error -> Optional.ofNullable(error.getDefaultMessage()).get())
                .orElseGet(UnhandledErrorType.UNHANDLED_ERROR_TYPE::getCode);
        log.error("MethodArgumentNotValidException occurred: " + errorCode);
        return responseFail(errorCode,messageSource,"400");
    }

    @ExceptionHandler(value = BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<Void> handleBusinessExceptionHandle(BusinessException e) {
        log.error("BusinessException occurred: {}",e.getCode());
        return responseFail(e.getCode(),messageSource,"402");
    }

}
