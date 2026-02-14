package com.example.stockservice.domain.service;

import com.example.stockservice.api.dto.request.GetStockRequest;
import com.example.stockservice.api.dto.request.StockRequestByBrandAndModel;
import com.example.stockservice.api.dto.request.StockRequest;
import com.example.stockservice.api.dto.response.ProductResponse;
import com.example.stockservice.api.dto.response.ProductResponseList;
import com.example.stockservice.api.dto.response.ResponseByMessage;

public interface ProductService {

    //addStock
    ResponseByMessage addStock(StockRequest request);

    //getStock
    ProductResponseList getStock(GetStockRequest request);

    //getStockByName
    ProductResponse getStockByBrandAndModel(StockRequestByBrandAndModel request);

    ResponseByMessage updateStock(StockRequestByBrandAndModel request);

    //removeStock

}
