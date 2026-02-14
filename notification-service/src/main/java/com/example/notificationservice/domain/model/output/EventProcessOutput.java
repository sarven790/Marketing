package com.example.notificationservice.domain.model.output;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventProcessOutput {
    private Boolean isExist;
}
