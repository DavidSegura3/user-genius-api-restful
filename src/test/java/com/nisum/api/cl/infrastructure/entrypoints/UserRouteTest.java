package com.nisum.api.cl.infrastructure.entrypoints;

import com.nisum.api.cl.domain.model.user.User;
import com.nisum.api.cl.domain.model.user.dtos.UserDTO;
import com.nisum.api.cl.domain.usecase.CreateUserUseCase;
import com.nisum.api.cl.infrastructure.adapters.jpa.user.data.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserRouteTest {

    private UserRoute userRoute;
    @Autowired
    private ModelMapper mapper;
    private CreateUserUseCase userUseCase;
    private BindingResult bindingResult;

    @BeforeEach
    public void setUp() {
        mapper = mock(ModelMapper.class);
        userUseCase = mock(CreateUserUseCase.class);
        bindingResult = mock(BindingResult.class);
        userRoute = new UserRoute(mapper, userUseCase);
    }

    @Test
    public void testSaveUser_Success() {

        UserData userData = new UserData();
        userData.setEmail("test@example.com");
        userData.setName("Test User");

        User user = User.builder()
                .email("test@example.com")
                .name("Test User")
                .build();

        UserDTO userDTO = UserDTO.builder()
                .id(UUID.randomUUID())
                .createdDate(new Date())
                .updatedDate(new Date())
                .isActive(true)
                .build();

        when(mapper.map(any(UserData.class), any(Class.class))).thenReturn(user);
        when(userUseCase.addUser(any(User.class))).thenReturn(userDTO);
        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<?> response = userRoute.save(userData, bindingResult);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
    }

    @Test
    public void testSaveUser_ValidationErrors() {
        UserData userData = new UserData();
        FieldError fieldError = new FieldError("userData", "email", "Email is required");
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<?> response = userRoute.save(userData, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> expectedResponse = new HashMap<>();
        expectedResponse.put("Error List", List.of("Field: 'email' Email is required"));
        assertEquals(expectedResponse, response.getBody());
    }
}