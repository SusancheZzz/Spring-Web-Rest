package com.rntgroup.api.service;

import com.rntgroup.api.dto.CommonPaymentForDepartment;
import com.rntgroup.api.dto.DepartmentEditDto;
import com.rntgroup.api.dto.DepartmentMessageDto;
import com.rntgroup.api.dto.DepartmentReadDto;
import com.rntgroup.api.dto.DepartmentSaveDto;
import com.rntgroup.api.dto.EmployeeShortInfoDto;
import com.rntgroup.api.entity.DepartmentEntity;
import com.rntgroup.api.entity.EmployeeEntity;
import com.rntgroup.api.exception.DepartmentStillHasEmployeesException;
import com.rntgroup.api.exception.UniqueAttributeAlreadyExistException;
import com.rntgroup.api.mapper.DepartmentMapper;
import com.rntgroup.api.mapper.EmployeeMapper;
import com.rntgroup.api.repository.DepartmentRepository;
import com.rntgroup.api.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepartmentService {

  private static final String DEPARTMENT_WITH_NOT_FOUND = "Department with %s: %s not found";
  private final DepartmentRepository departmentRepository;
  private final EmployeeRepository employeeRepository;
  private final DepartmentMapper departmentMapper;

  public DepartmentReadDto findDepartmentById(Long departmentId) {
    var department = departmentRepository.findById(departmentId);
    if (department.isEmpty()) {
      throw new EntityNotFoundException(DEPARTMENT_WITH_NOT_FOUND.formatted("id", departmentId));
    }
    var departmentEntity = department.get();
    var leaderOpt = employeeRepository.findLeaderInDepartment(departmentId);

    return getReadDto(
      departmentEntity,
      departmentEntity.getId(),
      leaderOpt.get().getId()
    );
  }

  public DepartmentReadDto findDepartmentByName(String name) {
    var department = departmentRepository.findByName(name);
    if (department.isEmpty()) {
      throw new EntityNotFoundException(DEPARTMENT_WITH_NOT_FOUND.formatted("name", name));
    }
    var departmentEntity = department.get();
    var leaderOpt = employeeRepository.findLeaderInDepartment(departmentEntity.getId());

    return getReadDto(
      departmentEntity,
      departmentEntity.getId(),
      leaderOpt.get().getId()
    );
  }

  public CommonPaymentForDepartment getPaymentForDepartment(Long departmentId) {
    var departmentOpt = departmentRepository.findById(departmentId);

    if (departmentOpt.isEmpty()) {
      throw new EntityNotFoundException(DEPARTMENT_WITH_NOT_FOUND.formatted("id", departmentId));
    }

    var departmentEntity = departmentOpt.get();
    int commonPayment = employeeRepository.findByDepartmentId(departmentId).stream()
      .mapToInt(EmployeeEntity::getPayment)
      .sum();

    return new CommonPaymentForDepartment(departmentEntity.getName(), commonPayment);
  }

  @Transactional
  public DepartmentReadDto saveDepartment(DepartmentSaveDto departmentSaveDto) {
    var departmentName = departmentSaveDto.name();
    var department = departmentRepository.findByName(departmentName);

    if (department.isPresent()) {
      throw new UniqueAttributeAlreadyExistException(
        "Department with name " + departmentName + " already exists");
    }

    long leaderId = departmentSaveDto.leaderId();
    var leaderOpt = employeeRepository.findEmployeeById(leaderId);
    if (leaderOpt.isEmpty()) {
      throw new EntityNotFoundException("Employee with id = " + leaderId + " not found");
    }

    var newDepartment = departmentMapper.mapSaveToEntity(departmentSaveDto);

    if (Objects.nonNull(departmentSaveDto.parentDepartmentId())) {
      var parentId = departmentSaveDto.parentDepartmentId();
      var parentDepartment = departmentRepository.findById(parentId);

      if (parentDepartment.isEmpty()) {
        throw new EntityNotFoundException(DEPARTMENT_WITH_NOT_FOUND.formatted("id", parentId));
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

    employeeRepository.updateIsLeaderById(true, leaderId);
    departmentRepository.addLeaderInDepartment(newDepartment.getId(), leaderId);

    return getReadDto(
      newDepartment,
      newDepartment.getId(),
      leaderId
    );
  }

  @Transactional
  public DepartmentReadDto deleteDepartment(Long departmentId) {
    var departmentOpt = departmentRepository.findById(departmentId);

    if (departmentOpt.isEmpty()) {
      throw new EntityNotFoundException("Department with id " + departmentId + " does not exist");
    }

    var departmentEntity = departmentOpt.get();
    var employeesInDepartment = employeeRepository.findByDepartmentId(departmentId);

    if (!employeesInDepartment.isEmpty()) {
      throw new DepartmentStillHasEmployeesException("Department with id " + departmentId
        + " still has employees");
    }

    var leaderId = employeeRepository.findLeaderInDepartment(departmentId).get().getId();

    if (departmentEntity.getIsMain()) {
      var nextMain = departmentEntity.removeChildDepartment(0);
      var childDepartmentsOfDeletingDepartment = departmentEntity.getChildDepartment();

      nextMain.addAllChildDepartments(childDepartmentsOfDeletingDepartment);
      nextMain.setIsMain(true);

      departmentRepository.deleteById(departmentEntity.getId());
      departmentRepository.saveAndFlush(nextMain);
    } else {
      var parentDepartment = departmentRepository.findDepartmentWithChildId(departmentId);
      var childDepartmentsOfDeletingDepartment = departmentEntity.getChildDepartment();
      parentDepartment.removeChildDepartment(departmentEntity);
      parentDepartment.addAllChildDepartments(childDepartmentsOfDeletingDepartment);
      departmentRepository.saveAndFlush(parentDepartment);
      departmentRepository.deleteById(departmentId);
    }

    //После удаления проверяем, руководит ли этот человек ещё каким-то департаментом.
    //Если нет -- снимаем флажок лидера
    long countDepartmentsOfLeader = employeeRepository.getCountDepartmentsOfLeaderById(leaderId);
    if (countDepartmentsOfLeader == 0) {
      employeeRepository.updateIsLeaderById(false, leaderId);
    }

    return getReadDto(
      departmentEntity,
      departmentId,
      leaderId
    );
  }

  @Transactional
  public DepartmentReadDto updateDepartment(DepartmentEditDto departmentEditDto,
    Long departmentId) {
    var department = departmentRepository.findById(departmentId);
    if (department.isEmpty()) {
      throw new EntityNotFoundException("Department with id " + departmentId + " does not exist");
    }
    var departmentFoundByName = departmentRepository.findByName(departmentEditDto.name());
    if (departmentFoundByName.isPresent()) {
      throw new UniqueAttributeAlreadyExistException(
        "Department with name " + departmentEditDto.name() + " already exists");
    }
    var departmentEntity = department.get();
    var updatedDepartment = departmentMapper.mapEditDtoToEntity(departmentEditDto,
      departmentEntity);

    departmentRepository.saveAndFlush(updatedDepartment);
    var leaderOpt = employeeRepository.findLeaderInDepartment(departmentId);
    if (leaderOpt.isEmpty()) {
      throw new EntityNotFoundException(
        "Leader department with id = " + departmentId + " not found");
    }

    return getReadDto(
      departmentEntity,
      departmentId,
      leaderOpt.get().getId()
    );
  }

  public List<EmployeeShortInfoDto> findAllEmployeesInDepartment(Long departmentId) {
    var department = departmentRepository.findById(departmentId);
    if (department.isEmpty()) {
      throw new EntityNotFoundException("Department with id " + departmentId + " does not exist");
    }
    var employeesInDepartment = employeeRepository.findByDepartmentId(departmentId);

    return employeesInDepartment.stream()
      .map(EmployeeMapper.INSTANCE::mapEntityToShortDto)
      .toList();
  }

  public DepartmentMessageDto findDepartmentShortInfo(Long departmentId) {
    var department = findDepartmentById(departmentId);
    return new DepartmentMessageDto(departmentId, department.name());
  }

  private DepartmentReadDto getReadDto(DepartmentEntity departmentEntity, Long departmentId,
    Long leaderId) {
    int employeesNumber = employeeRepository.findByDepartmentId(departmentId).size();

    return departmentMapper.mapEntityToRead(departmentEntity, leaderId,
      employeesNumber);
  }

}
