package com.rntgroup.api.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeReadDto {

  Long id;
  String surname;
  String name;
  String phoneNumber;
  LocalDate birthday;
  String departmentName;
  String position;
  Integer payment;
}