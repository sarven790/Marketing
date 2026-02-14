package com.example.notificationservice.dao.repository;

import com.example.notificationservice.dao.entity.EventProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventProcessRepository extends JpaRepository<EventProcess,String> {
    Optional<EventProcess> findAllByEventIdEqualsIgnoreCase(String eventId);

}
