package com.nisum.api.cl.domain.exceptions.business;

public class ResourceNotFoundException extends RuntimeException{

    private static final String ERROR_MESSAGE = "Does not exist record in database %s with id: %s";

    public ResourceNotFoundException(String message, Long id) {
        super(String.format(ERROR_MESSAGE, message, id));
    }

    public ResourceNotFoundException(String message, String value) {
        super(String.format(ERROR_MESSAGE, message, value));
    }
}
