package com.example.stockservice.api.controller;

import com.example.stockservice.api.dto.request.GetStockRequest;
import com.example.stockservice.api.dto.request.StockRequest;
import com.example.stockservice.api.dto.request.StockRequestByBrandAndModel;
import com.example.stockservice.api.dto.response.ProductResponse;
import com.example.stockservice.api.dto.response.ProductResponseList;
import com.example.stockservice.common.base.controller.BaseController;
import com.example.stockservice.common.base.response.BaseResponse;
import com.example.stockservice.domain.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stock")
public class ProductController extends BaseController {

    private final ProductService productService;

    @PostMapping("/add")
    public BaseResponse<Void> addStock(@Valid @RequestBody StockRequest request) {
        var response = productService.addStock(request);
        return responseSuccess(response.getMessage());
    }

    @PostMapping("/updateStock")
    public BaseResponse<Void> updateStock(@Valid @RequestBody StockRequestByBrandAndModel request) {
        var response = productService.updateStock(request);
        return responseSuccess(response.getMessage());
    }

    @PostMapping("/getStockByBrandAndModel")
    public BaseResponse<ProductResponse> getStockByBrandAndModel(@RequestBody StockRequestByBrandAndModel request) {
        var response = productService.getStockByBrandAndModel(request);
        return responseSuccess(response);
    }

    @PostMapping("/getStock")
    public BaseResponse<ProductResponseList> getStock(@RequestBody GetStockRequest request) {
        var response = productService.getStock(request);
        return responseSuccess(response);
    }

}
