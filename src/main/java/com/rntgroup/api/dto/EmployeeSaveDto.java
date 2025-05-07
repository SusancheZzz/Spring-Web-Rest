package com.rntgroup.api.dto;

import com.rntgroup.api.dto.validation.IsAdult;
import com.rntgroup.api.dto.validation.IsGender;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;

public record EmployeeSaveDto(

  @NotBlank(message = "Surname can't be nullable or empty")
  String surname,

  @NotBlank(message = "Name can't be nullable or empty")
  String name,

  @NotBlank(message = "Patronymic can't be nullable or empty")
  String patronymic,

  @NotBlank(message = "Gender can't be nullable or empty")
  @IsGender(message = "Gender must be MALE or FEMALE")
  String gender,

  @NotNull
  @Past
  @IsAdult(message = "New employee is too young")
  LocalDate birthday,

  @NotBlank
  @Pattern(
    regexp = "^\\+7 \\(\\d{3}\\) \\d{3}-\\d{2}-\\d{2}$",
    message = "This phone number is not correct"
  )
  String phoneNumber,

  @NotNull(message = "Department id can't be nullable")
  @Positive(message = "Department id must be positive")
  Long departmentId,

  @NotBlank
  String position,

  @NotNull(message = "Payment must be declared")
  @Min(value = 19_242, message = "Payment is less than minimum")
  Integer payment
) {

}