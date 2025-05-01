package com.rntgroup.api.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentReadDto {

  Long id;
  String name;
  LocalDate createdAt;
  String leaderSurname;
  Integer employeesNumber;
}