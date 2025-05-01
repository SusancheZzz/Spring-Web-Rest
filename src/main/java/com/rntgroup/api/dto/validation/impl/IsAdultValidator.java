package com.rntgroup.api.dto.validation.impl;

import com.rntgroup.api.dto.validation.IsAdult;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class IsAdultValidator implements ConstraintValidator<IsAdult, LocalDate> {

  private static final int ADULTHOOD = 18;

  @Override
  public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
    var now = LocalDate.now();
    return value != null && (now.getYear() - value.getYear() >= ADULTHOOD);
  }
}