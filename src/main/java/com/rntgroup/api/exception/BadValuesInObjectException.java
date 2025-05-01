package com.rntgroup.api.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BadValuesInObjectException extends RuntimeException {

  public BadValuesInObjectException(String message) {
    super(message);
  }
}