package com.example.notificationservice.domain.service;

import com.example.notificationservice.domain.model.input.EventProcessInput;
import com.example.notificationservice.domain.model.output.EventProcessOutput;

public interface EventProcessService {

    void deleteByProcessId(EventProcessInput input);
    EventProcessOutput tryStart(EventProcessInput input);
    void markSent(EventProcessInput input);
    void markFailed(EventProcessInput input, Exception ex);
}
