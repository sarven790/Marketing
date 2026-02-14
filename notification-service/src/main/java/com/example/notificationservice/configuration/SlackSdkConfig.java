package com.example.notificationservice.configuration;

import com.slack.api.Slack;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlackSdkConfig {

    @Bean
    public Slack slack() {
        return Slack.getInstance();
    }

}
