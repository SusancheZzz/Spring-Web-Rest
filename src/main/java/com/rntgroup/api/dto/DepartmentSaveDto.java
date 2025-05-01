package com.rntgroup.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentSaveDto {

  @NotBlank(message = "Department name can't be empty")
  String name;

  @Positive(message = "Parent Department id must be positive")
  Long parentDepartmentId;

  public Optional<Long> getParentDepartmentId() {
    return Optional.ofNullable(parentDepartmentId);
  }

  public DepartmentSaveDto(String name) {
    this.name = name;
    this.parentDepartmentId = null;
  }
}