package com.rntgroup.impl.integration.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@RequiredArgsConstructor
@AutoConfigureMockMvc
class CommutatorWithEmployeeServiceRestControllerImplTestIT extends TestBase {

  private static final String GET_MESSAGE_URL = "/api/v1/department/{id}";
  private final MockMvc mockMvc;
  private final DepartmentService departmentService;
  private final Gson gson = new GsonBuilder()
    .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
    .create();

  @Test
  void getDepartmentShortInfo() throws Exception {
    var message = gson.toJson(departmentService.findDepartmentShortInfo(1L));
    mockMvc.perform(get(GET_MESSAGE_URL, 1))
      .andExpect(content().contentType(APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(content().json(message));
  }

  @Test
  void getUnknownDepartment() throws Exception {
    mockMvc.perform(get(GET_MESSAGE_URL, 100))
      .andExpect(status().isNotFound());
  }
}