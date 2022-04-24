package com.example.demo.repository;

import com.example.demo.entity.EmailConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<EmailConfirmationToken, Long> {
    Optional<EmailConfirmationToken> findByToken(String token);
    Optional<EmailConfirmationToken> findByUserEntityId(Long id);

    @Modifying
    @Query("UPDATE EmailConfirmationToken c " +
            "SET c.confirmedAt = ?2, c.confirmed = true " +
            "WHERE c.token = ?1")
    Integer updateConfirmedAt(String token, LocalDateTime confirmedAt);
}