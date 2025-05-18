package com.rntgroup.impl.controller;

import com.rntgroup.api.controller.EmployeeRestController;
import com.rntgroup.api.dto.EmployeeEditDto;
import com.rntgroup.api.dto.EmployeeReadDto;
import com.rntgroup.api.dto.EmployeeSaveDto;
import com.rntgroup.api.dto.EmployeeShortInfoDto;
import com.rntgroup.api.service.EmployeeService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Employee API")
@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
@Validated
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

  //-------Небезопасный костыль---------

  @Hidden
  @GetMapping("payment/{departmentId}")
  public ResponseEntity<Integer> getCommonPaymentForDepartment(
    @PathVariable("departmentId") Long departmentId) {
    return new ResponseEntity<>(
      employeeService.getCommonPaymentForDepartment(departmentId),
      HttpStatus.OK
    );
  }

  @Hidden
  @GetMapping("{departmentId}/count")
  public ResponseEntity<Integer> countEmployeesInDepartment(
    @PathVariable("departmentId") Long departmentId) {
    return new ResponseEntity<>(
      employeeService.findEmployeesCountInDepartment(departmentId),
      HttpStatus.OK
    );
  }

  @Hidden
  @GetMapping("leader/{departmentId}")
  public ResponseEntity<EmployeeShortInfoDto> getLeaderInDepartment(
    @PathVariable("departmentId") Long departmentId) {
    return new ResponseEntity<>(
      employeeService.getLeaderShortInfo(departmentId),
      HttpStatus.OK
    );
  }

  @Hidden
  @GetMapping("department/{departmentId}")
  public ResponseEntity<List<EmployeeShortInfoDto>> getAllEmployeesByDepartmentId(
    @PathVariable("departmentId") Long departmentId) {
    return new ResponseEntity<>(
      employeeService.getEmployeesInDepartment(departmentId),
      HttpStatus.OK
    );
  }

  @Hidden
  @GetMapping("existence/{employeeId}")
  public ResponseEntity<Boolean> isExistsEmployeeById(
    @PathVariable("employeeId") Long employeeId) {
    return new ResponseEntity<>(
      employeeService.isExistsEmployeeById(employeeId),
      HttpStatus.OK
    );
  }

  @Hidden
  @GetMapping("countDepartments/{leaderId}")
  public ResponseEntity<Long> getCountDepartmentsOfLeaderById(
    @PathVariable("leaderId") Long leaderId) {
    return new ResponseEntity<>(
      employeeService.getCountDepartmentsOfLeaderById(leaderId),
      HttpStatus.OK
    );
  }

  @Hidden
  @PutMapping("{employeeId}/isLeader")
  public ResponseEntity<Void> updateIsLeaderById(
    @RequestBody Boolean isLeader,
    @PathVariable("employeeId") Long employeeId) {
    employeeService.updateIsLeaderById(isLeader, employeeId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
