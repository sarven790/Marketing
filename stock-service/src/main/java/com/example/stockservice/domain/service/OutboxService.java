package com.example.stockservice.domain.service;

import com.example.stockservice.domain.model.input.DebeziumDatabaseChangeInput;
import com.example.stockservice.domain.model.input.OutboxInput;
import com.example.stockservice.domain.model.input.OutboxInputById;

public interface OutboxService {

    void addOutbox(OutboxInput input);

    String findById(OutboxInputById input);

    void remove(OutboxInputById input);

    void debeziumDatabaseChange(DebeziumDatabaseChangeInput input);

}
