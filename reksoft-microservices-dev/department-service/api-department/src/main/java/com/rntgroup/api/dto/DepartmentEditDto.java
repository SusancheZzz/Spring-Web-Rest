package com.rntgroup.api.dto;

import jakarta.validation.constraints.NotBlank;

public record DepartmentEditDto(
  @NotBlank(message = "Department name can't be empty")
  String name
) {

}
