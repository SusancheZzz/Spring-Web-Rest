package com.rntgroup.impl.service.impl;

import com.rntgroup.api.dto.EmployeeEditDto;
import com.rntgroup.api.dto.EmployeeReadDto;
import com.rntgroup.api.dto.EmployeeSaveDto;
import com.rntgroup.api.dto.EmployeeShortInfoDto;
import com.rntgroup.api.service.EmployeeService;
import com.rntgroup.impl.client.DepartmentClient;
import com.rntgroup.impl.entity.EmployeeEntity;
import com.rntgroup.impl.exception.DepartmentWithNotFoundException;
import com.rntgroup.impl.exception.EmployeeNotFoundException;
import com.rntgroup.impl.exception.PaymentNotValidException;
import com.rntgroup.impl.exception.UniqueAttributeAlreadyExistException;
import com.rntgroup.impl.mapper.EmployeeMapper;
import com.rntgroup.impl.repository.EmployeeRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final DepartmentClient departmentClient;
  private final EmployeeMapper employeeMapper;

  @Override
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

  @Override
  @Transactional(timeout = 30)
  public EmployeeReadDto hireEmployee(EmployeeSaveDto employeeSaveDto) {
    var departmentId = employeeSaveDto.departmentId();

    if (departmentClient.findDepartmentById(employeeSaveDto.departmentId()).getStatusCode()
      .is4xxClientError()) {
      throw new DepartmentWithNotFoundException("id", String.valueOf(departmentId));
    }
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

    return employeeMapper.mapEntityToRead(employee, getDepartmentName(departmentId));
  }

  @Override
  @Transactional(timeout = 30)
  public EmployeeReadDto deleteEmployee(Long employeeId) {
    var employee = employeeRepository.findById(employeeId);

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

  @Override
  @Transactional(timeout = 30)
  public EmployeeReadDto updateEmployee(EmployeeEditDto employeeEditDto, Long employeeId) {
    EmployeeEntity employeeEntity = getEmployeeById(employeeId);
    var payment = employeeEditDto.payment();

    if (employeeEditDto.isLeader()) {
      var leaderInGroup = employeeRepository.findLeaderInDepartment(employeeEditDto.departmentId());

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

  @Override
  public Integer findEmployeesCountInDepartment(Long departmentId) {
    var employees = employeeRepository.findByDepartmentId(departmentId);
    return employees.size();
  }

  @Override
  public Integer getCommonPaymentForDepartment(Long departmentId) {
    return employeeRepository.commonPaymentForDepartment(departmentId);
  }

  @Override
  public EmployeeShortInfoDto getLeaderShortInfo(Long departmentId) {
    var employee = employeeRepository.findLeaderInDepartment(departmentId);

    if (employee.isEmpty()) {
      return new EmployeeShortInfoDto(0L, "Unknown", "Unknown");
    }
    var employeeEntity = employee.get();

    return new EmployeeShortInfoDto(employeeEntity.getId(), employeeEntity.getName(),
      employeeEntity.getSurname());
  }

  @Override
  public List<EmployeeShortInfoDto> getEmployeesInDepartment(Long departmentId) {
    var employees = employeeRepository.findByDepartmentId(departmentId);
    return employees.stream().map(employeeMapper::mapEntityToShortDto).toList();
  }

  public String getDepartmentName(Long departmentId) {
    var response = departmentClient.findDepartmentById(departmentId);

    if (response.getStatusCode().is4xxClientError()) {
      throw new DepartmentWithNotFoundException("id", String.valueOf(departmentId));
    }

    return response.getBody().name();
  }

  @Override
  public boolean isExistsEmployeeById(Long employeeId) {
    return Objects.nonNull(getEmployeeById(employeeId));
  }

  @Override
  @Transactional(timeout = 30)
  public void updateIsLeaderById(boolean isLeader, Long employeeId) {
    employeeRepository.updateIsLeaderById(isLeader, employeeId);
  }

  @Override
  public Long getCountDepartmentsOfLeaderById(Long leaderId) {
    return employeeRepository.getCountDepartmentsOfLeaderById(leaderId);
  }

  private boolean checkLeaderPayment(Integer employeePayment, Long departmentId) {
    var leaderOpt = employeeRepository.findLeaderInDepartment(departmentId);
    return leaderOpt.isEmpty() ? true : employeePayment < leaderOpt.get().getPayment();
  }

  private EmployeeEntity getEmployeeById(Long employeeId) {
    return employeeRepository.findEmployeeById(employeeId)
      .orElseThrow(() -> new EmployeeNotFoundException(employeeId));
  }
}
