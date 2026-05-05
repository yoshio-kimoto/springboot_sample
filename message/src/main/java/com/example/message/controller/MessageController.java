package com.example.message.controller;

import com.example.message.dto.*;
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
//        データはDTOで渡すべき。たとえ単純なLongであっても。
        return service.read(MessageId.from(id));
    }

    @PostMapping("/{id}/delete")
    public MessageResponse delete(@PathVariable String id) {
        return service.delete(MessageId.from(id));
    }

    @PostMapping("/{id}/tags")
    public MessageTagsResponse addTag(
            @PathVariable String id,
            @RequestParam String tag) {
        return service.addTag(MessageId.from(id), MessageTag.from(tag));

    }

}
