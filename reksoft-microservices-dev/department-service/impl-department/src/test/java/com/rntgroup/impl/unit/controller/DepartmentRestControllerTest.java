package com.rntgroup.impl.unit.controller;

import com.rntgroup.api.controller.DepartmentApi;
import com.rntgroup.api.dto.DepartmentEditDto;
import com.rntgroup.api.dto.DepartmentReadDto;
import com.rntgroup.api.dto.DepartmentSaveDto;
import com.rntgroup.api.service.DepartmentService;
import com.rntgroup.impl.controller.DepartmentRestController;
import com.rntgroup.impl.exception.DepartmentStillHasEmployeesException;
import com.rntgroup.impl.exception.DepartmentWithNotFoundException;
import com.rntgroup.impl.exception.UniqueAttributeAlreadyExistException;
import com.rntgroup.impl.unit.mock.service.MockDepartmentServiceCreator;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@ExtendWith(MockitoExtension.class)
class DepartmentRestControllerTest {

  private final MockDepartmentServiceCreator mockDepartmentServiceCreator
    = new MockDepartmentServiceCreator();
  private final DepartmentService departmentService
    = mockDepartmentServiceCreator.getDepartmentService();
  private final DepartmentRestController departmentRestController
    = new DepartmentRestController(departmentService);

  @Test
  void findDepartmentById() {
    var departmentFoundByController = departmentRestController.findDepartmentById(1L);
    var departmentFoundByService = departmentService.findDepartmentById(1L);

    assertThat(departmentFoundByController.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(departmentFoundByController.getBody()).isEqualTo(departmentFoundByService);
  }

  @Test
  void findUnknownDepartmentId() {
    assertThatThrownBy(() -> departmentRestController.findDepartmentById(100L))
      .isInstanceOf(DepartmentWithNotFoundException.class);
  }

  @Test
  void findDepartmentByName() {
    var departmentFoundByController = departmentRestController.findDepartmentByName("Department1");
    var departmentFoundByService = departmentService.findDepartmentByName("Department1");

    assertThat(departmentFoundByController.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(departmentFoundByController.getBody()).isEqualTo(departmentFoundByService);
  }

  @Test
  void findUnknownDepartmentByName() {
    assertThatThrownBy(() -> departmentRestController.findDepartmentByName("Unknown department"))
      .isInstanceOf(DepartmentWithNotFoundException.class);
  }

  @Test
  void saveDepartment() {
    var departmentSaveDto = getSaveDtoThatIsNotMain();
    var departmentSavedByController = departmentRestController.createDepartment(departmentSaveDto);
    var departmentFoundByService = departmentService.findDepartmentByName(
      departmentSaveDto.name());

    assertThat(departmentSavedByController.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(departmentSavedByController.getBody()).isEqualTo(departmentFoundByService);
  }

  @Test
  void saveExistentDepartment() {
    assertThatThrownBy(() -> departmentRestController.createDepartment(getExistingDto()))
      .isInstanceOf(UniqueAttributeAlreadyExistException.class);
  }

  @Test
  void saveDepartmentWithUnknownParentDepartment() {
    assertThatThrownBy(
      () -> departmentRestController.createDepartment(getSaveDtoWithUnknownParentId()))
      .isInstanceOfAny(DepartmentWithNotFoundException.class);
  }

  @Test
  void deleteDepartment() {
    var departmentDeletedByController = departmentRestController.deleteDepartment(3L);

    assertThat(departmentDeletedByController.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThatThrownBy(() -> departmentService.findDepartmentById(3L))
      .isInstanceOf(DepartmentWithNotFoundException.class);
  }

  @Test
  void deleteDepartmentWithIllegalId() {
    assertThatThrownBy(() -> departmentRestController.deleteDepartment(-1L))
      .isInstanceOf(DepartmentWithNotFoundException.class);
  }

  @Test
  void deleteUnRegisteredDepartment() {
    assertThatThrownBy(() -> departmentRestController.deleteDepartment(100L))
      .isInstanceOf(DepartmentWithNotFoundException.class);
  }

  @Test
  void deleteNotEmptyDepartment() {
    assertThatThrownBy(() -> departmentRestController.deleteDepartment(1L))
      .isInstanceOf(DepartmentStillHasEmployeesException.class);
  }

  @Test
  void updateDepartment() {
    var updatedDepartment = departmentRestController.updateDepartment(2L, getDepartmentEditDto());
    assertThat(updatedDepartment.getStatusCode()).isEqualTo(HttpStatus.OK);

    var updatedDepartmentFoundByService = departmentService.findDepartmentById(2L);
    assertThat(updatedDepartment.getBody()).isEqualTo(updatedDepartmentFoundByService);
  }

  @Test
  void updateUnknownDepartment() {
    assertThatThrownBy(
      () -> departmentRestController.updateDepartment(100L, getDepartmentEditDto()))
      .isInstanceOf(DepartmentWithNotFoundException.class);
  }
//
//  private DepartmentReadDto getReadDto() {
//    return new DepartmentReadDto(
//      1L,
//      "Department",
//      LocalDate.now(),
//      1,
//      1
//    );
//  }

  private DepartmentSaveDto getSaveDtoThatIsNotMain() {
    return new DepartmentSaveDto(
      "New Department",
      2L,
      1L
    );
  }
//
//  private DepartmentSaveDto getSaveDtoWithNullableName() {
//    return new DepartmentSaveDto(
//      "",
//      2L,
//      1L
//    );
//  }

  private DepartmentSaveDto getExistingDto() {
    return new DepartmentSaveDto(
      "Department1",
      2L,
      1L
    );
  }

  private DepartmentSaveDto getSaveDtoWithUnknownParentId() {
    return new DepartmentSaveDto(
      "New Department",
      1000L,
      1L
    );
  }

  private DepartmentEditDto getDepartmentEditDto() {
    return new DepartmentEditDto("_test");
  }
}
