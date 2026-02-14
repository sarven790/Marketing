package com.example.notificationservice.integration.service.impl;

import com.example.notificationservice.common.exception.SlackCallFailedException;
import com.example.notificationservice.integration.service.SlackMessageService;
import com.example.notificationservice.integration.service.request.SlackPostMessageClientRequest;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SlackMessageServiceImpl implements SlackMessageService {

    private final Slack slack;

    @Value("${slack-bot.oauth-token}")
    private String botToken;

    @Value("${slack-bot.channel-id}")
    private String channelId;

    @Override
    public void postMessageWithRateLimitHandling(SlackPostMessageClientRequest clientRequest) {
        MethodsClient client = slack.methods(botToken);

        int maxAttempts = 5;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                ChatPostMessageResponse response = client.chatPostMessage(
                        ChatPostMessageRequest.builder()
                                .channel(channelId)
                                .text(clientRequest.getText())
                                .build()
                );

                // Slack bazen HTTP 200 dönüp ok:false dönebilir
                if (!response.isOk()) {
                    throw new SlackCallFailedException("Slack error (ok:false): " + response.getError());
                }

                return;

            } catch (SlackApiException e) {
                Response httpResponse = e.getResponse();

                if (httpResponse != null && httpResponse.code() == 429) {
                    long waitSeconds = parseRetryAfterSeconds(httpResponse).orElse(1L);
                    log.warn("Slack rate limited (429). attempt={}/{} retryAfter={}s", attempt, maxAttempts, waitSeconds);

                    // Thread.sleep checked exception -> yakala
                    try {
                        Thread.sleep(waitSeconds * 1000L);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new SlackCallFailedException("Interrupted while waiting for Retry-After", ie);
                    }

                    continue;
                }

                // 429 değil: SlackApiException'ı runtime'a çevir
                throw new SlackCallFailedException("Slack API call failed. code=" +
                        (httpResponse != null ? httpResponse.code() : "unknown"), e);

            } catch (IOException e) {
                // chatPostMessage IOException da fırlatabilir
                throw new SlackCallFailedException("Slack IO error while calling chat.postMessage", e);
            }
        }

        throw new SlackCallFailedException("Slack postMessage failed after retries (rate limited).");
    }

    private Optional<Long> parseRetryAfterSeconds(Response httpResp) {
        try {
            String h = httpResp.header("Retry-After");
            if (h == null) return Optional.empty();
            return Optional.of(Long.parseLong(h.trim()));
        } catch (Exception ignore) {
            return Optional.empty();
        }
    }

}
