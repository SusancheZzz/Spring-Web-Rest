package com.rntgroup.impl.integration.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rntgroup.api.dto.EmployeeEditDto;
import com.rntgroup.api.dto.EmployeeSaveDto;
import com.rntgroup.api.service.EmployeeService;
import com.rntgroup.impl.integration.TestBase;
import com.rntgroup.impl.integration.annotation.IntegrationTest;
import com.rntgroup.impl.integration.controller.adapter.LocalDateTypeAdapter;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@RequiredArgsConstructor
@AutoConfigureMockMvc
class EmployeeRestControllerImplTestIT extends TestBase {

  private static final String EMPLOYEE_ID = "/api/v1/employee/{id}";
  private static final String EMPLOYEE = "/api/v1/employee";
  private final EmployeeService mockEmployeeService;
  private final MockMvc mockMvc;
  private final Gson gson = new GsonBuilder()
    .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
    .create();

  @Test
  void getEmployee() throws Exception {
    var employee = gson.toJson(mockEmployeeService.getEmployeeInfo(1L));

    mockMvc.perform(get(EMPLOYEE_ID, 1))
      .andExpect(status().isOk())
      .andExpect(content().json(employee));
  }

  @Test
  void getUnregisteredEmployee() throws Exception {
    mockMvc.perform(get(EMPLOYEE_ID, 100))
      .andExpect(status().isNotFound());
  }

  @Test
  void saveEmployee() throws Exception {
    var employeeDto = gson.toJson(getEmployeeSaveDto());

    mockMvc.perform(post(EMPLOYEE)
        .contentType(MediaType.APPLICATION_JSON)
        .content(employeeDto))
      .andExpect(status().isCreated());
  }

  @Test
  void saveNullableEmployee() throws Exception {
    var employeeDto = gson.toJson(getEmployeeWithNull());
    mockMvc.perform(post(EMPLOYEE)
        .contentType(MediaType.APPLICATION_JSON)
        .content(employeeDto))
      .andExpect(status().isBadRequest());
  }

  @Test
  void saveEmployeeFromUnregisteredDepartment() throws Exception {
    var employeeDto = gson.toJson(getEmployeeFromUnregisteredDepartment());
    mockMvc.perform(post(EMPLOYEE)
        .contentType(MediaType.APPLICATION_JSON)
        .content(employeeDto))
      .andExpect(status().isNotFound());
  }

  @Test
  void deleteEmployee() throws Exception {
    mockMvc.perform(delete(EMPLOYEE_ID, 3))
      .andExpect(status().isOk());
    mockMvc.perform(get(EMPLOYEE_ID, 3))
      .andExpect(status().isNotFound());
  }

  @Test
  void deleteUnregisteredEmployee() throws Exception {
    mockMvc.perform(delete(EMPLOYEE_ID, 100))
      .andExpect(status().isNotFound());
  }

  @Test
  void updateEmployee() throws Exception {
    var editDto = gson.toJson(getEmployeeEditDto(1L));
    mockMvc.perform(put(EMPLOYEE_ID, 4)
        .contentType(MediaType.APPLICATION_JSON)
        .content(editDto))
      .andExpect(status().isOk());
  }

  @Test
  void updateUnregisteredEmployee() throws Exception {
    var editDto = gson.toJson(getEmployeeEditDto(1L));
    mockMvc.perform(put(EMPLOYEE_ID, 100)
        .contentType(MediaType.APPLICATION_JSON)
        .content(editDto))
      .andExpect(status().isNotFound());
  }

  @Test
  void updateEmployeeFromUnregisteredDepartment() throws Exception {
    var editDto = gson.toJson(getEmployeeEditDto(100L));
    mockMvc.perform(put(EMPLOYEE_ID, 1)
        .contentType(MediaType.APPLICATION_JSON)
        .content(editDto))
      .andExpect(status().isNotFound());
  }

  private EmployeeSaveDto getEmployeeSaveDto() {
    return new EmployeeSaveDto(
      "Test",
      "Test",
      "Test",
      "MALE",
      LocalDate.of(1980, 11, 10),
      "+7 (900) 000-00-00",
      1L,
      "Test",
      20000
    );
  }

  private EmployeeEditDto getEmployeeEditDto(Long departmentId) {
    return new EmployeeEditDto(
      "Test",
      "Test",
      "+7 (900) 374-82-47",
      departmentId,
      "Test",
      20000,
      true
    );
  }

  private EmployeeSaveDto getEmployeeWithNull() {
    return new EmployeeSaveDto(
      null,
      null,
      null,
      null,
      LocalDate.of(1980, 11, 10),
      null,
      1L,
      null,
      null
    );
  }

  private EmployeeSaveDto getEmployeeFromUnregisteredDepartment() {
    return new EmployeeSaveDto(
      "Test",
      "Test",
      "Test",
      "MALE",
      LocalDate.of(1999, 11, 10),
      "+7 (900) 001-00-00",
      100L,
      "Test",
      20000
    );
  }
}