package com.rntgroup.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
@Schema(description = "Response with JWT")
public record JwtAuthenticationResponse(

  @NotBlank(message = "Jwt can't be nullable or empty")
  String token
) {

}
