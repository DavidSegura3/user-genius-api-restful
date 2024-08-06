package com.nisum.api.cl.infrastructure.entrypoints.exceptions;

import com.nisum.api.cl.domain.exceptions.business.ResourceEmailFoundException;
import com.nisum.api.cl.domain.exceptions.business.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@ControllerAdvice
public class UserHandlerException {

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<ErrorMessage> globalNotFoundException(ResourceNotFoundException exception, WebRequest request) {

        ErrorMessage message = ErrorMessage
                .builder()
                .statusCode(NOT_FOUND.value())
                .message(exception.getMessage())
                .route(request.getDescription(false))
                .build();
        log.error(message.toString());
        return new ResponseEntity<>(message, NOT_FOUND);
    }

    @ExceptionHandler(value = {ResourceEmailFoundException.class})
    public ResponseEntity<ErrorMessage> globalResourceFoundException(ResourceEmailFoundException exception, WebRequest request) {

        ErrorMessage message = ErrorMessage
                .builder()
                .statusCode(CONFLICT.value())
                .message(exception.getMessage())
                .route(request.getDescription(false))
                .build();
        log.error(message.toString());
        return new ResponseEntity<>(message, CONFLICT);
    }

    @ExceptionHandler(value = {HttpClientErrorException.class})
    public ResponseEntity<?> globalRestClientException(HttpClientErrorException exception, WebRequest request) {

        ErrorMessage message = ErrorMessage
                .builder()
                .statusCode(UNAUTHORIZED.value())
                .message(exception.getMessage())
                .route(request.getDescription(false))
                .build();
        return new ResponseEntity<>(message, UNAUTHORIZED);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<?> globalRestClientException(Exception exception, WebRequest request) {

        ErrorMessage message = ErrorMessage
                .builder()
                .statusCode(INTERNAL_SERVER_ERROR.value())
                .message(exception.getMessage())
                .route(request.getDescription(false))
                .build();
        return new ResponseEntity<>(message, INTERNAL_SERVER_ERROR);
    }
}
