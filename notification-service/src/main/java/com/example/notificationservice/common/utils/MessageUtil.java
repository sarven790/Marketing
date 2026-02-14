package com.example.notificationservice.common.utils;

import com.example.notificationservice.common.base.response.ResponseByMessage;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

@UtilityClass
@Slf4j
public class MessageUtil {

    public static ResponseByMessage getSuccessMessageForCreate(MessageSource messageSource, String code) {
        var message = messageSource.getMessage(code,null,
                LocaleContextHolder.getLocale());
        return ResponseByMessage.builder()
                .message(message)
                .build();
    }

    public static String getErrorMessageForCreate(MessageSource messageSource,String code) {
        return messageSource.getMessage(code,null,
                LocaleContextHolder.getLocale());
    }

    public static ResponseByMessage getMessageWithParameters(MessageSource messageSource,String code,String... args) {
        var message = messageSource.getMessage(code,args,
                LocaleContextHolder.getLocale());
        return ResponseByMessage.builder()
                .message(message)
                .build();
    }

}
