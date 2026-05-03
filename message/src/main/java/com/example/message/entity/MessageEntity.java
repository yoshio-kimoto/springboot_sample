package com.example.message.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class MessageEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String fromUser;
    private String toUser;
    private String text;
    private long timestamp;

    // getters/setters
}
