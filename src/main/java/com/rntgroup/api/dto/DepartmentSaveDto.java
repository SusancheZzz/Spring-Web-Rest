package com.rntgroup.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record DepartmentSaveDto(

  @NotBlank(message = "Department name can't be empty")
  String name,

  @Positive(message = "Parent Department id must be positive")
  Long parentDepartmentId,

  @Positive(message = "Leader id must be positive")
  Long leaderId
) {

}