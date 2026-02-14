package com.example.stockservice.common.kafka.consumer;

import com.example.stockservice.domain.model.input.OutboxInputById;
import com.example.stockservice.domain.service.OutboxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {

    private static final String GROUP_ID = "GroupId";
    private static final String CONTAINER_FACTORY = "ContainerFactory";
    private final OutboxService outboxService;

    @KafkaListener(topics = "${kafka.delete-topic.name}", groupId = GROUP_ID, containerFactory = CONTAINER_FACTORY)
    public void listener(String message) {
        log.info("KafkaConsumer -> Received message: " + message);
        String id = findById(message);
        removeById(id);
    }

    private String findById(String eventId) {
        return outboxService.findById(OutboxInputById.builder()
                .id(eventId)
                .build());
    }

    private void removeById(String id) {
        outboxService.remove(OutboxInputById.builder()
                .id(id)
                .build());
    }

}
