package com.nisum.api.cl.domain.model.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nisum.api.cl.infrastructure.adapters.jpa.role.data.RoleData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    private UUID id;
    private String username;
    private String name;
    private String email;
    private String password;
    private List<UserPhone> phones;
    private Date createdDate;
    private Date updatedDate;
    private Boolean isActive;
    private String token;
    private boolean admin;
    private List<RoleData> roles;
}
