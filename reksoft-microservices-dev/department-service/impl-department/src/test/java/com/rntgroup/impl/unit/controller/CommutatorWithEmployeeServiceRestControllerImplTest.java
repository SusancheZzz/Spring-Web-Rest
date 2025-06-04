package com.rntgroup.impl.unit.controller;

import com.rntgroup.api.controller.DepartmentApi;
import com.rntgroup.api.service.DepartmentService;
import com.rntgroup.impl.controller.DepartmentRestController;
import com.rntgroup.impl.exception.DepartmentWithNotFoundException;
import com.rntgroup.impl.unit.mock.service.MockDepartmentServiceCreator;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CommutatorWithEmployeeServiceRestControllerImplTest {

  private final MockDepartmentServiceCreator mockDepartmentServiceCreator
    = new MockDepartmentServiceCreator();
  private final DepartmentService departmentService
    = mockDepartmentServiceCreator.getDepartmentService();
  private final DepartmentApi departmentRestController
    = new DepartmentRestController(departmentService);

  @Test
  void getDepartmentShortInfo() {
    var departmentShortInfo = departmentRestController.findDepartmentById(1L);
    var departmentFoundByService = departmentService.findDepartmentById(1L);

    assertThat(departmentShortInfo.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(departmentShortInfo.getBody()).isEqualTo(departmentFoundByService);
  }

  @Test
  void findUnknownDepartment() {
    assertThatThrownBy(() -> departmentRestController.findDepartmentById(100L))
      .isInstanceOf(DepartmentWithNotFoundException.class);
  }
}