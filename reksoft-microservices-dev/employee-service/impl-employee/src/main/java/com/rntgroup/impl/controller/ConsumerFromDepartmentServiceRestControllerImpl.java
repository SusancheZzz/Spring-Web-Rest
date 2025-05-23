package com.rntgroup.impl.controller;

import com.rntgroup.api.controller.ConsumerFromDepartmentServiceRestController;
import com.rntgroup.api.dto.DepartmentMessageDto;
import com.rntgroup.api.service.DepartmentSnapshotService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/department-snapshot")
@RequiredArgsConstructor
public class ConsumerFromDepartmentServiceRestControllerImpl implements
  ConsumerFromDepartmentServiceRestController {

  private final DepartmentSnapshotService departmentSnapshotService;

  @Override
  @KafkaListener(
    id = "department-snapshot-modifying",
    topics = {"department.MODIFYING"},
    containerFactory = "singleFactory"
  )
  public void consumeDepartmentSnapshotForSaveOrUpdate(DepartmentMessageDto departmentMessageDto) {
    departmentSnapshotService.saveOrUpdate(departmentMessageDto);
  }

  @Override
  @KafkaListener(
    id = "department-snapshot-delete",
    topics = {"department.DELETE"},
    containerFactory = "singleFactory"
  )
  public void consumeDepartmentSnapshotForDelete(DepartmentMessageDto departmentMessageDto) {
    departmentSnapshotService.delete(departmentMessageDto);
  }
}