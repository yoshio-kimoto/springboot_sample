package com.example.order.dto;

import com.example.order.entity.OrderEntity;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public record OrderRequest (
//        @NotNull
//        OrderEntity.Status status,
        @Nullable
        String memo
) {
//        public OrderRequest() {
//                status = OrderEntity.Status.CREATED;
//                memo = "";
//        }
}

//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//public class OrderRequest {
//
//        @NotBlank
//        private OrderEntity.Status status;
//        @Nullable
//        private String memo;
//
//}