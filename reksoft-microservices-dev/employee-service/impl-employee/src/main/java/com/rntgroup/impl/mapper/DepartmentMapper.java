package com.rntgroup.impl.mapper;

import com.rntgroup.api.dto.DepartmentMessageDto;
import com.rntgroup.impl.entity.DepartmentSnapshotEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

  DepartmentMapper INSTANCE = Mappers.getMapper(DepartmentMapper.class);

  DepartmentSnapshotEntity mapMessageDtoToEntity(DepartmentMessageDto dto);
}