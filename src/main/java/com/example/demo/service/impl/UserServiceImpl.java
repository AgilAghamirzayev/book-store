package com.example.demo.service.impl;

import com.example.demo.entity.EmailConfirmationToken;
import com.example.demo.entity.UserEntity;
import com.example.demo.entity.enums.Status;
import com.example.demo.exception.*;
import com.example.demo.mapper.ObjectMapperUtils;
import com.example.demo.payload.dto.UserDTO;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import com.example.demo.service.email.ConfirmationTokenService;
import com.example.demo.service.email.EmailSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final EmailSender emailSender;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    @Transactional

    public void registration(UserDTO request) {
        userRepository.findByEmailAndStatusNot(request.getEmail(), Status.DELETED)
                .ifPresent(user -> {
                    throw new EmailAlreadyExistException(request.getEmail());
                });

        UserEntity userEntity = ObjectMapperUtils.map(request, UserEntity.class);
        userEntity.setPassword(encoder.encode(userEntity.getPassword()));
        userEntity.setStatus(Status.OFFLINE);
        UserEntity savedUserEntity = userRepository.save(userEntity);

        String token = emailConfirmation(userEntity);

        log.info("IN register - user: {} successfully returned token: ", token);

        String link = "http://localhost:8080/api/account/confirm?token=" + token;
        emailSender.send(request.getEmail(), link, String.format("Hi %s, please enter to link and activate your account", savedUserEntity.getName()));

        UserDTO userDTO = ObjectMapperUtils.map(savedUserEntity, UserDTO.class);
        log.info("IN register - user: {} successfully registered", userDTO);
    }

    @Override
    @Transactional
    public String confirmToken(String token) {
        EmailConfirmationToken confirmationToken = confirmationTokenService.getToken(token)
                .orElseThrow(() -> new TokenNotFoundException(token));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new EmailAlreadyConfirmedException(confirmationToken.getUserEntity().getEmail());
        }

        if (confirmationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new EmailTokenExpiredException(token);
        }
        confirmationToken.setConfirmed(true);

        confirmationTokenService.setConfirmedAt(token);
        userRepository.enableAppUser(confirmationToken.getUserEntity().getEmail());

        return "Confirmed";
    }

    @Override
    public UserDTO getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmailAndStatusNot(email, Status.DELETED).orElseThrow(() -> {
            throw new UsernameNotFoundException(email);
        });

        UserDTO userDTO = ObjectMapperUtils.map(userEntity, UserDTO.class);
        log.info("IN getUserDetailsByUsername - user: {} successfully found", userDTO);

        return userDTO;
    }

    @Override
    public void delete() {
        Long id = getIdFromToken();

        UserEntity user = userRepository.findById(id).orElseThrow(() -> {
            throw new UserNotFoundException("User", id);
        });
        user.setStatus(Status.DELETED);

        log.info("IN getUserDetailsByUsername - user: {} successfully deleted", user.getName());
    }

    @Override
    @Transactional
    public UserDTO login() {
        Long id = getIdFromToken();
        UserEntity userEntity = userRepository.findByIdAndStatusNot(id, Status.DELETED).orElseThrow(() -> {
            throw new UserNotFoundException(id);
        });

        if (!userEntity.getEnabled()) {
            throw new UserNotEnabledException(userEntity.getName());
        }

        if (userEntity.getStatus().equals(Status.ONLINE)) {
            throw new UserAlreadyWasLoginException(userEntity.getName());
        }

        userEntity.setStatus(Status.ONLINE);
        userRepository.save(userEntity);

        UserDTO userDTO = ObjectMapperUtils.map(userEntity, UserDTO.class);
        log.info("IN getUserDetailsByUsername - user: {} successfully login", userDTO);

        return userDTO;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserEntity userEntity = userRepository.findByEmailAndStatusNot(email, Status.DELETED).orElseThrow(() -> {
            throw new UsernameNotFoundException(email);
        });

        return new User(userEntity.getEmail(), userEntity.getPassword(), true, true, true, true, getAuthority(userEntity));
    }

    private Set<SimpleGrantedAuthority> getAuthority(UserEntity user) {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
    }

    private String emailConfirmation(UserEntity entity) {
        String token = UUID.randomUUID().toString();
        EmailConfirmationToken confirmationToken = new EmailConfirmationToken();
        confirmationToken.setToken(token);
        confirmationToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        confirmationToken.setUserEntity(entity);
        confirmationToken.setConfirmed(false);
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        log.info("IN register - user: {} successfully email confirmed", confirmationToken);
        return token;
    }

    private Long getIdFromToken() {
        return Long.parseLong(SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal().toString());
    }
}
