package com.nisum.api.cl.domain.exceptions.business;

public class PasswordInvalidException extends RuntimeException{

    private static final String ERROR_MESSAGE = "Password must be at least 8 characters long, include at least one uppercase letter, one lowercase letter, one digit, and one special character.";

    public PasswordInvalidException() {
        super(String.format(ERROR_MESSAGE));
    }
}
