package com.rntgroup.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Schema(description = "Register Request")
public record RegisterDto(

  @NotBlank(message = "Username can't be nullable or empty")
  String username,

  @Positive(message = "Employee ID must be positive")
  Long employeeId,

  @NotBlank
  @Email(message = "Email not valid")
  String email,

  @NotBlank(message = "Password can't be nullable or empty")
  String password
) {

}