package com.example.demo.payload.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@RequiredArgsConstructor
public class CreateUserRequestModel {
    @NotNull(message = "Username cannot be null")
    @Size(min = 3, max = 50, message = "Name must be equal or greater than 3 characters and less than 50 characters")
    private String name;

    @NotNull(message = "Password cannot be null")
    @Size(min = 3, max = 100, message = "Password must be equal or greater than 3 characters and less than 100 characters")
    private String password;

    @NotNull(message = "Email cannot be null")
    @Email(message = "Email doesn't match")
    private String email;

    @NotNull(message = "Role cannot be null")
    private List<RoleRequestModel> roles;

}