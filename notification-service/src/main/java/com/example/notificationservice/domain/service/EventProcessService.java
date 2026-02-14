package com.example.notificationservice.domain.service;

import com.example.notificationservice.domain.model.input.EventProcessInput;
import com.example.notificationservice.domain.model.output.EventProcessOutput;

public interface EventProcessService {

    EventProcessOutput eventProcessExist(EventProcessInput input);
    void addEventProcess(EventProcessInput input);
    void deleteByProcessId(EventProcessInput input);

}
