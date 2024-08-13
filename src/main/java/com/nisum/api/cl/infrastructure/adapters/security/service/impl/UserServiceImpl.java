package com.nisum.api.cl.infrastructure.adapters.security.service.impl;

import com.nisum.api.cl.infrastructure.adapters.jpa.user.UserDataDAO;
import com.nisum.api.cl.infrastructure.adapters.jpa.user.data.UserData;
import com.nisum.api.cl.infrastructure.adapters.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDataDAO userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserData> findAll() {
        return userRepository.findAll();
    }
}
