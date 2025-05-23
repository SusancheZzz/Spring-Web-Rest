package com.rntgroup.impl.service.impl;

import com.rntgroup.api.dto.DepartmentMessageDto;
import com.rntgroup.api.service.DepartmentSnapshotService;
import com.rntgroup.impl.mapper.DepartmentMapper;
import com.rntgroup.impl.repository.DepartmentSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentSnapshotServiceImpl implements DepartmentSnapshotService {

  private final DepartmentSnapshotRepository departmentSnapshotRepository;
  private final DepartmentMapper departmentMapper;

  @Override
  public void saveOrUpdate(DepartmentMessageDto departmentMessageDto) {
    var departmentSnapshotEntity = departmentMapper.mapMessageDtoToEntity(departmentMessageDto);
    departmentSnapshotRepository.saveAndFlush(departmentSnapshotEntity);
  }

  @Override
  public void delete(DepartmentMessageDto departmentMessageDto) {
    departmentSnapshotRepository.deleteById(departmentMessageDto.id());
  }
}