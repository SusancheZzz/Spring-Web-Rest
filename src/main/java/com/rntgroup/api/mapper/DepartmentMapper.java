package com.rntgroup.api.mapper;

import com.rntgroup.api.dto.DepartmentEditDto;
import com.rntgroup.api.dto.DepartmentMessageDto;
import com.rntgroup.api.dto.DepartmentReadDto;
import com.rntgroup.api.dto.DepartmentSaveDto;
import com.rntgroup.api.entity.DepartmentEntity;
import java.time.LocalDate;
import lombok.Generated;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Generated
@Mapper(imports = LocalDate.class)
public interface DepartmentMapper {

  DepartmentMapper INSTANCE = Mappers.getMapper(DepartmentMapper.class);

  @Mapping(source = "leaderSurname", target = "leaderSurname")
  @Mapping(source = "employeesNumber", target = "employeesNumber")
  DepartmentReadDto mapEntityToRead(DepartmentEntity departmentEntity, String leaderSurname,
    Integer employeesNumber);

  @Mapping(expression = "java(LocalDate.now())", target = "createdAt")
  @Mapping(expression = "java(false)", target = "isMain")
  DepartmentEntity mapSaveToEntity(DepartmentSaveDto departmentSaveDto);

  @Mapping(source = "departmentEditDto.name", target = "name")
  @Mapping(source = "departmentEntity.id", target = "id")
  @Mapping(source = "departmentEntity.createdAt", target = "createdAt")
  @Mapping(source = "departmentEntity.isMain", target = "isMain")
  @Mapping(source = "departmentEntity.childDepartment", target = "childDepartment")
  DepartmentEntity mapEditDtoToEntity(DepartmentEditDto departmentEditDto,
    DepartmentEntity departmentEntity);

  DepartmentMessageDto mapEntityToMessageDto(DepartmentEntity entity);
}
