package com.rntgroup.api.controller;

import com.rntgroup.api.dto.EmployeeEditDto;
import com.rntgroup.api.dto.EmployeeReadDto;
import com.rntgroup.api.dto.EmployeeSaveDto;
import com.rntgroup.api.service.EmployeeService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class EmployeeRestControllerImpl implements EmployeeRestController {
  private final EmployeeService employeeService;

  @Override
  public ResponseEntity<EmployeeReadDto> getEmployee(
    @PathVariable("id")
    @Positive(message = "Employee id must be positive")
    @Parameter(description = "Employee ID") Long id
  ) {
    var employee = employeeService.getEmployeeInfo(id);
    return new ResponseEntity<>(employee, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<EmployeeReadDto> saveEmployee(
    @RequestBody @Valid @Parameter(description = "Save DTO") EmployeeSaveDto employeeSaveDto
  ) {
    var employee = employeeService.hireEmployee(employeeSaveDto);
    return new ResponseEntity<>(employee, HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<EmployeeReadDto> deleteEmployee(
    @PathVariable("id") @Positive(message = "Employee id must be positive") Long id
  ) {
    var employee = employeeService.deleteEmployee(id);
    return new ResponseEntity<>(employee, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<EmployeeReadDto> updateEmployee(
    @PathVariable("id")
    @Positive(message = "Employee id must be positive")
    @Parameter(description = "Employee ID") Long id,
    @RequestBody @Valid @Parameter(description = "Employee update DTO")
    EmployeeEditDto employeeEditDto
  ) {
    var updatedEmployee = employeeService.updateEmployee(employeeEditDto, id);
    return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
  }
}
