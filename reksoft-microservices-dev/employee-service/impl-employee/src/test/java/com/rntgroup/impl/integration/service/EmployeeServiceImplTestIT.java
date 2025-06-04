package com.rntgroup.impl.integration.service;

import com.rntgroup.api.dto.EmployeeEditDto;
import com.rntgroup.api.dto.EmployeeSaveDto;
import com.rntgroup.api.dto.EmployeeShortInfoDto;
import com.rntgroup.api.service.EmployeeService;
import com.rntgroup.impl.entity.EmployeeEntity;
import com.rntgroup.impl.exception.PaymentNotValidException;
import com.rntgroup.impl.exception.UniqueAttributeAlreadyExistException;
import com.rntgroup.impl.integration.TestBase;
import com.rntgroup.impl.integration.annotation.IntegrationTest;
import com.rntgroup.impl.mapper.EmployeeMapper;
import com.rntgroup.impl.repository.EmployeeRepository;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IntegrationTest
@RequiredArgsConstructor
class EmployeeServiceImplTestIT extends TestBase {

  private static final long DEPARTMENT_ID = 1;

  private final EmployeeService employeeService;
  private final EmployeeRepository employeeRepository;
  private final EmployeeMapper employeeMapper = EmployeeMapper.INSTANCE;

  private final EntityManagerFactory entityManagerFactory;

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
    var deletedEmployee = employeeService.deleteEmployee(2L);
    var deletedEmployeeFromRepository = employeeRepository.findEmployeeById(deletedEmployee.id());
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
    var updatedEmployeeFromRepository = employeeRepository.getReferenceById(
      updatedEmployee.id());
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
    var entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    var employeesCount = entityManager.createQuery("select count(e) from EmployeeEntity e " +
        "where e.departmentId = :departmentId", Long.class)
      .setParameter("departmentId", DEPARTMENT_ID)
      .getSingleResult();
    entityManager.getTransaction().commit();

    var employeesCountFromService = (long) employeeService.findEmployeesCountInDepartment(
      DEPARTMENT_ID);
    assertThat(employeesCountFromService).isEqualTo(employeesCount);
  }

  @Test
  void getCommonPaymentForDepartment() {
    var entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    var employeesPayment = entityManager.createQuery(
        "select sum(e.payment) from EmployeeEntity e " +
          "where e.departmentId = :departmentId", Long.class)
      .setParameter("departmentId", DEPARTMENT_ID)
      .getSingleResult();
    entityManager.getTransaction().commit();

    var employeesPaymentFromService = (long) employeeService.getCommonPaymentForDepartment(
      DEPARTMENT_ID);
    assertThat(employeesPaymentFromService).isEqualTo(employeesPayment);
  }

  @Test
  void getLeaderShortInfo() {
    var entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    var leader = entityManager.createQuery("select e from EmployeeEntity e " +
        "where e.departmentId = :departmentId and e.isLeader = true", EmployeeEntity.class)
      .setParameter("departmentId", DEPARTMENT_ID)
      .getSingleResult();
    entityManager.getTransaction().commit();

    var leaderFoundByService = employeeService.getLeaderShortInfo(DEPARTMENT_ID);
    assertThat(leaderFoundByService.surname()).isEqualTo(leader.getSurname());
    assertThat(leaderFoundByService.name()).isEqualTo(leader.getName());
  }

  @Test
  void getEmployeesInDepartment() {
    var entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
    var employeesInDepartment = entityManager.createQuery("select e from EmployeeEntity e " +
        "where e.departmentId = :departmentId", EmployeeEntity.class)
      .setParameter("departmentId", DEPARTMENT_ID)
      .getResultList();
    var mappedEmployeesInDepartment = employeesInDepartment.stream()
      .map(employee -> new EmployeeShortInfoDto(
        employee.getId(),
        employee.getName(),
        employee.getSurname()
      ))
      .toList();
    entityManager.getTransaction().commit();

    var employeesInDepartmentFoundByService = employeeService.getEmployeesInDepartment(
      DEPARTMENT_ID);
    assertThat(employeesInDepartmentFoundByService).isEqualTo(mappedEmployeesInDepartment);
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

  private EmployeeSaveDto getSaveDtoWithExistingPhoneNumber() {
    return new EmployeeSaveDto(
      "Testov",
      "Test",
      "Testovich",
      "MALE",
      LocalDate.of(2000, 10, 11),
      "+7 (930) 364-01-64",
      1L,
      "Manager",
      45_000
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

  private EmployeeEditDto getEditDtoWithExistingPhoneNumber() {
    return new EmployeeEditDto(
      "Testov",
      "Test",
      "+7 (930) 364-01-64",
      1L,
      "Manager",
      45_000,
      true
    );
  }

  private EmployeeEditDto getEditDtoWithWrongPayment() {
    return new EmployeeEditDto(
      "Testov",
      "Test",
      "+7 (900) 100-11-13",
      1L,
      "Manager",
      1_000_000,
      false
    );
  }
}