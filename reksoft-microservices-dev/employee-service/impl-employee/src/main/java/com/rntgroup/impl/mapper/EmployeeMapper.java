package com.rntgroup.impl.mapper;

import com.rntgroup.api.dto.EmployeeEditDto;
import com.rntgroup.api.dto.EmployeeReadDto;
import com.rntgroup.api.dto.EmployeeSaveDto;
import com.rntgroup.api.dto.EmployeeShortInfoDto;
import com.rntgroup.impl.entity.EmployeeEntity;
import com.rntgroup.impl.entity.Gender;
import java.time.LocalDate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
  componentModel = "spring",
  imports = {Gender.class, LocalDate.class}
)
public interface EmployeeMapper {

  @Mapping(source = "entity.id", target = "id")
  @Mapping(source = "entity.name", target = "name")
  @Mapping(source = "entity.surname", target = "surname")
  @Mapping(source = "entity.phoneNumber", target = "phoneNumber")
  @Mapping(source = "entity.birthday", target = "birthday")
  @Mapping(source = "entity.position", target = "position")
  @Mapping(source = "entity.payment", target = "payment")
  @Mapping(source = "department", target = "departmentName")
  EmployeeReadDto mapEntityToRead(EmployeeEntity entity, String department);

  @Mapping(source = "name", target = "name")
  @Mapping(source = "departmentId", target = "departmentId")
  @Mapping(expression = "java(Gender.valueOf(dto.gender()))", target = "gender")
  @Mapping(expression = "java(LocalDate.now())", target = "employmentDate")
  @Mapping(expression = "java(false)", target = "isLeader")
  @Mapping(expression = "java(null)", target = "id")
  EmployeeEntity mapSaveToEntity(EmployeeSaveDto dto);

  EmployeeShortInfoDto mapEntityToShortDto(EmployeeEntity entity);

  default EmployeeEntity mapEditToEntity(EmployeeEditDto dto, EmployeeEntity employee) {
    employee.setName(dto.name());
    employee.setSurname(dto.surname());
    employee.setPhoneNumber(dto.phoneNumber());
    employee.setDepartmentId(dto.departmentId());
    employee.setPosition(dto.position());
    employee.setPayment(dto.payment());
    employee.setIsLeader(dto.isLeader());

    return employee;
  }
}
