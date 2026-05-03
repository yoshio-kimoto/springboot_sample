package com.example.message.service;

import com.example.message.entity.MessageEntity;
import com.example.message.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    private MessageRepository repo;

    public String send(String from, String to, String text) {
        MessageEntity msg = new MessageEntity();
        msg.setFrom(from);
        msg.setTo(to);
        msg.setText(text);
        msg.setTimestamp(System.currentTimeMillis());

        repo.save(msg);

        return "ok";
    }
}
