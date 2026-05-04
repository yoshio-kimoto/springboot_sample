package com.example.message.service;

import com.example.message.dto.MessageId;
import com.example.message.dto.MessageRequest;
import com.example.message.dto.MessageResponse;
import com.example.message.entity.AuditEntity;
import com.example.message.entity.MessageEntity;
import com.example.message.exception.BusinessException;
import com.example.message.exception.ResourceNotFoundException;
import com.example.message.repository.AuditRepository;
import com.example.message.repository.MessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    MessageRepository messageRepo;

    @Mock
    AuditRepository auditRepo;

    @InjectMocks
    MessageService service;

    // ---------------------------------------------------------
    // send()
    // ---------------------------------------------------------

    @Test
    void send_shouldReturnExistingMessage_whenIdempotencyKeyExists() {
        MessageEntity existing = MessageEntity.builder()
                .id(1L)
                .fromUser("A")
                .toUser("B")
                .text("hello")
                .sentAt(Instant.now())
                .idempotencyKey("key123")
                .build();

        when(messageRepo.findByIdempotencyKey("key123"))
                .thenReturn(Optional.of(existing));

        MessageResponse res = service.send("key123",
                new MessageRequest("A", "B", "hello"));

        assertEquals(1L, res.id());
        verify(messageRepo, never()).save(any());
    }

    @Test
    void send_shouldThrow_whenFromUserEqualsToUser() {
        MessageRequest req = new MessageRequest("A", "A", "hello");

        assertThrows(BusinessException.class,
                () -> service.send("key", req));
    }

    @Test
    void send_shouldSaveNewMessage_whenIdempotencyKeyNotFound() {
        MessageRequest req = new MessageRequest("A", "B", "hello");

        when(messageRepo.findByIdempotencyKey("key")).thenReturn(Optional.empty());
        when(messageRepo.save(any())).thenAnswer(inv -> {
            MessageEntity e = inv.getArgument(0);
            e = MessageEntity.builder()
                    .id(1L)
                    .fromUser(e.getFromUser())
                    .toUser(e.getToUser())
                    .text(e.getText())
                    .sentAt(e.getSentAt())
                    .idempotencyKey(e.getIdempotencyKey())
                    .build();
            return e;
        });

        MessageResponse res = service.send("key", req);

        assertEquals(1L, res.id());
        verify(messageRepo).save(any());
    }

    // ---------------------------------------------------------
    // read()
    // ---------------------------------------------------------

    @Test
    void read_shouldReturnExistingReadMessage_whenAlreadyRead() {
        MessageEntity entity = MessageEntity.builder()
                .id(1L)
                .sentAt(Instant.now())
                .readAt(Instant.now())
                .build();

        when(messageRepo.findById(1L)).thenReturn(Optional.of(entity));

        MessageResponse res = service.read(MessageId.from("1"));

        verify(messageRepo, never()).save(any());
        assertNotNull(res.readAt());
    }

    @Test
    void read_shouldSetReadAt_whenUnread() {
        MessageEntity entity = MessageEntity.builder()
                .id(1L)
//                .fromUser("A")
//                .toUser("B")
//                .text("hello")
                .sentAt(Instant.now())
//                .idempotencyKey("key")
//                .version(0L)
                .build();

        when(messageRepo.findById(1L)).thenReturn(Optional.of(entity));
//        stubbing the return from repo.save(entity)
        when(messageRepo.save(any())).thenReturn(entity);

        MessageResponse res = service.read(MessageId.from("1"));

        assertNotNull(res.readAt());
        verify(messageRepo).save(any());
    }

    @Test
    void read_shouldThrow_whenMessageDeleted() {
        MessageEntity entity = MessageEntity.builder()
                .id(1L)
                .sentAt(Instant.now())
                .deleteAt(Instant.now())
                .build();

        when(messageRepo.findById(1L)).thenReturn(Optional.of(entity));

        assertThrows(IllegalStateException.class,
                () -> service.read(MessageId.from("1")));
    }

    @Test
    void read_shouldThrow_whenMessageNotFound() {
        when(messageRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.read(MessageId.from("1")));
    }

    // ---------------------------------------------------------
    // delete()
    // ---------------------------------------------------------

    @Test
    void delete_shouldSetDeleteAt_andCreateAudit_whenFirstTime() {
        MessageEntity entity = MessageEntity.builder()
                .id(1L)
                .sentAt(Instant.now())
                .build();

        when(messageRepo.findById(1L)).thenReturn(Optional.of(entity));
        when(messageRepo.save(any())).thenReturn(entity);
        when(auditRepo.findByMessageId(1L)).thenReturn(Optional.empty());

        MessageResponse res = service.delete(MessageId.from("1"));

        assertNotNull(res.deleteAt());
        verify(messageRepo).save(any());
        verify(auditRepo).save(any(AuditEntity.class));
    }

    @Test
    void delete_shouldReturnImmediately_whenAlreadyDeleted() {
        MessageEntity entity = MessageEntity.builder()
                .id(1L)
                .sentAt(Instant.now())
                .deleteAt(Instant.now())
                .build();

        when(messageRepo.findById(1L)).thenReturn(Optional.of(entity));

        MessageResponse res = service.delete(MessageId.from("1"));

        verify(messageRepo, never()).save(any());
        verify(auditRepo, never()).save(any());
        assertNotNull(res.deleteAt());
    }

    @Test
    void delete_shouldLogError_whenAuditExistsButDeleteAtIsNull() {
        MessageEntity entity = MessageEntity.builder()
                .id(1L)
                .sentAt(Instant.now())
                .build();

        when(messageRepo.findById(1L)).thenReturn(Optional.of(entity));
        when(messageRepo.save(any())).thenReturn(entity);
        when(auditRepo.findByMessageId(1L))
                .thenReturn(Optional.of(new AuditEntity()));

        service.delete(MessageId.from("1"));

        verify(auditRepo, never()).save(any());
    }

    @Test
    void delete_shouldThrow_whenMessageNotFound() {
        when(messageRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.delete(MessageId.from("1")));
    }
}
