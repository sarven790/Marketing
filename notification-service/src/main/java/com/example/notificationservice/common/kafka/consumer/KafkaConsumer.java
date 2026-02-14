package com.example.notificationservice.common.kafka.consumer;

import com.example.notificationservice.domain.mapper.SlackMapper;
import com.example.notificationservice.domain.model.KafkaPayload;
import com.example.notificationservice.domain.model.input.EventProcessInput;
import com.example.notificationservice.domain.service.EventProcessService;
import com.example.notificationservice.domain.service.NotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private final ObjectMapper MAPPER = new ObjectMapper();
    private static final String GROUP_ID = "GroupId";
    private static final String CONTAINER_FACTORY = "ContainerFactory";
    private final NotificationService notificationService;
    private final EventProcessService eventProcessService;



    @KafkaListener(topics = "${kafka.topic.name}", groupId = GROUP_ID, containerFactory = CONTAINER_FACTORY)
    public void listener(@Payload Object event, ConsumerRecord consumerRecord) throws JsonProcessingException {
        String value = (String) consumerRecord.value();
        JsonNode root = MAPPER.readTree(value);
        JsonNode payload = MAPPER.readTree(root.asText());

        KafkaPayload kafkaPayload = MAPPER.readValue(payload.get("payload").asText(), KafkaPayload.class);
        log.info("KafkaConsumer -> listener - kafkaPayload {}",kafkaPayload);

        var isExist = eventIdIsExist(kafkaPayload.getEventId());

        if (!isExist) {
            saveEventId(kafkaPayload.getEventId());
            notificationService.sendSlack(SlackMapper.toInput(kafkaPayload));
            deleteByProcessId(kafkaPayload.getEventId());
        }

    }

    private void deleteByProcessId(String id) {
        eventProcessService.deleteByProcessId(EventProcessInput.builder()
                .eventId(id)
                .build());
    }

    private Boolean eventIdIsExist(String eventId) {
        var eventProcessOutput = eventProcessService.eventProcessExist(EventProcessInput.builder()
                .eventId(eventId)
                .build());
        return eventProcessOutput.getIsExist();
    }

    private void saveEventId(String eventId) {
        eventProcessService.addEventProcess(EventProcessInput.builder()
                .eventId(eventId)
                .build());
    }

}
