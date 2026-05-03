package com.example.message.controller;

import com.example.message.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService service;

    @PostMapping("/send")
    public String send(@RequestParam String from,
                       @RequestParam String to,
                       @RequestParam String text) {
        return service.send(from, to, text);
    }

}
