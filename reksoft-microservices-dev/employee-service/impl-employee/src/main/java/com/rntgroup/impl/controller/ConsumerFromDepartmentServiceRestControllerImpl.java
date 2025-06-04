package com.rntgroup.impl.controller;

import com.rntgroup.api.controller.ConsumerFromDepartmentServiceRestController;
import com.rntgroup.api.dto.DepartmentMessageDto;
import com.rntgroup.impl.listener.ConsumerFromDepartmentListener;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/department-snapshot")
@RequiredArgsConstructor
public class ConsumerFromDepartmentServiceRestControllerImpl implements
  ConsumerFromDepartmentServiceRestController {

  private final ConsumerFromDepartmentListener consumerFromDepartmentListener;

  @Override
  public void consumeDepartmentSnapshotForSaveOrUpdate(DepartmentMessageDto departmentMessageDto) {
    consumerFromDepartmentListener.snapshotForSaveOrUpdate(departmentMessageDto);
  }

  @Override
  public void consumeDepartmentSnapshotForDelete(DepartmentMessageDto departmentMessageDto) {
    consumerFromDepartmentListener.snapshotForDelete(departmentMessageDto);
  }
}