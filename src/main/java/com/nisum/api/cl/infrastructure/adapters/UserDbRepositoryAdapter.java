package com.nisum.api.cl.infrastructure.adapters;

import com.nisum.api.cl.domain.exceptions.business.ResourceEmailFoundException;
import com.nisum.api.cl.domain.model.user.User;
import com.nisum.api.cl.domain.model.user.dtos.UserDTO;
import com.nisum.api.cl.domain.model.user.gateway.UserRepository;
import com.nisum.api.cl.infrastructure.adapters.jpa.UserDataDAO;
import com.nisum.api.cl.infrastructure.adapters.jpa.UserPhoneDataDAO;
import com.nisum.api.cl.infrastructure.adapters.jpa.data.UserData;
import com.nisum.api.cl.infrastructure.adapters.jpa.data.UserPhoneData;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDbRepositoryAdapter implements UserRepository {

    private final UserDataDAO userDao;
    private final UserPhoneDataDAO userPhoneDao;
    private final ModelMapper modelMapper;
    //private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO save(User user) {

        user.setIsActive(true);
        //user.setPassword(passwordEncoder.encode(user.getPassword()));
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
