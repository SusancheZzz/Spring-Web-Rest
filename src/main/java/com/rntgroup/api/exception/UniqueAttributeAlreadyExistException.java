package com.rntgroup.api.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UniqueAttributeAlreadyExistException extends RuntimeException {

  public UniqueAttributeAlreadyExistException(String message) {
    super(message);
  }
}
