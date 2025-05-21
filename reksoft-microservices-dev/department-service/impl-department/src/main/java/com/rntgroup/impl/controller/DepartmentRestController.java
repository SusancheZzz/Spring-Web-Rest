package com.rntgroup.impl.controller;

import com.rntgroup.api.controller.DepartmentApi;
import com.rntgroup.api.dto.CommonPaymentForDepartment;
import com.rntgroup.api.dto.DepartmentEditDto;
import com.rntgroup.api.dto.DepartmentReadDto;
import com.rntgroup.api.dto.DepartmentSaveDto;
import com.rntgroup.api.dto.EmployeeShortInfoDto;
import com.rntgroup.api.service.DepartmentService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Department API")
@RestController
@RequestMapping("api/v1/department")
@RequiredArgsConstructor
@Validated
public class DepartmentRestController implements DepartmentApi {

  private final DepartmentService departmentService;

  @Override
  public ResponseEntity<DepartmentReadDto> findDepartmentById(
    @PathVariable("id") @Positive Long id
  ) {
    var department = departmentService.findDepartmentById(id);
    return new ResponseEntity<>(department, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<DepartmentReadDto> findDepartmentByName(
    @PathVariable("name") @NotBlank String name
  ) {
    var department = departmentService.findDepartmentByName(name);
    return new ResponseEntity<>(department, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<DepartmentReadDto> createDepartment(
    @RequestBody @Valid DepartmentSaveDto department
  ) {
    var savedDepartment = departmentService.saveDepartment(department);
    return new ResponseEntity<>(savedDepartment, HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<DepartmentReadDto> deleteDepartment(
    @PathVariable("id") @Positive Long id
  ) {
    var deletedDepartment = departmentService.deleteDepartment(id);
    return new ResponseEntity<>(deletedDepartment, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<DepartmentReadDto> updateDepartment(
    @PathVariable("id") @Positive @Parameter(description = "ID of updated department") Long id,
    @RequestBody @Valid @Parameter(description = "DTO for update") DepartmentEditDto department
  ) {
    var updatedDepartment = departmentService.updateDepartment(department, id);
    return new ResponseEntity<>(updatedDepartment, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<CommonPaymentForDepartment> getPayment(
    @PathVariable("id") @Positive @Parameter(description = "Department ID") Long id
  ) {
    var paymentDto = departmentService.getPaymentForDepartment(id);
    return new ResponseEntity<>(paymentDto, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<List<EmployeeShortInfoDto>> getEmployees(
    @PathVariable("id") @Positive @Parameter(description = "Department ID") Long id
  ) {
    var employees = departmentService.findAllEmployeesInDepartment(id);
    return new ResponseEntity<>(employees, HttpStatus.OK);
  }
}
