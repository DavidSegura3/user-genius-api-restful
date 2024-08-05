package com.nisum.api.cl.domain.model.user.gateway;

import com.nisum.api.cl.domain.model.user.User;

public interface UserRepository {

    User save(User user);
}
