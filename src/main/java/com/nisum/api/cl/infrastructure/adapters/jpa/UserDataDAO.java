package com.nisum.api.cl.infrastructure.adapters.jpa;

import com.nisum.api.cl.infrastructure.adapters.jpa.data.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserDataDAO extends JpaRepository<UserData, UUID> {
    Optional<UserData> findByEmail(String emailAddress);
}
