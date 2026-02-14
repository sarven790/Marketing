package com.example.stockservice.api.dto.response;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class ProductResponseList {
    private List<ProductResponse> productResponses;
    private Long total; // toplam kayıt sayısı
    private Integer totalPages; // toplam sayfa
    private Boolean hasNext; // sonraki sayfa varmı
}
