package com.example.stockservice.common.base.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@MappedSuperclass
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "modified_date")
    private Date modifiedDate;

    @PrePersist
    private void prePersist() {
        createdDate = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
    }

    @PreUpdate
    private void preUpdate() {
        modifiedDate = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC));
    }

}
