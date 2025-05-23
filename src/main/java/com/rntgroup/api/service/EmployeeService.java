package com.rntgroup.api.service;

import com.rntgroup.api.dto.EmployeeEditDto;
import com.rntgroup.api.dto.EmployeeReadDto;
import com.rntgroup.api.dto.EmployeeSaveDto;
import com.rntgroup.api.dto.EmployeeShortInfoDto;
import com.rntgroup.api.entity.EmployeeEntity;
import com.rntgroup.api.exception.EmployeeNotFoundException;
import com.rntgroup.api.exception.PaymentNotValidException;
import com.rntgroup.api.exception.UniqueAttributeAlreadyExistException;
import com.rntgroup.api.mapper.EmployeeMapper;
import com.rntgroup.api.repository.DepartmentRepository;
import com.rntgroup.api.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final DepartmentRepository departmentRepository;
  private final EmployeeMapper employeeMapper;

  public EmployeeReadDto getEmployeeInfo(Long employeeId) {
    var foundEmployee = employeeRepository.findEmployeeById(employeeId);

    if (foundEmployee.isEmpty()) {
      throw new EmployeeNotFoundException(employeeId);
    }

    var employee = foundEmployee.get();
    var quiteDate = Optional.ofNullable(employee.getQuiteDate());

    var departmentId = employee.getDepartmentId();

    if (quiteDate.isPresent()) {
      throw new EmployeeNotFoundException(employeeId);
    }

    var department = getDepartmentName(departmentId);

    return employeeMapper.mapEntityToRead(employee, department);
  }

  @Transactional(timeout = 30)
  public EmployeeReadDto hireEmployee(EmployeeSaveDto employeeSaveDto) {
    var departmentId = employeeSaveDto.departmentId();
    var departmentName = getDepartmentName(departmentId);

    if (!checkLeaderPayment(employeeSaveDto.payment(), departmentId)) {
      throw new PaymentNotValidException("Employee payment should not exceed that of their boss");
    }

    var foundByPhoneNumber = employeeRepository.findByPhoneNumber(employeeSaveDto.phoneNumber());

    if (foundByPhoneNumber.isPresent()) {
      throw new UniqueAttributeAlreadyExistException(
        "Employee with this phone number already exist");
    }

    var newEmployee = employeeMapper.mapSaveToEntity(employeeSaveDto);
    var employee = employeeRepository.saveAndFlush(newEmployee);

    return employeeMapper.mapEntityToRead(employee, departmentName);
  }

  @Transactional(timeout = 30)
  public EmployeeReadDto deleteEmployee(Long employeeId) {
    var employee = employeeRepository.findEmployeeById(employeeId);

    if (employee.isEmpty()) {
      throw new EmployeeNotFoundException(employeeId);
    }

    var employeeEntity = employee.get();
    employeeEntity.setQuiteDate(LocalDate.now());
    employeeRepository.saveAndFlush(employeeEntity);

    var departmentId = employeeEntity.getDepartmentId();
    var departmentName = getDepartmentName(departmentId);

    return employeeMapper.mapEntityToRead(employeeEntity, departmentName);
  }

  @Transactional(timeout = 30)
  public EmployeeReadDto updateEmployee(EmployeeEditDto employeeEditDto, Long employeeId) {
    var employeeEntity = getEmployeeById(employeeId);
    var payment = employeeEditDto.payment();

    if (employeeEditDto.isLeader()) {
      var leaderInGroup = employeeRepository.findLeaderInDepartment(
        employeeEditDto.departmentId());

      if (leaderInGroup.isPresent()) {
        var leader = leaderInGroup.get();
        employeeEntity.setIsLeader(true);
        leader.setIsLeader(false);
        payment = leader.getPayment();
        employeeRepository.saveAndFlush(leader);
      }
    } else {
      if (!checkLeaderPayment(employeeEditDto.payment(), employeeEditDto.departmentId())) {
        throw new PaymentNotValidException("Employee payment should not exceed that of their boss");
      }
    }

    var update = employeeMapper.mapEditToEntity(employeeEditDto, employeeEntity);
    update.setPayment(payment);
    employeeRepository.saveAndFlush(update);
    String departmentName = getDepartmentName(employeeEditDto.departmentId());

    return employeeMapper.mapEntityToRead(update, departmentName);
  }

  public Integer findEmployeesCountInDepartment(Long departmentId) {
    var employees = employeeRepository.findByDepartmentId(departmentId);
    return employees.size();
  }

  public Integer getCommonPaymentForDepartment(Long departmentId) {
    return employeeRepository.commonPaymentForDepartment(departmentId);
  }

  public EmployeeShortInfoDto getLeaderShortInfo(Long departmentId) {
    var employee = employeeRepository.findLeaderInDepartment(departmentId);

    if (employee.isEmpty()) {
      return new EmployeeShortInfoDto(
        "Unknown",
        "Unknown"
      );
    }
    var employeeEntity = employee.get();

    return new EmployeeShortInfoDto(
      employeeEntity.getName(),
      employeeEntity.getSurname()
    );
  }

  public List<EmployeeShortInfoDto> getEmployeesInDepartment(Long departmentId) {
    var employees = employeeRepository.findByDepartmentId(departmentId);
    return employees.stream()
      .map(employeeMapper::mapEntityToShortDto)
      .toList();
  }

  private String getDepartmentName(Long departmentId) {
    var department = departmentRepository.findById(departmentId);

    if (department.isPresent()) {
      return department.get().getName();
    } else {
      throw new EntityNotFoundException("Department with id " + departmentId + " not found");
    }
  }

  private boolean checkLeaderPayment(Integer employeePayment, Long departmentId) {
    var leaderOpt = employeeRepository.findLeaderInDepartment(departmentId);
    return leaderOpt.isEmpty() ? true : employeePayment < leaderOpt.get().getPayment();
  }

  private EmployeeEntity getEmployeeById(Long employeeId) {
    return employeeRepository.findEmployeeById(employeeId)
      .orElseThrow(
        () -> new EmployeeNotFoundException(employeeId));
  }
}

