package com.example.demo.common.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String entityName, Long id) {
        super(String.format("%s with id %d not found", entityName, id));
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}