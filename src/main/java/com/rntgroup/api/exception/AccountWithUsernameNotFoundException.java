package com.rntgroup.api.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class AccountWithUsernameNotFoundException extends UsernameNotFoundException {

  private static final String USER_WITH_USERNAME_NOT_FOUND = "User with username: %s not found";

  public AccountWithUsernameNotFoundException(String username) {
    super(USER_WITH_USERNAME_NOT_FOUND.formatted(username));
  }
}
