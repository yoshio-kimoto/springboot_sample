package com.example.sample.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

@Entity
// userはSQLの予約語
@Table(name = "users")
//This doesn't use @Data since no final variables, no RequiredArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//Builder pattern can be much easier to make a User entity instance
@Builder
//@ToString
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    @Nullable
    private String note;

    // Optimistic lock
    @Version
    private Long version;

}
