package com.rntgroup.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignUpDto(

  @NotBlank
  String username,

  @NotBlank
  @Email (message = "Email not valid")
  String email,

  @NotBlank
  String password
) {

}