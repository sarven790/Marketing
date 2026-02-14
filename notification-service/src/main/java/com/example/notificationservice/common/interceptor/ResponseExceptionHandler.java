package com.example.notificationservice.common.interceptor;

import com.example.notificationservice.common.base.controller.BaseController;
import com.example.notificationservice.common.base.response.BaseResponse;
import com.example.notificationservice.common.exception.BusinessException;
import com.example.notificationservice.common.exception.enums.UnhandledErrorType;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

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

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public BaseResponse<Void> handleExceptionError(Exception e, HandlerMethod handlerMethod) {
        log.error("Exception occurred: " + e.getMessage(), e);
        String messageCode = getDefaultMessageModel(handlerMethod);
        return responseFail(messageCode,messageSource, "500");
    }

    private String getDefaultMessageModel(HandlerMethod handlerMethod){
        var methodName = getErrorFunction(handlerMethod);
        return StringUtils.isNotEmpty(methodName) ?
                "DEFAULT_MESSAGE_BOX" + "." + methodName :
                UnhandledErrorType.UNHANDLED_ERROR_TYPE.getCode();
    }

    private String getErrorFunction(HandlerMethod handlerMethod) {
        String controllerName = handlerMethod.getMethod().getDeclaringClass().getName();
        controllerName = controllerName.substring(controllerName.lastIndexOf('.') + 1);
        return controllerName.replace("Controller","") + "_" + handlerMethod.getMethod().getName();
    }

}
