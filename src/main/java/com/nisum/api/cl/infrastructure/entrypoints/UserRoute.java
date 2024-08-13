package com.nisum.api.cl.infrastructure.entrypoints;

import com.nisum.api.cl.domain.model.user.User;
import com.nisum.api.cl.domain.usecase.CreateUserUseCase;
import com.nisum.api.cl.infrastructure.adapters.jpa.user.data.UserData;
import com.nisum.api.cl.infrastructure.entrypoints.utils.Validations;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "API RESTful users", description = "OpenAPI integration BCI")
@RequiredArgsConstructor
public class UserRoute {

    @Qualifier("defaultMapper")
    private final ModelMapper mapper;

    private final CreateUserUseCase userUseCase;

    @Operation(summary = "Save in system a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "save user", content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "When the request have a field invalid we response this",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "When email already exists ", content = @Content)})
    @PostMapping("/register")
    public ResponseEntity<?> save(@Valid @RequestBody UserData user, BindingResult result) {

        if(result.hasErrors()){
            return Validations.validation(result);
        }
        return new ResponseEntity<>(userUseCase.addUser(mapper.map(user, User.class)), HttpStatus.CREATED);
    }
}
