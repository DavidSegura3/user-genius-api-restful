package com.nisum.api.cl.domain.exceptions.business;

public class ResourceEmailFoundException extends RuntimeException{

    private static final String ERROR_MESSAGE = "E-mail: %s already exists in database";

    public ResourceEmailFoundException(String email) {
        super(String.format(ERROR_MESSAGE, email));
    }
}
