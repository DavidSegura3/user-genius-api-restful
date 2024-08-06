package com.nisum.api.cl.domain.usecase;

import com.nisum.api.cl.domain.model.user.User;
import com.nisum.api.cl.domain.model.user.dtos.UserDTO;
import com.nisum.api.cl.domain.model.user.gateway.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    public CreateUserUseCaseTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddUser() {

        User user = new User();
        UserDTO expectedUserDTO = new UserDTO();
        when(userRepository.save(user)).thenReturn(expectedUserDTO);
        UserDTO actualUserDTO = createUserUseCase.addUser(user);
        assertEquals(expectedUserDTO, actualUserDTO);
        verify(userRepository, times(1)).save(user);
    }
}