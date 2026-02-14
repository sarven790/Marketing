package com.example.stockservice.domain.model;

import com.example.stockservice.common.base.model.BaseModel;
import com.example.stockservice.domain.model.enums.EventType;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class ProductPayload extends BaseModel {

    private String eventId;
    private EventType eventType;
    private String brand;
    private String model;
    private String category;
    private Integer beforeTotalStock;
    private Integer afterTotalStock;
    private Date occurredAt;

}
