package com.rntgroup.api.controller;

import com.rntgroup.api.dto.CommonPaymentForDepartment;
import com.rntgroup.api.dto.DepartmentEditDto;
import com.rntgroup.api.dto.DepartmentReadDto;
import com.rntgroup.api.dto.DepartmentSaveDto;
import com.rntgroup.api.dto.EmployeeShortInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface DepartmentApi {

  @Operation(
    summary = "Getting department information by ID",
    description = "Getting department information containing name, "
      + "creation date, leader name and employees number of department"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Department exists and successfully found"),
    @ApiResponse(responseCode = "404", description = "Department not found")
  })
  @GetMapping("/{id}")
  ResponseEntity<DepartmentReadDto> findDepartmentById(
    @PathVariable("id") @Positive @Parameter(description = "Department ID") Long id
  );

  @Operation(
    summary = "Getting department information by name",
    description = "Getting department information containing name, "
      + "creation date, leader name and employees number of department"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Department exists and successfully found"),
    @ApiResponse(responseCode = "404", description = "Department not found")
  })
  @GetMapping("/name/{name}")
  ResponseEntity<DepartmentReadDto> findDepartmentByName(
    @PathVariable("name") @NotBlank @Parameter(description = "Department name") String name
  );

  @Operation(
    summary = "New department registration",
    description = "New department registration and getting read DTO after that"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "Department successfully registered"),
    @ApiResponse(responseCode = "400", description = "There are some problems in DTO's fields")
  })
  @PostMapping
  ResponseEntity<DepartmentReadDto> createDepartment(
    @RequestBody
    @Valid
    @Parameter(description = "DTO for creation new department") DepartmentSaveDto department
  );

  @Operation(
    summary = "Department deleting",
    description = "Delete existing department that has no employees by ID"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Department delete successfully"),
    @ApiResponse(responseCode = "404", description = "Department with declared id not found"),
    @ApiResponse(responseCode = "400", description = "Department still has employees")
  })
  @DeleteMapping("/{id}")
  ResponseEntity<DepartmentReadDto> deleteDepartment(
    @PathVariable("id") @Positive @Parameter(description = "Department id") Long id
  );

  @Operation(
    summary = "Update department with declared ID",
    description = "Update department with id by edit dto"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Department update successfully"),
    @ApiResponse(responseCode = "404", description = "Department not found by declared ID"),
    @ApiResponse(responseCode = "400", description = "Specified name is not unique")
  })
  @PutMapping("/{id}")
  ResponseEntity<DepartmentReadDto> updateDepartment(
    @PathVariable("id") @Positive @Parameter(description = "ID of updated department") Long id,
    @RequestBody @Valid @Parameter(description = "DTO for update") DepartmentEditDto department
  );

  @Operation(
    summary = "Getting payment info for department",
    description = "Get common payment for department by department ID"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Payment indo for department"),
    @ApiResponse(responseCode = "404", description = "Department not found")
  })
  @GetMapping("{id}/payment")
  ResponseEntity<CommonPaymentForDepartment> getPayment(
    @PathVariable("id") @Positive @Parameter(description = "Department ID") Long id
  );

  @Operation(
    summary = "Employees info from department",
    description = "Get all employees in department found by ID"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Employees in department"),
    @ApiResponse(responseCode = "404", description = "Department not found")
  })
  @GetMapping("{id}/employees")
  ResponseEntity<List<EmployeeShortInfoDto>> getEmployees(
    @PathVariable("id") @Positive @Parameter(description = "Department ID") Long id
  );
}