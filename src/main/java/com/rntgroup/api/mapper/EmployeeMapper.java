package com.rntgroup.api.mapper;

import com.rntgroup.api.dto.EmployeeEditDto;
import com.rntgroup.api.dto.EmployeeReadDto;
import com.rntgroup.api.dto.EmployeeSaveDto;
import com.rntgroup.api.dto.EmployeeShortInfoDto;
import com.rntgroup.api.entity.EmployeeEntity;
import com.rntgroup.api.entity.Gender;
import java.time.LocalDate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper(imports = {Gender.class, LocalDate.class})
public interface EmployeeMapper {

  EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

  @Mapping(source = "entity.id", target = "id")
  @Mapping(source = "entity.name", target = "name")
  @Mapping(source = "entity.surname", target = "surname")
  @Mapping(source = "entity.phoneNumber", target = "phoneNumber")
  @Mapping(source = "entity.birthday", target = "birthday")
  @Mapping(source = "entity.position", target = "position")
  @Mapping(source = "entity.payment", target = "payment")
  @Mapping(source = "department", target = "departmentName")
  EmployeeReadDto mapEntityToRead(EmployeeEntity entity, String department);

  @Mapping(source = "dto.name", target = "name")
  @Mapping(source = "departmentId", target = "departmentId")
  @Mapping(expression = "java(Gender.valueOf(dto.getGender()))", target = "gender")
  @Mapping(expression = "java(LocalDate.now())", target = "employmentDate")
  @Mapping(expression = "java(false)", target = "isLeader")
  @Mapping(expression = "java(null)", target = "id")
  EmployeeEntity mapSaveToEntity(EmployeeSaveDto dto);

  EmployeeShortInfoDto mapEntityToShortDto(EmployeeEntity entity);

  default EmployeeEntity mapEditToEntity(EmployeeEditDto dto, EmployeeEntity employee) {
    employee.setName(dto.getName());
    employee.setSurname(dto.getSurname());
    employee.setPhoneNumber(dto.getPhoneNumber());
    employee.setDepartmentId(dto.getDepartmentId());
    employee.setPosition(dto.getPosition());
    employee.setPayment(dto.getPayment());
    employee.setIsLeader(dto.getIsLeader());

    return employee;
  }
}
