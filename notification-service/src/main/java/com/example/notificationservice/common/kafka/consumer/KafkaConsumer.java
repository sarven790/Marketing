package com.example.notificationservice.common.kafka.consumer;

import com.example.notificationservice.domain.mapper.SlackMapper;
import com.example.notificationservice.domain.model.KafkaPayload;
import com.example.notificationservice.domain.model.enums.ProcessDecision;
import com.example.notificationservice.domain.model.input.EventProcessInput;
import com.example.notificationservice.domain.service.EventProcessService;
import com.example.notificationservice.domain.service.NotificationService;
import com.example.notificationservice.domain.util.EventProcessValidateUtil;
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

        // - İlk kez geliyorsa: PENDING kaydı oluştur (atomik)
        // - Daha önce geldiyse: status'a göre karar ver
        var decision = eventProcessService.tryStart(EventProcessInput.builder()
                .eventId(kafkaPayload.getEventId())
                .consumer("notification-service")
                .build()).getProcessDecision();

        if (EventProcessValidateUtil.decisionIsEqualsToSkipAlreadySent(decision)) {
            log.info("Event already SENT. Skipping. eventId={}", kafkaPayload.getEventId());
            return;
        }

        if (EventProcessValidateUtil.decisionIsEqualsToSkipInProgress(decision)) {
            // Aynı event paralel işleniyor olabilir (veya önceki attempt yarım kaldı)
            log.info("Event is still PENDING. Skipping for now. eventId={}", kafkaPayload.getEventId());
            return;
        }

        try {
            notificationService.sendSlack(SlackMapper.toInput(kafkaPayload));
            eventProcessService.markSent(EventProcessInput.builder()
                    .eventId(kafkaPayload.getEventId())
                    .build());
            deleteByProcessId(kafkaPayload.getEventId());
        } catch (Exception e) {
            eventProcessService.markFailed(EventProcessInput.builder()
                    .eventId(kafkaPayload.getEventId())
                    .build(),e);
        }

    }

    private void deleteByProcessId(String id) {
        eventProcessService.deleteByProcessId(EventProcessInput.builder()
                .eventId(id)
                .build());
    }

}
