package com.example.message.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MessageRequest (
        @NotBlank String fromUser,
        @NotBlank String toUser,
        @NotBlank @Size(max = 2000) String text
){}
