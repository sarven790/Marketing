package com.example.notificationservice.domain.service.impl;

import com.example.notificationservice.common.utils.MessageUtil;
import com.example.notificationservice.domain.model.enums.EventType;
import com.example.notificationservice.domain.model.input.SlackInput;
import com.example.notificationservice.domain.service.NotificationService;
import com.example.notificationservice.integration.service.SlackMessageService;
import com.example.notificationservice.integration.service.request.SlackPostMessageClientRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final SlackMessageService slackMessageService;
    private final MessageSource messageSource;
    private final static String MESSAGE_CODE = "app.notification_update.slack_message";

    @Override
    public void sendSlack(SlackInput input) {

        if (input.getEventType().equals(EventType.STOCK_REPLENISHED)) {
            var responseByMessage = MessageUtil.getMessageWithParameters(messageSource,MESSAGE_CODE,
                    input.getBrand(),input.getModel());
            try {
                slackMessageService.postMessageWithRateLimitHandling(SlackPostMessageClientRequest.builder()
                        .text(responseByMessage.getMessage())
                        .build());
            }catch (Exception e) {
                log.error("An exception occurrence is {}",e.getMessage());
            }
        }

    }

    @Override
    public void sendMail() {

    }
}
