package com.rntgroup.impl.integration.service.impl;

import com.rntgroup.api.dto.DepartmentEditDto;
import com.rntgroup.api.dto.DepartmentSaveDto;
import com.rntgroup.api.service.DepartmentService;
import com.rntgroup.impl.client.EmployeeClient;
import com.rntgroup.impl.exception.DepartmentStillHasEmployeesException;
import com.rntgroup.impl.exception.UniqueAttributeAlreadyExistException;
import com.rntgroup.impl.integration.TestBase;
import com.rntgroup.impl.integration.annotation.IntegrationTest;
import com.rntgroup.impl.mapper.DepartmentMapper;
import com.rntgroup.impl.repository.DepartmentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IntegrationTest
@RequiredArgsConstructor
class DepartmentServiceImplTestIT extends TestBase {

  private final DepartmentService departmentService;
  private final DepartmentRepository departmentRepository;
  private final EmployeeClient employeeClient;
  private final DepartmentMapper departmentMapper;

  @Test
  @Transactional
  void findDepartmentById() {
    var department = departmentService.findDepartmentById(1L);
    var departmentFromRepository = departmentRepository.findById(department.id()).get();
    var mappedDepartment = departmentMapper.mapEntityToRead(departmentFromRepository, 1L, 1);
    assertThat(department).isEqualTo(mappedDepartment);
  }

