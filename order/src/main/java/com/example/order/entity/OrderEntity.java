package com.example.order.entity;

import com.example.order.exception.BusinessException;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
// orderはSQLの予約語
@Table(name = "orders")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {

    public enum Status {
        CREATED, PAID, SHIPPED, CANCELLED
    };

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
//    private String status; // CREATED, PAID, SHIPPED, CANCELLED
    private Status status; // CREATED, PAID, SHIPPED, CANCELLED
    @Nullable
    private String memo;

/*
//    Do we need?
    @Version
    private Long version;
*/

    // getter/setter 省略 <- annotatorで追加した。

    public void pay() {
        if (status != Status.CREATED) {
            throw new BusinessException("Order status is not CREATED: " + status);
        }
        status = Status.PAID;
    }

    public void ship() {
        if (status != Status.PAID) {
            throw new BusinessException("Order ID " + id + " cannot ship: " + status);
        }
        status = OrderEntity.Status.SHIPPED;
    }


}
