package com.rntgroup.api.controller;

import com.rntgroup.api.dto.AuthenticateDto;
import com.rntgroup.api.dto.JwtAuthenticationResponse;
import com.rntgroup.api.dto.RegisterDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthRestController {

  @Operation(
    summary = "New account registration",
    description = "Registering a new account and getting it JWT after that"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Account successfully registered"),
    @ApiResponse(responseCode = "409", description =
      "The account could not be created because the username, email, employeeId is not valid")
  })
  @PostMapping("/registration")
  JwtAuthenticationResponse registrationAccount(
    @RequestBody
    @Valid
    @Parameter(description = "RegisterDto for creation new account") RegisterDto registerDto
  );

  @Operation(
    summary = "Login to account",
    description = "Login to account and get it JWT"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Successfully logged into account"),
    @ApiResponse(responseCode = "401", description = "Incorrect login or password")
  })
  @PostMapping("/auth")
  JwtAuthenticationResponse authenticateAccount(
    @RequestBody
    @Valid
    @Parameter(description = "AuthenticateDto for account login") AuthenticateDto authenticateDto
  );
}
