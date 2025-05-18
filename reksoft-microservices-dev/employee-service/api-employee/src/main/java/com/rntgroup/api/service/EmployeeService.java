package com.rntgroup.api.service;

import com.rntgroup.api.dto.EmployeeEditDto;
import com.rntgroup.api.dto.EmployeeReadDto;
import com.rntgroup.api.dto.EmployeeSaveDto;
import com.rntgroup.api.dto.EmployeeShortInfoDto;
import java.util.List;

public interface EmployeeService {

  EmployeeReadDto getEmployeeInfo(Long employeeId);

  EmployeeReadDto hireEmployee(EmployeeSaveDto employeeSaveDto);

  EmployeeReadDto deleteEmployee(Long employeeId);

  EmployeeReadDto updateEmployee(EmployeeEditDto employeeEditDto, Long employeeId);

  Integer findEmployeesCountInDepartment(Long departmentId);

  Integer getCommonPaymentForDepartment(Long departmentId);

  EmployeeShortInfoDto getLeaderShortInfo(Long departmentId);

  List<EmployeeShortInfoDto> getEmployeesInDepartment(Long departmentId);

  boolean isExistsEmployeeById(Long employeeId);

  void updateIsLeaderById(boolean isLeader, Long employeeId);

  Long getCountDepartmentsOfLeaderById(Long leaderId);
}
