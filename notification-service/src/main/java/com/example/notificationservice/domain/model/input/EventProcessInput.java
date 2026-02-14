package com.example.notificationservice.domain.model.input;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventProcessInput {
    private String eventId;
}
