package com.example.message.dto;

import com.example.message.exception.InvalidTagException;

public record MessageTag (
        String tag
) {
    public static MessageTag from(String str) {
        final int MaxLength = 10;
        if (str == null || str.isBlank() || str.length() > MaxLength)
            throw new InvalidTagException("Tag is not a valid string");
        return new MessageTag(str);
    }
}
