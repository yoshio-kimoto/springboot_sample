package com.example.sample.dto;

import lombok.*;

//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class UserResponse {
//    private Long id;
//    private String name;
//    private String email;
//}

public record UserResponse (
        Long id,
        String name,
        String email,
        String note
) {}
