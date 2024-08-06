package com.nisum.api.cl.domain.model.user.gateway;

import com.nisum.api.cl.domain.model.user.User;
import com.nisum.api.cl.domain.model.user.dtos.UserDTO;

public interface UserRepository {

    UserDTO save(User user);
}
