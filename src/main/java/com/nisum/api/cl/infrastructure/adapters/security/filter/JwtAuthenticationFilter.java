package com.nisum.api.cl.infrastructure.adapters.security.filter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nisum.api.cl.infrastructure.adapters.jpa.user.data.UserData;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.nisum.api.cl.infrastructure.adapters.security.config.TokenJwtConfig.SECRET_KEY;
import static com.nisum.api.cl.infrastructure.adapters.security.enums.TokenJwt.AUTHORITIES;
import static com.nisum.api.cl.infrastructure.adapters.security.enums.TokenJwt.CONTENT_TYPE;
import static com.nisum.api.cl.infrastructure.adapters.security.enums.TokenJwt.HEADER_AUTHORIZATION;
import static com.nisum.api.cl.infrastructure.adapters.security.enums.TokenJwt.PREFIX_TOKEN;
import static com.nisum.api.cl.infrastructure.adapters.security.enums.TokenJwt.USERNAME;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    //private final static SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
    private final AuthenticationManager authenticationManager;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        UserData user;
        String username = null;
        String password = null;

        try {
            user = new ObjectMapper().readValue(request.getInputStream(), UserData.class);
            username = user.getUsername();
            password = user.getPassword();
        } catch (StreamReadException | DatabindException e) {
            log.error(e.getMessage());
        } catch (IOException e) {
            log.error(e.getCause().toString());
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();
        String username = user.getUsername();
        Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();

        Claims claims = Jwts.claims()
                .add(AUTHORITIES.getValue(), new ObjectMapper().writeValueAsString(roles))
                .add(USERNAME.getValue(), username)
                .build();


        String token = Jwts.builder()
                .subject(username)
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .issuedAt(new Date())
                .signWith(SECRET_KEY)
                .compact();

        response.addHeader(HEADER_AUTHORIZATION.getValue(), PREFIX_TOKEN.getValue() + token);

        Map<String, String> body = new HashMap<>();
        body.put("token", token);
        body.put("message", String.format("Hi %s you are successfully logged in!", username));

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setContentType(CONTENT_TYPE.getValue());
        response.setStatus(200);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        Map<String, String> body = new HashMap<>();
        body.put("message", "Incorrect username or password!, please try again");
        body.put("error", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);
        response.setContentType(CONTENT_TYPE.getValue());
    }
}
