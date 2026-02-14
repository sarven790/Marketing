package com.example.stockservice.api.controller;

import com.example.stockservice.api.dto.request.CategoryRequest;
import com.example.stockservice.api.dto.response.GetCategoryResponseList;
import com.example.stockservice.common.base.controller.BaseController;
import com.example.stockservice.common.base.response.BaseResponse;
import com.example.stockservice.domain.constants.MessageConstants;
import com.example.stockservice.domain.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController extends BaseController {

    private final CategoryService categoryService;


    @PostMapping("/add")
    public BaseResponse<Void> addCategory(@RequestBody CategoryRequest request) {
        var response = categoryService.addCategory(request);
        return responseSuccess(response.getMessage());
    }

    @GetMapping("/list")
    public BaseResponse<GetCategoryResponseList> getCategoryList() {
        var response = categoryService.getCategoryList();
        return responseSuccess(response);
    }

}
