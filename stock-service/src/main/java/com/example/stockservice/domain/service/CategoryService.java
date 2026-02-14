package com.example.stockservice.domain.service;

import com.example.stockservice.api.dto.request.CategoryRequest;
import com.example.stockservice.api.dto.request.GetCategoryRequestByKey;
import com.example.stockservice.api.dto.response.CategoryResponse;
import com.example.stockservice.api.dto.response.GetCategoryResponseList;
import com.example.stockservice.api.dto.response.ResponseByMessage;
import com.example.stockservice.dao.entity.Category;

public interface CategoryService {

    ResponseByMessage addCategory(CategoryRequest request);

    GetCategoryResponseList getCategoryList();

    Category findByCategoryByKey(GetCategoryRequestByKey request);

    CategoryResponse getCategoryByKey(GetCategoryRequestByKey request);

}
