package com.example.demo.exception.handling;


import com.example.demo.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Optional;

@Slf4j
@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String EXCUSE = "Something went wrong. Please contact with me!";

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                               HttpHeaders headers, HttpStatus status,
                                                               WebRequest request) {
        Optional<String> optionalErrorMessage = e.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).findFirst();
        String errorMessage = EXCUSE;
        if (optionalErrorMessage.isPresent()) {
            errorMessage = optionalErrorMessage.get();
        }
        log.error("Validation error:", e);
        return new ResponseEntity<>(new RequestError(InternalErrorCode.INVALID_VALUE, errorMessage), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<RequestError> userNotFoundExceptionHandle(UserNotFoundException e) {
        log.error("User not found: {}", e.getMessage());
        return new ResponseEntity<>(new RequestError(InternalErrorCode.USER_NOT_FOUND, e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<RequestError> emailAlreadyExistExceptionHandle(EmailAlreadyExistException e) {
        log.error("Email Already Exist: {}", e.getMessage());
        return new ResponseEntity<>(new RequestError(InternalErrorCode.EMAIL_ALREADY_EXIST, e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(EmailAlreadyConfirmedException.class)
    public ResponseEntity<RequestError> emailAlreadyConfirmedExceptionHandle(EmailAlreadyConfirmedException e) {
        log.error("Email Already Confirmed: {}", e.getMessage());
        return new ResponseEntity<>(new RequestError(InternalErrorCode.EMAIL_ALREADY_CONFIRMED, e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ResponseStatus(HttpStatus.IM_USED)
    @ExceptionHandler(EmailTokenExpiredException.class)
    public ResponseEntity<RequestError> emailTokenExpiredExceptionHandle(EmailTokenExpiredException e) {
        log.error("Email Token Expired: {}", e.getMessage());
        return new ResponseEntity<>(new RequestError(InternalErrorCode.EMAIL_TOKEN_EXPIRED, e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
    }
//
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(AuthenticationException.class)
//    public ResponseEntity<RequestError> authenticationExceptionHandle(AuthenticationException e) {
//        log.error("Invalid username or password: {}", e.getMessage());
//        return new ResponseEntity<>(new RequestError(InternalErrorCode.AUTHENTICATION_EXCEPTION, e.getMessage()), HttpStatus.UNAUTHORIZED);
//    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(ExpiredJwtException.class)
//    public ResponseEntity<RequestError> expiredJwtExceptionHandle(ExpiredJwtException e) {
//        log.error("Expired Jwt Exception: {}", e.getMessage());
//        return new ResponseEntity<>(new RequestError(InternalErrorCode.EXPIRED_JWT, e.getMessage()), HttpStatus.BAD_REQUEST);
//    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotEnabledException.class)
    public ResponseEntity<RequestError> userNotEnabledExceptionHandle(UserNotEnabledException e) {
        log.error("User Not Enabled: {}", e.getMessage());
        return new ResponseEntity<>(new RequestError(InternalErrorCode.USER_NOT_ENABLED, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.ALREADY_REPORTED)
    @ExceptionHandler(UserAlreadyWasLoginException.class)
    public ResponseEntity<RequestError> userAlreadyWasLoginExceptionHandle(UserAlreadyWasLoginException e) {
        log.error("User Already Was Login : {}", e.getMessage());
        return new ResponseEntity<>(new RequestError(InternalErrorCode.USER_ALREADY_WAS_LOGIN, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

//    @ResponseStatus(HttpStatus.PROXY_AUTHENTICATION_REQUIRED)
//    @ExceptionHandler(JwtAuthenticationException.class)
//    public ResponseEntity<RequestError> jwtAuthenticationExceptionHandle(JwtAuthenticationException e) {
//        log.error("Jwt Authentication Exception: {}", e.getMessage());
//        return new ResponseEntity<>(new RequestError(InternalErrorCode.JWT_AUTHENTICATION, e.getMessage()), HttpStatus.BAD_REQUEST);
//    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<RequestError> tokenNotFoundExceptionHandle(TokenNotFoundException e) {
        log.error("Token Not Found: {}", e.getMessage());
        return new ResponseEntity<>(new RequestError(InternalErrorCode.TOKEN_NOT_FOUND, e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(TokenNotValidException.class)
    public ResponseEntity<RequestError> tokenNotValidExceptionHandle(TokenNotValidException e) {
        log.error("Token Not Valid: {}", e.getMessage());
        return new ResponseEntity<>(new RequestError(InternalErrorCode.TOKEN_NOT_VALID, e.getMessage()), HttpStatus.NOT_ACCEPTABLE);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<RequestError> runtimeExceptionHandle(RuntimeException e) {
        log.error("Runtime Exception: {}", e.getMessage());
        return new ResponseEntity<>(new RequestError(InternalErrorCode.RUNTIME_EXCEPTION, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_MODIFIED)
    @ExceptionHandler(TokenNotConfirmedException.class)
    public ResponseEntity<RequestError> tokenNotConfirmedException(TokenNotConfirmedException e) {
        log.error("Runtime Exception: {}", e.getMessage());
        return new ResponseEntity<>(new RequestError(InternalErrorCode.TOKEN_NOT_CONFIRMED, e.getMessage()), HttpStatus.NOT_MODIFIED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CommonException.class)
    public ResponseEntity<RequestError> commonExceptionHandle(CommonException e) {
        log.error("Runtime Exception: {}", e.getMessage());
        return new ResponseEntity<>(new RequestError(InternalErrorCode.COMMON_EXCEPTION, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

}