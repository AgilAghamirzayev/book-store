package com.example.demo.service.email;

public interface EmailSender {
    void send(String to, String body, String subject);
}