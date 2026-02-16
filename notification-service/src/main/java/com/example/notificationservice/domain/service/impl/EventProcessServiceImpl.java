package com.example.notificationservice.domain.service.impl;

import com.example.notificationservice.common.kafka.publisher.KafkaPublisher;
import com.example.notificationservice.dao.entity.EventProcess;
import com.example.notificationservice.dao.enums.ProcessStatus;
import com.example.notificationservice.dao.repository.EventProcessRepository;
import com.example.notificationservice.domain.mapper.EventProcessMapper;
import com.example.notificationservice.domain.model.enums.ProcessDecision;
import com.example.notificationservice.domain.model.input.EventProcessInput;
import com.example.notificationservice.domain.model.output.EventProcessOutput;
import com.example.notificationservice.domain.service.EventProcessService;
import com.example.notificationservice.domain.util.EventProcessValidateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventProcessServiceImpl implements EventProcessService {

    private final EventProcessRepository eventProcessRepository;
    private final KafkaPublisher kafkaPublisher;

    @Value("${kafka.delete-topic.name}")
    private String TOPIC_NAME;

    @Override
    public void deleteByProcessId(EventProcessInput input) {
        log.info("EventProcessService -> deleteByProcessId -> {}",input.getEventId());
        kafkaPublisher.publish(TOPIC_NAME,input.getEventId());
    }

    @Transactional
    @Override
    public EventProcessOutput tryStart(EventProcessInput input) {

        try {
            eventProcessRepository.save(EventProcessMapper.toEntity(input));

            return EventProcessOutput.builder()
                    .processDecision(ProcessDecision.PROCESS_NOW)
                    .build();

        }catch (DataIntegrityViolationException e) {
            // zaten var, status'a bak
            Optional<EventProcess> existingOpt = eventProcessRepository.findAllByEventIdEqualsIgnoreCase(input.getEventId());
            if (existingOpt.isEmpty()) {
                // çok nadir: race/replication vb. durumda bir daha dene gibi davran
                return EventProcessOutput.builder()
                        .processDecision(ProcessDecision.PROCESS_NOW)
                        .build();
            }

            ProcessStatus status = existingOpt.get().getStatus();

            if (EventProcessValidateUtil.statusIsEqualsToSent(status))
                return EventProcessOutput.builder()
                        .processDecision(ProcessDecision.SKIP_ALREADY_SENT)
                        .build();

            if (EventProcessValidateUtil.statusIsEqualsToPending(status))
                return EventProcessOutput.builder()
                        .processDecision(ProcessDecision.SKIP_IN_PROGRESS)
                        .build();

            // FAILED ise retry edelim
            // FAILED -> tekrar PENDING'e çek
            eventProcessRepository.markPending(input.getEventId(), Instant.now());
            return EventProcessOutput.builder()
                    .processDecision(ProcessDecision.PROCESS_NOW)
                    .build();
        }

    }

    @Transactional
    @Override
    public void markSent(EventProcessInput input) {
        eventProcessRepository.markSent(input.getEventId(),Instant.now());
    }

    @Transactional
    @Override
    public void markFailed(EventProcessInput input, Exception ex) {
        eventProcessRepository.markFailed(input.getEventId(), Instant.now(), safeMsg(ex));
    }

    private String safeMsg(Exception ex) {
        String msg = ex.getMessage();
        if (msg == null)
            return ex.getClass().getSimpleName();
        return msg.length() > 2000 ? msg.substring(0,2000) : msg;
    }

}
