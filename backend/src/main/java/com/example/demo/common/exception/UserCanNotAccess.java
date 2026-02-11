package com.example.demo.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserCanNotAccess extends RuntimeException {
    public UserCanNotAccess(String message) {
        super(message);
    }
}
