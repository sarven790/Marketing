package com.example.stockservice.dao.repository;

import com.example.stockservice.dao.entity.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboxRepository extends JpaRepository<Outbox,String> {
}
