package com.rntgroup.impl.exception;

import jakarta.persistence.EntityNotFoundException;

public class EmployeeNotFoundException extends EntityNotFoundException {

  private static final String EMPLOYEE_WITH_ID_NOT_FOUND = "Employee with id: %s not found";

  public EmployeeNotFoundException(Long employeeId) {
    super(EMPLOYEE_WITH_ID_NOT_FOUND.formatted(employeeId));
  }
}
