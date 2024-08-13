package com.nisum.api.cl.infrastructure.adapters;

import com.nisum.api.cl.domain.exceptions.business.PasswordInvalidException;
import com.nisum.api.cl.domain.exceptions.business.ResourceEmailFoundException;
import com.nisum.api.cl.domain.model.user.User;
import com.nisum.api.cl.domain.model.user.dtos.UserDTO;
import com.nisum.api.cl.infrastructure.adapters.jpa.role.RoleDataDAO;
import com.nisum.api.cl.infrastructure.adapters.jpa.role.data.RoleData;
import com.nisum.api.cl.infrastructure.adapters.jpa.user.UserDataDAO;
import com.nisum.api.cl.infrastructure.adapters.jpa.user.UserPhoneDataDAO;
import com.nisum.api.cl.infrastructure.adapters.jpa.user.data.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDbRepositoryAdapterTest {

    @Mock
    private UserDataDAO userDao;

    @Mock
    private RoleDataDAO roleDao;

    @Mock
    private UserPhoneDataDAO userPhoneDao;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserDbRepositoryAdapter userDbRepositoryAdapter;

    private User user;
    private UserData userData;
    private RoleData roleData;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("Password123!");
        user.setAdmin(false);

        userData = new UserData();
        userData.setEmail("test@example.com");
        userData.setPassword("encodedPassword");
        userData.setUpdatedDate(new Date());

        roleData = new RoleData();
        roleData.setName("ROLE_USER");
    }

    @Test
    void testSave_ValidUser_Success() {

        when(roleDao.findByName("ROLE_USER")).thenReturn(Optional.of(roleData));
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userDao.save(any(UserData.class))).thenReturn(userData);
        when(modelMapper.map(any(User.class), eq(UserData.class))).thenReturn(userData);
        when(modelMapper.map(any(UserData.class), eq(UserDTO.class))).thenReturn(new UserDTO());

        userData.setPhones(Collections.emptyList());
        UserDTO result = userDbRepositoryAdapter.save(user);

        assertNotNull(result);
        verify(userDao, times(1)).save(any(UserData.class));
    }

    @Test
    void testSave_EmailAlreadyExists_ThrowsResourceEmailFoundException() {
        when(userDao.findByEmail(user.getEmail())).thenReturn(Optional.of(userData));
        assertThrows(ResourceEmailFoundException.class, () -> userDbRepositoryAdapter.save(user));
    }

    @Test
    void testSave_InvalidPassword_ThrowsPasswordInvalidException() {
        user.setPassword("weakpass");
        assertThrows(PasswordInvalidException.class, () -> userDbRepositoryAdapter.save(user));
    }
}
