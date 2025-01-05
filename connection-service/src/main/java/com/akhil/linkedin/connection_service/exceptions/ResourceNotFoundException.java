package com.akhil.linkedin.connection_service.exceptions;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException() {}

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
