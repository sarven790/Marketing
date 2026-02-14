package com.example.notificationservice.domain.mapper;

import com.example.notificationservice.domain.model.KafkaPayload;
import com.example.notificationservice.domain.model.input.SlackInput;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SlackMapper {

    public static SlackInput toInput(KafkaPayload kafkaPayload) {
        return SlackInput.builder()
                .eventType(kafkaPayload.getEventType())
                .brand(kafkaPayload.getBrand())
                .model(kafkaPayload.getModel())
                .oldUnit(kafkaPayload.getBeforeTotalStock())
                .newUnit(kafkaPayload.getAfterTotalStock())
                .build();
    }

}
