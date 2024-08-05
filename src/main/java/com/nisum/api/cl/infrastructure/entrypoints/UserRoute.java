package com.nisum.api.cl.infrastructure.entrypoints;

import com.nisum.api.cl.domain.model.user.User;
import com.nisum.api.cl.domain.usecase.CreateUserUseCase;
import com.nisum.api.cl.infrastructure.adapters.jpa.data.UserData;
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
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Example API", description = "Example API for demonstrating OpenAPI integration")
@RequiredArgsConstructor
public class UserRoute {

    private final CreateUserUseCase userUseCase;

    @Qualifier("defaultMapper")
    private final ModelMapper mapper;

    @Operation(summary = "Get a greeting message")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the greeting"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Greeting not found")
    })
    @GetMapping("/greeting")
    public String getGreeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello, %s!", name);
    }

    @ApiResponse(responseCode = "400", description = "When the request have a field invalid we response this",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            })
    @Operation(summary = "Save in system a document")
    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody UserData user){

        return new ResponseEntity<>(userUseCase.addUser(mapper.map(user, User.class)), HttpStatus.CREATED);
    }
}
