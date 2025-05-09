package com.rntgroup.api.controller;

import com.rntgroup.api.dto.AuthenticateDto;
import com.rntgroup.api.dto.JwtAuthenticationResponse;
import com.rntgroup.api.dto.RegisterDto;
import com.rntgroup.api.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthRestControllerImpl implements AuthRestController {

  private final AuthenticationService authenticationService;

  @Override
  public JwtAuthenticationResponse registrationAccount(
    @RequestBody @Valid RegisterDto registerDto) {
    return authenticationService.registration(registerDto);
  }

  @Override
  public JwtAuthenticationResponse authenticateAccount(
    @RequestBody @Valid AuthenticateDto authenticateDto) {
    return authenticationService.authenticate(authenticateDto);
  }
}
