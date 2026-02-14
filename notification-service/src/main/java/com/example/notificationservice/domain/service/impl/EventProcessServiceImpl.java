package com.example.notificationservice.domain.service.impl;

import com.example.notificationservice.common.kafka.publisher.KafkaPublisher;
import com.example.notificationservice.dao.entity.EventProcess;
import com.example.notificationservice.dao.repository.EventProcessRepository;
import com.example.notificationservice.domain.model.input.EventProcessInput;
import com.example.notificationservice.domain.model.output.EventProcessOutput;
import com.example.notificationservice.domain.service.EventProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventProcessServiceImpl implements EventProcessService {

    private final EventProcessRepository eventProcessRepository;
    private final KafkaPublisher kafkaPublisher;

    @Value("${kafka.delete-topic.name}")
    private String TOPIC_NAME;

    @Override
    public EventProcessOutput eventProcessExist(EventProcessInput input) {
        var optEventProcess = eventProcessRepository.findAllByEventIdEqualsIgnoreCase(input.getEventId());
        return EventProcessOutput.builder()
                .isExist(optEventProcess.isPresent())
                .build();
    }

    @Override
    public void addEventProcess(EventProcessInput input) {
        EventProcess eventProcess = EventProcess.builder()
                .eventId(input.getEventId())
                .build();
        eventProcessRepository.save(eventProcess);
    }

    @Override
    public void deleteByProcessId(EventProcessInput input) {
        log.info("EventProcessService -> deleteByProcessId -> {}",input.getEventId());
        kafkaPublisher.publish(TOPIC_NAME,input.getEventId());
    }
}
