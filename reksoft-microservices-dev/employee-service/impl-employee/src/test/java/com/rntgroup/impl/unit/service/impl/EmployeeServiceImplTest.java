package com.rntgroup.impl.unit.service.impl;

import com.rntgroup.api.dto.EmployeeEditDto;
import com.rntgroup.api.dto.EmployeeSaveDto;
import com.rntgroup.api.service.EmployeeService;
import com.rntgroup.impl.exception.PaymentNotValidException;
import com.rntgroup.impl.exception.UniqueAttributeAlreadyExistException;
import com.rntgroup.impl.mapper.EmployeeMapper;
import com.rntgroup.impl.repository.EmployeeRepository;
import com.rntgroup.impl.unit.mock.service.MockEmployeeServiceCreator;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EmployeeServiceImplTest {

  private final static long DEPARTMENT_ID = 1;

  private final MockEmployeeServiceCreator serviceCreator = new MockEmployeeServiceCreator();
  private final EmployeeService employeeService = serviceCreator.getMockEmployeeService();

  private final EmployeeRepository employeeRepository = serviceCreator.mockEmployeeRepository();
  private final EmployeeMapper employeeMapper = EmployeeMapper.INSTANCE;

  @Test
  @Transactional
  void getEmployeeInfo() {
    var employeeInfo = employeeService.getEmployeeInfo(1L);
    var employeeFoundByRepository = employeeRepository.findById(employeeInfo.id());
    var mappedEmployee = employeeMapper.mapEntityToRead(employeeFoundByRepository.get(),
      employeeInfo.departmentName());

    assertThat(employeeInfo).isEqualTo(mappedEmployee);
  }

  @Test
  void findUnregisteredEmployee() {
    assertThatThrownBy(() -> employeeService.getEmployeeInfo(-1L))
      .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void findQuitedEmployee() {
    assertThatThrownBy(() -> {
      var quitedEmployee = employeeService.deleteEmployee(4L);
      employeeService.getEmployeeInfo(quitedEmployee.id());
    }).isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  @Transactional
  void hireEmployee() {
    var saveDto = getSaveDto();
    var savedEmployeeReadDto = employeeService.hireEmployee(saveDto);
    var savedEmployee = employeeRepository.findById(savedEmployeeReadDto.id());
    assertThat(savedEmployee).isPresent();

    var mappedSavedEmployee = employeeMapper.mapEntityToRead(savedEmployee.get(),
      savedEmployeeReadDto.departmentName());
    assertThat(mappedSavedEmployee).isEqualTo(savedEmployeeReadDto);
  }

  @Test
  void hireEmployeeWithBigPayment() {
    assertThatThrownBy(() -> {
      var saveDto = getSaveDtoPaymentThatIsGreaterThanLeaderHas();
      employeeService.hireEmployee(saveDto);
    }).isInstanceOf(PaymentNotValidException.class);
  }

  @Test
  void hireEmployeeWithExistingPhoneNumber() {
    assertThatThrownBy(() -> {
      var saveDto = getSaveDtoWithExistingPhoneNumber();
      employeeService.hireEmployee(saveDto);
    }).isInstanceOf(UniqueAttributeAlreadyExistException.class);
  }

  @Test
  @Transactional
  void deleteEmployee() {
    var deletedEmployee = employeeService.deleteEmployee(3L);
    var deletedEmployeeFromRepository = employeeRepository.findById(deletedEmployee.id());
    assertThat(deletedEmployeeFromRepository).isEmpty();
  }

  @Test
  void deleteUnregisteredEmployee() {
    assertThatThrownBy(() -> {
      employeeService.deleteEmployee(-1L);
    }).isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  @Transactional
  void updateEmployee() {
    var updateDto = getEditDto();
    var updatedEmployee = employeeService.updateEmployee(updateDto, 1L);
    var updatedEmployeeFromRepository = employeeRepository.findById(updatedEmployee.id()).get();
    var mappedEmployee = employeeMapper.mapEntityToRead(updatedEmployeeFromRepository,
      updatedEmployee.departmentName());
    assertThat(mappedEmployee).isEqualTo(updatedEmployee);
  }

  @Test
  void updateEmployeeWithWrongPayment() {
    assertThatThrownBy(() -> {
      var editDto = getEditDtoWithWrongPayment();
      employeeService.updateEmployee(editDto, 1L);
    }).isInstanceOf(PaymentNotValidException.class);
  }

  @Test
  void findEmployeesCountInDepartment() {
    var employeesCountFromService = (long) employeeService.findEmployeesCountInDepartment(
      DEPARTMENT_ID);
    assertThat(employeesCountFromService).isEqualTo(2);
  }

  @Test
  void getCommonPaymentForDepartment() {
    var employeesPaymentFromService = (long) employeeService.getCommonPaymentForDepartment(1L);
    assertThat(employeesPaymentFromService).isEqualTo(83_000);
  }

  @Test
  void getEmployeesInDepartment() {
    var employeesInDepartmentFoundByService = employeeService.getEmployeesInDepartment(
      DEPARTMENT_ID);
    assertThat(employeesInDepartmentFoundByService.size()).isEqualTo(2);
  }

  private EmployeeSaveDto getSaveDto() {
    return new EmployeeSaveDto(
      "Testov",
      "Test",
      "Testovich",
      "MALE",
      LocalDate.of(2000, 10, 11),
      "+7 (900) 100-11-12",
      1L,
      "Manager",
      45_000

    );
  }

  private EmployeeSaveDto getSaveDtoPaymentThatIsGreaterThanLeaderHas() {
    return new EmployeeSaveDto(
      "Testov",
      "Test",
      "Testovich",
      "MALE",
      LocalDate.of(2000, 10, 11),
      "+7 (900) 100-11-12",
      1L,
      "Manager",
      1_000_000
    );
  }


  private EmployeeEditDto getEditDto() {
    return new EmployeeEditDto(
      "Testov",
      "Test",
      "+7 (900) 100-11-12",
      1L,
      "Manager",
      45_000,
      true
    );
  }

  private EmployeeSaveDto getSaveDtoWithExistingPhoneNumber() {
    return new EmployeeSaveDto(
      "Testov",
      "Test",
      "Testovich",
      "MALE",
      LocalDate.of(2000, 10, 11),
      "+7 (930) 222-22-22",
      1L,
      "Manager",
      45_000
    );
  }

  private EmployeeEditDto getEditDtoWithWrongPayment() {
    return new EmployeeEditDto(
      "Testov",
      "Test",
      "+7 (930) 232-52-62",
      1L,
      "Manager",
      1_000_000,
      false
    );
  }
}