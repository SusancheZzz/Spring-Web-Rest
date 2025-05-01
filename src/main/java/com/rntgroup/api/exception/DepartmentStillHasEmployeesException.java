package com.rntgroup.api.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DepartmentStillHasEmployeesException extends RuntimeException {

  public DepartmentStillHasEmployeesException(String message) {
    super(message);
  }
}
