package com.example.demo.payload.dto;

import com.example.demo.entity.enums.Status;
import com.example.demo.payload.model.RoleRequestModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String password;
    private String email;
    private Status status;

    private Set<RoleRequestDTO> roles;

    public UserDTO(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
