package com.example.message.dto;

import java.time.Instant;

public record MessageResponse (
        Long id,
        String from,
        String to,
        String text,
        Instant sentAt
){}
