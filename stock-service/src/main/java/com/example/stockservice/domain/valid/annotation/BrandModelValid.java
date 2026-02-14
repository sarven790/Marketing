package com.example.stockservice.domain.valid.annotation;

import com.example.stockservice.domain.valid.validator.BrandModelValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = BrandModelValidator.class)
public @interface BrandModelValid {

    String message() default "app.product_create.duplicate_brand_model";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
