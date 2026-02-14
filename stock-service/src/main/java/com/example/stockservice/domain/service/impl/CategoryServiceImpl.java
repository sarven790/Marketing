package com.example.stockservice.domain.service.impl;

import com.example.stockservice.api.dto.request.CategoryRequest;
import com.example.stockservice.api.dto.request.GetCategoryRequestByKey;
import com.example.stockservice.api.dto.response.CategoryResponse;
import com.example.stockservice.api.dto.response.GetCategoryResponseList;
import com.example.stockservice.api.dto.response.ResponseByMessage;
import com.example.stockservice.common.utils.MessageUtil;
import com.example.stockservice.dao.entity.Category;
import com.example.stockservice.dao.repository.CategoryRepository;
import com.example.stockservice.domain.constants.MessageConstants;
import com.example.stockservice.domain.mapper.CategoryMapper;
import com.example.stockservice.domain.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final MessageSource messageSource;

    @Override
    public ResponseByMessage addCategory(CategoryRequest request) {

        Category category = CategoryMapper.toEntity(request);
        categoryRepository.save(category);
        return MessageUtil.getSuccessMessageForCreate(messageSource,
                MessageConstants.CREATE_CATEGORY_SUCCESS_MESSAGE);
    }

    @Override
    public GetCategoryResponseList getCategoryList() {
        List<Category> categories = categoryRepository.findAll();
        return CategoryMapper.getDtoList(categories);
    }

    @Override
    public Category findByCategoryByKey(GetCategoryRequestByKey request) {
        return findByCategory(request.getKey());
    }

    @Override
    public CategoryResponse getCategoryByKey(GetCategoryRequestByKey request) {
        Category category = findByCategory(request.getKey());
        return CategoryMapper.getDto(category);
    }

    private Category findByCategory(String key) {
        return categoryRepository
                .findByKeyEqualsIgnoreCase(key).orElse(new Category());
    }

}
