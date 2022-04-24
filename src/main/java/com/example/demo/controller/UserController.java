package com.example.demo.controller;

import com.example.demo.mapper.ObjectMapperUtils;
import com.example.demo.payload.dto.RoleRequestDTO;
import com.example.demo.payload.dto.UserDTO;
import com.example.demo.payload.model.CreateUserRequestModel;
import com.example.demo.payload.model.CreateUserResponseModel;
import com.example.demo.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/account")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public void signUp(@RequestBody @Valid CreateUserRequestModel userRequestModel) {
        UserDTO userDTO = ObjectMapperUtils.map(userRequestModel, UserDTO.class);

        Set<RoleRequestDTO> roleRequestDTOS = ObjectMapperUtils.mapAll(userRequestModel.getRoles(), RoleRequestDTO.class);
        userDTO.setRoles(roleRequestDTOS);
        userService.registration(userDTO);

        log.info("IN signUp - user:  successfully signup");
    }

    @GetMapping("/me")
    public ResponseEntity<CreateUserResponseModel> me() {
        UserDTO user = userService.login();
        CreateUserResponseModel returnedValue = ObjectMapperUtils.map(user, CreateUserResponseModel.class);
        return ResponseEntity.ok(returnedValue);
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirm(@RequestParam("token") String token) {
        return ResponseEntity.ok(userService.confirmToken(token));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete() {
        userService.delete();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}