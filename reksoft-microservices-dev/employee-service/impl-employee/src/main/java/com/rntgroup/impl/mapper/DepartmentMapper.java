package com.rntgroup.impl.mapper;

import com.rntgroup.api.dto.DepartmentMessageDto;
import com.rntgroup.impl.entity.DepartmentSnapshotEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

  DepartmentSnapshotEntity mapMessageDtoToEntity(DepartmentMessageDto dto);
}