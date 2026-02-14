package com.example.stockservice.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockRequestByBrandAndModel {
    private String brand;
    private String model;
    private Integer unit;
}
