package com.nisum.api.cl.infrastructure.adapters.jpa.role;

import com.nisum.api.cl.infrastructure.adapters.jpa.role.data.RoleData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleDataDAO extends JpaRepository<RoleData, UUID> {

    Optional<RoleData> findByName(String name);
}
