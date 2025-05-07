package com.rntgroup.api.dto;

import java.time.LocalDate;

public record DepartmentReadDto(

  Long id,
  String name,
  LocalDate createdAt,
  Integer leaderId,
  Integer employeesNumber
) {

}