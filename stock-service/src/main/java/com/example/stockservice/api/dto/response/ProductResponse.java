package com.example.stockservice.api.dto.response;

import com.example.stockservice.common.base.model.BaseModel;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class ProductResponse extends BaseModel {

    private String brand;
    private String model;
    private String category;
    private Integer unit;
    private Double unitPrice;

}
