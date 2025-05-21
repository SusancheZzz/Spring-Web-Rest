package com.rntgroup.common.error;

import java.time.LocalDateTime;

public record ErrorResponse(
  ErrorCode code,
  String message,
  LocalDateTime timestamp
) {

}
