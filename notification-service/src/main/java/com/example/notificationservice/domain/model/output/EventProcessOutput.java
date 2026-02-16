package com.example.notificationservice.domain.model.output;

import com.example.notificationservice.domain.model.enums.ProcessDecision;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventProcessOutput {
    private ProcessDecision processDecision;
}
