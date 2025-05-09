//package com.rntgroup.api.security;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.rntgroup.api.controller.advice.ErrorCode;
//import com.rntgroup.api.controller.advice.ErrorResponse;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.time.LocalDateTime;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.web.access.AccessDeniedHandler;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class RestAccessDeniedHandler implements AccessDeniedHandler {
//
//  private final ObjectMapper mapper;
//
//  @Override
//  public void handle(
//    HttpServletRequest request,
//    HttpServletResponse response,
//    AccessDeniedException accessDeniedException) throws IOException {
//
//    ErrorResponse errorResponse = new ErrorResponse(
//      ErrorCode.FORBIDDEN,
//      "The user does not have sufficient rights to perform this operation",
//      LocalDateTime.now()
//    );
//
//    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//    response.setContentType("application/json");
//    response.getWriter().write(mapper.writeValueAsString(errorResponse));
//  }
//}
