package com.rntgroup.impl.integration.configuration;

import com.rntgroup.api.dto.DepartmentMessageDto;
import com.rntgroup.api.service.EmployeeService;
import com.rntgroup.impl.client.DepartmentClient;
import com.rntgroup.impl.mapper.DepartmentMapper;
import com.rntgroup.impl.mapper.EmployeeMapper;
import com.rntgroup.impl.repository.DepartmentSnapshotRepository;
import com.rntgroup.impl.repository.EmployeeRepository;
import com.rntgroup.impl.service.impl.EmployeeServiceImpl;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
@Profile("test")
public class ServiceEmployeeImplConfiguration {

  @Bean
  public DepartmentClient mockDepartmentClient() {
    var client = mock(DepartmentClient.class);

    when(client.findDepartmentById(1L))
      .thenReturn(new ResponseEntity<>(new DepartmentMessageDto(1L, "test1"), HttpStatus.OK));

    when(client.findDepartmentById(2L))
      .thenReturn(new ResponseEntity<>(new DepartmentMessageDto(2L, "test2"), HttpStatus.OK));

    when(client.findDepartmentById(100L))
      .thenReturn(
        new ResponseEntity<>(new DepartmentMessageDto(100L, "test3"), HttpStatus.NOT_FOUND));

    return client;
  }

  @Bean
  @Primary
  public EmployeeService mockEmployeeService(
    EmployeeRepository employeeRepository,
    DepartmentSnapshotRepository departmentSnapshotRepository) {
    return new EmployeeServiceImpl(
      employeeRepository,
      mockDepartmentClient(),
      EmployeeMapper.INSTANCE,
      DepartmentMapper.INSTANCE,
      departmentSnapshotRepository
    );
  }
}
