package com.example.message.entity;

import com.example.message.dto.MessageRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Clock;
import java.time.Instant;

@Entity
@Table(name = "messages")
@Getter
//@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String fromUser;
    @NotNull
    private String toUser;
    @NotNull @Size(max = 2000)
    private String text;
    @NotNull
    private Instant sentAt;
    private Instant readAt;
    private Instant deleteAt;

    @NotNull
    @Column(unique = true, nullable = false)
    private String idempotencyKey;

    @Version
    private Long version;

//    idempotencyKeyは同じメッセージを２度送るのを避けるため。
//    Createを呼ぶ際はMessageにはIDはまだ無いので、リクエストに対する一意のIDは無い。
    public static MessageEntity create(MessageRequest request, String key) {
        return MessageEntity.builder()
                .fromUser(request.fromUser())
                .toUser(request.toUser())
                .text(request.text())
                .sentAt(Instant.now(Clock.systemUTC()))
                .idempotencyKey(key)
                .build();
    }

    public void markRead() {
        if (deleteAt != null)
            throw new IllegalStateException("Trying to mark as read for deleted message");
        if (readAt == null)
           readAt = Instant.now(Clock.systemUTC());
    }

    public void markDelete() {
        if (deleteAt == null)
            deleteAt = Instant.now(Clock.systemUTC());
    }

}
