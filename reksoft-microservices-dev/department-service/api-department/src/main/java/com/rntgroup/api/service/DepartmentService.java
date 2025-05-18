package com.rntgroup.api.service;

import com.rntgroup.api.dto.CommonPaymentForDepartment;
import com.rntgroup.api.dto.DepartmentEditDto;
import com.rntgroup.api.dto.DepartmentMessageDto;
import com.rntgroup.api.dto.DepartmentReadDto;
import com.rntgroup.api.dto.DepartmentSaveDto;
import com.rntgroup.api.dto.EmployeeShortInfoDto;
import java.util.List;

public interface DepartmentService {

  DepartmentReadDto findDepartmentById(Long departmentId);

  DepartmentReadDto findDepartmentByName(String name);

  CommonPaymentForDepartment getPaymentForDepartment(Long departmentId);

  DepartmentReadDto saveDepartment(DepartmentSaveDto departmentSaveDto);

  DepartmentReadDto deleteDepartment(Long departmentId);

  DepartmentReadDto updateDepartment(DepartmentEditDto departmentEditDto, Long departmentId);

  List<EmployeeShortInfoDto> findAllEmployeesInDepartment(Long departmentId);

  DepartmentMessageDto findDepartmentShortInfo(Long departmentId);

  void updatePaymentInfo();
}