package com.rntgroup.impl.unit.mock.service;

import com.rntgroup.api.dto.DepartmentMessageDto;
import com.rntgroup.api.service.EmployeeService;
import com.rntgroup.impl.client.DepartmentClient;
import com.rntgroup.impl.entity.EmployeeEntity;
import com.rntgroup.impl.entity.Gender;
import com.rntgroup.impl.mapper.DepartmentMapper;
import com.rntgroup.impl.mapper.EmployeeMapper;
import com.rntgroup.impl.repository.DepartmentSnapshotRepository;
import com.rntgroup.impl.repository.EmployeeRepository;
import com.rntgroup.impl.service.impl.EmployeeServiceImpl;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;

public class MockEmployeeServiceCreator {

  private final List<EmployeeEntity> employees;

  public MockEmployeeServiceCreator() {
    employees = new ArrayList<>();
    employees.add(new EmployeeEntity(
        1L,
        RandomStringUtils.randomAlphanumeric(8),
        RandomStringUtils.randomAlphanumeric(8),
        RandomStringUtils.randomAlphanumeric(8),
        Gender.MALE,
        LocalDate.now(),
        "+7 (930) 222-22-22",
        1L,
        LocalDate.now(),
        null,
        RandomStringUtils.randomAlphanumeric(8),
        48_000,
        true
      )
    );

    employees.add(new EmployeeEntity(
        2L,
        RandomStringUtils.randomAlphanumeric(8),
        RandomStringUtils.randomAlphanumeric(8),
        RandomStringUtils.randomAlphanumeric(8),
        Gender.MALE,
        LocalDate.now(),
        "+7 (930) 111-11-11",
        1L,
        LocalDate.now(),
        null,
        RandomStringUtils.randomAlphanumeric(8),
        35_000,
        false
      )
    );

    employees.add(new EmployeeEntity(
        3L,
        RandomStringUtils.randomAlphanumeric(8),
        RandomStringUtils.randomAlphanumeric(8),
        RandomStringUtils.randomAlphanumeric(8),
        Gender.MALE,
        LocalDate.now(),
        "+7 (930) 333-33-33",
        2L,
        LocalDate.now(),
        null,
        RandomStringUtils.randomAlphanumeric(8),
        39_000,
        true
      )
    );
  }

  public EmployeeService getMockEmployeeService() {
    return new EmployeeServiceImpl(
      mockEmployeeRepository(),
      mockDepartmentClient(),
      EmployeeMapper.INSTANCE,
      DepartmentMapper.INSTANCE,
      mockDepartmentSnapshotRepository()
    );
  }

  public EmployeeRepository mockEmployeeRepository() {
    var mockRepository = Mockito.mock(EmployeeRepository.class);

    Mockito.when(mockRepository.findEmployeeById(Mockito.anyLong()))
      .then(invocation -> {
        var arg = invocation.getArgument(0);
        return employees.stream()
          .filter(employee ->
            employee.getId().equals(arg) && Objects.isNull(employee.getQuiteDate())
          )
          .findFirst();
      });

    Mockito.when(mockRepository.findById(Mockito.anyLong()))
      .then(invocation -> {
        var arg = invocation.getArgument(0);
        return employees.stream()
          .filter(employee ->
//              employee.getId().equals(arg)
            employee.getId().equals(arg) && Objects.isNull(employee.getQuiteDate())
          )
          .findFirst();
      });

    Mockito.when(mockRepository.findByPhoneNumber(Mockito.anyString()))
      .then(invocation -> {
        var arg = invocation.getArgument(0);
        return employees.stream()
          .filter(
            employee ->
              employee.getPhoneNumber().equals(arg) && Objects.isNull(employee.getQuiteDate())
          )
          .findFirst();
      });

    Mockito.when(mockRepository.saveAndFlush(Mockito.any()))
      .then(invocation -> {
        var arg = (EmployeeEntity) invocation.getArgument(0);
        if (Objects.nonNull(arg.getId()) && arg.getId() <= employees.size()) {
          var oldEmployee = employees.stream()
            .filter(employee -> employee.getId().equals(arg.getId()))
            .findFirst()
            .get();

          oldEmployee.setPhoneNumber(arg.getPhoneNumber());
          oldEmployee.setPayment(arg.getPayment());
          oldEmployee.setDepartmentId(arg.getDepartmentId());
          oldEmployee.setPosition(arg.getPosition());
          oldEmployee.setQuiteDate(arg.getQuiteDate());

          return oldEmployee;
        }

        var nextId = employees.stream()
          .mapToLong(EmployeeEntity::getId)
          .max().orElse(0) + 1;
        arg.setId(nextId);
        employees.add(arg);

        return arg;
      });

    Mockito.when(mockRepository.findByDepartmentId(Mockito.anyLong()))
      .then(invocation -> {
        var arg = (long) invocation.getArgument(0);
        return employees.stream()
          .filter(employee -> employee.getDepartmentId().equals(arg))
          .toList();
      });

    Mockito.when(mockRepository.findLeaderInDepartment(Mockito.anyLong()))
      .then(invocation -> {
        var arg = invocation.getArgument(0);
        return employees.stream()
          .filter(employee ->
            employee.getDepartmentId().equals(arg) && employee.getIsLeader()
          )
          .findFirst();
      });

    Mockito.when(mockRepository.commonPaymentForDepartment(Mockito.anyLong()))
      .then(invocation -> {
        var arg = (Long) invocation.getArgument(0);

        if (arg.equals(1L)) {
          return 83_000;
        }
        if (arg.equals(2L)) {
          return 39_000;
        }
        return 0;
      });

    return mockRepository;
  }

  public DepartmentSnapshotRepository mockDepartmentSnapshotRepository() {
    var mockRepository = Mockito.mock(DepartmentSnapshotRepository.class);

    Mockito.when(mockRepository.findById(Mockito.anyLong()))
      .thenReturn(Optional.empty());

    Mockito.when(mockRepository.saveAndFlush(Mockito.any()))
      .thenReturn(null);

    return mockRepository;
  }

  public DepartmentClient mockDepartmentClient() {
    var mockClient = Mockito.mock(DepartmentClient.class);

    Mockito.when(mockClient.findDepartmentById(Mockito.anyLong()))
      .then(invocation -> {
        var arg = (long) invocation.getArgument(0);
        HttpStatus status = HttpStatus.NOT_FOUND;

        if (arg > 0 && arg <= 4) {
          status = HttpStatus.OK;
        }

        return new ResponseEntity<>(new DepartmentMessageDto(arg, "Department"), status);
      });

    return mockClient;
  }
}
