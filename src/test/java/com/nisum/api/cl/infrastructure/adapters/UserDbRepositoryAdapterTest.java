package com.nisum.api.cl.infrastructure.adapters;

import com.nisum.api.cl.domain.exceptions.business.ResourceEmailFoundException;
import com.nisum.api.cl.domain.model.user.User;
import com.nisum.api.cl.domain.model.user.dtos.UserDTO;
import com.nisum.api.cl.infrastructure.adapters.jpa.user.UserDataDAO;
import com.nisum.api.cl.infrastructure.adapters.jpa.user.UserPhoneDataDAO;
import com.nisum.api.cl.infrastructure.adapters.jpa.user.data.UserData;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class UserDbRepositoryAdapterTest {

    @Mock
    private UserDataDAO userDao;

    @Mock
    private UserPhoneDataDAO userPhoneDao;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserDbRepositoryAdapter userRepositoryAdapter;

    public UserDbRepositoryAdapterTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave_UserAlreadyExists_ShouldThrowException() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(new UserData()));

        assertThrows(ResourceEmailFoundException.class, () -> userRepositoryAdapter.save(user));
    }

    @Test
    void testSave_SuccessfulSave_ShouldReturnUserDTO() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setIsActive(true);

        UserData userData = new UserData();
        userData.setPhones(Collections.emptyList()); // Ensure phones is an empty list
        UserData savedUserData = new UserData();
        UserDTO userDTO = new UserDTO();

        when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(modelMapper.map(user, UserData.class)).thenReturn(userData);
        when(userDao.save(userData)).thenReturn(savedUserData);
        when(modelMapper.map(savedUserData, UserDTO.class)).thenReturn(userDTO);

        UserDTO result = userRepositoryAdapter.save(user);

        verify(userDao).findByEmail(user.getEmail());
        verify(userDao).save(userData);
        verify(modelMapper).map(user, UserData.class);
        verify(modelMapper).map(savedUserData, UserDTO.class);

        assertEquals(userDTO, result);
    }
}