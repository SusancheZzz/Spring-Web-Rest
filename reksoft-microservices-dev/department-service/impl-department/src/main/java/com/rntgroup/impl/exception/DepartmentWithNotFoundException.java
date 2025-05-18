package com.rntgroup.impl.exception;

import jakarta.persistence.EntityNotFoundException;

public class DepartmentWithNotFoundException extends EntityNotFoundException {

  private static final String DEPARTMENT_WITH_NOT_FOUND = "Department with %s: %s not found";

  public DepartmentWithNotFoundException(String source, String value) {
    super(DEPARTMENT_WITH_NOT_FOUND.formatted(source, value));
  }
}
