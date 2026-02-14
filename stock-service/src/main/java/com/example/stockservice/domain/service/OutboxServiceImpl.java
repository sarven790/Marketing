package com.example.stockservice.domain.service;

import com.example.stockservice.common.kafka.publisher.KafkaPublisher;
import com.example.stockservice.dao.entity.Outbox;
import com.example.stockservice.dao.repository.OutboxRepository;
import com.example.stockservice.domain.model.ProductPayload;
import com.example.stockservice.domain.model.input.DebeziumDatabaseChangeInput;
import com.example.stockservice.domain.model.input.OutboxInput;
import com.example.stockservice.domain.model.input.OutboxInputById;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxServiceImpl implements OutboxService{

    private final OutboxRepository outboxRepository;
    private final KafkaPublisher kafkaPublisher;
    private final ObjectMapper MAPPER = new ObjectMapper();

    @Value("${kafka.topic.name}")
    private String KAFKA_TOPIC;

    @Override
    public void addOutbox(OutboxInput input) {
        Outbox outbox = Outbox.builder()
                .payload(input.getPayload())
                .build();
        outboxRepository.save(outbox);
    }

    @Override
    public String findById(OutboxInputById input) {
        List<Outbox> outboxList = outboxRepository.findAll();
        String id = "";
        try {
            for (var outbox : outboxList) {
                String payload = outbox.getPayload();
                ProductPayload productPayload = MAPPER.readValue(payload, ProductPayload.class);
                if (input.getId().equalsIgnoreCase(productPayload.getEventId())) {
                    id = outbox.getId();
                }
            }
        } catch (JsonProcessingException e) {
            log.error("An exception occurrence is {}",e.getMessage());
        }
        return id;
    }

    @Override
    public void remove(OutboxInputById input) {
        outboxRepository.deleteById(input.getId());
    }

    @Override
    public void debeziumDatabaseChange(DebeziumDatabaseChangeInput input) {
        log.info("OutboxService -> debeziumDatabaseChange - Debezium payload {}",input.getPayload());
        try {
            kafkaPublisher.publish(KAFKA_TOPIC, MAPPER.writeValueAsString(input.getPayload()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
