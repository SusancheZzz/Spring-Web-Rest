package com.rntgroup.impl.unit.controller;

import com.rntgroup.api.service.EmployeeService;
import com.rntgroup.impl.controller.CommutatorWithDepartmentServiceRestController;
import com.rntgroup.impl.unit.mock.service.MockEmployeeServiceCreator;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class CommutatorWithDepartmentServiceRestControllerImplTest {

  private final MockEmployeeServiceCreator mockEmployeeServiceCreator
    = new MockEmployeeServiceCreator();
  private final EmployeeService employeeService
    = mockEmployeeServiceCreator.getMockEmployeeService();
  private final CommutatorWithDepartmentServiceRestController controller
    = new CommutatorWithDepartmentServiceRestController(employeeService);
  private final long DEPARTMENT_ID = 1;

  @Test
  void getAllEmployees() {
    var employees = controller.getAllEmployeesByDepartmentId(DEPARTMENT_ID);
    var employeesFromService = employeeService.getEmployeesInDepartment(DEPARTMENT_ID);

    assertThat(employees.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(employees.getBody()).isEqualTo(employeesFromService);
  }

  @Test
  void countEmployeesInDepartment() {
    var countEmployees = controller.countEmployeesInDepartment(DEPARTMENT_ID);
    var countEmployeesFromService = employeeService.findEmployeesCountInDepartment(DEPARTMENT_ID);

    assertThat(countEmployees.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(countEmployees.getBody()).isEqualTo(countEmployeesFromService);
  }

  @Test
  void getCommonPaymentForDepartment() {
    var commonPaymentForDepartment = controller.getCommonPaymentForDepartment(DEPARTMENT_ID);
    var commonPaymentForDepartmentFromService = employeeService.getCommonPaymentForDepartment(DEPARTMENT_ID);

    assertThat(commonPaymentForDepartment.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(commonPaymentForDepartment.getBody()).isEqualTo(
      commonPaymentForDepartmentFromService);
  }

  @Test
  void getLeaderInDepartment() {
    var leaderInDepartment = controller.getLeaderInDepartment(DEPARTMENT_ID);
    var leaderFoundByService = employeeService.getLeaderShortInfo(DEPARTMENT_ID);

    assertThat(leaderInDepartment.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(leaderInDepartment.getBody()).isEqualTo(leaderFoundByService);
  }

  @Test
  void isExistsEmployeeById() {
    var isExistsEmployeeByController = controller.isExistsEmployeeById(DEPARTMENT_ID);
    var isExistsEmployeeByService = employeeService.isExistsEmployeeById(DEPARTMENT_ID);

    assertThat(isExistsEmployeeByController.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(isExistsEmployeeByController.getBody()).isEqualTo(isExistsEmployeeByService);
  }

  @Test
  void getCountDepartmentsOfLeaderById() {
    var countDepartmentsOfLeaderByController = controller.getCountDepartmentsOfLeaderById(DEPARTMENT_ID);
    var countDepartmentsOfLeaderByService = employeeService.getCountDepartmentsOfLeaderById(DEPARTMENT_ID);

    assertThat(countDepartmentsOfLeaderByController.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(countDepartmentsOfLeaderByController.getBody()).isEqualTo(countDepartmentsOfLeaderByService);
  }

}