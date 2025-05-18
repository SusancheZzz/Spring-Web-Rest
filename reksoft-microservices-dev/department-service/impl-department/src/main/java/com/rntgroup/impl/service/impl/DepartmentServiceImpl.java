package com.rntgroup.impl.service.impl;

import com.rntgroup.api.dto.CommonPaymentForDepartment;
import com.rntgroup.api.dto.DepartmentEditDto;
import com.rntgroup.api.dto.DepartmentMessageDto;
import com.rntgroup.api.dto.DepartmentReadDto;
import com.rntgroup.api.dto.DepartmentSaveDto;
import com.rntgroup.api.dto.EmployeeShortInfoDto;
import com.rntgroup.api.service.DepartmentService;
import com.rntgroup.impl.client.EmployeeClient;
import com.rntgroup.impl.entity.DepartmentEntity;
import com.rntgroup.impl.entity.Operation;
import com.rntgroup.impl.exception.DepartmentStillHasEmployeesException;
import com.rntgroup.impl.exception.DepartmentWithNotFoundException;
import com.rntgroup.impl.exception.UniqueAttributeAlreadyExistException;
import com.rntgroup.impl.listener.event.AuditObjectEvent;
import com.rntgroup.impl.mapper.DepartmentMapper;
import com.rntgroup.impl.repository.DepartmentPaymentRepository;
import com.rntgroup.impl.repository.DepartmentRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepartmentServiceImpl implements DepartmentService {

  private static final int SCHEDULER_DURATION = 60_000;

  private final DepartmentRepository departmentRepository;
  private final DepartmentPaymentRepository departmentPaymentRepository;
  private final EmployeeClient employeeClient;
  private final DepartmentMapper departmentMapper;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  public DepartmentReadDto findDepartmentById(Long departmentId) {
    var departmentOpt = departmentRepository.findById(departmentId);
    if (departmentOpt.isEmpty()) {
      throw new DepartmentWithNotFoundException("id", String.valueOf(departmentId));
    }

    return getReadDto(departmentOpt.get());
  }

  @Override
  public DepartmentReadDto findDepartmentByName(String name) {
    var departmentOpt = departmentRepository.findByName(name);
    if (departmentOpt.isEmpty()) {
      throw new DepartmentWithNotFoundException("name", name);
    }

    return getReadDto(departmentOpt.get());
  }

  @Override
  public CommonPaymentForDepartment getPaymentForDepartment(Long departmentId) {
    var departmentOpt = departmentRepository.findById(departmentId);
    if (departmentOpt.isEmpty()) {
      throw new DepartmentWithNotFoundException("id", String.valueOf(departmentId));
    }

    var department = departmentOpt.get();
    var commonPayment = employeeClient.getCommonPaymentForDepartment(departmentId).getBody();

    return new CommonPaymentForDepartment(department.getName(), commonPayment);
  }

  @Override
  @Transactional
  public DepartmentReadDto saveDepartment(DepartmentSaveDto departmentSaveDto) {
    var departmentName = departmentSaveDto.name();
    var departmentOpt = departmentRepository.findByName(departmentName);

    if (departmentOpt.isPresent()) {
      throw new UniqueAttributeAlreadyExistException(
        "Department with name " + departmentName + " already exists");
    }

    long leaderId = departmentSaveDto.leaderId();
    if (Boolean.FALSE.equals(employeeClient.isExistsEmployeeById(leaderId).getBody())) {
      throw new EntityNotFoundException("Employee with id = " + leaderId + " not found");
    }

    var newDepartment = departmentMapper.mapSaveToEntity(departmentSaveDto, LocalDate.now(), false);

    if (Objects.nonNull(departmentSaveDto.parentDepartmentId())) {
      var parentId = departmentSaveDto.parentDepartmentId();
      var parentDepartment = departmentRepository.findById(parentId);

      if (parentDepartment.isEmpty()) {
        throw new DepartmentWithNotFoundException("id", String.valueOf(parentId));
      }

      newDepartment = departmentRepository.saveAndFlush(newDepartment);
      departmentRepository.addChildDepartment(parentId, newDepartment.getId());
    } else {
      var mainDepartment = departmentRepository.findMainDepartment();
      mainDepartment.setIsMain(false);
      newDepartment.setIsMain(true);
      newDepartment.addChildDepartment(mainDepartment);

      departmentRepository.saveAndFlush(mainDepartment);
      departmentRepository.saveAndFlush(newDepartment);
    }

    employeeClient.updateIsLeaderById(true, leaderId);
    departmentRepository.addLeaderInDepartment(newDepartment.getId(), leaderId);

    eventPublisher.publishEvent(
      new AuditObjectEvent(newDepartment.getId(), Instant.now(), Operation.CREATE));

    return getReadDto(newDepartment);
  }

  @Override
  @Transactional
  public DepartmentReadDto deleteDepartment(Long departmentId) {
    var departmentOpt = departmentRepository.findById(departmentId);
    if (departmentOpt.isEmpty()) {
      throw new DepartmentWithNotFoundException("id", String.valueOf(departmentId));
    }
    var department = departmentOpt.get();
    var employeesInDepartment = employeeClient.getAllEmployeesByDepartmentId(departmentId)
      .getBody();

    if (Objects.nonNull(employeesInDepartment) && !employeesInDepartment.isEmpty()) {
      throw new DepartmentStillHasEmployeesException("Department with id " + departmentId
        + " still has employees");
    }

    var leader = employeeClient.getLeaderInDepartment(departmentId).getBody();

    if (department.getIsMain()) {
      var nextMain = department.removeChildDepartment(0);
      var childDepartmentsOfDeletingDepartment = department.getChildDepartment();

      nextMain.addAllChildDepartments(childDepartmentsOfDeletingDepartment);
      nextMain.setIsMain(true);

      departmentRepository.deleteById(department.getId());
      departmentRepository.saveAndFlush(nextMain);
    } else {
      var parentDepartment = departmentRepository.findDepartmentWithChildId(departmentId);
      var childDepartmentsOfDeletingDepartment = department.getChildDepartment();
      parentDepartment.removeChildDepartment(department);
      parentDepartment.addAllChildDepartments(childDepartmentsOfDeletingDepartment);
      departmentRepository.saveAndFlush(parentDepartment);
      departmentRepository.deleteById(departmentId);
    }

    //После удаления проверяем, руководит ли этот человек ещё каким-то департаментом.
    //Если нет -- снимаем флажок лидера
    var countDepartmentsOfLeader = employeeClient.getCountDepartmentsOfLeaderById(leader.id());
    if (Objects.nonNull(countDepartmentsOfLeader) && countDepartmentsOfLeader.getBody() == 0) {
      employeeClient.updateIsLeaderById(false, leader.id());
    }
    eventPublisher.publishEvent(
      new AuditObjectEvent(department.getId(), Instant.now(), Operation.DELETE));

    return getReadDto(department);
  }

  @Override
  @Transactional
  public DepartmentReadDto updateDepartment(
    DepartmentEditDto departmentEditDto,
    Long departmentId) {

    var departmentOpt = departmentRepository.findById(departmentId);
    if (departmentOpt.isEmpty()) {
      throw new DepartmentWithNotFoundException("id", String.valueOf(departmentId));
    }
    var departmentFoundByName = departmentRepository.findByName(departmentEditDto.name());
    if (departmentFoundByName.isPresent()) {
      throw new UniqueAttributeAlreadyExistException(
        "Department with name " + departmentEditDto.name() + " already exists");
    }
    var department = departmentOpt.get();
    var updatedDepartment = departmentMapper.mapEditDtoToEntity(departmentEditDto,
      department);

    departmentRepository.saveAndFlush(updatedDepartment);
    var leader = employeeClient.getLeaderInDepartment(departmentId).getBody();
    if (Objects.isNull(leader)) {
      throw new EntityNotFoundException(
        "Leader department with id = " + departmentId + " not found");
    }

    eventPublisher.publishEvent(
      new AuditObjectEvent(updatedDepartment.getId(), Instant.now(), Operation.UPDATE));

    return getReadDto(department);
  }

  @Override
  public List<EmployeeShortInfoDto> findAllEmployeesInDepartment(Long departmentId) {
    var departmentOpt = departmentRepository.findById(departmentId);
    if (departmentOpt.isEmpty()) {
      throw new DepartmentWithNotFoundException("id", String.valueOf(departmentId));
    }
    var employeesInDepartment = employeeClient.getAllEmployeesByDepartmentId(departmentId);

    return Objects.isNull(employeesInDepartment) ? List.of() : employeesInDepartment.getBody();
  }

  @Override
  public DepartmentMessageDto findDepartmentShortInfo(Long departmentId) {
    var department = findDepartmentById(departmentId);
    return new DepartmentMessageDto(departmentId, department.name());
  }

  @Override
  @Transactional
  @Scheduled(fixedDelay = SCHEDULER_DURATION)
  public void updatePaymentInfo() {
    var departmentsPayments = departmentPaymentRepository.findAll();

    for (var departmentPayment : departmentsPayments) {
      var id = departmentPayment.getId();
      var commonPayment = employeeClient.getCommonPaymentForDepartment(id).getBody();
      departmentPayment.setPayment(commonPayment);
    }

    departmentPaymentRepository.saveAll(departmentsPayments);
  }

  private DepartmentReadDto getReadDto(DepartmentEntity departmentEntity) {

    var employeesNumber = employeeClient.countEmployeesInDepartment(departmentEntity.getId())
      .getBody();
    var leaderId = employeeClient.getLeaderInDepartment(departmentEntity.getId()).getBody().id();

    return departmentMapper.mapEntityToRead(departmentEntity, leaderId,
      employeesNumber);
  }
}
