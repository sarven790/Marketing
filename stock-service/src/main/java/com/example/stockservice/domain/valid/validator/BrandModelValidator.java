package com.example.stockservice.domain.valid.validator;

import com.example.stockservice.api.dto.request.StockRequest;
import com.example.stockservice.dao.repository.ProductRepository;
import com.example.stockservice.domain.valid.annotation.BrandModelValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class BrandModelValidator implements ConstraintValidator<BrandModelValid, StockRequest> {

    private final ProductRepository productRepository;

    @Override
    public boolean isValid(StockRequest request, ConstraintValidatorContext constraintValidatorContext) {

        if ((!Objects.isNull(request.getBrand()) && !Objects.isNull(request.getModel())) &&
                (!request.getBrand().isBlank() && !request.getModel().isBlank())) {
            var optProduct = productRepository.findAllByBrandAndModelEqualsIgnoreCase(request.getBrand(),
                    request.getModel());
            return optProduct.isEmpty();
        }
        return false;
    }
}
