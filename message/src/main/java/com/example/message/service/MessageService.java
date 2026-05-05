package com.example.message.service;

import com.example.message.dto.*;
import com.example.message.entity.AuditEntity;
import com.example.message.entity.MessageEntity;
import com.example.message.exception.BusinessException;
import com.example.message.exception.InvalidTagException;
import com.example.message.exception.ResourceNotFoundException;
import com.example.message.repository.AuditRepository;
import com.example.message.repository.MessageRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.aspectj.bridge.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepo;
    private final AuditRepository auditRepo;

    private static final Logger log = LoggerFactory.getLogger(MessageService.class);


    @Transactional
    public MessageResponse send(String key, @Valid MessageRequest request) {
        Optional<MessageEntity> entity = messageRepo.findByIdempotencyKey(key);
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
        MessageEntity saved = messageRepo.save(msg);
        return toResponse(saved);

    }

    @Transactional
    public MessageResponse read(MessageId id) {
        MessageEntity entity = messageRepo.findById(id.id())
                .orElseThrow(() -> new ResourceNotFoundException("Message not found: ID " + id));

        if (entity.getReadAt() != null)
            return toResponse(entity);

        entity.markRead();
        MessageEntity saved = messageRepo.save(entity);

        return toResponse(saved);

    }

    @Transactional
    public MessageResponse delete(MessageId id) {
        MessageEntity entity = messageRepo.findById(id.id())
                .orElseThrow(() -> new ResourceNotFoundException("Message not found: ID " + id));

        if (entity.getDeleteAt() != null)
            return toResponse(entity);

        entity.markDelete();
        MessageEntity savedMessage = messageRepo.save(entity);

        Optional<AuditEntity> isAuditThere = auditRepo.findByMessageId(id.id());
        if (isAuditThere.isPresent()) {
            log.error("Inconsistent audit log");
        } else {
            AuditEntity auditEntity = AuditEntity.create(id.id());
            auditRepo.save(auditEntity);
        }

        return toResponse(savedMessage);


/*
//        MessageとAuditの両方のConsistencyをチェックしている。
//        でもこのAPIはMessageに関するものなので、両方でExceptionを投げると混乱する。
        Optional<AuditEntity> isAuditThere = auditRepo.findByMessageId(id.id());
        if (isAuditThere.isPresent() && entity.getDeleteAt() != null)
            return toResponse(entity);
        if ((isAuditThere.isEmpty() && entity.getDeleteAt() != null) ||
                (isAuditThere.isPresent() && entity.getDeleteAt() == null)) {
            throw new IllegalStateException("Inconsistent condition found");
        }

        entity.markDelete();
        MessageEntity savedMessage = messageRepo.save(entity);

        AuditEntity auditEntity = AuditEntity.builder()
                .messageId(id.id())
                .deleteAt(Instant.now(Clock.systemUTC()))
                .build();
        AuditEntity savedAudit = auditRepo.save(auditEntity);

        return toResponse(savedMessage);
*/

    }

    @Transactional
    public MessageTagsResponse addTag(MessageId id, MessageTag tag) {

        MessageEntity entity = messageRepo.findById(id.id())
                .orElseThrow(() -> new ResourceNotFoundException("Message not found: ID: " + id.id()));

        String newTag = tag.tag();
        if (entity.getTags().contains(newTag)) {
//            This is not idempotent.
//            throw new BusinessException("Tag exists already: tag" + newTag);
            log.info("Tag exists already: tag: {}", newTag);
            return new MessageTagsResponse(
                    entity.getId(), entity.getTags()
            );
        }

        entity.getTags().add(newTag);
        MessageEntity saved = messageRepo.save(entity);

        return new MessageTagsResponse(
                saved.getId(), saved.getTags()
        );

    }

    private MessageResponse toResponse(MessageEntity entity) {
        return new MessageResponse(
                entity.getId(),
                entity.getFromUser(),
                entity.getToUser(),
                entity.getText(),
                entity.getSentAt(),
                entity.getReadAt(),
                entity.getDeleteAt()
        );
    }

}
