package com.example.message.service;

import com.example.message.dto.MessageRequest;
import com.example.message.dto.MessageResponse;
import com.example.message.entity.MessageEntity;
import com.example.message.exception.BusinessException;
import com.example.message.repository.MessageRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository repo;

    @Transactional
    public MessageResponse send(String key, @Valid MessageRequest request) {
        Optional<MessageEntity> entity = repo.findByIdempotencyKey(key);
        if (entity.isPresent())
            return toResponse(entity.get());

        if (request.fromUser().equals(request.toUser())) {
            throw new BusinessException("fromUser and toUser shouldn't be same");
        }
/*
        MessageEntity msg = MessageEntity.builder()
                .fromUser(request.fromUser())
                .toUser(request.toUser())
                .text(request.text())
                .sentAt(Instant.now(Clock.systemUTC()))
                .idempotencyKey(key)
                .build();
*/
        MessageEntity msg = MessageEntity.create(request, key);
        MessageEntity saved = repo.save(msg);
        return toResponse(saved);

    }

    private MessageResponse toResponse(MessageEntity entity) {
        return new MessageResponse(
                entity.getId(),
                entity.getFromUser(),
                entity.getToUser(),
                entity.getText(),
                entity.getSentAt()
        );
    }
}
