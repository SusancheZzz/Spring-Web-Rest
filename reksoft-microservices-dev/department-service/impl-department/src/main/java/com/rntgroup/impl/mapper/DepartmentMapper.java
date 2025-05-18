package com.rntgroup.impl.mapper;

import com.rntgroup.api.dto.DepartmentEditDto;
import com.rntgroup.api.dto.DepartmentMessageDto;
import com.rntgroup.api.dto.DepartmentReadDto;
import com.rntgroup.api.dto.DepartmentSaveDto;
import com.rntgroup.impl.entity.DepartmentEntity;
import java.time.LocalDate;
import lombok.Generated;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Generated
@Mapper(componentModel = "spring")
public interface DepartmentMapper {

  DepartmentMapper INSTANCE = Mappers.getMapper(DepartmentMapper.class);

  @Mapping(source = "leaderId", target = "leaderId")
  @Mapping(source = "employeesNumber", target = "employeesNumber")
  DepartmentReadDto mapEntityToRead(DepartmentEntity departmentEntity, Long leaderId,
    Integer employeesNumber);

  @Mapping(source = "createdAt", target = "createdAt")
  @Mapping(source = "isMain", target = "isMain")
  DepartmentEntity mapSaveToEntity(DepartmentSaveDto departmentSaveDto, LocalDate createdAt,
    Boolean isMain);

  @Mapping(source = "departmentEditDto.name", target = "name")
  @Mapping(source = "departmentEntity.id", target = "id")
  @Mapping(source = "departmentEntity.createdAt", target = "createdAt")
  @Mapping(source = "departmentEntity.isMain", target = "isMain")
  @Mapping(source = "departmentEntity.childDepartment", target = "childDepartment")
  DepartmentEntity mapEditDtoToEntity(DepartmentEditDto departmentEditDto,
    DepartmentEntity departmentEntity);

  DepartmentMessageDto mapEntityToMessageDto(DepartmentEntity entity);
}
