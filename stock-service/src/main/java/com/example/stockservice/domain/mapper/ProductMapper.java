package com.example.stockservice.domain.mapper;

import com.example.stockservice.api.dto.request.StockRequest;
import com.example.stockservice.api.dto.response.ProductResponse;
import com.example.stockservice.api.dto.response.ProductResponseList;
import com.example.stockservice.dao.entity.Category;
import com.example.stockservice.dao.entity.Product;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ProductMapper {

    public static Product toEntity(StockRequest request, Category category) {
        return Product.builder()
                .brand(request.getBrand())
                .model(request.getModel())
                .unit(request.getUnit())
                .unitPrice(request.getUnitPrice())
                .category(category)
                .active(true)
                .build();
    }

    public static ProductResponse toDto(Product product) {
        return ProductResponse.builder()
                .brand(product.getBrand())
                .model(product.getModel())
                .unit(product.getUnit())
                .unitPrice(product.getUnitPrice())
                .category(product.getCategory().getValue())
                .build();
    }

    public static ProductResponseList toDtoList(List<Product> products, Long total,
                                                Integer totalPages, Boolean hasNext) {
        return ProductResponseList.builder()
                .productResponses(products.stream()
                        .map(ProductMapper::toDto)
                        .collect(Collectors.toList()))
                .total(total)
                .totalPages(totalPages)
                .hasNext(hasNext)
                .build();
    }

}
