package com.nisum.api.cl.infrastructure.adapters.security.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenJwt {

    PREFIX_TOKEN("Bearer "),
    HEADER_AUTHORIZATION("Authorization"),
    CONTENT_TYPE("application/json"),
    AUTHORITIES("authorities"),
    USERNAME("username");


    private final String value;
}
