package com.rntgroup.impl.controller;

import com.rntgroup.api.controller.CommunicatorWithDepartmentServiceApi;
import com.rntgroup.api.dto.EmployeeShortInfoDto;
import com.rntgroup.api.service.EmployeeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Commutator With Department Service")
@RestController
@RequiredArgsConstructor
@Validated
public class CommutatorWithDepartmentServiceRestController implements
  CommunicatorWithDepartmentServiceApi {

  private final EmployeeService employeeService;

  @Override
  public ResponseEntity<Integer> getCommonPaymentForDepartment(
    @PathVariable("departmentId") Long departmentId) {
    return new ResponseEntity<>(
      employeeService.getCommonPaymentForDepartment(departmentId),
      HttpStatus.OK
    );
  }

  @Override
  public ResponseEntity<Map<Long, Integer>> getAllCommonPaymentForDepartments(
    @RequestBody List<Long> departmentsPaymentIds) {
    return new ResponseEntity<>(
      employeeService.getAllCommonPaymentForDepartments(departmentsPaymentIds),
      HttpStatus.OK
    );
  }

  @Override
  public ResponseEntity<Integer> countEmployeesInDepartment(
    @PathVariable("departmentId") Long departmentId) {
    return new ResponseEntity<>(
      employeeService.findEmployeesCountInDepartment(departmentId),
      HttpStatus.OK
    );
  }

  @Override
  public ResponseEntity<EmployeeShortInfoDto> getLeaderInDepartment(
    @PathVariable("departmentId") Long departmentId) {
    return new ResponseEntity<>(
      employeeService.getLeaderShortInfo(departmentId),
      HttpStatus.OK
    );
  }

  @Override
  public ResponseEntity<List<EmployeeShortInfoDto>> getAllEmployeesByDepartmentId(
    @PathVariable("departmentId") Long departmentId) {
    return new ResponseEntity<>(
      employeeService.getEmployeesInDepartment(departmentId),
      HttpStatus.OK
    );
  }

  @Override
  public ResponseEntity<Boolean> isExistsEmployeeById(
    @PathVariable("employeeId") Long employeeId) {
    return new ResponseEntity<>(
      employeeService.isExistsEmployeeById(employeeId),
      HttpStatus.OK
    );
  }

  @Override
  public ResponseEntity<Long> getCountDepartmentsOfLeaderById(
    @PathVariable("leaderId") Long leaderId) {
    return new ResponseEntity<>(
      employeeService.getCountDepartmentsOfLeaderById(leaderId),
      HttpStatus.OK
    );
  }

  @Override
  public ResponseEntity<Void> updateIsLeaderById(
    @RequestBody Boolean isLeader,
    @PathVariable("employeeId") Long employeeId) {
    employeeService.updateIsLeaderById(isLeader, employeeId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
