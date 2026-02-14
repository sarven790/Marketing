package com.example.stockservice.api.dto.request;

import com.example.stockservice.domain.constants.MessageConstants;
import com.example.stockservice.domain.valid.annotation.BrandModelValid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@BrandModelValid
public class StockRequest {
    private String brand;
    private String model;
    @Positive(message = MessageConstants.CREATE_BIGGER_THAN_ZERO)
    private Integer unit;
    private String categoryKey;
    private Double unitPrice;

}
