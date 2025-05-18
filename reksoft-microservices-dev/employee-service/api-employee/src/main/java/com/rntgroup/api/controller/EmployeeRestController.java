package com.rntgroup.api.controller;

import com.rntgroup.api.dto.EmployeeEditDto;
import com.rntgroup.api.dto.EmployeeReadDto;
import com.rntgroup.api.dto.EmployeeSaveDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface EmployeeRestController {

  @Operation(
    summary = "Get information about employee",
    description = "Get information about employee by id"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Employee found successfully"),
    @ApiResponse(responseCode = "404", description = "Employee not found")
  })
  @GetMapping("{id}")
  ResponseEntity<EmployeeReadDto> getEmployee(
    @PathVariable("id")
    @Positive(message = "Employee id must be positive")
    @Parameter(description = "Employee ID") Long id
  );

  @Operation(
    summary = "Hire a worker",
    description = "Hire a new worker with department indication"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Employee saved successfully"),
    @ApiResponse(responseCode = "404", description = "Department not found"),
    @ApiResponse(responseCode = "400", description = "Phone number already registered")
  })
  @PostMapping
  ResponseEntity<EmployeeReadDto> saveEmployee(
    @RequestBody @Valid @Parameter(description = "Save DTO") EmployeeSaveDto employeeSaveDto
  );

  @Operation(
    summary = "Delete an employee",
    description = "Employee deleting by ID"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Employee delete successfully"),
    @ApiResponse(responseCode = "404", description = "Employee not found")
  })
  @DeleteMapping("{id}")
  ResponseEntity<EmployeeReadDto> deleteEmployee(
    @PathVariable("id") @Positive(message = "Employee id must be positive") Long id
  );

  @Operation(
    summary = "Update employee info",
    description = "Changing information about a worker, the ability to make him "
      + "a department leader or move him to another department"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Employee update successfully"),
    @ApiResponse(responseCode = "404", description = "Employee not found"),
    @ApiResponse(responseCode = "400", description = "Phone number already registered")
  })
  @PutMapping("{id}")
  ResponseEntity<EmployeeReadDto> updateEmployee(
    @PathVariable("id")
    @Positive(message = "Employee id must be positive")
    @Parameter(description = "Employee ID") Long id,
    @RequestBody @Valid @Parameter(description = "Employee update DTO")
    EmployeeEditDto employeeEditDto
  );
}
