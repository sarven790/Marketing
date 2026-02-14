package com.example.stockservice.dao.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public Integer fetchOldUnitFromDb(String productId) {
        return jdbcTemplate.query("select p.unit from Product p where p.id = ?",
                ps -> ps.setString(1,productId),
                rs -> rs.next() ? (Integer) rs.getInt(1) : null
        );
    }

}
