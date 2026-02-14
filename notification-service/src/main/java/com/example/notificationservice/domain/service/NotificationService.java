package com.example.notificationservice.domain.service;

import com.example.notificationservice.domain.model.input.SlackInput;

public interface NotificationService {

    //sendSlack
    void sendSlack(SlackInput input);

    //sendMail
    void sendMail();

}
