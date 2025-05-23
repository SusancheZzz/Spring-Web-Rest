package com.rntgroup.impl.client.fallback;

import com.rntgroup.api.dto.EmployeeShortInfoDto;
import com.rntgroup.impl.client.EmployeeClient;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
// На случай падения сервиса, в результате обращения к его методам будут возвращаться дефолты ниже:
public class EmployeeClientFallbackFactory implements FallbackFactory<EmployeeClient> {

  @Override
  public EmployeeClient create(Throwable cause) {
    return new EmployeeClient() {
      @Override
      public ResponseEntity<Integer> getCommonPaymentForDepartment(Long departmentId) {
        return new ResponseEntity<>(0, HttpStatus.NOT_FOUND);
      }

      @Override
      public ResponseEntity<Map<Long, Integer>> getAllCommonPaymentForDepartments(
        List<Long> departmentsPaymentIds) {
        return new ResponseEntity<>(Collections.emptyMap(), HttpStatus.NOT_FOUND);
      }

      @Override
      public ResponseEntity<Boolean> isExistsEmployeeById(Long employeeId) {
        return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
      }

      @Override
      public ResponseEntity<Integer> countEmployeesInDepartment(Long departmentId) {
        return new ResponseEntity<>(0, HttpStatus.NOT_FOUND);
      }

      @Override
      public ResponseEntity<EmployeeShortInfoDto> getLeaderInDepartment(Long departmentId) {
        return new ResponseEntity<>(getShortDto(), HttpStatus.NOT_FOUND);
      }

      @Override
      public ResponseEntity<List<EmployeeShortInfoDto>> getAllEmployeesByDepartmentId(
        Long departmentId) {
        return new ResponseEntity<>(Collections.emptyList(), HttpStatus.NOT_FOUND);
      }

      @Override
      public ResponseEntity<Void> updateIsLeaderById(Boolean isLeader, Long employeeId) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

      @Override
      public ResponseEntity<Long> getCountDepartmentsOfLeaderById(Long leaderId) {
        return new ResponseEntity<>(0L, HttpStatus.NOT_FOUND);
      }

      private EmployeeShortInfoDto getShortDto() {
        return new EmployeeShortInfoDto(
          0L,
          "Unknown",
          "Unknown"
        );
      }
    };
  }
}
