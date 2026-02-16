package com.example.notificationservice.domain.mapper;

import com.example.notificationservice.dao.entity.EventProcess;
import com.example.notificationservice.dao.enums.ProcessStatus;
import com.example.notificationservice.domain.model.input.EventProcessInput;
import lombok.experimental.UtilityClass;

import java.time.Instant;

@UtilityClass
public class EventProcessMapper {

    public static EventProcess toEntity(EventProcessInput input) {
        return EventProcess.builder()
                .eventId(input.getEventId())
                .consumer(input.getConsumer())
                .status(ProcessStatus.PENDING)
                .attemptCount(0)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

}
