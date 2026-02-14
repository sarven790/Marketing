package com.example.notificationservice.integration.service;

import com.example.notificationservice.integration.service.request.SlackPostMessageClientRequest;

import java.io.IOException;

public interface SlackMessageService {

    void postMessageWithRateLimitHandling(SlackPostMessageClientRequest clientRequest) throws IOException, InterruptedException;

}