  @Test
  void findUnknownDepartmentById() {
    assertThatThrownBy(() -> departmentService.findDepartmentById(-1L))
      .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  @Transactional
  void findDepartmentByName() {
    var department = departmentService.findDepartmentByName("Main department");
    var departmentFromRepository = departmentRepository.findByName(department.name()).get();
    var mappedDepartment = departmentMapper.mapEntityToRead(departmentFromRepository, 1L, 1);
    assertThat(department).isEqualTo(mappedDepartment);
  }

  @Test
  void findUnknownDepartmentByName() {
    assertThatThrownBy(() -> departmentService.findDepartmentByName("Unknown department"))
      .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void getPaymentForDepartment() {
    var payment = departmentService.getPaymentForDepartment(1L);
    var paymentFromClient = employeeClient.getCommonPaymentForDepartment(1L).getBody();
    assertThat(payment.commonPayment()).isEqualTo(paymentFromClient);
  }

  @Test
  void getPaymentForUnknownDepartment() {
    assertThatThrownBy(() -> departmentService.getPaymentForDepartment(-1L))
      .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  @Transactional
  void saveDepartment() {
    var saveDto = getDepartmentSaveDto();
    var savedDepartment = departmentService.saveDepartment(saveDto);
    var department = departmentRepository.findById(savedDepartment.id());

    assertThat(department).isPresent();
    assertThat(department.get().getName()).isEqualTo(savedDepartment.name());

    var parentId = saveDto.parentDepartmentId();
    var parentDepartment = departmentRepository.findById(parentId).get();
    var childDepartments = parentDepartment.getChildDepartment();

    assertThat(childDepartments).contains(department.get());
  }

  @Test
  @Transactional
  void saveNewMainDepartment() {
    var saveDto = getMainDepartmentSaveDto();
    var savedDepartment = departmentService.saveDepartment(saveDto);
    var department = departmentRepository.findById(savedDepartment.id());

    var departmentEntity = department.get();

    assertThat(department).isPresent();
    assertThat(departmentEntity.getName()).isEqualTo(savedDepartment.name());

    var currentMainDepartmentName = departmentRepository.findMainDepartment().getName();
    assertThat(departmentEntity.getName()).isEqualTo(currentMainDepartmentName);
  }

  @Test
  void saveDepartmentWithExistingName() {
    var saveDto = getExistingDepartmentSaveDto();
    assertThatThrownBy(() -> departmentService.saveDepartment(saveDto))
      .isInstanceOf(UniqueAttributeAlreadyExistException.class);
  }

  @Test
  @Transactional
  void deleteMainDepartment() {
    var saveDto = getMainDepartmentSaveDto();
    departmentService.saveDepartment(saveDto);

    var mainDepartmentBeforeDeleting = departmentRepository.findMainDepartment();
    departmentService.deleteDepartment(mainDepartmentBeforeDeleting.getId());
    var mainDepartmentAfterDeleting = departmentRepository.findMainDepartment();

    var department = departmentRepository.findById(mainDepartmentBeforeDeleting.getId());

    assertThat(department).isEmpty();
    assertThat(mainDepartmentAfterDeleting.getIsMain()).isTrue();
  }

  @Test
  @Transactional
  void deleteDepartment() {
    var saveDto = getDepartmentSaveDto();
    var savedDepartment = departmentService.saveDepartment(saveDto);
    departmentService.deleteDepartment(savedDepartment.id());
    var departmentEntityAfterDeleting = departmentRepository.findById(savedDepartment.id());
    assertThat(departmentEntityAfterDeleting).isEmpty();
  }

  @Test
  void findDepartmentShortInfo() {
    var department = departmentService.findDepartmentById(1L);
    var departmentFromRepository = departmentRepository.findById(department.id()).get();
    var mappedDepartment = departmentMapper.mapEntityToMessageDto(departmentFromRepository);
    assertThat(mappedDepartment.id()).isEqualTo(department.id());
    assertThat(mappedDepartment.name()).isEqualTo(department.name());
  }

  @Test
  void findUnknownDepartmentShortInfo() {
    assertThatThrownBy(() -> departmentService.findDepartmentShortInfo(-1L))
      .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  @Transactional
  void updateDepartment() {
    var editDto = getDepartmentEditDto();
    var departmentBeforeUpdate = departmentRepository.findById(2L).get();
    var updatedDepartment = departmentService.updateDepartment(editDto, 2L);
    var departmentAfterUpdate = departmentRepository.findById(updatedDepartment.id()).get();

    assertThat(departmentAfterUpdate.getName()).isEqualTo(updatedDepartment.name());
    assertThat(departmentAfterUpdate.getId()).isEqualTo(departmentBeforeUpdate.getId());
    assertThat(departmentAfterUpdate.getCreatedAt()).isEqualTo(
      departmentBeforeUpdate.getCreatedAt());
    assertThat(departmentAfterUpdate.getChildDepartment()).isEqualTo(
      departmentBeforeUpdate.getChildDepartment());
  }

  @Test
  void updateDepartmentWithExistingName() {
    var editDto = getDepartmentEditDtoWithExistingName();
    assertThatThrownBy(() -> departmentService.updateDepartment(editDto, 2L))
      .isInstanceOf(UniqueAttributeAlreadyExistException.class);
  }

  @Test
  void deleteDepartmentWithEmployees() {
    assertThatThrownBy(() -> departmentService.deleteDepartment(1L))
      .isInstanceOf(DepartmentStillHasEmployeesException.class);
  }

  @Test
  void deleteDepartmentWithWrongId() {
    assertThatThrownBy(() -> departmentService.deleteDepartment(-1L))
      .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  void findAllEmployeesInDepartment() {
    var employees = departmentService.findAllEmployeesInDepartment(2L);
    var employeesFromClient = employeeClient.getAllEmployeesByDepartmentId(2L).getBody();
    assertThat(employees).hasSize(employeesFromClient.size());
  }

  @Test
  void findAllEmployeesInUnregisteredDepartment() {
    assertThatThrownBy(() -> departmentService.findAllEmployeesInDepartment(-1L))
      .isInstanceOf(EntityNotFoundException.class);
  }

  private DepartmentSaveDto getDepartmentSaveDto() {
    return new DepartmentSaveDto("Test", 2L, 2L);
  }

  private DepartmentSaveDto getMainDepartmentSaveDto() {
    return new DepartmentSaveDto("Test", null, 2L);
  }

  private DepartmentSaveDto getExistingDepartmentSaveDto() {
    return new DepartmentSaveDto("Sales Department", 1L, 1L);
  }

  private DepartmentEditDto getDepartmentEditDto() {
    return new DepartmentEditDto("Test_");
  }

  private DepartmentEditDto getDepartmentEditDtoWithExistingName() {
    return new DepartmentEditDto("Accounting");
  }
}