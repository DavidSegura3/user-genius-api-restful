package com.nisum.api.cl.infrastructure.adapters;

import com.nisum.api.cl.domain.exceptions.business.PasswordInvalidException;
import com.nisum.api.cl.domain.exceptions.business.ResourceEmailFoundException;
import com.nisum.api.cl.domain.model.user.User;
import com.nisum.api.cl.domain.model.user.dtos.UserDTO;
import com.nisum.api.cl.domain.model.user.gateway.UserRepository;
import com.nisum.api.cl.infrastructure.adapters.jpa.role.RoleDataDAO;
import com.nisum.api.cl.infrastructure.adapters.jpa.role.data.RoleData;
import com.nisum.api.cl.infrastructure.adapters.jpa.user.UserDataDAO;
import com.nisum.api.cl.infrastructure.adapters.jpa.user.UserPhoneDataDAO;
import com.nisum.api.cl.infrastructure.adapters.jpa.user.data.UserData;
import com.nisum.api.cl.infrastructure.adapters.jpa.user.data.UserPhoneData;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Repository
@RequiredArgsConstructor
public class UserDbRepositoryAdapter implements UserRepository {

    private final UserDataDAO userDao;
    private final RoleDataDAO roleDao;
    private final UserPhoneDataDAO userPhoneDao;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private Optional<RoleData> rol;
    private List<RoleData> roles = new ArrayList<>();

    @Override
    public UserDTO save(User user) {

        if (!Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$.!%*?&]{8,}$", user.getPassword())) {
            throw new PasswordInvalidException();
        }

        rol = roleDao.findByName("ROLE_USER");
        rol.ifPresent(roles::add);

        if(user.isAdmin()){
            rol = roleDao.findByName("ROLE_ADMIN");
            rol.ifPresent(roles::add);
        }
        user.setRoles(roles);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        UserData userData = toDataUser(user);

        Optional<UserData> userFound = userDao.findByEmail(user.getEmail());
        if(userFound.isPresent()){
            throw new ResourceEmailFoundException(user.getEmail());
        }
        UserData savedUser  = userDao.save(userData);
        List<UserPhoneData> phones = userData.getPhones();

        if (phones.isEmpty()) {
            phones = Collections.emptyList();
        }

        phones.forEach(p -> {
            p.setUser(userData);
            userPhoneDao.save(p);
        });
        savedUser.setPhones(phones);
        return toDtoUser(savedUser);
    }

    private UserDTO toDtoUser(UserData userData){
        return modelMapper.map(userData, UserDTO.class);
    }
    private UserData toDataUser(User user){
        return modelMapper.map(user, UserData.class);
    }

}
