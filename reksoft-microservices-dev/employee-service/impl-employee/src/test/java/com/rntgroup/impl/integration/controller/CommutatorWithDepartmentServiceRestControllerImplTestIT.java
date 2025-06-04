package com.rntgroup.impl.integration.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rntgroup.api.service.EmployeeService;
import com.rntgroup.impl.integration.TestBase;
import com.rntgroup.impl.integration.annotation.IntegrationTest;
import com.rntgroup.impl.integration.controller.adapter.LocalDateTypeAdapter;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@RequiredArgsConstructor
@AutoConfigureMockMvc
class CommutatorWithDepartmentServiceRestControllerImplTestIT extends TestBase {

  private static final long DEPARTMENT_ID = 1;
  private final EmployeeService employeeService;
  private final MockMvc mockMvc;
  private final Gson gson = new GsonBuilder()
    .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
    .create();

  @Test
  void getAllEmployees() throws Exception {
    var employees = gson.toJson(employeeService.getEmployeesInDepartment(DEPARTMENT_ID));
    mockMvc.perform(get("/api/v1/employee/communication/1"))
      .andExpect(status().isOk())
      .andExpect(content().json(employees));
  }

  @Test
  void getCommonPaymentForDepartment() throws Exception {
    var commonPayment = gson.toJson(employeeService.getCommonPaymentForDepartment(DEPARTMENT_ID));
    mockMvc.perform(get("/api/v1/employee/communication/payment/1"))
      .andExpect(status().isOk())
      .andExpect(content().json(commonPayment));
  }

  @Test
  void getLeaderInDepartment() throws Exception {
    var leader = gson.toJson(employeeService.getLeaderShortInfo(DEPARTMENT_ID));
    mockMvc.perform(get("/api/v1/employee/communication/leader/1"))
      .andExpect(status().isOk())
      .andExpect(content().json(leader));
  }


}