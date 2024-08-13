package com.nisum.api.cl.infrastructure.entrypoints.utils;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.Map;

public abstract class Validations {

    public static ResponseEntity<?> validation(BindingResult result) {

        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors()
                .forEach(err -> errors.put(err.getField(), "Field: " + err.getField() + " " + err.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }
}
