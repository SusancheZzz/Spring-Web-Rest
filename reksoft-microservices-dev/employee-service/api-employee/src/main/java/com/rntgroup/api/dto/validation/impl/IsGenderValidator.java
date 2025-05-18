package com.rntgroup.api.dto.validation.impl;

import com.rntgroup.api.dto.validation.IsGender;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsGenderValidator implements ConstraintValidator<IsGender, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return value != null && (value.equals("MALE") || value.equals("FEMALE"));
  }
}