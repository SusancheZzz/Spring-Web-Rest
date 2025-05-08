package com.rntgroup.api.dto;

import java.time.LocalDate;

public record EmployeeReadDto(

  Long id,
  String surname,
  String name,
  String phoneNumber,
  LocalDate birthday,
  String departmentName,
  String position,
  Integer payment
) {

}