package com.rntgroup.impl.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PaymentNotValidException extends RuntimeException {

  public PaymentNotValidException(String message) {
    super(message);
  }
}