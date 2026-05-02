package com.example.sample.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class UserRequest {
//
//    @NotBlank
//    private String name;
//    @NotBlank
//    @Email
//    private String email;
//
//}

// record version
public record UserRequest (
        @NotBlank String name,
        @Email String email,
//        @Max(10000) String note
        String note
) {}
