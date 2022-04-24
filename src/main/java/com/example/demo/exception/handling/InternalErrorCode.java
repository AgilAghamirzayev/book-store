package com.example.demo.exception.handling;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InternalErrorCode {

    USER_NOT_FOUND(100, "unknown_user"),
    INVALID_VALUE(101, "invalid_value"),
    INVALID_USER_OR_PASSWORD(102, "invalid_user_or_password"),
    EXPIRED_JWT(103, "expired_jwt"),
    USER_NOT_ENABLED(104, "user_not_enabled"),
    USER_ALREADY_WAS_LOGIN(105, "user_already_was_login"),
    EMAIL_ALREADY_EXIST(106, "email_already_exist"),
    EMAIL_ALREADY_CONFIRMED(107, "email_already_confirmed"),
    EMAIL_TOKEN_EXPIRED(108, "email_token_expired"),
    AUTHENTICATION_EXCEPTION(109, "authentication_exception"),
    JWT_AUTHENTICATION(110, "jwt_authentication_exception"),
    TOKEN_NOT_FOUND(111, "token_not_found"),
    TOKEN_NOT_VALID(112, "token_not_valid"),
    TOKEN_NOT_CONFIRMED(113, "token_not_confirmed"),
    RUNTIME_EXCEPTION(114, "runtime_exception"),
    COMMON_EXCEPTION(114, "common_exception")
    ;

    private final int id;
    private final String code;
}