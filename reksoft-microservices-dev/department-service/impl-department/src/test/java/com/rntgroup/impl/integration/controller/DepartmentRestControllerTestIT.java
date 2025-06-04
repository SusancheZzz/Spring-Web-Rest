package com.rntgroup.impl.integration.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rntgroup.api.dto.DepartmentEditDto;
import com.rntgroup.api.dto.DepartmentSaveDto;
import com.rntgroup.api.service.DepartmentService;
import com.rntgroup.impl.integration.TestBase;
import com.rntgroup.impl.integration.annotation.IntegrationTest;
import com.rntgroup.impl.integration.controller.adapter.LocalDateTypeAdapter;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@RequiredArgsConstructor
@AutoConfigureMockMvc
class DepartmentRestControllerTestIT extends TestBase {

  private static final String DEPARTMENT = "/api/v1/department";
  private static final String DEPARTMENTS_ID = "/api/v1/department/{id}";
  private final MockMvc mockMvc;
  private final DepartmentService departmentService;

  @Test
  void findDepartmentById() throws Exception {
    mockMvc.perform(get(DEPARTMENTS_ID, 1L))
      .andExpect(content().contentType(APPLICATION_JSON))
      .andExpect(status().isOk());
  }

  @Test
  void findUnknownDepartmentId() throws Exception {
    mockMvc.perform(get(DEPARTMENTS_ID, 100L))
      .andExpect(status().isNotFound());
  }

  @Test
  void findDepartmentByName() throws Exception {
    mockMvc.perform(get("/api/v1/department/name/{name}", "Main department"))
      .andExpect(content().contentType(APPLICATION_JSON))
      .andExpect(status().isOk());
  }

  @Test
  void findUnknownDepartmentByName() throws Exception {
    mockMvc.perform(get("/api/v1/department/name/{name}", "Unknown department"))
      .andExpect(status().isNotFound());
  }

  @Test
  void createDepartment() throws Exception {
    var saveDto = gson.toJson(getDepartmentSaveDto());
    mockMvc.perform(post(DEPARTMENT)
        .contentType(APPLICATION_JSON)
        .content(saveDto))
      .andExpect(status().isCreated());
  }

  @Test
  void saveExistentDepartment() throws Exception {
    var saveDto = gson.toJson(getExistentDepartment());
    mockMvc.perform(post(DEPARTMENT)
        .contentType(APPLICATION_JSON)
        .content(saveDto))
      .andExpect(status().isBadRequest());
  }

  @Test
  void saveDepartmentWithUnknownParentDepartment() throws Exception {
    var saveDto = gson.toJson(getDepartmentSaveDtoWithUnknownParent());
    mockMvc.perform(post(DEPARTMENT)
        .content(saveDto)
        .contentType(APPLICATION_JSON))
      .andExpect(status().isCreated());
  }

  @Test
  void deleteNotEmptyDepartment() throws Exception {
    mockMvc.perform(delete(DEPARTMENTS_ID, 1L))
      .andExpect(status().isBadRequest());
  }

  @Test
  void deleteEmptyDepartment() throws Exception {
    mockMvc.perform(delete(DEPARTMENTS_ID, 7L))
      .andExpect(status().isOk());
  }

  @Test
  void deleteUnRegisteredDepartment() throws Exception {
    mockMvc.perform(delete(DEPARTMENTS_ID, 100L))
      .andExpect(status().isNotFound());
  }

  @Test
  void updateDepartment() throws Exception {
    var editDto = gson.toJson(getDepartmentEditDto());
    mockMvc.perform(put(DEPARTMENTS_ID, 1L)
        .contentType(APPLICATION_JSON)
        .content(editDto))
      .andExpect(status().isOk())
      .andExpect(content().contentType(APPLICATION_JSON));
  }

  @Test
  void updateUnknownDepartment() throws Exception {
    var editDto = gson.toJson(getDepartmentEditDto());
    mockMvc.perform(put(DEPARTMENTS_ID, 100L)
        .contentType(APPLICATION_JSON)
        .content(editDto))
      .andExpect(status().isNotFound());
  }

  @Test
  void getPayment() throws Exception {
    var payment = gson.toJson(departmentService.getPaymentForDepartment(1L));
    mockMvc.perform(get("/api/v1/department/1/payment"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(APPLICATION_JSON))
      .andExpect(content().json(payment));
  }

  @Test
  void getEmployees() throws Exception {
    var employees = gson.toJson(departmentService.findAllEmployeesInDepartment(1L));
    mockMvc.perform(get("/api/v1/department/1/employees"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(APPLICATION_JSON))
      .andExpect(content().json(employees));
  }

  private final Gson gson = new GsonBuilder()
    .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
    .create();

  private DepartmentSaveDto getDepartmentSaveDto() {
    return new DepartmentSaveDto("Test", 1L, 1L);
  }

  private DepartmentSaveDto getExistentDepartment() {
    return new DepartmentSaveDto("Accounting",  1L, 1L);
  }

  private DepartmentSaveDto getDepartmentSaveDtoWithUnknownParent() {
    return new DepartmentSaveDto("Test_", null, 100L);
  }

  private DepartmentEditDto getDepartmentEditDto() {
    return new DepartmentEditDto("_test");
  }
}