package com.example.message.repository;

import com.example.message.entity.AuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuditRepository extends JpaRepository<AuditEntity, Long> {
    Optional<AuditEntity> findByMessageId(Long key);
}
