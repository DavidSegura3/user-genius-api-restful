package com.nisum.api.cl.infrastructure.adapters.security.utils;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;

public class TokenSecretKey {
    public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
}
