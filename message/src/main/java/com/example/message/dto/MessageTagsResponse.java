package com.example.message.dto;

import java.util.Set;

public record MessageTagsResponse (
    Long id,
    Set<String> tags
){}
