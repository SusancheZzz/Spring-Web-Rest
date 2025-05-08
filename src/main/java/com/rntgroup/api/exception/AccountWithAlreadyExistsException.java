package com.rntgroup.api.exception;

public class AccountWithAlreadyExistsException extends RuntimeException {

  private static final String ACCOUNT_EXISTS_MESSAGE = "Account with this %s:%s already exists";

  public AccountWithAlreadyExistsException(String with, String data) {
    super(ACCOUNT_EXISTS_MESSAGE.formatted(with, data));
  }
}
