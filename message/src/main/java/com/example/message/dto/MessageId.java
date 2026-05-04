package com.example.message.dto;

import com.example.message.exception.BusinessException;

public record MessageId(
        Long id
) {
    public static MessageId from(String str) {
        try {
            return new MessageId(Long.parseLong(str));
        } catch (NumberFormatException ex) {
            throw new BusinessException("Message ID " + str + " is not parsable");
        }
    }
}
