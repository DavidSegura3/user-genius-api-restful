package com.nisum.api.cl.infrastructure.adapters.security.service;

import com.nisum.api.cl.infrastructure.adapters.jpa.user.data.UserData;

import java.util.List;

public interface UserService {
    List<UserData> findAll();
}
