package com.rntgroup.impl.controller.advice;

import java.time.LocalDateTime;

public record ErrorResponse(
  ErrorCode code,
  String message,
  LocalDateTime timestamp
) {

}
