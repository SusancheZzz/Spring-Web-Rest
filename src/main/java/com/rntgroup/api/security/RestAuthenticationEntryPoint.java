package com.rntgroup.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rntgroup.api.controller.advice.ErrorCode;
import com.rntgroup.api.controller.advice.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper mapper;

  @Override
  public void commence(
    HttpServletRequest request,
    HttpServletResponse response,
    AuthenticationException authException) throws IOException {

    ErrorResponse errorResponse = new ErrorResponse(
      ErrorCode.UNAUTHORIZED_ACCOUNT,
      "You need to authenticate first",
      LocalDateTime.now()
    );

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");
    response.getWriter().write(mapper.writeValueAsString(errorResponse));
  }
}