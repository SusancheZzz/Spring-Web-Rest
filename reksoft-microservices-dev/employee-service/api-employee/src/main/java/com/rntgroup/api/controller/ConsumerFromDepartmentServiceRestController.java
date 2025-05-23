package com.rntgroup.api.controller;

import com.rntgroup.api.dto.DepartmentMessageDto;
import org.springframework.web.bind.annotation.PostMapping;

public interface ConsumerFromDepartmentServiceRestController {

  @PostMapping("/modifying")
  void consumeDepartmentSnapshotForSaveOrUpdate(DepartmentMessageDto departmentMessageDto);

  @PostMapping("/delete")
  void consumeDepartmentSnapshotForDelete(DepartmentMessageDto departmentMessageDto);
}