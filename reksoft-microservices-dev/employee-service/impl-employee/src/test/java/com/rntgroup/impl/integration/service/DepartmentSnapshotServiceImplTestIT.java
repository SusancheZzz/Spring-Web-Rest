package com.rntgroup.impl.integration.service;

import com.rntgroup.api.dto.DepartmentMessageDto;
import com.rntgroup.api.service.DepartmentSnapshotService;
import com.rntgroup.impl.integration.TestBase;
import com.rntgroup.impl.integration.annotation.IntegrationTest;
import com.rntgroup.impl.repository.DepartmentSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@RequiredArgsConstructor
class DepartmentSnapshotServiceImplTestIT extends TestBase {

  private final DepartmentSnapshotService snapshotService;
  private final DepartmentSnapshotRepository departmentSnapshotRepository;

  @Test
  @Transactional
  void save() {
    var messageDto = getDepartmentMessageDto(1L);
    snapshotService.saveOrUpdate(messageDto);

    var message = departmentSnapshotRepository.findById(messageDto.id());
    assertThat(message).isPresent();

    var messageEntity = message.get();
    assertThat(messageEntity.getId()).isEqualTo(messageDto.id());
    assertThat(messageEntity.getName()).isEqualTo(messageDto.name());
  }

  @Test
  @Transactional
  void update() {
    var messageDto = getDepartmentMessageDto(3L);
    snapshotService.saveOrUpdate(messageDto);

    var updateDto = new DepartmentMessageDto(messageDto.id(), "New name");
    snapshotService.saveOrUpdate(updateDto);

    var updateDepartment = departmentSnapshotRepository.findById(messageDto.id());
    assertThat(updateDepartment).isPresent();
    assertThat(updateDepartment.get().getName()).isEqualTo(updateDto.name());
  }

  @Test
  @Transactional
  void delete() {
    var messageDto = getDepartmentMessageDto(2L);
    snapshotService.saveOrUpdate(messageDto);
    snapshotService.delete(messageDto);

    var deletedMessage = departmentSnapshotRepository.findById(messageDto.id());
    assertThat(deletedMessage).isEmpty();
  }

  private DepartmentMessageDto getDepartmentMessageDto(Long id) {
    return new DepartmentMessageDto(id, "Department");
  }
}