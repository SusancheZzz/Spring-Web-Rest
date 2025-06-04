package com.rntgroup.impl.integration.configuration;

import com.rntgroup.api.dto.EmployeeShortInfoDto;
import com.rntgroup.api.service.DepartmentService;
import com.rntgroup.impl.client.EmployeeClient;
import com.rntgroup.impl.mapper.DepartmentMapper;
import com.rntgroup.impl.repository.DepartmentPaymentRepository;
import com.rntgroup.impl.repository.DepartmentRepository;
import com.rntgroup.impl.service.impl.DepartmentServiceImpl;
import java.util.Collections;
import java.util.List;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Configuration
@Profile("test")
public class ServiceDepartmentImplConfiguration {

  @Bean
  public EmployeeClient mockEmployeeClient() {
    var client = mock(EmployeeClient.class);

    when(client.getAllEmployeesByDepartmentId(anyLong()))
      .thenAnswer(invocation -> {
        if ((Long) invocation.getArgument(0) != 1) {
          return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }

        return new ResponseEntity<>(List.of(new EmployeeShortInfoDto(1L, "test", "test")),
          HttpStatus.OK);
      });

    when(client.isExistsEmployeeById(anyLong()))
      .thenReturn(new ResponseEntity<>(true, HttpStatus.OK));

    when(client.getCommonPaymentForDepartment(anyLong()))
      .thenReturn(new ResponseEntity<>(0, HttpStatus.OK));

    when(client.countEmployeesInDepartment(anyLong()))
      .thenReturn(new ResponseEntity<>(1, HttpStatus.OK));

    when(client.getLeaderInDepartment(anyLong()))
      .thenReturn(
        new ResponseEntity<>(new EmployeeShortInfoDto(1L, "test", "test"), HttpStatus.OK));

    return client;
  }

  @Bean
  ApplicationEventPublisher eventPublisher() {
    var mockPublisher = Mockito.mock(ApplicationEventPublisher.class);
    Mockito.doNothing().when(mockPublisher).publishEvent(any(ApplicationEvent.class));

    return mockPublisher;
  }

  @Bean
  @Primary
  public DepartmentService mockDepartmentService(
    DepartmentRepository departmentRepository,
    DepartmentPaymentRepository departmentPaymentRepository
  ) {
    return new DepartmentServiceImpl(
      departmentRepository,
      departmentPaymentRepository,
      mockEmployeeClient(),
      DepartmentMapper.INSTANCE,
      eventPublisher()
    );
  }
}
