package com.rntgroup.api.service;

import com.rntgroup.api.dto.DepartmentMessageDto;

public interface DepartmentSnapshotService {

  void saveOrUpdate(DepartmentMessageDto departmentMessageDto);

  void delete(DepartmentMessageDto departmentMessageDto);
}