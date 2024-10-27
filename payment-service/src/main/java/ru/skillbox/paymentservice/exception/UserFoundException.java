package ru.skillbox.paymentservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FOUND)
public class UserFoundException extends RuntimeException {
    public UserFoundException(Long userId) {
        super("User with id '" + userId + "' already exists.");
    }
}
