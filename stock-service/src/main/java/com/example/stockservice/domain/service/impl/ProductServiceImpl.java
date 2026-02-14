package com.example.stockservice.domain.service.impl;

import com.example.stockservice.api.dto.request.GetCategoryRequestByKey;
import com.example.stockservice.api.dto.request.GetStockRequest;
import com.example.stockservice.api.dto.request.StockRequestByBrandAndModel;
import com.example.stockservice.api.dto.request.StockRequest;
import com.example.stockservice.api.dto.response.ProductResponse;
import com.example.stockservice.api.dto.response.ProductResponseList;
import com.example.stockservice.api.dto.response.ResponseByMessage;
import com.example.stockservice.common.exception.BusinessException;
import com.example.stockservice.common.utils.MessageUtil;
import com.example.stockservice.dao.entity.Product;
import com.example.stockservice.dao.repository.ProductRepository;
import com.example.stockservice.domain.constants.MessageConstants;
import com.example.stockservice.domain.mapper.ProductMapper;
import com.example.stockservice.domain.service.CategoryService;
import com.example.stockservice.domain.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final MessageSource messageSource;

    @Override
    public ResponseByMessage addStock(StockRequest request) {
        var category = categoryService.findByCategoryByKey(GetCategoryRequestByKey.builder()
                .key(request.getCategoryKey())
                .build());
        var product = ProductMapper.toEntity(request,category);
        productRepository.save(product);
        return MessageUtil.getSuccessMessageForCreate(messageSource,
                MessageConstants.CREATE_PRODUCT_SUCCESS_MESSAGE);
    }

    @Override
    public ProductResponseList getStock(GetStockRequest request) {
        Pageable pageable = PageRequest
                .of(request.getPage(),request.getSize(), Sort.by("id").descending());
        var page = productRepository.findAllByActiveIsTrue(pageable);
        return ProductMapper.toDtoList(page.getContent(),page.getTotalElements(),
                page.getTotalPages(),page.hasNext());
    }

    @Override
    public ProductResponse getStockByBrandAndModel(StockRequestByBrandAndModel request) {
        var optStock = findCategoryFromBrandAndModel(request.getBrand(),request.getModel());
        return ProductMapper.toDto(optStock.orElse(new Product()));
    }

    @Transactional
    @Override
    public ResponseByMessage updateStock(StockRequestByBrandAndModel request) {
        // birim ekleme yap -> x ürününün a markası 1 tane var, 1 eklendi
        var optProduct = findCategoryFromBrandAndModel(request.getBrand(), request.getModel());

        if (optProduct.isPresent()) {
            Product product = optProduct.get();
            Integer unit = product.getUnit();
            unit += request.getUnit();
            product.setUnit(unit);
            productRepository.save(product);
            return MessageUtil.getMessageWithParameters(messageSource,
                    MessageConstants.UPDATE_STOCK_SUCCESS_MESSAGE,
                    product.getBrand() +" "+product.getModel(),request.getUnit().toString());
        }
        throw new BusinessException(MessageConstants.UPDATE_STOCK_NOT_FOUND);
    }

    private Optional<Product> findCategoryFromBrandAndModel(String brand, String model) {
        return productRepository.findAllByBrandAndModelEqualsIgnoreCase(brand,model);
    }

}
