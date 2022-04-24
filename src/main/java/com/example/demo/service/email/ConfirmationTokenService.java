package com.example.demo.service.email;

import com.example.demo.entity.EmailConfirmationToken;

import java.util.Optional;

public interface ConfirmationTokenService {
    void saveConfirmationToken(EmailConfirmationToken token);

    Optional<EmailConfirmationToken> getToken(String token);

    Optional<EmailConfirmationToken> getTokenByUserEntityId(Long id);

    int setConfirmedAt(String token);
}