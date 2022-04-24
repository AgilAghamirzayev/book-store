package com.example.demo.security;

import com.example.demo.entity.EmailConfirmationToken;
import com.example.demo.exception.TokenNotConfirmedException;
import com.example.demo.exception.TokenNotFoundException;
import com.example.demo.payload.dto.UserDTO;
import com.example.demo.payload.model.AuthenticationRequestModel;
import com.example.demo.service.UserService;
import com.example.demo.service.email.ConfirmationTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.SneakyThrows;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final UserService userService;
    private final Environment environment;
    private final ConfirmationTokenService confirmationTokenService;

    public AuthenticationFilter(UserService userService, Environment environment, ConfirmationTokenService confirmationTokenService, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.environment = environment;
        this.confirmationTokenService = confirmationTokenService;
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        AuthenticationRequestModel authenticationRequestModel =
                new ObjectMapper().readValue(request.getInputStream(), AuthenticationRequestModel.class);

        UserDTO userDetails = userService.getUserDetailsByEmail(authenticationRequestModel.getEmail());

        Long id = userDetails.getId();

        EmailConfirmationToken emailConfirmationToken = confirmationTokenService.getTokenByUserEntityId(id)
                .orElseThrow(() -> new TokenNotFoundException(String.valueOf(id)));

        if (!emailConfirmationToken.getConfirmed()) {
            throw new TokenNotConfirmedException("Token not confirmed");
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authenticationRequestModel.getEmail(),
                authenticationRequestModel.getPassword()
        );

        return getAuthenticationManager().authenticate(authentication);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {


        String authorities = authResult.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));


        String username = ((User) authResult.getPrincipal()).getUsername();
        UserDTO userDetails = userService.getUserDetailsByEmail(username);

        Key key = Keys.hmacShaKeyFor(environment.getProperty("jwt.secret").getBytes());

        String token = Jwts.builder()
                .claim(environment.getProperty("jwt.authorities"), authorities)
                .setSubject(String.valueOf(userDetails.getId()))
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(Objects.requireNonNull(environment.getProperty("jwt.expired")))))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        response.addHeader("Authorization", token);
    }



}
