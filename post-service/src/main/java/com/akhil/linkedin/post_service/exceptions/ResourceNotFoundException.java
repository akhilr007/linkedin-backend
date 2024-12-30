package com.akhil.linkedin.post_service.exceptions;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException() {}

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
