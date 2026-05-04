package com.example.message.controller;

import com.example.message.dto.MessageId;
import com.example.message.dto.MessageRequest;
import com.example.message.dto.MessageResponse;
import com.example.message.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService service;

    @PostMapping("/send")
    public MessageResponse send(
            @RequestHeader("Idempotency-Key") String key,
            @Valid @RequestBody MessageRequest request
    ) {
        return service.send(key, request);
    }

    @PostMapping("/{id}/read")
    public MessageResponse read(@PathVariable String id) {
        return service.read(MessageId.from(id));
    }

}
