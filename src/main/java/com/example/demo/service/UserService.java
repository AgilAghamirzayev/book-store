package com.example.demo.service;

import com.example.demo.payload.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void registration(UserDTO user);
    String confirmToken(String token);
    UserDTO getUserDetailsByEmail(String username);
    void delete();
    UserDTO login();
    UserDetails loadUserByUsername(String email);
}
