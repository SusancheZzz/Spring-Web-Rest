package com.rntgroup.impl.client;

import com.rntgroup.api.dto.EmployeeShortInfoDto;
import com.rntgroup.impl.client.fallback.EmployeeClientFallbackFactory;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
  value = "employee-service",
  fallbackFactory = EmployeeClientFallbackFactory.class,
  dismiss404 = true
)
public interface EmployeeClient {

  @GetMapping("/api/v1/employee/payment/{departmentId}")
  ResponseEntity<Integer> getCommonPaymentForDepartment(
    @PathVariable("departmentId") Long departmentId);

  @GetMapping("/api/v1/employee/{departmentId}/count")
  ResponseEntity<Integer> countEmployeesInDepartment(
    @PathVariable("departmentId") Long departmentId);

  @GetMapping("/api/v1/employee/leader/{departmentId}")
  ResponseEntity<EmployeeShortInfoDto> getLeaderInDepartment(
    @PathVariable("departmentId") Long departmentId);

  @GetMapping("/api/v1/employee/department/{departmentId}")
  ResponseEntity<List<EmployeeShortInfoDto>> getAllEmployeesByDepartmentId(
    @PathVariable("departmentId") Long departmentId);

  @GetMapping("/api/v1/employee/existence/{employeeId}")
  ResponseEntity<Boolean> isExistsEmployeeById(
    @PathVariable("employeeId") Long employeeId);

  @PutMapping("/api/v1/employee/{employeeId}/isLeader")
  ResponseEntity<Void> updateIsLeaderById(
    @RequestBody Boolean isLeader,
    @PathVariable("employeeId") Long employeeId
  );

  @GetMapping("/api/v1/employee/countDepartments/{leaderId}")
  ResponseEntity<Long> getCountDepartmentsOfLeaderById(
    @PathVariable("leaderId") Long leaderId
  );

}
