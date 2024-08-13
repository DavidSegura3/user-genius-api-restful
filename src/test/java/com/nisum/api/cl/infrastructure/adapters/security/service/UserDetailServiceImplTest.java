package com.nisum.api.cl.infrastructure.adapters.security.service;

import com.nisum.api.cl.infrastructure.adapters.jpa.user.UserDataDAO;
import com.nisum.api.cl.infrastructure.adapters.jpa.user.data.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailServiceImplTest {

    @Mock
    private UserDataDAO userRepository;

    @InjectMocks
    private UserDetailServiceImpl userDetailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_UserExists_ReturnsUserDetails() {
        // Arrange
        String username = "testuser";
        UserData user = new UserData();
        user.setUsername(username);
        user.setPassword("password");
        user.setIsActive(true);
        user.setRoles(Collections.emptyList()); // Suponiendo que no hay roles

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userDetailService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void loadUserByUsername_UserDoesNotExist_ThrowsUsernameNotFoundException() {
        // Arrange
        String username = "nonexistentuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailService.loadUserByUsername(username);
        });

        verify(userRepository, times(1)).findByUsername(username);
    }
}
