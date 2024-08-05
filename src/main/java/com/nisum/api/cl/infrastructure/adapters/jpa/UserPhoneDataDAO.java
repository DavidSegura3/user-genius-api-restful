package com.nisum.api.cl.infrastructure.adapters.jpa;

import com.nisum.api.cl.infrastructure.adapters.jpa.data.UserPhoneData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserPhoneDataDAO extends JpaRepository<UserPhoneData, UUID> {
}
