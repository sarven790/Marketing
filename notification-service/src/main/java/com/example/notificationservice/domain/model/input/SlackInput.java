package com.example.notificationservice.domain.model.input;

import com.example.notificationservice.domain.model.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SlackInput {

    private EventType eventType;
    private String brand;
    private String model;
    private Integer oldUnit;
    private Integer newUnit;

}
