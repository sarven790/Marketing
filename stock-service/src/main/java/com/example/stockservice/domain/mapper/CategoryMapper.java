package com.example.stockservice.domain.mapper;

import com.example.stockservice.api.dto.request.CategoryRequest;
import com.example.stockservice.api.dto.response.CategoryResponse;
import com.example.stockservice.api.dto.response.GetCategoryResponseList;
import com.example.stockservice.dao.entity.Category;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@UtilityClass
public class CategoryMapper {

    public static Category toEntity(CategoryRequest request) {
        String key = String.valueOf(ThreadLocalRandom.current().nextInt(1, 100));
        return Category.builder()
                .key(key)
                .value(request.getName())
                .build();
    }

    public static CategoryResponse getDto(Category category) {
        return CategoryResponse.builder()
                .key(category.getKey())
                .value(category.getValue())
                .build();
    }

    public static GetCategoryResponseList getDtoList(List<Category> categories) {
        return GetCategoryResponseList.builder()
                .categoryResponses(categories.stream()
                        .map(CategoryMapper::getDto)
                        .collect(Collectors.toList()))
                .build();
    }

}
