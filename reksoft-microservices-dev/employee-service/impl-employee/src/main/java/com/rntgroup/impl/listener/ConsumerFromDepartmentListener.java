package com.rntgroup.impl.listener;

import com.rntgroup.api.dto.DepartmentMessageDto;
import com.rntgroup.api.service.DepartmentSnapshotService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConsumerFromDepartmentListener {

  private final DepartmentSnapshotService departmentSnapshotService;

  @KafkaListener(
    id = "department-snapshot-modifying",
    topics = {"department.MODIFYING"},
    containerFactory = "singleFactory"
  )
  public void snapshotForSaveOrUpdate(DepartmentMessageDto departmentMessageDto) {
    departmentSnapshotService.saveOrUpdate(departmentMessageDto);
  }

  @KafkaListener(
    id = "department-snapshot-delete",
    topics = {"department.DELETE"},
    containerFactory = "singleFactory"
  )
  public void snapshotForDelete(DepartmentMessageDto departmentMessageDto) {
    departmentSnapshotService.delete(departmentMessageDto);
  }
}
