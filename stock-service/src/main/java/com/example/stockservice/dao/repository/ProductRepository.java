package com.example.stockservice.dao.repository;

import com.example.stockservice.dao.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,String> {

    Page<Product> findAllByActiveIsTrue(Pageable pageable);
    Optional<Product> findAllByBrandAndModelEqualsIgnoreCase(String brand, String model);

}
