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
    @Enumerated(EnumType.STRING)
//    private String status; // CREATED, PAID, SHIPPED, CANCELLED
    private Status status; // CREATED, PAID, SHIPPED, CANCELLED
    @Nullable
    private String memo;

//    This is for Entity. Pessimistic lock is defined in the Repository.
//    ただし同じTransactionalの中で両方を同時に使うのはDeadLockの可能性があるので避けるべき
    @Version
    private Long version;

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
        status = Status.SHIPPED;
    }

    public void cancel() {
        if (status == Status.SHIPPED || status == Status.CANCELLED)
            throw new BusinessException("Order can't be canceled: status:" + status);
        status = Status.CANCELLED;
    }


}
