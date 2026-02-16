package com.example.notificationservice.dao.repository;

import com.example.notificationservice.dao.entity.EventProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventProcessRepository extends JpaRepository<EventProcess, String> {

    Optional<EventProcess> findAllByEventIdEqualsIgnoreCase(String eventId);

    @Modifying
    @Query(value = """
        update event_process
        set status='SENT', attempt_count = attempt_count + 1, last_error = null, updated_at = :now
        where event_id = :eventId
        """, nativeQuery = true)
    void markSent(@Param("eventId") String eventId, @Param("now") Instant now);

    @Modifying
    @Query(value = """
        update event_process
        set status='FAILED', attempt_count = attempt_count + 1, last_error = :err, updated_at = :now
        where event_id = :eventId
        """, nativeQuery = true)
    void markFailed(@Param("eventId") String eventId, @Param("now") Instant now, @Param("err") String err);

    @Modifying
    @Query(value = """
        update event_process
        set status='PENDING', updated_at = :now
        where event_id = :eventId
        """, nativeQuery = true)
    void markPending(@Param("eventId") String eventId, @Param("now") Instant now);

}
