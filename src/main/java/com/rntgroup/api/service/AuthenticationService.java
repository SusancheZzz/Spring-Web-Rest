package com.rntgroup.api.service;

import com.rntgroup.api.dto.AuthenticateDto;
import com.rntgroup.api.dto.JwtAuthenticationResponse;
import com.rntgroup.api.dto.RegisterDto;
import com.rntgroup.api.entity.AccountEntity;
import com.rntgroup.api.entity.Role;
import com.rntgroup.api.exception.EmployeeNotFoundException;
import com.rntgroup.api.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@DependsOn({"employeeRepository", "accountService", "passwordEncoder"})
public class AuthenticationService {

  private final AccountService accountService;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final EmployeeRepository employeeRepository;

  public JwtAuthenticationResponse registration(RegisterDto request) {
    Long employeeId = request.employeeId();
    var employee = employeeRepository.findById(employeeId)
      .orElseThrow(() -> new EmployeeNotFoundException(employeeId));

    var account = AccountEntity.builder()
      .username(request.username())
      .employee(employee)
      .email(request.email())
      .password(passwordEncoder.encode(request.password()))
      .role(Role.ROLE_USER)
      .build();

    accountService.createAccount(account);
    var jwt = jwtService.generateToken(account);
    return new JwtAuthenticationResponse(jwt);
  }

  public JwtAuthenticationResponse authenticate(AuthenticateDto request) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
      request.username(),
      request.password()
    ));
    var account = accountService
      .userDetailsService()
      .loadUserByUsername(request.username());

    var jwt = jwtService.generateToken(account);
    return new JwtAuthenticationResponse(jwt);
  }

}
