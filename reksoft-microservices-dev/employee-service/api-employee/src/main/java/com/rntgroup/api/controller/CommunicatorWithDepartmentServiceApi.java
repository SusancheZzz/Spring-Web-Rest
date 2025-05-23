package com.rntgroup.api.controller;

import com.rntgroup.api.dto.EmployeeShortInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface CommunicatorWithDepartmentServiceApi {

  @Operation(
    summary = "Get list of all employees in department",
    description = "Get list of all employees in department"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "List is got"),
    @ApiResponse(responseCode = "404", description = "Department is not found")
  })
  @GetMapping("/api/v1/employee/communication/{departmentId}")
  ResponseEntity<List<EmployeeShortInfoDto>> getAllEmployeesByDepartmentId(
    @PathVariable("departmentId")
    @Positive(message = "Department id must be positive")
    @Parameter(description = "Department ID") Long departmentId
  );

  @Operation(
    summary = "Get employees number in department",
    description = "Get the number of workers in the department given by ID"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Number of workers received"),
    @ApiResponse(responseCode = "404", description = "Department is not found")
  })
  @GetMapping("/api/v1/employee/communication/{departmentId}/count")
  ResponseEntity<Integer> countEmployeesInDepartment(
    @PathVariable("departmentId")
    @Positive(message = "Department id must be positive")
    @Parameter(description = "Department ID") Long departmentId
  );

  @Operation(
    summary = "Obtaining information about the payroll fund in the department",
    description = "Obtaining information about the salary fund in the department obtained by ID"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Payment info is getting"),
    @ApiResponse(responseCode = "404", description = "Department not found")
  })
  @GetMapping("/api/v1/employee/communication/payment/{departmentId}")
  ResponseEntity<Integer> getCommonPaymentForDepartment(
    @PathVariable("departmentId")
    @Positive(message = "Department id must be positive")
    @Parameter(description = "Department ID") Long departmentId
  );

  @PostMapping("/api/v1/employee/communication/allpayments")
  ResponseEntity<Map<Long, Integer>> getAllCommonPaymentForDepartments(
    @NotEmpty List<Long> departmentPaymentIds
  );

  @Operation(
    summary = "Get a department leader",
    description = "Information about the leader in the specified department"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Leader is getting"),
    @ApiResponse(responseCode = "404", description = "Department not found")
  })
  @GetMapping("/api/v1/employee/communication/leader/{departmentId}")
  ResponseEntity<EmployeeShortInfoDto> getLeaderInDepartment(
    @PathVariable("departmentId")
    @Positive(message = "Department id must be positive")
    @Parameter(description = "Department ID") Long id
  );

  @Operation(
    summary = "Check existence employee"
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "true if employee exists / false if not")
  })
  @GetMapping("/api/v1/employee/communication/existence/{employeeId}")
  ResponseEntity<Boolean> isExistsEmployeeById(
    @PathVariable("employeeId")
    @Positive(message = "Employee id must be positive")
    @Parameter(description = "Employee ID") Long employeeId);

  @Operation(
    summary = "Update marker isLeader of employee"
  )
  @PutMapping("/api/v1/employee/communication/{employeeId}/isLeader")
  ResponseEntity<Void> updateIsLeaderById(
    @Parameter(description = "Set leader marker")
    @RequestBody Boolean isLeader,
    @PathVariable("employeeId")
    @Positive(message = "Employee id must be positive")
    @Parameter(description = "Employee ID") Long employeeId
  );

  @Operation(
    summary = "Get count departments of leader"
  )
  @GetMapping("/api/v1/employee/communication/countDepartments/{leaderId}")
  ResponseEntity<Long> getCountDepartmentsOfLeaderById(
    @PathVariable("leaderId")
    @Positive(message = "Leader id must be positive")
    @Parameter(description = "Leader ID") Long leaderId
  );

}
