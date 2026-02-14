package com.example.notificationservice.domain.model;

import com.example.notificationservice.domain.model.enums.EventType;
import lombok.Data;

import java.util.Date;

@Data
public class KafkaPayload {
    private String eventId;
    private EventType eventType;
    private String brand;
    private String model;
    private String category;
    private Integer beforeTotalStock;
    private Integer afterTotalStock;
    private Date occurredAt;
}
