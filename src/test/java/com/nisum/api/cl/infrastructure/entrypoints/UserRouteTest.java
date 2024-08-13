package com.nisum.api.cl.infrastructure.entrypoints;

import com.nisum.api.cl.domain.model.user.User;
import com.nisum.api.cl.domain.model.user.dtos.UserDTO;
import com.nisum.api.cl.domain.usecase.CreateUserUseCase;
import com.nisum.api.cl.infrastructure.adapters.jpa.user.data.UserData;
import com.nisum.api.cl.infrastructure.entrypoints.utils.Validations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRouteTest {

    @Mock
    private ModelMapper mapper;

    @Mock
    private CreateUserUseCase userUseCase;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private Validations validations;

    @InjectMocks
    private UserRoute userRoute;

    private UserData userData;
    private User user;

    @BeforeEach
    void setUp() {
        userData = new UserData();
        userData.setEmail("test@example.com");
        userData.setPassword("Password123!");

        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("Password123!");
    }

    @Test
    void testSave_ValidUser_ReturnsCreated() {

        UserDTO userDTO = UserDTO.builder()
                .id(UUID.randomUUID())
                .createdDate(new Date())
                .updatedDate(new Date())
                .isActive(true)
                .build();

        when(bindingResult.hasErrors()).thenReturn(false);
        when(mapper.map(userData, User.class)).thenReturn(user);
        when(userUseCase.addUser(any(User.class))).thenReturn(userDTO);

        ResponseEntity<?> response = userRoute.save(userData, bindingResult);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
        verify(userUseCase, times(1)).addUser(any(User.class));
    }

    @Test
    void testSave_InvalidUser_ReturnsBadRequest() {
        try (var mockedValidations = mockStatic(Validations.class)) {
            when(bindingResult.hasErrors()).thenReturn(true);
            mockedValidations.when(() -> Validations.validation(bindingResult))
                    .thenReturn(new ResponseEntity<>("Validation Error", HttpStatus.BAD_REQUEST));

            ResponseEntity<?> response = userRoute.save(userData, bindingResult);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("Validation Error", response.getBody());
            verify(userUseCase, never()).addUser(any(User.class));
        }
    }
}
