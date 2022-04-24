package com.example.demo.service.email.impl;

import com.example.demo.entity.EmailConfirmationToken;
import com.example.demo.repository.ConfirmationTokenRepository;
import com.example.demo.service.email.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final ConfirmationTokenRepository repository;

    @Override
    public void saveConfirmationToken(EmailConfirmationToken token) {
        repository.save(token);
    }

    @Override
    public Optional<EmailConfirmationToken> getToken(String token) {
        return repository.findByToken(token);
    }

    @Override
    public Optional<EmailConfirmationToken> getTokenByUserEntityId(Long id) {
        return repository.findByUserEntityId(id);
    }

    @Override
    public int setConfirmedAt(String token) {
        return repository.updateConfirmedAt(
                token, LocalDateTime.now());
    }
}
