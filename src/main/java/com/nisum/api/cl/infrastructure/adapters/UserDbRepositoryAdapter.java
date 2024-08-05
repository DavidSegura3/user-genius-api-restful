package com.nisum.api.cl.infrastructure.adapters;

import com.nisum.api.cl.domain.model.user.User;
import com.nisum.api.cl.domain.model.user.UserPhone;
import com.nisum.api.cl.domain.model.user.gateway.UserRepository;
import com.nisum.api.cl.infrastructure.adapters.jpa.UserDataDAO;
import com.nisum.api.cl.infrastructure.adapters.jpa.UserPhoneDataDAO;
import com.nisum.api.cl.infrastructure.adapters.jpa.data.UserData;
import com.nisum.api.cl.infrastructure.adapters.jpa.data.UserPhoneData;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserDbRepositoryAdapter implements UserRepository {

    private final UserDataDAO userDao;
    private final UserPhoneDataDAO userPhoneDao;
    private final ModelMapper modelMapper;

    @Override
    public User save(User user) {

        UserData userData = toDataUser(user);
        UserData resultUser = userDao.save(userData);

        List<UserPhone> phones = user.getPhones();
        phones.forEach(p -> {
            userData.addPhone(toDataPhone(p));
            UserPhoneData userPhoneData = toDataPhone(p);
            userPhoneDao.save(userPhoneData);
        });

        //userPhoneDao.save(userPhoneData);


        /*List<UserPhone> phones = user.getPhones();

        phones.forEach(p -> {

            UserPhoneData userPhoneData2 = UserPhoneData
                    .builder()
                    .number(p.getNumber())
                    .cityCode(p.getCityCode())
                    .countryCode(p.getCountryCode())
                    .user(resultUser)
                    .build();
            user.getPhones().add(p);
            UserPhoneData userPhoneData = toDataPhone(toEntityPhone(userPhoneData2));
            userPhoneDao.save(userPhoneData);
        });*/

        return toEntityUser(resultUser);
    }

    private User toEntityUser(UserData userData){
        return modelMapper.map(userData, User.class);
    }
    private UserData toDataUser(User user){
        return modelMapper.map(user, UserData.class);
    }


    private UserPhone toEntityPhone(UserPhoneData userPhoneData){
        return modelMapper.map(userPhoneData, UserPhone.class);
    }

    private UserPhoneData toDataPhone(UserPhone userPhone){
        return modelMapper.map(userPhone, UserPhoneData.class);
    }
}
