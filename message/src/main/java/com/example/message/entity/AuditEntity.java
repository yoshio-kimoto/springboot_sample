package com.example.message.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Clock;
import java.time.Instant;

@Entity
@Table(name = "audit")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull
    Long messageId;
    @NotNull
    Instant deleteAt;

    public static AuditEntity create(Long id) {
        return AuditEntity.builder()
                .messageId(id)
                .deleteAt(Instant.now(Clock.systemUTC()))
                .build();
    }

}
