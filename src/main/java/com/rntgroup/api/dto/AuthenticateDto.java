package com.rntgroup.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Auth Request")
public record AuthenticateDto(

  @NotBlank(message = "Username can't be nullable or empty")
  String username,

  @NotBlank(message = "Password can't be nullable or empty")
  String password
) {

}
